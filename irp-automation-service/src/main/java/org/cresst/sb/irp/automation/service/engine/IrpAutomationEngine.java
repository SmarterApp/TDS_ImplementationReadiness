package org.cresst.sb.irp.automation.service.engine;

import org.cresst.sb.irp.automation.service.domain.IrpAutomationRequest;
import org.cresst.sb.irp.automation.service.domain.IrpAutomationRequestError;
import org.cresst.sb.irp.automation.service.domain.IrpAutomationToken;
import org.cresst.sb.irp.automation.service.engine.task.AutomationTask;
import org.cresst.sb.irp.automation.service.engine.task.AutomationTaskFactory;
import org.cresst.sb.irp.automation.service.requesthandler.AutomationRequestResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class IrpAutomationEngine implements AutomationEngine, Runnable {
    private final static Logger logger = LoggerFactory.getLogger(IrpAutomationEngine.class);

    // Controls execution of processor thread
    private volatile static boolean runProcessor = false;
    private final static Object lockObject = new Object();

    // Queues automation requests
    private final BlockingQueue<IrpAutomationRequest> irpAutomationRequestQueue = new ArrayBlockingQueue<>(16);

    // Contains tokens for running automation requests
    private final static Set<IrpAutomationToken> IRP_AUTOMATION_TOKENs = Collections.synchronizedSet(new HashSet<IrpAutomationToken>());

    private final TaskExecutor taskExecutor;
    private final AutomationRequestResultHandler automationRequestResultHandler;
    private final AutomationTaskFactory automationTaskFactory;

    public IrpAutomationEngine(TaskExecutor taskExecutor,
                               AutomationRequestResultHandler automationRequestResultHandler,
                               AutomationTaskFactory automationTaskFactory) {
        this.taskExecutor = taskExecutor;
        this.automationRequestResultHandler = automationRequestResultHandler;
        this.automationTaskFactory = automationTaskFactory;
    }

    @Override
    public void automate(IrpAutomationRequest irpAutomationRequest) {
        queueAutomationRequest(irpAutomationRequest);
    }

    /**
     * Starts the queue processor in its own thread
     */
    public void initializeProcessor() {
        taskExecutor.execute(this);
    }

    /**
     * Stops the queue processor
     */
    public void shutdownProcessor() {
        runProcessor = false;
    }

    @Override
    public void run() {
        synchronized (lockObject) {
            if (runProcessor) {
                return;
            }
            runProcessor = true;
        }
        processAutomationRequests();
    }

    private void queueAutomationRequest(IrpAutomationRequest irpAutomationRequest) {
        if (!irpAutomationRequestQueue.offer(irpAutomationRequest)) {
            sendAutomationStartError("Request queue is full", irpAutomationRequest);
        }
    }

    /**
     * Only one of these runs in the application.
     */
    private void processAutomationRequests()  {
        logger.info("Started Automation Request Processor");
        try {
            while (runProcessor) {
                final IrpAutomationRequest irpAutomationRequest = irpAutomationRequestQueue.take();
                final IrpAutomationToken irpAutomationToken = new IrpAutomationToken(irpAutomationRequest);

                if (IRP_AUTOMATION_TOKENs.contains(irpAutomationToken)) {
                    logger.info("Duplicate automation requested. Sending existing automation token.");
                    sendAutomationToken(irpAutomationRequest, irpAutomationToken);
                } else {
                    if (startAutomationTask(irpAutomationRequest, irpAutomationToken)) {
                        logger.info("Started automation task for {} with token {}", irpAutomationRequest, irpAutomationToken);
                        IRP_AUTOMATION_TOKENs.add(irpAutomationToken);
                        sendAutomationToken(irpAutomationRequest, irpAutomationToken);
                    } else {
                        sendAutomationStartError("Task executor is busy", irpAutomationRequest);
                    }
                }
            }

            clearQueue();
        } catch (InterruptedException e) {
            logger.info("Ending AutomationRequest processor because blocking queue has been interrupted while waiting.");

            clearQueue();

            // Restore interrupt status
            Thread.currentThread().interrupt();
        }

        logger.info("Automation Request Processor stopped");
    }

    private void clearQueue() {
        final List<IrpAutomationRequest> remainingIrpAutomationRequests = new ArrayList<>();
        irpAutomationRequestQueue.drainTo(remainingIrpAutomationRequests);

        for (IrpAutomationRequest irpAutomationRequest : remainingIrpAutomationRequests) {
            sendAutomationStartError("IRP is shutting down", irpAutomationRequest);
        }
    }

    private boolean startAutomationTask(final IrpAutomationRequest irpAutomationRequest, final IrpAutomationToken irpAutomationToken) {
        try {
            AutomationTask automationTask = automationTaskFactory.buildAutomationTask();
            automationTask.setIrpAutomationRequest(irpAutomationRequest);
            automationTask.setIrpAutomationToken(irpAutomationToken);
            automationTask.onCompletion(new Runnable() {
                @Override
                public void run() {
                    IRP_AUTOMATION_TOKENs.remove(irpAutomationToken);
                }
            });

            taskExecutor.execute(automationTask);
        } catch (TaskRejectedException e) {
            logger.error("Unable to start automation task", e);
            return false;
        }

        return true;
    }

    private void sendAutomationToken(final IrpAutomationRequest irpAutomationRequest, final IrpAutomationToken irpAutomationToken) {
        automationRequestResultHandler.handleAutomationRequestResult(irpAutomationRequest, irpAutomationToken);
    }

    private void sendAutomationStartError(final String message, final IrpAutomationRequest irpAutomationRequest) {
        final String errorMessage = "Can't start IRP Automation - " + message;
        final IrpAutomationRequestError irpAutomationRequestError = new IrpAutomationRequestError(errorMessage, irpAutomationRequest);

        automationRequestResultHandler.handleAutomationRequestError(irpAutomationRequestError);
    }
}

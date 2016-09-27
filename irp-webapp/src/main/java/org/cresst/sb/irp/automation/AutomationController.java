package org.cresst.sb.irp.automation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * This controller handles automation requests and status reports. It is designed to run as a single instance
 * on a single server since it stores status results in-memory.
 */
@Controller
public class AutomationController {
    private final static Logger logger = LoggerFactory.getLogger(AutomationController.class);

}

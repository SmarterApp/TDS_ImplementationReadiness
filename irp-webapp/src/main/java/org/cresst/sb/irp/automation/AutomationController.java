package org.cresst.sb.irp.automation;

import org.cresst.sb.irp.domain.analysis.AutomationRequest;
import org.cresst.sb.irp.domain.analysis.AutomationResponse;
import org.cresst.sb.irp.service.AutomationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
public class AutomationController {
    private final static Logger logger = LoggerFactory.getLogger(AutomationController.class);

    @Autowired
    private AutomationService automationService;

    @RequestMapping(value = "/automate", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public AutomationResponse automate(@Valid @RequestBody AutomationRequest automationRequest) {
        AutomationResponse automationResponse = automationService.automate(automationRequest);
        return automationResponse;
    }
}

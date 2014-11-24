package org.cresst.sb.irp.jsondoc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/***
 * Controller for giving the JSONDoc template the web application context path.
 */
@Controller
public class JsonDocController {

    @RequestMapping(value="/apidoc.html")
    public ModelAndView apiDoc(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("apidoc");
        mav.addObject("contextpath", request.getContextPath());
        return mav;
    }
}

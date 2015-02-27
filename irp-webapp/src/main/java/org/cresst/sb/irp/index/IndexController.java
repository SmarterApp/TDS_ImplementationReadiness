package org.cresst.sb.irp.index;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/***
 * The index controller passes the IRP irpVersion to the view
 */
@Controller
public class IndexController {
    private final String irpVersion;

    @Autowired
    public IndexController(@Value("${irp.version}") String irpVersion) {
        this.irpVersion = irpVersion;
    }

    @RequestMapping({"/irp-scaffold.html"})
    public String index(final ModelMap model) {
        model.put("irpVersion", irpVersion);
        return "irp-scaffold";
    }
}

package org.cresst.sb.irp.download;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.zeroturnaround.zip.ZipUtil;

/**
 * This controller handles the irp-package download.
 */
@Controller
public class DownloadController {
    private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);

    @Value("${irp.package.location}")
    private Resource irpPackage;

    @RequestMapping(value = "/irp-package.zip", produces = "application/zip", method = RequestMethod.GET)
    public void downloadIrpPackage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // setting headers
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"irp-package.zip\"");

        ZipUtil.pack(irpPackage.getFile(), response.getOutputStream());
    }
}

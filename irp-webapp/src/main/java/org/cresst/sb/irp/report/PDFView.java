package org.cresst.sb.irp.report;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringReader;

/**
 * View to generate PDF IRP Report
 */
@Controller
public class PDFView {
    private static final Logger log = LoggerFactory.getLogger(PDFView.class);

    @Autowired
    private TemplateEngine templateEngine;

    @RequestMapping(value="/pdfreport.html", method = RequestMethod.POST)
    public void pdfReport(@ModelAttribute IndividualResponse individualResponses, HttpServletRequest request, HttpServletResponse response) {
        try {
            WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
            context.setVariable("individualResponse", individualResponses);
            String htmlReport = templateEngine.process("htmlreport", context);

            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment; filename=report.pdf");

            // step 1
            Document document = new Document();
            // step 2
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            // step 3
            document.open();
            // step 4
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(htmlReport));
            //step 5
            document.close();
        } catch (Exception ex) {
            log.error("pdf error", ex);
        }
    }
}

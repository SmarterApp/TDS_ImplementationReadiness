package org.cresst.sb.irp.cat.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.cresst.sb.irp.cat.domain.analysis.CATAnalysisResponse;
import org.cresst.sb.irp.exception.PDFGenerationException;
import org.cresst.sb.irp.report.PageStamper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringReader;

@Controller
public class CATPDFController {
    private static final Logger log = LoggerFactory.getLogger(CATPDFController.class);

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private TemplateEngine templateEngine;

    @RequestMapping(value="/catPdfreport.html", method = RequestMethod.POST)
    public void pdfReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            String catResults = request.getParameter("catResults");

            CATAnalysisResponse catAnalysisResponse = jacksonObjectMapper.readValue(catResults, CATAnalysisResponse.class);

            WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
            context.setVariable("catResults", catAnalysisResponse);

            templateEngine.clearTemplateCache();
            String htmlReport = templateEngine.process("cathtmlreport", context);

            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment; filename=irp-cat-report.pdf");

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new PageStamper());

            document.addAuthor("Smarter Balanced IRP");
            document.addCreationDate();
            document.addTitle("CAT - Implementation Readiness Package Report");
            document.open();

            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(htmlReport));

            document.close();
            writer.close();
        } catch (Exception ex) {
            log.error("Error generating TDS PDF Report", ex);
            throw new PDFGenerationException();
        }
    }
}
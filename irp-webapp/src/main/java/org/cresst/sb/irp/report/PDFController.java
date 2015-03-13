package org.cresst.sb.irp.report;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.cresst.sb.irp.domain.analysis.AnalysisResponse;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.ItemCategory;
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

/**
 * View to generate PDF IRP Report
 */
@Controller
public class PDFController {
    private static final Logger log = LoggerFactory.getLogger(PDFController.class);

    @Autowired
    private ObjectMapper pdfObjectMapper;

    @Autowired
    private TemplateEngine templateEngine;

    @RequestMapping(value="/pdfreport.html", method = RequestMethod.POST)
    public void pdfReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            String analysisResponseParameter = request.getParameter("analysisResponses");

            pdfObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            AnalysisResponse analysisResponse = pdfObjectMapper.readValue(analysisResponseParameter, AnalysisResponse.class);

            WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
            context.setVariable("responses", analysisResponse);
            context.setVariable("helper", new DisplayHelper());

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

    class DisplayHelper {
        public String status(FieldCheckType input) {
            if (input == null) { return ""; }
            if (input.getEnumfieldCheckType() == FieldCheckType.EnumFieldCheckType.D) { return "Ignored"; }

            boolean valid = !input.isFieldEmpty() && input.isCorrectDataType() && input.isAcceptableValue();

            if (input.getEnumfieldCheckType() == FieldCheckType.EnumFieldCheckType.PC) {
                valid = valid && input.isCorrectValue();
            }

            return valid ? "No Errors" : "Errors Found";
        }

        public String explanation(FieldCheckType input) {
            if (input == null) { return ""; }
            if (input.getEnumfieldCheckType() == FieldCheckType.EnumFieldCheckType.D) { return ""; }

            StringBuilder reasons = new StringBuilder(input.getEnumfieldCheckType().toString());
            if (input.isFieldEmpty()) reasons.append("Field Empty");
            if (!input.isCorrectDataType()) reasons.append("Incorrect Data Type");
            if (!input.isAcceptableValue()) reasons.append("Unacceptable Value");

            if (input.getEnumfieldCheckType() == FieldCheckType.EnumFieldCheckType.PC) {
                if (!input.isCorrectValue()) reasons.append("Incorrect Value");
            }

            return reasons.toString();
        }

        public String itemStatus(ItemCategory item) {
            return (item.getStatus() == ItemCategory.ItemStatusEnum.FOUND && item.isItemFormatCorrect()) ? "Match" : "Errors Found";
        }

        public String itemExplanation(ItemCategory item) {
            if (item.getStatus() == ItemCategory.ItemStatusEnum.FOUND && item.isItemFormatCorrect()) { return "This Item matches a given IRP Item. See detailed analysis below."; }
            if (item.getStatus() == ItemCategory.ItemStatusEnum.FOUND && !item.isItemFormatCorrect()) { return "The Item's format is incorrect"; }
            if (item.getStatus() == ItemCategory.ItemStatusEnum.MISSING) { return "This Item is missing from the TDS Report."; }
            if (item.getStatus() == ItemCategory.ItemStatusEnum.EXTRA) { return "This Item is not part of the IRP Package and cannot be analyzed"; }
            return "";
        }
    }
}

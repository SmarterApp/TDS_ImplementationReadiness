package org.cresst.sb.irp.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math.Field;
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
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private TemplateEngine templateEngine;

    @RequestMapping(value="/pdfreport.html", method = RequestMethod.POST)
    public void pdfReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            String analysisResponseParameter = request.getParameter("analysisResponses");

            AnalysisResponse analysisResponse = jacksonObjectMapper.readValue(analysisResponseParameter, AnalysisResponse.class);

            WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale());
            log.debug(analysisResponse.getIrpVersion());
            log.debug(analysisResponse.getVendorName());
            context.setVariable("responses", analysisResponse);
            context.setVariable("helper", new DisplayHelper());

            templateEngine.clearTemplateCache();
            String htmlReport = templateEngine.process("htmlreport", context);

            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment; filename=irp-report.pdf");

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new PageStamper());

            document.addAuthor("Smarter Balanced IRP");
            document.addCreationDate();
            document.addTitle("Implementation Readiness Package Report");
            document.open();

            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(htmlReport));

            document.close();
            writer.close();

        } catch (Exception ex) {
            log.error("pdf error", ex);
        }
    }

    class DisplayHelper {
        public String status(FieldCheckType input) {
            if (input == null) { return ""; }
            if (input.getEnumfieldCheckType() == FieldCheckType.EnumFieldCheckType.D) { return "Ignored"; }

            boolean valid = !input.isFieldValueEmpty() && input.isCorrectDataType() && input.isAcceptableValue();

            if (input.getEnumfieldCheckType() == FieldCheckType.EnumFieldCheckType.PC) {
                valid = valid && input.isCorrectValue();
            }

            return valid ? "No Errors" : "Errors Found";
        }

        public String statusIcon(FieldCheckType input) {
            if (input.getEnumfieldCheckType() == FieldCheckType.EnumFieldCheckType.D) {
                return "http://localhost:8080/check.png";
            }

            if (input.isRequiredFieldMissing() ||
                    ((input.isOptionalValue() && !input.isFieldValueEmpty()) || !input.isOptionalValue()) &&
                            (!input.isCorrectDataType() ||
                                    !input.isAcceptableValue() ||
                                    !input.isCorrectWidth() ||
                                    (input.getEnumfieldCheckType() == FieldCheckType.EnumFieldCheckType.PC && !input.isCorrectValue()))) {
                return "http://localhost:8080/error.png";
            }

            return "http://localhost:8080/check-circle.png";
        }

        public String explanation(FieldCheckType input) {
            if (input == null) { return ""; }
            if (input.getEnumfieldCheckType() == FieldCheckType.EnumFieldCheckType.D) { return ""; }

            StringBuilder reasons = new StringBuilder(input.getEnumfieldCheckType().toString());
            if (input.isFieldValueEmpty()) reasons.append(" Field Empty");
            if (!input.isCorrectDataType()) reasons.append(" Incorrect Data Type");
            if (!input.isAcceptableValue()) reasons.append(" Unacceptable Value");

            if (input.getEnumfieldCheckType() == FieldCheckType.EnumFieldCheckType.PC) {
                if (!input.isCorrectValue()) reasons.append(" Incorrect Value");
            }

            return reasons.toString();
        }

        public String itemStatus(ItemCategory item) {
            return (item.getStatus() == ItemCategory.ItemStatusEnum.FOUND && item.isItemFormatCorrect())
                    ? "Match"
                    : (item.getStatus() == ItemCategory.ItemStatusEnum.NOTUSED ? "CAT Item" : "Errors Found");
        }

        public boolean itemFound(ItemCategory item) {
            return item.getStatus() == ItemCategory.ItemStatusEnum.FOUND;
        }

        public String itemExplanation(ItemCategory item) {
            if (item.getStatus() == ItemCategory.ItemStatusEnum.FOUND && item.isItemFormatCorrect()) { return "This Item matches a given IRP Item. The detailed analysis follows."; }
            if (item.getStatus() == ItemCategory.ItemStatusEnum.FOUND && !item.isItemFormatCorrect()) { return "The Item ID matches a given IRP Item's ID but the Item format does not match the IRP Item's format."; }
            if (item.getStatus() == ItemCategory.ItemStatusEnum.MISSING) { return "This IRP Item is missing from the TDS Report."; }
            if (item.getStatus() == ItemCategory.ItemStatusEnum.EXTRA) { return "This Item is unknown to IRP or it is a duplicate of an already analyzed Item."; }
            if (item.getStatus() == ItemCategory.ItemStatusEnum.NOTUSED) { return "This is a CAT Item that was not served to the student."; }
            return "";
        }

        public String rowClass(FieldCheckType fieldCheckType) {
            return fieldCheckType.isOptionalValue() ? "optionalField" : "requiredField";
        }

        public String expectedValueClass(String tdsExpectedValue) {
            return StringUtils.isNotBlank(tdsExpectedValue) ? "expectedValue" : "noExpectedValue";
        }

        public String adjustedExpectedValueCell(String input) {
            return StringUtils.isNotBlank(input) ? input : "XXXXX";
        }
    }
}

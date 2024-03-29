package org.cresst.sb.irp.analysis.engine;

import java.util.List;

import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ToolUsageCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.ToolUsage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToolUsageAnalysisAction extends AnalysisAction<ToolUsage, ToolUsageAnalysisAction.EnumToolUsageFieldName, Object> {
    private final static Logger logger = LoggerFactory.getLogger(ToolUsageAnalysisAction.class);

    static public enum EnumToolUsageFieldName {
        type(100), code(100);

        private int maxWidth;
        private boolean isRequired;

        EnumToolUsageFieldName(int maxWidth) {
            this.maxWidth = maxWidth;
            this.isRequired = true;
        }

        EnumToolUsageFieldName(int maxWidth, boolean isRequired) {
            this.maxWidth = maxWidth;
            this.isRequired = isRequired;
        }

        public int getMaxWidth() {
            return maxWidth;
        }

        public boolean isRequired() {
            return isRequired;
        }
    }

    @Autowired
    ToolPageAnalysisAction toolPageAnalysisAction;

    @Override
    public void analyze(IndividualResponse individualResponse) {
        try {
            TDSReport tdsReport = individualResponse.getTDSReport();
            List<ToolUsage> listToolUsage = tdsReport.getToolUsage();

            for (ToolUsage toolUsage : listToolUsage) {
                ToolUsageCategory toolUsageCategory = new ToolUsageCategory();
                individualResponse.addToolUsageCategory(toolUsageCategory);
                analyzeToolUsage(toolUsageCategory, toolUsage);

                // Analyze the ToolPages and assign the results to the ToolUsageCategory
                toolUsageCategory.setToolPageCategories(toolPageAnalysisAction.analyzeToolPage(toolUsage.getToolPage()));
            }
        } catch (Exception e) {
            logger.error("analyze exception: ", e);
        }
    }

    private void analyzeToolUsage(ToolUsageCategory toolUsageCategory, ToolUsage toolUsage) {
        validate(toolUsageCategory, toolUsage, toolUsage.getType(), EnumFieldCheckType.D, EnumToolUsageFieldName.type, null);
        validate(toolUsageCategory, toolUsage, toolUsage.getCode(), EnumFieldCheckType.D, EnumToolUsageFieldName.code, null);
    }

    /**
     * Field Check Type (P) --> check that field is not empty, and field value is of correct data type
     * and within acceptable values
     *
     * @param toolUsage      ToolUsage with fields to check
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType This is where the results are stored
     */
    @Override
    protected void checkP(ToolUsage toolUsage, EnumToolUsageFieldName enumFieldName, FieldCheckType fieldCheckType) {
        try {
            fieldCheckType.setOptionalValue(true);
            switch (enumFieldName) {
                case type:
                    // 	<xs:attribute name="type" use="required" />
                    processP_PrintableASCIIone(toolUsage.getType(), fieldCheckType);
                    break;
                case code:
                    // 	<xs:attribute name="code" use="required" />
                    processP_PrintableASCIIone(toolUsage.getCode(), fieldCheckType);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error("checkP exception: ", e);
        }
    }

    /**
     * Checks if the ToolUsage field has the correct value
     *
     * @param toolUsage      ToolUsage with field to check
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType This is where the results are stored
     * @param unused         Unused parameter
     */
    @Override
    protected void checkC(ToolUsage toolUsage, EnumToolUsageFieldName enumFieldName, FieldCheckType fieldCheckType, Object unused) {

    }
}

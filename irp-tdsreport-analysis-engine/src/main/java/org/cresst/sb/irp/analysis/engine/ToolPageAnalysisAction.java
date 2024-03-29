package org.cresst.sb.irp.analysis.engine;

import java.util.ArrayList;
import java.util.List;

import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ToolPageCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.ToolUsage.ToolPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ToolPageAnalysisAction extends AnalysisAction<ToolPage, ToolPageAnalysisAction.EnumToolPageFieldName, Object> {
    private final static Logger logger = LoggerFactory.getLogger(ToolPageAnalysisAction.class);

    static public enum EnumToolPageFieldName {
        page(8), groupId(59), count(8);

        private int maxWidth;
        private boolean isRequired;

        EnumToolPageFieldName(int maxWidth) {
            this.maxWidth = maxWidth;
            this.isRequired = true;
        }

        EnumToolPageFieldName(int maxWidth, boolean isRequired) {
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

    /**
     * Not implemented since this will be invoked by {@link org.cresst.sb.irp.domain.tdsreport.TDSReport.ToolUsage}.
     *
     * @param individualResponse Where the TDS Report is obtained and where the result is stored
     */
    @Override
    public void analyze(IndividualResponse individualResponse) {
        // This class meant to be called by ToolUsageAnalysisAction via analyzeToolPage
    }

    /**
     * Takes the role of the analyze method to analyze ToolPages
     *
     * @param toolPages List of ToolPages that will be analyzed
     * @return List of ToolPageCategory objects
     */
    public List<ToolPageCategory> analyzeToolPage(List<ToolPage> toolPages) {
        List<ToolPageCategory> toolPageCategories = new ArrayList<>();

        try {
            for (ToolPage toolPage : toolPages) {
                ToolPageCategory toolPageCategory = new ToolPageCategory();
                toolPageCategories.add(toolPageCategory);

                validate(toolPageCategory, toolPage, toolPage.getPage(), EnumFieldCheckType.D, EnumToolPageFieldName.page, null);
                validate(toolPageCategory, toolPage, toolPage.getGroupId(), EnumFieldCheckType.D, EnumToolPageFieldName.groupId, null);
                validate(toolPageCategory, toolPage, toolPage.getCount(), EnumFieldCheckType.D, EnumToolPageFieldName.count, null);
            }
        } catch (Exception e) {
            logger.error("analyzeToolPage exception: ", e);
        }

        return toolPageCategories;
    }

    /**
     * Field Check Type (P) --> check that field is not empty, and field value is of correct data type
     * and within acceptable values
     *
     * @param toolPage       ToolPage with fields to check
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType This is where the results are stored
     */
    @Override
    protected void checkP(ToolPage toolPage, EnumToolPageFieldName enumFieldName, FieldCheckType fieldCheckType) {
        try {
            fieldCheckType.setOptionalValue(true);
            switch (enumFieldName) {
                case page:
                    // 	<xs:attribute name="page" use="required" type="xs:unsignedInt" />
                    processP_Positive32bit(Long.toString(toolPage.getPage()), fieldCheckType);
                    break;
                case groupId:
                    // 	<xs:attribute name="groupId" use="required" />
                    processP_PrintableASCIIone(toolPage.getGroupId(), fieldCheckType);
                    break;
                case count:
                    // 	<xs:attribute name="count" use="required" type="xs:unsignedInt" />
                    processP_Positive32bit(Long.toString(toolPage.getCount()), fieldCheckType);
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
     * @param toolPage       ToolUsage with field to check
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType This is where the results are stored
     * @param unused         Unused parameter
     */
    @Override
    protected void checkC(ToolPage toolPage, EnumToolPageFieldName enumFieldName, FieldCheckType fieldCheckType, Object unused) {
    }
}

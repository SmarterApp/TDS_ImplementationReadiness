package org.cresst.sb.irp.dao;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ToolPageCategory;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.ToolUsage.ToolPage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToolPageAnalysisAction extends AnalysisAction<ToolPage, ToolPageAnalysisAction.EnumToolPageFieldName, Object> {
    private static Logger logger = Logger.getLogger(ToolPageAnalysisAction.class);

    static public enum EnumToolPageFieldName {
        page, groupId, count
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

                validate(toolPageCategory, toolPage, toolPage.getPage(), EnumFieldCheckType.P, EnumToolPageFieldName.page, null);
                validate(toolPageCategory, toolPage, toolPage.getGroupId(), EnumFieldCheckType.P, EnumToolPageFieldName.groupId, null);
                validate(toolPageCategory, toolPage, toolPage.getCount(), EnumFieldCheckType.P, EnumToolPageFieldName.count, null);
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
            switch (enumFieldName) {
                case page:
                    // 	<xs:attribute name="page" use="required" type="xs:unsignedInt" />
                    processP_Positive32bit(Long.toString(toolPage.getPage()), fieldCheckType);
                    break;
                case groupId:
                    // 	<xs:attribute name="groupId" use="required" />
                    processP_PritableASCIIone(toolPage.getGroupId(), fieldCheckType);
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

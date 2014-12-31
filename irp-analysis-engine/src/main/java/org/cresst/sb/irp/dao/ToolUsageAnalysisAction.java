package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.ToolPageCategory;
import org.cresst.sb.irp.domain.analysis.ToolUsageCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.ToolUsage;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.ToolUsage.ToolPage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
public class ToolUsageAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(ToolUsageAnalysisAction.class);

	public enum EnumToolUsageFieldName {
		type, code;
	}
	
	public enum EnumToolPageFieldName {
		page, groupId, count;
	}
	
	@Override
	public void analyze(IndividualResponse individualResponse) throws IOException {
		try {
			TDSReport tdsReport = individualResponse.getTDSReport();
			List<ToolUsageCategory> listToolUsageCategory = individualResponse.getToolUsageCategories();
			ToolUsageCategory toolUsageCategory;
			List<ToolUsage> listToolUsage = tdsReport.getToolUsage();
			for (ToolUsage t : listToolUsage) {
				toolUsageCategory = new ToolUsageCategory();
				listToolUsageCategory.add(toolUsageCategory);
				analysisToolUsage(toolUsageCategory, t);
				analysisToolPages(toolUsageCategory, t.getToolPage());
			}
		} catch (Exception e) {
			logger.error("analyze exception: ", e);
		}
	}

	private void analysisToolUsage(ToolUsageCategory toolUsageCategory, ToolUsage tdsToolUsage) {
		try {
			FieldCheckType fieldCheckType;
			
			toolUsageCategory.setType(tdsToolUsage.getType());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			toolUsageCategory.setTypeFieldCheckType(fieldCheckType);
			validateField(tdsToolUsage, EnumFieldCheckType.P, EnumToolUsageFieldName.type, fieldCheckType);
			
			toolUsageCategory.setCode(tdsToolUsage.getCode());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			toolUsageCategory.setCodeFieldCheckType(fieldCheckType);
			validateField(tdsToolUsage, EnumFieldCheckType.P, EnumToolUsageFieldName.code, fieldCheckType);
		} catch (Exception e) {
			logger.error("analysisToolUsage exception: ", e);
		}
	}
	
	private void validateField(ToolUsage tdsToolUsage, EnumFieldCheckType enumFieldCheckType, 
			EnumToolUsageFieldName enumFieldName, FieldCheckType fieldCheckType)
	{
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(tdsToolUsage, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(tdsToolUsage, enumFieldName, fieldCheckType);
				//checkC(tdsToolUsage, enumFieldName, fieldCheckType);
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
		}
	}
	
	private void checkP(ToolUsage tdsToolUsage, EnumToolUsageFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case type:
				// 	<xs:attribute name="type" use="required" />
				processP_PritableASCIIone(tdsToolUsage.getType(), fieldCheckType);
				break;
			case code:
				// 	<xs:attribute name="code" use="required" />
				processP_PritableASCIIone(tdsToolUsage.getCode(), fieldCheckType);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}
	
	private void analysisToolPages(ToolUsageCategory toolUsageCategory, List<ToolPage> toolPages){
		try {
			ToolPageCategory toolPageCategory;
			List<ToolPageCategory> toolPageCategories = toolUsageCategory.getToolPageCategories();
			for(ToolPage t: toolPages){
				toolPageCategory = new ToolPageCategory();
				toolPageCategories.add(toolPageCategory);
				analysisToolPage(toolPageCategory, t);
			}
			
		} catch (Exception e) {
			logger.error("analysisToolPages exception: ", e);
		}
	}
	
	private void analysisToolPage(ToolPageCategory toolPageCategory, ToolPage tdsToolPage) {
		try {
			FieldCheckType fieldCheckType;
			
			toolPageCategory.setPage(tdsToolPage.getPage());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			toolPageCategory.setPageFieldCheckType(fieldCheckType);
			validateField(tdsToolPage, EnumFieldCheckType.P, EnumToolPageFieldName.page, fieldCheckType);
			
			toolPageCategory.setGroupId(tdsToolPage.getGroupId());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			toolPageCategory.setGroupIdFieldCheckType(fieldCheckType);
			validateField(tdsToolPage, EnumFieldCheckType.P, EnumToolPageFieldName.groupId, fieldCheckType);
			
			toolPageCategory.setCount(tdsToolPage.getCount());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			toolPageCategory.setCountFieldCheckType(fieldCheckType);
			validateField(tdsToolPage, EnumFieldCheckType.P, EnumToolPageFieldName.count, fieldCheckType);
		}catch (Exception e) {
			logger.error("analysisToolPage exception: ", e);
		}
	}
	
	private void validateField(ToolPage tdsToolPage, EnumFieldCheckType enumFieldCheckType, 
			EnumToolPageFieldName enumFieldName, FieldCheckType fieldCheckType)
	{
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(tdsToolPage, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(tdsToolPage, enumFieldName, fieldCheckType);
				//checkC(tdsToolPage, enumFieldName, fieldCheckType);
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
		}
	}
	
	private void checkP(ToolPage tdsToolPage, EnumToolPageFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case page:
				// 	<xs:attribute name="page" use="required" type="xs:unsignedInt" />
				processP_Positive32bit(Long.toString(tdsToolPage.getPage()), fieldCheckType);
				break;
			case groupId:
				// 	<xs:attribute name="groupId" use="required" />
				processP_PritableASCIIone(tdsToolPage.getGroupId(), fieldCheckType);
				break;
			case count:
				// 	<xs:attribute name="count" use="required" type="xs:unsignedInt" />
				processP_Positive32bit(Long.toString(tdsToolPage.getCount()), fieldCheckType);
				break;	
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}
	
}

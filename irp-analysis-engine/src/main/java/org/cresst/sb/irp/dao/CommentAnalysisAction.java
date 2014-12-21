package org.cresst.sb.irp.dao;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.analysis.CommentCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Comment;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
public class CommentAnalysisAction extends AnalysisAction {
	private static Logger logger = Logger.getLogger(CommentAnalysisAction.class);

	public enum EnumCommentFieldName {
		context, itemPosition, date, content;
	}
	
	@Override
	public void analysis(IndividualResponse individualResponse) throws IOException {
		try {
			TDSReport tdsReport = individualResponse.getTDSReport();
			List<CommentCategory> listCommentCategory = individualResponse.getCommentCategories();
			CommentCategory commentCategory;
			List<Comment> listComment = tdsReport.getComment();
			for(Comment c: listComment){
				commentCategory = new CommentCategory();
				listCommentCategory.add(commentCategory);
				analysisComment(commentCategory, c);
			}
			
		} catch (Exception e) {
			logger.error("analysis exception: ", e);
		}
	}
	
	private void analysisComment(CommentCategory commentCategory, Comment tdsComment){
		try {
			FieldCheckType fieldCheckType;
			
			commentCategory.setContext(tdsComment.getContext());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			commentCategory.setContextFieldCheckType(fieldCheckType);
			validateField(tdsComment, EnumFieldCheckType.P, EnumCommentFieldName.context, fieldCheckType);
			
			commentCategory.setItemPosition(tdsComment.getItemPosition());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			commentCategory.setItemPositionFieldCheckType(fieldCheckType);
			validateField(tdsComment, EnumFieldCheckType.P, EnumCommentFieldName.itemPosition, fieldCheckType);
			
			commentCategory.setDate(tdsComment.getDate().toString());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			commentCategory.setDateFieldCheckType(fieldCheckType);
			validateField(tdsComment, EnumFieldCheckType.P, EnumCommentFieldName.date, fieldCheckType);
			
			commentCategory.setContent(tdsComment.getContent());
			fieldCheckType = new FieldCheckType();
			fieldCheckType.setEnumfieldCheckType(EnumFieldCheckType.P);
			commentCategory.setContentFieldCheckType(fieldCheckType);
			validateField(tdsComment, EnumFieldCheckType.P, EnumCommentFieldName.content, fieldCheckType);
			
		} catch (Exception e) {
			logger.error("analysisComment exception: ", e);
		}
	}
	
	private void validateField(Comment tdsComment, EnumFieldCheckType enumFieldCheckType, EnumCommentFieldName enumFieldName,
			FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldCheckType) {
			case D:
				break;
			case P:
				checkP(tdsComment, enumFieldName, fieldCheckType);
				break;
			case PC:
				checkP(tdsComment, enumFieldName, fieldCheckType);
				//checkC(tdsComment, enumFieldName, fieldCheckType);
				break;
			}
		} catch (Exception e) {
			logger.error("validateField exception: ", e);
		}
	}
	
	private void checkP(Comment tdsComment, EnumCommentFieldName enumFieldName, FieldCheckType fieldCheckType) {
		try {
			switch (enumFieldName) {
			case context:
				// <xs:attribute name="context" use="required" />
				processP_PritableASCIIone(tdsComment.getContext(), fieldCheckType);
				break;
			case itemPosition:
				// <xs:attribute name="itemPosition" type="NullableUInt" />
				//Positive 32-bit integer, Null allowed
				if (tdsComment.getItemPosition().length() == 0)
					setPcorrect(fieldCheckType);
				else
					processP_Positive32bit(tdsComment.getItemPosition(), fieldCheckType);
				break;
			case date:
				//<xs:attribute name="date" use="required" type="xs:dateTime" />
				processP(tdsComment.getDate().toString(), fieldCheckType, true); //required Y
				break;
			case content:
				processP(tdsComment.getContent(), fieldCheckType, false); //required N
				break;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("checkP exception: ", e);
		}
	}
	

}

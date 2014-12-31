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
import org.springframework.stereotype.Service;

@Service
public class CommentAnalysisAction extends AnalysisAction<Comment, CommentAnalysisAction.EnumCommentFieldName> {
	private static Logger logger = Logger.getLogger(CommentAnalysisAction.class);

	static public enum EnumCommentFieldName {
		context, itemPosition, date, content
	}
	
	@Override
	public void analyze(IndividualResponse individualResponse) throws IOException {
		TDSReport tdsReport = individualResponse.getTDSReport();
		List<CommentCategory> listCommentCategory = individualResponse.getCommentCategories();
		CommentCategory commentCategory;
		List<Comment> listComment = tdsReport.getComment();
		for(Comment c: listComment){
			commentCategory = new CommentCategory();
			listCommentCategory.add(commentCategory);
			analysisComment(commentCategory, c);
		}
	}
	
	private void analysisComment(CommentCategory commentCategory, Comment tdsComment){
		validate(commentCategory, tdsComment, tdsComment.getContext(), EnumFieldCheckType.P, EnumCommentFieldName.context);
		validate(commentCategory, tdsComment, tdsComment.getItemPosition(), EnumFieldCheckType.P, EnumCommentFieldName.itemPosition);
		validate(commentCategory, tdsComment, tdsComment.getDate(), EnumFieldCheckType.P, EnumCommentFieldName.date);
		validate(commentCategory, tdsComment, tdsComment.getContent(), EnumFieldCheckType.P, EnumCommentFieldName.content);
	}

	@Override
	protected void checkP(Comment tdsComment, EnumCommentFieldName enumFieldName, FieldCheckType fieldCheckType) {
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

	/**
	 * Checks if the field has correct value
	 *
	 * @param checkObj       Object with fields to check
	 * @param enumFieldName  Specifies the field to check
	 * @param fieldCheckType This is where the results are stored
	 */
	@Override
	protected void checkC(Comment checkObj, EnumCommentFieldName enumFieldName, FieldCheckType fieldCheckType) {

	}

}

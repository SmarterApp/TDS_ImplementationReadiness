package org.cresst.sb.irp.analysis.engine;

import java.util.List;

import org.cresst.sb.irp.domain.analysis.CommentCategory;
import org.cresst.sb.irp.domain.analysis.FieldCheckType;
import org.cresst.sb.irp.domain.analysis.FieldCheckType.EnumFieldCheckType;
import org.cresst.sb.irp.domain.analysis.IndividualResponse;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CommentAnalysisAction extends AnalysisAction<Comment, CommentAnalysisAction.EnumCommentFieldName, Object> {
    private final static Logger logger = LoggerFactory.getLogger(CommentAnalysisAction.class);

    public enum EnumCommentFieldName {
        // 'unlimited' in Transmission format is represented as
        // Integer.MAX_VALUE
        context(200), itemPosition(8), date(23), content(Integer.MAX_VALUE);

        private int maxWidth;
        private boolean isRequired;

        EnumCommentFieldName(int maxWidth) {
            this.maxWidth = maxWidth;
            this.isRequired = true;
        }

        EnumCommentFieldName(int maxWidth, boolean isRequired) {
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

    @Override
    public void analyze(IndividualResponse individualResponse) {
        TDSReport tdsReport = individualResponse.getTDSReport();
        List<CommentCategory> listCommentCategory = individualResponse.getCommentCategories();
        CommentCategory commentCategory;
        List<Comment> listComment = tdsReport.getComment();
        for (Comment c : listComment) {
            commentCategory = new CommentCategory();
            listCommentCategory.add(commentCategory);
            analysisComment(commentCategory, c);
        }
    }

    private void analysisComment(CommentCategory commentCategory, Comment tdsComment) {
        validate(commentCategory, tdsComment, tdsComment.getContext(), EnumFieldCheckType.D, EnumCommentFieldName.context, null);
        validate(commentCategory, tdsComment, tdsComment.getItemPosition(), EnumFieldCheckType.D, EnumCommentFieldName.itemPosition, null);
        validate(commentCategory, tdsComment, tdsComment.getDate(), EnumFieldCheckType.D, EnumCommentFieldName.date, null);
        validate(commentCategory, tdsComment, tdsComment.getContent(), EnumFieldCheckType.D, EnumCommentFieldName.content, null);
    }

    @Override
    protected void checkP(Comment tdsComment, EnumCommentFieldName enumFieldName, FieldCheckType fieldCheckType) {
        try {
            fieldCheckType.setOptionalValue(true);
            switch (enumFieldName) {
                case context:
                    // <xs:attribute name="context" use="required" />
                    processP_PrintableASCIIoneMaxWidth(tdsComment.getContext(), fieldCheckType, 200);
                    break;
                case itemPosition:
                    // <xs:attribute name="itemPosition" type="NullableUInt" />
                    // Positive 32-bit integer, Null allowed
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
     * Noop for now
     *
     * @param checkObj       Object with fields to check
     * @param enumFieldName  Specifies the field to check
     * @param fieldCheckType This is where the results are stored
     * @param unused         Unused parameter
     */
    @Override
    protected void checkC(Comment checkObj, EnumCommentFieldName enumFieldName, FieldCheckType fieldCheckType, Object unused) {
        switch (enumFieldName) {
            case context:
                break;
            default:
                break;
        }
    }
}

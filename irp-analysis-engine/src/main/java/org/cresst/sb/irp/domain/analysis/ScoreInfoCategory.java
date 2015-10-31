package org.cresst.sb.irp.domain.analysis;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ScoreInfoCategory extends Category {

    private ScoreRationaleCategory scoreRationaleCategory;

    public ScoreRationaleCategory getScoreRationaleCategory() {
        return scoreRationaleCategory;
    }

    public void setScoreRationaleCategory(ScoreRationaleCategory scoreRationaleCategory) {
        this.scoreRationaleCategory = scoreRationaleCategory;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
    
}

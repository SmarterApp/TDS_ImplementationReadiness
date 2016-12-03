package org.cresst.sb.irp.cat.domain.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BlueprintStatement {
    private final static Logger logger = LoggerFactory.getLogger(BlueprintStatement.class);

    private String specification;
    private int min;
    private int max;
    private int grade;
    // Number of "true" occurrences of the blueprint test
    private int matchCount;
    // Number of occurrences are accumulated into violation count
    private ViolationCount violationCount = new ViolationCount();

    @JsonIgnore
    private BlueprintCondition condition;

    public BlueprintStatement(){}

    public BlueprintStatement(String claimName, int min, int max, BlueprintCondition condition) {
        this.specification = claimName;
        this.min = min;
        this.max = max;
        this.condition = condition;
        this.matchCount = 0;
        this.violationCount = new ViolationCount();
    }

    public String getSpecification() {
        return specification;
    }
    public void setSpecification(String claimName) {
        this.specification = claimName;
    }

    public int getMin() {
        return min;
    }

    public String getMaxStr() {
        if (max == Integer.MAX_VALUE) {
            return "NA";
        }
            return String.valueOf(max);
    }

    public void setMin(int min) {
        this.min = min;
    }
    public int getMax() {
        return max;
    }
    public void setMax(int max) {
        this.max = max;
    }
    @Override
    public String toString() {
        return "BlueprintStatement [specification=" + specification + ", min=" + min + ", max="
                + max + ", matchCount=" + matchCount + ", violationCount=" + violationCount + "]";
    }

    public boolean test(PoolItem item) {
        if (condition != null) {
            return condition.test(item);
        }
        logger.warn("No condition specified.");
        return false;
    }

    public BlueprintCondition getCondition() {
        return condition;
    }

    /**
     * 
     * @param condition generally an inline anonymous class that overrides the `test` method
     * Allows us to set a custom function for each instance of the blueprint statement.
     */
    public void setCondition(BlueprintCondition condition) {
        this.condition = condition;
    }

    public ViolationCount getViolationCount() {
        return violationCount;
    }

    public void setViolationCount(ViolationCount violationCount) {
        this.violationCount = violationCount;
    }

    public void incMatch() {
        matchCount += 1;
    }

    /**
     * Clears the match count for the current blueprint and updates violation count
     */
    public void updateViolations() {
        if (matchCount < min) {
            violationCount.incUnder();
        } else if (matchCount >= min && matchCount <= max) {
            violationCount.incMatch();
        } else {
            violationCount.incOver();
        }
        matchCount = 0;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}

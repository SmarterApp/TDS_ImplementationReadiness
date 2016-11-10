package org.cresst.sb.irp.cat.domain.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BlueprintStatement {
    private final static Logger logger = LoggerFactory.getLogger(BlueprintStatement.class);

    private String claimName;
    private int claimNumber;
    private int min;
    private int max;
    private int matchCount;
    private ViolationCount violationCount = new ViolationCount();

    @JsonIgnore
    private BlueprintCondition condition;

    public BlueprintStatement(){}

    public BlueprintStatement(String claimName, int claimNumber, int min, int max, BlueprintCondition condition) {
        this.claimName = claimName;
        this.claimNumber = claimNumber;
        this.min = min;
        this.max = max;
        this.condition = condition;
        this.matchCount = 0;
        this.violationCount = new ViolationCount();
    }

    public String getClaimName() {
        return claimName;
    }
    public void setClaimName(String claimName) {
        this.claimName = claimName;
    }
    public int getClaimNumber() {
        return claimNumber;
    }
    public void setClaimNumber(int claimNumber) {
        this.claimNumber = claimNumber;
    }
    public int getMin() {
        return min;
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
        return "BlueprintStatement [claimName=" + claimName + ", claimNumber=" + claimNumber + ", min=" + min + ", max="
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
}

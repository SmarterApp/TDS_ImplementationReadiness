package org.cresst.sb.irp.cat.domain.analysis;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Data model for an ELA Pool Item, excepts rows to be formatted in order shown in JsonPropertyOrder
 */
@JsonPropertyOrder({
    "stimId","itemId","subject","itemGrade","poolGrade","claim","target","useTarget","blueprintTarget",
    "dok","asmtType","itemType","irtA","irtB","irtC",
    "irtStep1","irtStep2","irtStep3","irtStep4","irtStep5","irtStep6","irtStep7","irtStep8",
    "braille","enemyItem","extPool","shortAnswer","fullWrite","passage",
    "writeRevise","claim2cat","stimMax","stimMin","spaErrors"
})
public class PoolItemELA implements PoolItem {
    private String stimId;
    private String itemId;
    private String subject;
    private String itemGrade;
    private String poolGrade;
    private String claim;
    private String target;
    private String useTarget;
    private String blueprintTarget;
    // Depth of knowledge
    private String dok;
    private String asmtType;
    private String itemType;
    private String irtA;
    private String irtB;
    private String irtC;
    private String irtStep1;
    private String irtStep2;
    private String irtStep3;
    private String irtStep4;
    private String irtStep5;
    private String irtStep6;
    private String irtStep7;
    private String irtStep8;
    private String braille;
    private String enemyItem;
    private String extPool;
    private String shortAnswer;
    private String fullWrite;
    private String passage;
    private String writeRevise;
    private String claim2cat;
    private String stimMax;
    private String stimMin;
    private String spaErrors;

    @Override
    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getItemGrade() {
        return itemGrade;
    }
    public void setItemGrade(String itemGrade) {
        this.itemGrade = itemGrade;
    }
    @Override
    public String getClaim() {
        return claim;
    }
    public void setClaim(String claim) {
        this.claim = claim;
    }
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public String getUseTarget() {
        return useTarget;
    }
    public void setUseTarget(String useTarget) {
        this.useTarget = useTarget;
    }
    public String getBlueprintTarget() {
        return blueprintTarget;
    }
    public void setBlueprintTarget(String blueprintTarget) {
        this.blueprintTarget = blueprintTarget;
    }
    public String getDok() {
        return dok;
    }
    public void setDok(String dok) {
        this.dok = dok;
    }
    public String getStimId() {
        return stimId;
    }
    public void setStimId(String stimId) {
        this.stimId = stimId;
    }
    public String getAsmtType() {
        return asmtType;
    }
    public void setAsmtType(String asmtType) {
        this.asmtType = asmtType;
    }
    public String getItemType() {
        return itemType;
    }
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    public String getIrtA() {
        return irtA;
    }
    public void setIrtA(String irtA) {
        this.irtA = irtA;
    }
    public String getIrtB() {
        return irtB;
    }
    public void setIrtB(String irtB) {
        this.irtB = irtB;
    }
    public String getIrtC() {
        return irtC;
    }
    public void setIrtC(String irtC) {
        this.irtC = irtC;
    }
    public String getIrtStep1() {
        return irtStep1;
    }
    public void setIrtStep1(String irtStep1) {
        this.irtStep1 = irtStep1;
    }
    public String getIrtStep2() {
        return irtStep2;
    }
    public void setIrtStep2(String irtStep2) {
        this.irtStep2 = irtStep2;
    }
    public String getIrtStep3() {
        return irtStep3;
    }
    public void setIrtStep3(String irtStep3) {
        this.irtStep3 = irtStep3;
    }
    public String getIrtStep4() {
        return irtStep4;
    }
    public void setIrtStep4(String irtStep4) {
        this.irtStep4 = irtStep4;
    }
    public String getIrtStep5() {
        return irtStep5;
    }
    public void setIrtStep5(String irtStep5) {
        this.irtStep5 = irtStep5;
    }
    public String getIrtStep6() {
        return irtStep6;
    }
    public void setIrtStep6(String irtStep6) {
        this.irtStep6 = irtStep6;
    }
    public String getIrtStep7() {
        return irtStep7;
    }
    public void setIrtStep7(String irtStep7) {
        this.irtStep7 = irtStep7;
    }
    public String getIrtStep8() {
        return irtStep8;
    }
    public void setIrtStep8(String irtStep8) {
        this.irtStep8 = irtStep8;
    }
    public String getBraille() {
        return braille;
    }
    public void setBraille(String braille) {
        this.braille = braille;
    }
    public String getEnemyItem() {
        return enemyItem;
    }
    public void setEnemyItem(String enemyItem) {
        this.enemyItem = enemyItem;
    }
    public String getPoolGrade() {
        return poolGrade;
    }
    public void setPoolGrade(String poolGrade) {
        this.poolGrade = poolGrade;
    }
    public String getExtPool() {
        return extPool;
    }
    public void setExtPool(String extPool) {
        this.extPool = extPool;
    }
    public String getShortAnswer() {
        return shortAnswer;
    }
    public void setShortAnswer(String shortAnswer) {
        this.shortAnswer = shortAnswer;
    }
    public String getFullWrite() {
        return fullWrite;
    }
    public void setFullWrite(String fullWrite) {
        this.fullWrite = fullWrite;
    }
    public String getSpaErrors() {
        return spaErrors;
    }
    public void setSpaErrors(String spaErrors) {
        this.spaErrors = spaErrors;
    }
    @Override
    public String toString() {
        return "PoolItemELA [stimId=" + stimId + ", itemId=" + itemId + ", subject=" + subject + ", itemGrade="
                + itemGrade + ", poolGrade=" + poolGrade + ", claim=" + claim + ", target=" + target + ", useTarget="
                + useTarget + ", blueprintTarget=" + blueprintTarget + ", dok=" + dok + ", asmtType=" + asmtType
                + ", itemType=" + itemType + ", irtA=" + irtA + ", irtB=" + irtB + ", irtC=" + irtC + ", irtStep1="
                + irtStep1 + ", irtStep2=" + irtStep2 + ", irtStep3=" + irtStep3 + ", irtStep4=" + irtStep4
                + ", irtStep5=" + irtStep5 + ", irtStep6=" + irtStep6 + ", irtStep7=" + irtStep7 + ", irtStep8="
                + irtStep8 + ", braille=" + braille + ", enemyItem=" + enemyItem + ", extPool=" + extPool
                + ", shortAnswer=" + shortAnswer + ", fullWrite=" + fullWrite + ", passage=" + passage
                + ", writeRevise=" + writeRevise + ", claim2cat=" + claim2cat + ", stimMax=" + stimMax + ", stimMin="
                + stimMin + ", spaErrors=" + spaErrors + "]";
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((asmtType == null) ? 0 : asmtType.hashCode());
        result = prime * result + ((blueprintTarget == null) ? 0 : blueprintTarget.hashCode());
        result = prime * result + ((braille == null) ? 0 : braille.hashCode());
        result = prime * result + ((claim == null) ? 0 : claim.hashCode());
        result = prime * result + ((claim2cat == null) ? 0 : claim2cat.hashCode());
        result = prime * result + ((dok == null) ? 0 : dok.hashCode());
        result = prime * result + ((enemyItem == null) ? 0 : enemyItem.hashCode());
        result = prime * result + ((extPool == null) ? 0 : extPool.hashCode());
        result = prime * result + ((fullWrite == null) ? 0 : fullWrite.hashCode());
        result = prime * result + ((irtA == null) ? 0 : irtA.hashCode());
        result = prime * result + ((irtB == null) ? 0 : irtB.hashCode());
        result = prime * result + ((irtC == null) ? 0 : irtC.hashCode());
        result = prime * result + ((irtStep1 == null) ? 0 : irtStep1.hashCode());
        result = prime * result + ((irtStep2 == null) ? 0 : irtStep2.hashCode());
        result = prime * result + ((irtStep3 == null) ? 0 : irtStep3.hashCode());
        result = prime * result + ((irtStep4 == null) ? 0 : irtStep4.hashCode());
        result = prime * result + ((irtStep5 == null) ? 0 : irtStep5.hashCode());
        result = prime * result + ((irtStep6 == null) ? 0 : irtStep6.hashCode());
        result = prime * result + ((irtStep7 == null) ? 0 : irtStep7.hashCode());
        result = prime * result + ((irtStep8 == null) ? 0 : irtStep8.hashCode());
        result = prime * result + ((itemGrade == null) ? 0 : itemGrade.hashCode());
        result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
        result = prime * result + ((itemType == null) ? 0 : itemType.hashCode());
        result = prime * result + ((passage == null) ? 0 : passage.hashCode());
        result = prime * result + ((poolGrade == null) ? 0 : poolGrade.hashCode());
        result = prime * result + ((shortAnswer == null) ? 0 : shortAnswer.hashCode());
        result = prime * result + ((spaErrors == null) ? 0 : spaErrors.hashCode());
        result = prime * result + ((stimId == null) ? 0 : stimId.hashCode());
        result = prime * result + ((stimMax == null) ? 0 : stimMax.hashCode());
        result = prime * result + ((stimMin == null) ? 0 : stimMin.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        result = prime * result + ((useTarget == null) ? 0 : useTarget.hashCode());
        result = prime * result + ((writeRevise == null) ? 0 : writeRevise.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PoolItemELA other = (PoolItemELA) obj;
        if (asmtType == null) {
            if (other.asmtType != null)
                return false;
        } else if (!asmtType.equals(other.asmtType))
            return false;
        if (blueprintTarget == null) {
            if (other.blueprintTarget != null)
                return false;
        } else if (!blueprintTarget.equals(other.blueprintTarget))
            return false;
        if (braille == null) {
            if (other.braille != null)
                return false;
        } else if (!braille.equals(other.braille))
            return false;
        if (claim == null) {
            if (other.claim != null)
                return false;
        } else if (!claim.equals(other.claim))
            return false;
        if (claim2cat == null) {
            if (other.claim2cat != null)
                return false;
        } else if (!claim2cat.equals(other.claim2cat))
            return false;
        if (dok == null) {
            if (other.dok != null)
                return false;
        } else if (!dok.equals(other.dok))
            return false;
        if (enemyItem == null) {
            if (other.enemyItem != null)
                return false;
        } else if (!enemyItem.equals(other.enemyItem))
            return false;
        if (extPool == null) {
            if (other.extPool != null)
                return false;
        } else if (!extPool.equals(other.extPool))
            return false;
        if (fullWrite == null) {
            if (other.fullWrite != null)
                return false;
        } else if (!fullWrite.equals(other.fullWrite))
            return false;
        if (irtA == null) {
            if (other.irtA != null)
                return false;
        } else if (!irtA.equals(other.irtA))
            return false;
        if (irtB == null) {
            if (other.irtB != null)
                return false;
        } else if (!irtB.equals(other.irtB))
            return false;
        if (irtC == null) {
            if (other.irtC != null)
                return false;
        } else if (!irtC.equals(other.irtC))
            return false;
        if (irtStep1 == null) {
            if (other.irtStep1 != null)
                return false;
        } else if (!irtStep1.equals(other.irtStep1))
            return false;
        if (irtStep2 == null) {
            if (other.irtStep2 != null)
                return false;
        } else if (!irtStep2.equals(other.irtStep2))
            return false;
        if (irtStep3 == null) {
            if (other.irtStep3 != null)
                return false;
        } else if (!irtStep3.equals(other.irtStep3))
            return false;
        if (irtStep4 == null) {
            if (other.irtStep4 != null)
                return false;
        } else if (!irtStep4.equals(other.irtStep4))
            return false;
        if (irtStep5 == null) {
            if (other.irtStep5 != null)
                return false;
        } else if (!irtStep5.equals(other.irtStep5))
            return false;
        if (irtStep6 == null) {
            if (other.irtStep6 != null)
                return false;
        } else if (!irtStep6.equals(other.irtStep6))
            return false;
        if (irtStep7 == null) {
            if (other.irtStep7 != null)
                return false;
        } else if (!irtStep7.equals(other.irtStep7))
            return false;
        if (irtStep8 == null) {
            if (other.irtStep8 != null)
                return false;
        } else if (!irtStep8.equals(other.irtStep8))
            return false;
        if (itemGrade == null) {
            if (other.itemGrade != null)
                return false;
        } else if (!itemGrade.equals(other.itemGrade))
            return false;
        if (itemId == null) {
            if (other.itemId != null)
                return false;
        } else if (!itemId.equals(other.itemId))
            return false;
        if (itemType == null) {
            if (other.itemType != null)
                return false;
        } else if (!itemType.equals(other.itemType))
            return false;
        if (passage == null) {
            if (other.passage != null)
                return false;
        } else if (!passage.equals(other.passage))
            return false;
        if (poolGrade == null) {
            if (other.poolGrade != null)
                return false;
        } else if (!poolGrade.equals(other.poolGrade))
            return false;
        if (shortAnswer == null) {
            if (other.shortAnswer != null)
                return false;
        } else if (!shortAnswer.equals(other.shortAnswer))
            return false;
        if (spaErrors == null) {
            if (other.spaErrors != null)
                return false;
        } else if (!spaErrors.equals(other.spaErrors))
            return false;
        if (stimId == null) {
            if (other.stimId != null)
                return false;
        } else if (!stimId.equals(other.stimId))
            return false;
        if (stimMax == null) {
            if (other.stimMax != null)
                return false;
        } else if (!stimMax.equals(other.stimMax))
            return false;
        if (stimMin == null) {
            if (other.stimMin != null)
                return false;
        } else if (!stimMin.equals(other.stimMin))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        if (target == null) {
            if (other.target != null)
                return false;
        } else if (!target.equals(other.target))
            return false;
        if (useTarget == null) {
            if (other.useTarget != null)
                return false;
        } else if (!useTarget.equals(other.useTarget))
            return false;
        if (writeRevise == null) {
            if (other.writeRevise != null)
                return false;
        } else if (!writeRevise.equals(other.writeRevise))
            return false;
        return true;
    }
    public String getWriteRevise() {
        return writeRevise;
    }
    public void setWriteRevise(String writeRevise) {
        this.writeRevise = writeRevise;
    }
    public String getClaim2cat() {
        return claim2cat;
    }
    public void setClaim2cat(String claim2cat) {
        this.claim2cat = claim2cat;
    }
    public String getStimMax() {
        return stimMax;
    }
    public void setStimMax(String stimMax) {
        this.stimMax = stimMax;
    }
    public String getStimMin() {
        return stimMin;
    }
    public void setStimMin(String stimMin) {
        this.stimMin = stimMin;
    }
    public String getPassage() {
        return passage;
    }
    public void setPassage(String passage) {
        this.passage = passage;
    }

}

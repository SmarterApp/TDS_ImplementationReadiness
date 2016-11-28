package org.cresst.sb.irp.cat.domain.analysis;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Data model for an Math Pool Item, excepts rows to be formatted in order shown in JsonPropertyOrder
 */
@JsonPropertyOrder({
    "itemId","subject","itemGrade","poolGrade","claim","target","dok","stimId","asmtType",
    "itemType","calculator","irtA","irtB","irtC","irtStep1","irtStep2","irtStep3",
    "irtStep4","irtStep5","irtStep6","irtStep7","irtStep8","braille","translation","extPool","spaErrors"
})
public class PoolItemMath implements PoolItem {
    private String itemId;
    private String subject;
    private String itemGrade;
    private String poolGrade;
    private String claim;
    private String target;
    // Depth of knowledge
    private String dok;
    private String stimId;
    private String asmtType;
    private String itemType;
    private String calculator;
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
    private String translation;
    private String extPool;
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
    public String getPoolGrade() {
        return poolGrade;
    }
    public void setPoolGrade(String poolGrade) {
        this.poolGrade = poolGrade;
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
    public String getCalculator() {
        return calculator;
    }
    public void setCalculator(String calculator) {
        this.calculator = calculator;
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
    public String getTranslation() {
        return translation;
    }
    public void setTranslation(String translation) {
        this.translation = translation;
    }
    public String getExtPool() {
        return extPool;
    }
    public void setExtPool(String extPool) {
        this.extPool = extPool;
    }
    public String getSpaErrors() {
        return spaErrors;
    }
    public void setSpaErrors(String spaErrors) {
        this.spaErrors = spaErrors;
    }
    @Override
    public String toString() {
        return "PoolItemMath [itemId=" + itemId + ", subject=" + subject + ", itemGrade=" + itemGrade + ", poolGrade="
                + poolGrade + ", claim=" + claim + ", target=" + target + ", dok=" + dok + ", stimId=" + stimId
                + ", asmtType=" + asmtType + ", itemType=" + itemType + ", calculator=" + calculator + ", irtA=" + irtA
                + ", irtB=" + irtB + ", irtC=" + irtC + ", irtStep1=" + irtStep1 + ", irtStep2=" + irtStep2
                + ", irtStep3=" + irtStep3 + ", irtStep4=" + irtStep4 + ", irtStep5=" + irtStep5 + ", irtStep6="
                + irtStep6 + ", irtStep7=" + irtStep7 + ", irtStep8=" + irtStep8 + ", braille=" + braille
                + ", translation=" + translation + ", extPool=" + extPool + ", spaErrors=" + spaErrors + "]";
    }
    @Override
    public int hashCode() {
        return Objects.hash(itemId,subject,itemGrade,poolGrade, claim, target, dok,
                stimId, asmtType, itemType, calculator, irtA, irtB, irtC, irtStep1,
                irtStep2, irtStep3, irtStep4, irtStep5, irtStep6, irtStep7, irtStep8,
                braille, translation, extPool, spaErrors);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PoolItemMath other = (PoolItemMath) obj;
        if (asmtType == null) {
            if (other.asmtType != null)
                return false;
        } else if (!asmtType.equals(other.asmtType))
            return false;
        if (braille == null) {
            if (other.braille != null)
                return false;
        } else if (!braille.equals(other.braille))
            return false;
        if (calculator == null) {
            if (other.calculator != null)
                return false;
        } else if (!calculator.equals(other.calculator))
            return false;
        if (claim == null) {
            if (other.claim != null)
                return false;
        } else if (!claim.equals(other.claim))
            return false;
        if (dok == null) {
            if (other.dok != null)
                return false;
        } else if (!dok.equals(other.dok))
            return false;
        if (extPool == null) {
            if (other.extPool != null)
                return false;
        } else if (!extPool.equals(other.extPool))
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
        if (poolGrade == null) {
            if (other.poolGrade != null)
                return false;
        } else if (!poolGrade.equals(other.poolGrade))
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
        if (translation == null) {
            if (other.translation != null)
                return false;
        } else if (!translation.equals(other.translation))
            return false;
        return true;
    }
}

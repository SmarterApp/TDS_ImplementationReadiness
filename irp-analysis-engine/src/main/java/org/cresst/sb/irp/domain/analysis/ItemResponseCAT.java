package org.cresst.sb.irp.domain.analysis;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"sId", "itemId", "score"})
public class ItemResponseCAT {
    private String sId;
    private String itemId;
    private int score;

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "ItemResponseCAT [sId=" + sId + ", itemId=" + itemId + ", score=" + score + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(sId, itemId, score);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ItemResponseCAT other = (ItemResponseCAT) obj;
        if (itemId == null) {
            if (other.itemId != null)
                return false;
        } else if (!itemId.equals(other.itemId))
            return false;
        if (sId == null) {
            if (other.sId != null)
                return false;
        } else if (!sId.equals(other.sId))
            return false;
        if (score != other.score)
            return false;
        return true;
    }


}

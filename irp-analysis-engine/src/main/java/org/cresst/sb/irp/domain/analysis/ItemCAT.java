package org.cresst.sb.irp.domain.analysis;

public class ItemCAT {
    private String sId;
    private String itemId;
    private int score;

    public ItemCAT(String sId, String itemId, int score) {
        this.sId = sId;
        this.itemId = itemId;
        this.score = score;
    }

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


}

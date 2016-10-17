package org.cresst.sb.irp.domain.analysis;

public class ItemResponseCAT {
    private String sId;
    private String itemId;
    private int score;

    public ItemResponseCAT(String sId, String itemId, int score) {
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

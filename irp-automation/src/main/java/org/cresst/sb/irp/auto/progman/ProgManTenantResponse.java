package org.cresst.sb.irp.auto.progman;

public class ProgManTenantResponse {
    private int returnCount;
    private ProgManTenantSearchResult[] searchResults;

    public ProgManTenantResponse() {}

    public int getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(int returnCount) {
        this.returnCount = returnCount;
    }

    public ProgManTenantSearchResult[] getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(ProgManTenantSearchResult[] searchResults) {
        this.searchResults = searchResults;
    }
}

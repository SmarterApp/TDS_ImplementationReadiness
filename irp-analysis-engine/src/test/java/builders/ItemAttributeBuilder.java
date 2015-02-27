package builders;

import org.cresst.sb.irp.domain.tdsreport.TDSReport;

/**
 * Builder for ItemAttribute objects
 */
public class ItemAttributeBuilder {
    private TDSReport.Opportunity.Item opportunityItem = new TDSReport.Opportunity.Item();

    public ItemAttributeBuilder() {}

    public ItemAttributeBuilder bankKey(long bankKey) {
        opportunityItem.setBankKey(bankKey);
        return this;
    }

    public ItemAttributeBuilder key(long key) {
        opportunityItem.setKey(key);
        return this;
    }

    public TDSReport.Opportunity.Item toOpportunityItem() {
        return opportunityItem;
    }
}

package builders;

import org.cresst.sb.irp.domain.tdsreport.TDSReport;
import org.cresst.sb.irp.domain.tdsreport.TDSReport.Opportunity.GenericVariable;

public class GenericVariableBuilder {

	private TDSReport.Opportunity.GenericVariable genericVariable = new GenericVariable();

	public GenericVariableBuilder name(String name) {
		genericVariable.setName(name);
		return this;
	}

	public GenericVariableBuilder value(String value) {
		genericVariable.setValue(value);
		return this;
	}

	public GenericVariableBuilder context(String context) {
		genericVariable.setContext(context);
		return this;
	}

	public TDSReport.Opportunity.GenericVariable toGenericVariable() {
		return genericVariable;
	}

}

package builders;

import org.cresst.sb.irp.domain.items.Itemrelease.Item.MachineRubric;

public class MachineRubricBuilder {
	
	private org.cresst.sb.irp.domain.items.Itemrelease.Item.MachineRubric machineRubric = new MachineRubric();
	
	public MachineRubricBuilder filename(String filename){
		machineRubric.setFilename(filename);
		return this;
	}
	
	
	public MachineRubric toMachineRubric(){
		return machineRubric;
	}

}

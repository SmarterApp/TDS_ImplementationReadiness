package org.cresst.sb.irp.dao;

import org.cresst.sb.irp.domain.items.ItemAttribute;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.manifest.Manifest;

import java.io.FileNotFoundException;
import java.util.List;
import javax.xml.bind.JAXBException;

public interface ItemDao {
	
	void loadData(List<Manifest.Resources> listResources) throws JAXBException, FileNotFoundException;

	Itemrelease.Item getItemById(int id);

	ItemAttribute getItemAttribute(int id);

	Itemrelease.Item.Attriblist getAttriblist(int id);

	Itemrelease.Item.Attriblist.Attrib getAttribByIntAttid(int id, int attid); // index of attriblist

	Itemrelease.Item.Attriblist.Attrib getAttribByStrAttid(int id, String attid);// <attrib attid="itm_item_desc">

	Itemrelease.Item.Tutorial gettutorial(int id);
	
	Itemrelease.Item.Resourceslist getResourceslist(int id);
	
	String getStatistic(int id);
	
	Itemrelease.Item.MachineRubric getMachineRubric(int id);
	
	Itemrelease.Item.RendererSpec getRendererSpec(int id);
	
	String getGridanswerspace(int id);
	
	List<Itemrelease.Item.Content> getContents(int id);
	
	Itemrelease.Item.Content getContentByLanguage(int id, String language);
	
	List<Itemrelease.Item.KeywordList> getkeywordList(int id);

}

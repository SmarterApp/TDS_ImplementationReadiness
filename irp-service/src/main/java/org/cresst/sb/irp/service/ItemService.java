package org.cresst.sb.irp.service;

import org.cresst.sb.irp.domain.items.ItemAttribute;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.items.Itemrelease.Item;

import java.util.List;

public interface ItemService {

	Itemrelease.Item getItem(int id);

	Itemrelease.Item getItemByIdentifier(String Identifier);

	ItemAttribute getItemAttribute(int id);

	Itemrelease.Item.Attriblist getAttriblist(int id);

	Itemrelease.Item.Attriblist getItemAttriblistFromIRPitem(Item item);

	Itemrelease.Item.Attriblist.Attrib getAttribByIntAttid(int id, int attid); // index of attriblist

	Itemrelease.Item.Attriblist.Attrib getAttribByStrAttid(int id, String attid); // <attrib
	// attid="itm_item_desc">

	Itemrelease.Item.Attriblist.Attrib getItemAttribValueFromIRPitemAttriblist(Itemrelease.Item.Attriblist attriblist,
			String attid);

	Itemrelease.Item.Attriblist.Attrib getItemAttribFromIRPitem(Item item, String attid);

	Itemrelease.Item.Tutorial gettutorial(int id);

	Itemrelease.Item.Resourceslist getResourceslist(int id);

	String getStatistic(int id);

	Itemrelease.Item.MachineRubric getMachineRubric(int id);

	Itemrelease.Item.RendererSpec getRendererSpec(int id);

	String getGridanswerspace(int id);

	List<Itemrelease.Item.Content> getContents(int id);

	Itemrelease.Item.Content getContent(int id, String language);

	List<Itemrelease.Item.KeywordList> getkeywordList(int id);

}

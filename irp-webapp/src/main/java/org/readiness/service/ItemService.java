package org.readiness.service;

import java.util.List;

import org.readiness.items.domain.ItemAttribute;
import org.readiness.items.domain.Itemrelease.Item;
import org.readiness.items.domain.Itemrelease.Item.Attriblist;
import org.readiness.items.domain.Itemrelease.Item.Content;
import org.readiness.items.domain.Itemrelease.Item.KeywordList;
import org.readiness.items.domain.Itemrelease.Item.MachineRubric;
import org.readiness.items.domain.Itemrelease.Item.RendererSpec;
import org.readiness.items.domain.Itemrelease.Item.Resourceslist;
import org.readiness.items.domain.Itemrelease.Item.Tutorial;
import org.readiness.items.domain.Itemrelease.Item.Attriblist.Attrib;

public interface ItemService {

	Item getItem(int id);

	ItemAttribute getItemAttribute(int id);

	Attriblist getAttriblist(int id);

	Attrib getAttribByIntAttid(int id, int attid); // index of attriblist

	Attrib getAttribByStrAttid(int id, String attid); // <attrib
														// attid="itm_item_desc">

	Tutorial gettutorial(int id);
	
	Resourceslist getResourceslist(int id);
	
	String getStatistic(int id);
	
	MachineRubric getMachineRubric(int id);
	
	RendererSpec getRendererSpec(int id);
	
	String getGridanswerspace(int id);
	
	List<Content> getContents(int id);
	
	Content getContent(int id, String language);

	List<KeywordList> getkeywordList(int id);
	
}

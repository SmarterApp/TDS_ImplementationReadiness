package org.cresst.sb.irp.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.dao.ItemDao;
import org.cresst.sb.irp.domain.items.ItemAttribute;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {
	private static Logger logger = Logger.getLogger(ItemServiceImpl.class);

	@Autowired
	private ItemDao itemDao;
	
	public ItemServiceImpl(){

	}
	
	@Override
	public Itemrelease.Item getItem(int id) {
		return itemDao.getItemById(id);
	}

	@Override
	public ItemAttribute getItemAttribute(int id) {
		return itemDao.getItemAttribute(id);
	}

	@Override
	public Itemrelease.Item.Attriblist getAttriblist(int id) {
		return itemDao.getAttriblist(id);
	}

	@Override
	public Itemrelease.Item.Attriblist.Attrib getAttribByIntAttid(int id, int attid) {
		return itemDao.getAttribByIntAttid(id, attid);
	}

	@Override
	public Itemrelease.Item.Attriblist.Attrib getAttribByStrAttid(int id, String attid) {
		return itemDao.getAttribByStrAttid(id, attid);
	}

	@Override
	public Itemrelease.Item.Tutorial gettutorial(int id) {
		return itemDao.gettutorial(id);
	}

	@Override
	public Itemrelease.Item.Resourceslist getResourceslist(int id) {
		return itemDao.getResourceslist(id);
	}

	@Override
	public String getStatistic(int id) {
		return itemDao.getStatistic(id);
	}

	@Override
	public Itemrelease.Item.MachineRubric getMachineRubric(int id) {
		return itemDao.getMachineRubric(id);
	}

	@Override
	public Itemrelease.Item.RendererSpec getRendererSpec(int id) {
		return itemDao.getRendererSpec(id);
	}

	@Override
	public String getGridanswerspace(int id) {
		return itemDao.getGridanswerspace(id);
	}

	@Override
	public List<Itemrelease.Item.Content> getContents(int id) {
		return itemDao.getContents(id);
	}

	@Override
	public Itemrelease.Item.Content getContent(int id, String language) {
		return itemDao.getContentByLanguage(id, language);
	}

	@Override
	public List<Itemrelease.Item.KeywordList> getkeywordList(int id) {
		return itemDao.getkeywordList(id);
	}

}

package org.readiness.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.readiness.dao.ItemDao;
import org.readiness.items.domain.ItemAttribute;
import org.readiness.items.domain.Itemrelease.Item;
import org.readiness.items.domain.Itemrelease.Item.Attriblist;
import org.readiness.items.domain.Itemrelease.Item.Attriblist.Attrib;
import org.readiness.items.domain.Itemrelease.Item.Content;
import org.readiness.items.domain.Itemrelease.Item.KeywordList;
import org.readiness.items.domain.Itemrelease.Item.MachineRubric;
import org.readiness.items.domain.Itemrelease.Item.RendererSpec;
import org.readiness.items.domain.Itemrelease.Item.Resourceslist;
import org.readiness.items.domain.Itemrelease.Item.Tutorial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {
	private static Logger logger = Logger.getLogger(ItemServiceImpl.class);

	@Autowired
	private ItemDao itemDao;
	
	public ItemServiceImpl(){
		logger.info("initializing");
	}
	
	@Override
	public Item getItem(int id) {
		logger.info("getItem");
		return itemDao.getItemById(id);
	}

	@Override
	public ItemAttribute getItemAttribute(int id) {
		logger.info("getItemAttribute");
		return itemDao.getItemAttribute(id);
	}

	@Override
	public Attriblist getAttriblist(int id) {
		logger.info("getAttriblist");
		return itemDao.getAttriblist(id);
	}

	@Override
	public Attrib getAttribByIntAttid(int id, int attid) {
		logger.info("getAttribByIntAttid");
		return itemDao.getAttribByIntAttid(id, attid);
	}

	@Override
	public Attrib getAttribByStrAttid(int id, String attid) {
		logger.info("getAttribByStrAttid");
		return itemDao.getAttribByStrAttid(id, attid);
	}

	@Override
	public Tutorial gettutorial(int id) {
		logger.info("getAttribByStrAttid");
		return itemDao.gettutorial(id);
	}

	@Override
	public Resourceslist getResourceslist(int id) {
		logger.info("getResourceslist");
		return itemDao.getResourceslist(id);
	}

	@Override
	public String getStatistic(int id) {
		logger.info("getStatistic");
		return itemDao.getStatistic(id);
	}

	@Override
	public MachineRubric getMachineRubric(int id) {
		logger.info("getMachineRubric");
		return itemDao.getMachineRubric(id);
	}

	@Override
	public RendererSpec getRendererSpec(int id) {
		logger.info("getRendererSpec");
		return itemDao.getRendererSpec(id);
	}

	@Override
	public String getGridanswerspace(int id) {
		logger.info("getGridanswerspace");
		return itemDao.getGridanswerspace(id);
	}

	@Override
	public List<Content> getContents(int id) {
		logger.info("getContents");
		return itemDao.getContents(id);
	}

	@Override
	public Content getContent(int id, String language) {
		logger.info("getContent");
		return itemDao.getContentByLanguage(id, language);
	}

	@Override
	public List<KeywordList> getkeywordList(int id) {
		logger.info("getkeywordList");
		return itemDao.getkeywordList(id);
	}

}

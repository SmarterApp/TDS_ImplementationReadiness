package org.cresst.sb.irp.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.items.ItemAttribute;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.items.Itemrelease.Item.Tutorial;
import org.cresst.sb.irp.domain.items.ObjectFactory;
import org.cresst.sb.irp.domain.manifest.Manifest;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.springframework.stereotype.Repository;


@Repository
public class ItemDaoImpl implements ItemDao {
	private static Logger logger = Logger.getLogger(ItemDaoImpl.class);
	private static Map<Integer, Itemrelease.Item> map = new ConcurrentHashMap<Integer, Itemrelease.Item>();
	private String rootResourceFolderName = "SampleAssessmentItemPackage";
	private String resourceType = "imsqti_apipitem_xmlv2p2";
	// "imsqti_apipitem_xmlv2p2"; not dependency type of
	// resourcemetadata/apipv1p0
	private List<Manifest.Resources.Resource> listResource;

	public ItemDaoImpl() {
		try {
			/***
			 * use this constractor if you don't want to circular dependency
			 * scenario ManifestDaoImpl.afterPropertiesSet()
			 ***/
			/*
			 * JAXBContext ctx = JAXBContext
			 * .newInstance(org.readiness.manifest.domain.ObjectFactory.class);
			 * Unmarshaller unmarshaller = ctx.createUnmarshaller(); manifest =
			 * (Manifest) unmarshaller.unmarshal(new File(
			 * rootResourceFolderName + "/imsmanifest.xml")); loadData();
			 */
		} catch (Exception e) {
			logger.error("ItemDaoImpl.ItemDaoImpl() Exception thrown", e);
		}
	}

	@Override
	public Itemrelease.Item getItemById(int id) {
		logger.info("getItemById");
		Itemrelease.Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		return item;
	}

	public void loadData(List<Manifest.Resources> listResources) throws JAXBException, FileNotFoundException {
		logger.info("ItemDaoImpl.loadData()");
		Manifest.Resources resources = listResources.get(0);
		this.listResource = resources.getResource();

		JAXBContext ctx = JAXBContext
				.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = ctx.createUnmarshaller();
		Itemrelease itemrelease;
		Itemrelease.Item item;
		for (Manifest.Resources.Resource rs : listResource) {
			if (rs.getType().trim().toLowerCase().equals(resourceType)) {
				String resourceIdentifier = rs.getIdentifier();
				List<Manifest.Resources.Resource.File> listFile = rs
						.getFile();
				Manifest.Resources.Resource.File _file = listFile
						.get(0);
				String[] identifierArray = resourceIdentifier.split("-");
				if (identifierArray.length == 3) {
					try {
						int itemid = Integer.parseInt(identifierArray[2]);
						itemrelease = (Itemrelease) unmarshaller
								.unmarshal(new File(rootResourceFolderName
										+ "/" + _file.getHref()));
						item = itemrelease.getItem().get(0);
						map.put(itemid, item);
					} catch (NumberFormatException e) {
						logger.error("the last part of resource identifier (xxx-number-number) in imsmanifest.xml is not a number !!", e);
					} catch (Exception e) {
						logger.error("ItemDaoImpl.loadData() Exception", e);
					}

				} else {
					logger.info("identifier's pattern should be like xxx-number-number");
				}
			}
		}
	}

	@Override
	public ItemAttribute getItemAttribute(int id) {
		logger.info("getItemAttribute");
		Itemrelease.Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		ItemAttribute itemAttribute = new ItemAttribute();
		itemAttribute.setFormat(item.getFormat());
		itemAttribute.setVersion(item.getVersion());
		itemAttribute.setBankkey(item.getBankkey());
		return itemAttribute;
	}

	@Override
	public Itemrelease.Item.Attriblist getAttriblist(int id) {
		logger.info("getAttriblist with id " + id);
		Itemrelease.Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Itemrelease.Item.Attriblist> listAttriblist = item.getAttriblist();
		if (listAttriblist == null || listAttriblist.size() == 0)
			throw new NotFoundException("Could not find listAttriblist for item " + id);
		return listAttriblist.get(0);
	}

	@Override
	public Itemrelease.Item.Attriblist.Attrib getAttribByIntAttid(int id, int attid) {
		logger.info("getAttribByIntAttid with id " + id + " and attid " + attid);
		Itemrelease.Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Itemrelease.Item.Attriblist> listAttriblist = item.getAttriblist();
		if (listAttriblist == null || listAttriblist.size() == 0)
			throw new NotFoundException("Could not find listAttriblist for item " + id);
		Itemrelease.Item.Attriblist attriblist = listAttriblist.get(0);
		List<Itemrelease.Item.Attriblist.Attrib> listAttrib = attriblist.getAttrib();
		if (listAttrib == null || listAttrib.size() == 0)
			throw new NotFoundException("Could not find listAttrib for item " + id);
		return listAttrib.get(attid);
	}

	@Override
	public Itemrelease.Item.Attriblist.Attrib getAttribByStrAttid(int id, String attid) {
		logger.info("getAttribByStrAttid with id " + id + " and attid " + attid);
		Itemrelease.Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Itemrelease.Item.Attriblist> listAttriblist = item.getAttriblist();
		if (listAttriblist == null || listAttriblist.size() == 0)
			throw new NotFoundException("Could not find listAttriblist for item " + id);
		Itemrelease.Item.Attriblist attriblist = listAttriblist.get(0);
		List<Itemrelease.Item.Attriblist.Attrib> listAttrib = attriblist.getAttrib();
		if (listAttrib == null || listAttrib.size() == 0)
			throw new NotFoundException("Could not find listAttrib for item " + id);
		for (Itemrelease.Item.Attriblist.Attrib att : listAttrib) {
			if (attid.toLowerCase().trim()
					.equals(att.getAttid().toLowerCase().trim())) {
				return att;
			}
		}
		return null;
	}

	@Override
	public Tutorial gettutorial(int id) {
		logger.info("gettutorial with id " + id);
		Itemrelease.Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Tutorial> listTutorial = item.getTutorial();
		if (listTutorial == null || listTutorial.size() == 0)
			throw new NotFoundException("Could not find listTutorial for item " + id);
		return listTutorial.get(0); // suppose just one tutorial
	}

	@Override
	public Itemrelease.Item.Resourceslist getResourceslist(int id) {
		logger.info("getResourceslist with id " + id);
		Itemrelease.Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Itemrelease.Item.Resourceslist> listResourceslist = item.getResourceslist();
		if (listResourceslist == null || listResourceslist.size() == 0)
			throw new NotFoundException("Could not find getResourceslist for item " + id);
		return listResourceslist.get(0);
	}

	@Override
	public String getStatistic(int id) {
		logger.info("getStatistic with id " + id);
		Itemrelease.Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		return item.getStatistic();
	}

	@Override
	public Itemrelease.Item.MachineRubric getMachineRubric(int id) {
		logger.info("getMachineRubric with id " + id);
		Itemrelease.Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Itemrelease.Item.MachineRubric> listMachineRubric = item.getMachineRubric();
		if (listMachineRubric == null || listMachineRubric.size() < 1) {
			throw new IndexOutOfBoundsException();
		} else {
			Itemrelease.Item.MachineRubric machineRubric = listMachineRubric.get(0);
			if (machineRubric == null) {
				throw new IndexOutOfBoundsException();
			}else{
				return machineRubric;
			}
		}
	}

	@Override
	public Itemrelease.Item.RendererSpec getRendererSpec(int id) {
		logger.info("getRendererSpec with id " + id);
		Itemrelease.Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Itemrelease.Item.RendererSpec> listRendererSpec = item.getRendererSpec();
		if (listRendererSpec == null || listRendererSpec.size() < 1) {
			throw new IndexOutOfBoundsException();
		} else {
			return listRendererSpec.get(0);
		}
	}

	@Override
	public String getGridanswerspace(int id) {
		logger.info("getGridanswerspace with id " + id);
		Itemrelease.Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		String gridanswerspace = null;
		gridanswerspace = item.getGridanswerspace();
		if (gridanswerspace == null) {
			throw new NotFoundException(
					"Could not find gridanswerspace for item " + id);
		}
		return gridanswerspace;
	}

	@Override
	public List<Itemrelease.Item.Content> getContents(int id) {
		logger.info("getContents with id " + id);
		Itemrelease.Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Itemrelease.Item.Content> listContent = item.getContent();
		if (listContent == null) {
			throw new NotFoundException("Could not find listContent for item " + id);
		}
		return listContent;
	}

	@Override
	public Itemrelease.Item.Content getContentByLanguage(int id, String language) {
		logger.info("getContentByLanguage with id " + id + " language " + language);
		Itemrelease.Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Itemrelease.Item.Content> listContent = item.getContent();
		if (listContent == null){
			throw new NotFoundException("Could not find listContent for item " + id);
		}
		for (Itemrelease.Item.Content contentTmp : listContent) {
			if (language.trim().toLowerCase()
					.equals(contentTmp.getLanguage().toLowerCase().trim())) {
				return contentTmp;
			}
		}
		{
			throw new NotFoundException("Could not find content for item " + id + " language " + language);
		}
	}

	@Override
	public List<Itemrelease.Item.KeywordList> getkeywordList(int id) {
		logger.info("getkeywordList with id " + id);
		Itemrelease.Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Itemrelease.Item.KeywordList> listKeywordList = item.getKeywordList();
		if (listKeywordList == null){
			throw new NotFoundException("Could not find listKeywordList for item " + id);
		}
		return listKeywordList;
	}

	

}

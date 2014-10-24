package org.readiness.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.readiness.exceptions.NotFoundException;
import org.readiness.items.domain.ItemAttribute;
import org.readiness.items.domain.Itemrelease;
import org.readiness.items.domain.Itemrelease.Item;
import org.readiness.items.domain.Itemrelease.Item.Attriblist;
import org.readiness.items.domain.Itemrelease.Item.Attriblist.Attrib;
import org.readiness.items.domain.Itemrelease.Item.Content;
import org.readiness.items.domain.Itemrelease.Item.KeywordList;
import org.readiness.items.domain.Itemrelease.Item.MachineRubric;
import org.readiness.items.domain.Itemrelease.Item.RendererSpec;
import org.readiness.items.domain.Itemrelease.Item.Resourceslist;
import org.readiness.items.domain.Itemrelease.Item.Tutorial;
import org.readiness.manifest.domain.Manifest;
import org.readiness.manifest.domain.Manifest.Resources;
import org.readiness.manifest.domain.Manifest.Resources.Resource;
import org.springframework.stereotype.Repository;


@Repository
public class ItemDaoImpl implements ItemDao {
	private static Logger logger = Logger.getLogger(ItemDaoImpl.class);
	private static Map<Integer, Item> map = new ConcurrentHashMap<Integer, Item>();
	private String rootResourceFolderName = "SampleAssessmentItemPackage";
	private String resourceType = "imsqti_apipitem_xmlv2p2";
	// "imsqti_apipitem_xmlv2p2"; not dependency type of
	// resourcemetadata/apipv1p0
	private List<Resource> listResource;
	private Manifest manifest;

	public void setManifest(Manifest manifest) {
		this.manifest = manifest;
	}

	public ItemDaoImpl() {
		logger.info("initializing");
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
			System.out.println("ItemDaoImpl.ItemDaoImpl() Exception thrown  :"
					+ e);
			e.printStackTrace();
		}
	}

	@Override
	public Item getItemById(int id) {
		logger.info("getItemById");
		Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		return item;
	}

	public void loadData() throws JAXBException, FileNotFoundException {
		logger.info("ItemDaoImpl.loadData()");
		List<Resources> listResources = manifest.getResources();
		Resources resources = listResources.get(0);
		this.listResource = resources.getResource();

		JAXBContext ctx = JAXBContext
				.newInstance(org.readiness.items.domain.ObjectFactory.class);
		Unmarshaller unmarshaller = ctx.createUnmarshaller();
		Itemrelease itemrelease;
		Itemrelease.Item item;
		for (Resource rs : listResource) {
			if (rs.getType().trim().toLowerCase().equals(resourceType)) {
				String resourceIdentifier = rs.getIdentifier();
				List<org.readiness.manifest.domain.Manifest.Resources.Resource.File> listFile = rs
						.getFile();
				org.readiness.manifest.domain.Manifest.Resources.Resource.File _file = listFile
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
						logger.info("the last part of resource identifier (xxx-number-number) in imsmanifest.xml is not a number !!");
						System.out
								.println("ItemDaoImpl.loadData() Exception thrown  :"
										+ e);
						e.printStackTrace();
					} catch (Exception e) {
						logger.info("ItemDaoImpl.loadData() Exception");
						System.out
								.println("ItemDaoImpl.loadData() Exception thrown  :"
										+ e);
						e.printStackTrace();
					}

				} else {
					logger.info("identifier's pattern should be like xxx-number-number");
					System.out
							.println("identifier's pattern should be like xxx-number-number");
				}
			}
		}
	}

	@Override
	public ItemAttribute getItemAttribute(int id) {
		logger.info("getItemAttribute");
		Item item = map.get(id);
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
	public Attriblist getAttriblist(int id) {
		logger.info("getAttriblist with id " + id);
		Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Attriblist> listAttriblist = item.getAttriblist();
		if (listAttriblist == null || listAttriblist.size() == 0)
			throw new NotFoundException("Could not find listAttriblist for item " + id);
		return listAttriblist.get(0);
	}

	@Override
	public Attrib getAttribByIntAttid(int id, int attid) {
		logger.info("getAttribByIntAttid with id " + id + " and attid " + attid);
		Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Attriblist> listAttriblist = item.getAttriblist();
		if (listAttriblist == null || listAttriblist.size() == 0)
			throw new NotFoundException("Could not find listAttriblist for item " + id);
		Attriblist attriblist = listAttriblist.get(0);
		List<Attrib> listAttrib = attriblist.getAttrib();
		if (listAttrib == null || listAttrib.size() == 0)
			throw new NotFoundException("Could not find listAttrib for item " + id);
		return listAttrib.get(attid);
	}

	@Override
	public Attrib getAttribByStrAttid(int id, String attid) {
		logger.info("getAttribByStrAttid with id " + id + " and attid " + attid);
		Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Attriblist> listAttriblist = item.getAttriblist();
		if (listAttriblist == null || listAttriblist.size() == 0)
			throw new NotFoundException("Could not find listAttriblist for item " + id);
		Attriblist attriblist = listAttriblist.get(0);
		List<Attrib> listAttrib = attriblist.getAttrib();
		if (listAttrib == null || listAttrib.size() == 0)
			throw new NotFoundException("Could not find listAttrib for item " + id);
		for (Attrib att : listAttrib) {
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
		Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Tutorial> listTutorial = item.getTutorial();
		if (listTutorial == null || listTutorial.size() == 0)
			throw new NotFoundException("Could not find listTutorial for item " + id);
		return listTutorial.get(0); // suppose just one tutorial
	}

	@Override
	public Resourceslist getResourceslist(int id) {
		logger.info("getResourceslist with id " + id);
		Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Resourceslist> listResourceslist = item.getResourceslist();
		if (listResourceslist == null || listResourceslist.size() == 0)
			throw new NotFoundException("Could not find getResourceslist for item " + id);
		return listResourceslist.get(0);
	}

	@Override
	public String getStatistic(int id) {
		logger.info("getStatistic with id " + id);
		Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		return item.getStatistic();
	}

	@Override
	public MachineRubric getMachineRubric(int id) {
		logger.info("getMachineRubric with id " + id);
		Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<MachineRubric> listMachineRubric = item.getMachineRubric();
		if (listMachineRubric == null || listMachineRubric.size() < 1) {
			throw new IndexOutOfBoundsException();
		} else {
			MachineRubric machineRubric = listMachineRubric.get(0);
			if (machineRubric == null) {
				throw new IndexOutOfBoundsException();
			}else{
				return machineRubric;
			}
		}
	}

	@Override
	public RendererSpec getRendererSpec(int id) {
		logger.info("getRendererSpec with id " + id);
		Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<RendererSpec> listRendererSpec = item.getRendererSpec();
		if (listRendererSpec == null || listRendererSpec.size() < 1) {
			throw new IndexOutOfBoundsException();
		} else {
			return listRendererSpec.get(0);
		}
	}

	@Override
	public String getGridanswerspace(int id) {
		logger.info("getGridanswerspace with id " + id);
		Item item = map.get(id);
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
	public List<Content> getContents(int id) {
		logger.info("getContents with id " + id);
		Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Content> listContent = item.getContent();
		if (listContent == null) {
			throw new NotFoundException("Could not find listContent for item " + id);
		}
		return listContent;
	}

	@Override
	public Content getContentByLanguage(int id, String language) {
		logger.info("getContentByLanguage with id " + id + " language " + language);
		Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<Content> listContent = item.getContent();
		if (listContent == null){
			throw new NotFoundException("Could not find listContent for item " + id);
		}
		for (Content contentTmp : listContent) {
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
	public List<KeywordList> getkeywordList(int id) {
		logger.info("getkeywordList with id " + id);
		Item item = map.get(id);
		if (item == null){
			throw new NullPointerException();
		}
		List<KeywordList> listKeywordList = item.getKeywordList();
		if (listKeywordList == null){
			throw new NotFoundException("Could not find listKeywordList for item " + id);
		}
		return listKeywordList;
	}

	

}

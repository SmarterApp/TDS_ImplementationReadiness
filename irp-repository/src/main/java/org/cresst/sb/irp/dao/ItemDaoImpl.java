package org.cresst.sb.irp.dao;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.cresst.sb.irp.domain.items.ItemAttribute;
import org.cresst.sb.irp.domain.items.Itemrelease;
import org.cresst.sb.irp.domain.items.Itemrelease.Item;
import org.cresst.sb.irp.domain.items.Itemrelease.Item.Attriblist;
import org.cresst.sb.irp.domain.items.Itemrelease.Item.Tutorial;
import org.cresst.sb.irp.domain.manifest.Manifest;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Repository;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

@Repository
public class ItemDaoImpl implements ItemDao {
	private final static Logger logger = LoggerFactory.getLogger(ItemDaoImpl.class);
	private static Map<Integer, Itemrelease.Item> map = new ConcurrentHashMap<Integer, Itemrelease.Item>();
	private static Map<String, Itemrelease.Item> map2 = new ConcurrentHashMap<String, Itemrelease.Item>();
	private String rootResourceFolderName = "irp-package/IrpContentPackage";
	private String resourceType = "imsqti_apipitem_xmlv2p2";
	private List<Manifest.Resources.Resource> listResource;


	private Unmarshaller unmarshaller;

	@Autowired
	public ItemDaoImpl(Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}

	@Override
	public Itemrelease.Item getItemById(int id) {
		logger.info("getItemById");
		Itemrelease.Item item = map.get(id);
		if (item == null) {
			throw new NullPointerException();
		}
		return item;
	}

	@Override
	public Itemrelease.Item getItemByIdentifier(String Identifier) {
		logger.info("getItemByIdentifier");
		Itemrelease.Item item = map2.get(Identifier);
		if (item == null){
			return null;
		}
		return item;
	}

	public void loadData(List<Manifest.Resources> listResources) throws FileNotFoundException {
		logger.info("ItemDaoImpl.loadData()");
		Manifest.Resources resources = listResources.get(0);
		this.listResource = resources.getResource();

		Itemrelease itemrelease;
		Itemrelease.Item item;
		for (Manifest.Resources.Resource rs : listResource) {
			if (rs.getType().trim().toLowerCase().equals(resourceType)) {
				String resourceIdentifier = rs.getIdentifier();
				List<Manifest.Resources.Resource.File> listFile = rs.getFile();
				Manifest.Resources.Resource.File _file = listFile.get(0);
				String[] identifierArray = resourceIdentifier.split("-");
				if (identifierArray.length == 3) {
					try {
						int itemid = Integer.parseInt(identifierArray[2]);
						Resource resource = new ClassPathResource(rootResourceFolderName + "/" + _file.getHref());
						Source source = new StreamSource(resource.getInputStream());
						itemrelease = (Itemrelease) unmarshaller.unmarshal(source);
						item = itemrelease.getItem().get(0);
						map.put(itemid, item);
						map2.put(resourceIdentifier.trim(), item);
					} catch (NumberFormatException e) {
						logger.error(
								"the last part of resource identifier (xxx-number-number) in imsmanifest.xml is not a number !!",
								e);
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
		if (item == null) {
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
		if (item == null) {
			throw new NullPointerException();
		}
		List<Itemrelease.Item.Attriblist> listAttriblist = item.getAttriblist();
		if (listAttriblist == null || listAttriblist.size() == 0)
			throw new NotFoundException("Could not find listAttriblist for item " + id);
		return listAttriblist.get(0);
	}

	@Override
	public Attriblist getItemAttriblistFromIRPitem(Item item) {
		List<Itemrelease.Item.Attriblist> listAttriblist = item.getAttriblist();
		if (listAttriblist == null || listAttriblist.size() == 0)
			return null;
		return listAttriblist.get(0);
	}

	@Override
	public Itemrelease.Item.Attriblist.Attrib getAttribByIntAttid(int id, int attid) {
		logger.info("getAttribByIntAttid with id " + id + " and attid " + attid);
		Itemrelease.Item item = map.get(id);
		if (item == null) {
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
		if (item == null) {
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
			if (attid.toLowerCase().trim().equals(att.getAttid().toLowerCase().trim())) {
				return att;
			}
		}
		return null;
	}

	@Override
	public Itemrelease.Item.Attriblist.Attrib getItemAttribValueFromIRPitemAttriblist(Attriblist attriblist, String attid) {
		List<Itemrelease.Item.Attriblist.Attrib> listAttrib = attriblist.getAttrib();
		if (listAttrib == null || listAttrib.size() == 0)
			return null;
		for (Itemrelease.Item.Attriblist.Attrib att : listAttrib) {
			if (attid.toLowerCase().trim().equals(att.getAttid().toLowerCase().trim())) {
				return att;
			}
		}
		return null;
	}

	@Override
	public Itemrelease.Item.Attriblist.Attrib getItemAttribFromIRPitem(Item item, String attid) {
		List<Itemrelease.Item.Attriblist> listAttriblist = item.getAttriblist();
		if (listAttriblist == null || listAttriblist.size() == 0)
			return null;
		Itemrelease.Item.Attriblist attriblist = listAttriblist.get(0);
		List<Itemrelease.Item.Attriblist.Attrib> listAttrib = attriblist.getAttrib();
		if (listAttrib == null || listAttrib.size() == 0)
			return null;
		for (Itemrelease.Item.Attriblist.Attrib att : listAttrib) {
			if (attid.toLowerCase().trim().equals(att.getAttid().toLowerCase().trim())) {
				return att;
			}
		}
		return null;
	}

	@Override
	public Tutorial gettutorial(int id) {
		logger.info("gettutorial with id " + id);
		Itemrelease.Item item = map.get(id);
		if (item == null) {
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
		if (item == null) {
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
		if (item == null) {
			throw new NullPointerException();
		}
		return item.getStatistic();
	}

	@Override
	public Itemrelease.Item.MachineRubric getMachineRubric(int id) {
		logger.info("getMachineRubric with id " + id);
		Itemrelease.Item item = map.get(id);
		if (item == null) {
			throw new NullPointerException();
		}
		List<Itemrelease.Item.MachineRubric> listMachineRubric = item.getMachineRubric();
		if (listMachineRubric == null || listMachineRubric.size() < 1) {
			throw new IndexOutOfBoundsException();
		} else {
			Itemrelease.Item.MachineRubric machineRubric = listMachineRubric.get(0);
			if (machineRubric == null) {
				throw new IndexOutOfBoundsException();
			} else {
				return machineRubric;
			}
		}
	}

	@Override
	public Itemrelease.Item.RendererSpec getRendererSpec(int id) {
		logger.info("getRendererSpec with id " + id);
		Itemrelease.Item item = map.get(id);
		if (item == null) {
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
		if (item == null) {
			throw new NullPointerException();
		}
		String gridanswerspace = null;
		gridanswerspace = item.getGridanswerspace();
		if (gridanswerspace == null) {
			throw new NotFoundException("Could not find gridanswerspace for item " + id);
		}
		return gridanswerspace;
	}

	@Override
	public List<Itemrelease.Item.Content> getContents(int id) {
		logger.info("getContents with id " + id);
		Itemrelease.Item item = map.get(id);
		if (item == null) {
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
		if (item == null) {
			throw new NullPointerException();
		}
		List<Itemrelease.Item.Content> listContent = item.getContent();
		if (listContent == null) {
			throw new NotFoundException("Could not find listContent for item " + id);
		}
		for (Itemrelease.Item.Content contentTmp : listContent) {
			if (language.trim().toLowerCase().equals(contentTmp.getLanguage().toLowerCase().trim())) {
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
		if (item == null) {
			throw new NullPointerException();
		}
		List<Itemrelease.Item.KeywordList> listKeywordList = item.getKeywordList();
		if (listKeywordList == null) {
			throw new NotFoundException("Could not find listKeywordList for item " + id);
		}
		return listKeywordList;
	}

}

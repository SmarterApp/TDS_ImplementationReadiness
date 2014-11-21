package org.cresst.sb.irp.dao;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.manifest.ObjectFactory;
import org.cresst.sb.irp.domain.manifest.Manifest;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.utils.ManifestUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ManifestDaoImpl implements ManifestDao, InitializingBean {
	private static Logger logger = Logger.getLogger(ManifestDaoImpl.class);
	
	private Map<String, Manifest> map = new ConcurrentHashMap<String, Manifest>(); ;
	private String rootResourceFolderName = "SampleAssessmentItemPackage";
	private String manifestFileName = "imsmanifest.xml";
	private Manifest manifest;
	
	@Autowired
	private ManifestUtil manifestUtil;
	
	@Autowired
	private ItemDao itemDao;
	
	public ManifestDaoImpl(){
		try {
			JAXBContext ctx = JAXBContext
					.newInstance(ObjectFactory.class);
			Unmarshaller unmarshaller = ctx.createUnmarshaller();
			manifest = (Manifest) unmarshaller.unmarshal(new File(
					rootResourceFolderName + "/" + manifestFileName));
			String identifier = manifest.getIdentifier();
			map.put(identifier, manifest);
		} catch (Exception e) {
			logger.error("ManifestDaoImpl exception: ", e);
		}
	}
	
	@Override
	public Manifest getManifests() {
		return manifest;
	}

	@Override
	public List<Manifest.Resources> getResources() {
		return manifest.getResources();
	}

	@Override
	public Manifest.Resources.Resource getResource(String identifier) {
		List<Manifest.Resources> listResources = manifest.getResources();
		if (listResources == null || listResources.size() == 0)
			throw new NotFoundException("Could not find listResources for resource " + identifier);
		Manifest.Resources resources = listResources.get(0);
		List<Manifest.Resources.Resource> listResource = resources.getResource();
		if (listResource == null || listResource.size() == 0)
			throw new NotFoundException("Could not find listResource for resource " + identifier);
		Manifest.Resources.Resource resource = manifestUtil.getResource(listResource, identifier);
		if (resource == null)
			throw new NotFoundException("Could not find resource " + identifier);
		return resource;
	}

	@Override
	public Manifest.Metadata getMetadata() {
		List<Manifest.Metadata> listMetadata = manifest.getMetadata();
		if (listMetadata == null || listMetadata.size() == 0)
			throw new NotFoundException("Could not find listMetadata ");
		return  listMetadata.get(0);	 // only one element
	}

	@Override
	public String getOrganizations() {
		return manifest.getOrganizations();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//or in Constructor of ItemDaoImpl - re generate manifest used in ManifestDaoImpl() above 
		itemDao.setManifest(manifest);
		itemDao.loadData();
		
	}

}

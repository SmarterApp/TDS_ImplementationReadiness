package org.readiness.dao;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.readiness.exceptions.NotFoundException;
import org.readiness.manifest.domain.Manifest;
import org.readiness.manifest.domain.Manifest.Metadata;
import org.readiness.manifest.domain.Manifest.Resources;
import org.readiness.manifest.domain.Manifest.Resources.Resource;
import org.readiness.utils.ManifestUtil;
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
					.newInstance(org.readiness.manifest.domain.ObjectFactory.class);
			Unmarshaller unmarshaller = ctx.createUnmarshaller();
			manifest = (Manifest) unmarshaller.unmarshal(new File(
					rootResourceFolderName + "/" + manifestFileName));

			String identifier = manifest.getIdentifier();
			map.put(identifier, manifest);
		} catch (Exception e) {
			logger.info("ManifestDaoImpl exception: " + e);
			System.out.println("ManifestData.ManifestDaoImpl() Exception thrown  :" + e);
			e.printStackTrace();
		}
	}
	
	@Override
	public Manifest getManifests() {
		return manifest;
	}

	@Override
	public List<Resources> getResources() {
		return manifest.getResources();
	}

	@Override
	public Resource getResource(String identifier) {
		List<Resources> listResources = manifest.getResources();
		if (listResources == null || listResources.size() == 0)
			throw new NotFoundException("Could not find listResources for resource " + identifier);
		Resources resources = listResources.get(0);
		List<Resource> listResource = resources.getResource();
		if (listResource == null || listResource.size() == 0)
			throw new NotFoundException("Could not find listResource for resource " + identifier);
		Resource resource = manifestUtil.getResource(listResource,
				identifier);
		if (resource == null)
			throw new NotFoundException("Could not find resource " + identifier);
		return resource;
	}

	@Override
	public Metadata getMetadata() {
		List<Metadata> listMetadata = manifest.getMetadata();
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
		logger.info("afterPropertiesSet()");  
		//or in Constructor of ItemDaoImpl - re generate manifest used in ManifestDaoImpl() above 
		itemDao.setManifest(manifest);
		itemDao.loadData();
		
	}

}

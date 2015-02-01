package org.cresst.sb.irp.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.cresst.sb.irp.domain.manifest.Manifest;
import org.cresst.sb.irp.exceptions.NotFoundException;
import org.cresst.sb.irp.utils.ManifestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.xml.transform.stream.StreamSource;

@Repository
public class ManifestDaoImpl implements ManifestDao {
	private final static Logger logger = LoggerFactory.getLogger(ManifestDaoImpl.class);
	
	private Map<String, Manifest> map = new ConcurrentHashMap<String, Manifest>(); ;
	private Manifest manifest;

	@Value("classpath:irp-package/TrainingTestContent/imsmanifest.xml")
	private Resource manifestResource;

	@Autowired
	private Unmarshaller unmarshaller;

	@Autowired
	private ManifestUtil manifestUtil;
	
	@Autowired
	private ItemDao itemDao;
	
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

	@PostConstruct
	public void loadData() throws Exception {
		try {
			manifest = (Manifest) unmarshaller.unmarshal(new StreamSource(manifestResource.getInputStream()));
			String identifier = manifest.getIdentifier();
			map.put(identifier, manifest);
		} catch (Exception e) {
			logger.error("ManifestDaoImpl exception: ", e);
		}

		itemDao.loadData(manifest.getResources());
		
	}

}

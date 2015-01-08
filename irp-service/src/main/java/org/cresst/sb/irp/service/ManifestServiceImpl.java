package org.cresst.sb.irp.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.dao.ManifestDao;
import org.cresst.sb.irp.domain.manifest.Manifest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManifestServiceImpl implements ManifestService {
	private static Logger logger = Logger.getLogger(ManifestServiceImpl.class);
	
	@Autowired
	private ManifestDao manifestDao;
	
	public ManifestServiceImpl(){
		logger.info("initializing");
	}
	
	@Override
	public Manifest getManifest() {
		return manifestDao.getManifests();
	}

	@Override
	public List<Manifest.Resources> getResources() {
		return manifestDao.getResources();
	}

	@Override
	public Manifest.Resources.Resource getResource(String identifier) {
		return manifestDao.getResource(identifier);
	}

	@Override
	public Manifest.Metadata getMetadata() {
		return manifestDao.getMetadata();
	}

	@Override
	public String getOrganizations() {
		return manifestDao.getOrganizations();
	}

}

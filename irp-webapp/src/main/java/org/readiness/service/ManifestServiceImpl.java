package org.readiness.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.readiness.dao.ManifestDao;
import org.readiness.manifest.domain.Manifest;
import org.readiness.manifest.domain.Manifest.Metadata;
import org.readiness.manifest.domain.Manifest.Resources;
import org.readiness.manifest.domain.Manifest.Resources.Resource;
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
	public List<Resources> getResources() {
		return manifestDao.getResources();
	}

	@Override
	public Resource getResource(String identifier) {
		return manifestDao.getResource(identifier);
	}

	@Override
	public Metadata getMetadata() {
		return manifestDao.getMetadata();
	}

	@Override
	public String getOrganizations() {
		return manifestDao.getOrganizations();
	}

}

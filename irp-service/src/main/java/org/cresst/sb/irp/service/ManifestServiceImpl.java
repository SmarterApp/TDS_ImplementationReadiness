package org.cresst.sb.irp.service;

import java.util.List;

import org.cresst.sb.irp.dao.ManifestDao;
import org.cresst.sb.irp.domain.manifest.Manifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManifestServiceImpl implements ManifestService {
	private final static Logger logger = LoggerFactory.getLogger(ManifestServiceImpl.class);
	
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

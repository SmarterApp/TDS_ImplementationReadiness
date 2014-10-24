package org.cresst.sb.irp.service;

import org.cresst.sb.irp.domain.manifest.Manifest;

import java.util.List;


public interface ManifestService {

	Manifest getManifest();
	
	List<Manifest.Resources> getResources();
	
	Manifest.Resources.Resource getResource(String identifier);
	
	Manifest.Metadata getMetadata();
	
	String getOrganizations();
	
}

package org.readiness.dao;

import java.util.List;

import org.readiness.manifest.domain.Manifest;
import org.readiness.manifest.domain.Manifest.Metadata;
import org.readiness.manifest.domain.Manifest.Resources;
import org.readiness.manifest.domain.Manifest.Resources.Resource;

public interface ManifestDao {
	
	Manifest getManifests();
	List<Resources> getResources();
	Resource getResource(String identifier);
	Metadata getMetadata();
	String getOrganizations();

}

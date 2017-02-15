package org.cresst.sb.irp.dao;

import org.cresst.sb.irp.domain.manifest.Manifest;

import java.util.List;

/**
 * Data repository for Manifest data. Enables query operations on the manifest
 * from the irp-package.
 */
public interface ManifestDao {
	
	Manifest getManifests();
	List<Manifest.Resources> getResources();
	Manifest.Resources.Resource getResource(String identifier);
	Manifest.Metadata getMetadata();
	String getOrganizations();

}

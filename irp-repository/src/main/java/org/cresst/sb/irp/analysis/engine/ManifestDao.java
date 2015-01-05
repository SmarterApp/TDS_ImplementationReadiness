package org.cresst.sb.irp.analysis.engine;

import org.cresst.sb.irp.domain.manifest.Manifest;

import java.util.List;

public interface ManifestDao {
	
	Manifest getManifests();
	List<Manifest.Resources> getResources();
	Manifest.Resources.Resource getResource(String identifier);
	Manifest.Metadata getMetadata();
	String getOrganizations();

}

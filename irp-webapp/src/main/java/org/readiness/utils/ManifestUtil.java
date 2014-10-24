package org.readiness.utils;

import java.util.List;

import org.readiness.manifest.domain.Manifest.Resources.Resource;
import org.springframework.stereotype.Service;

@Service
public class ManifestUtil {
	
	public Resource getResource(List<Resource> listResource, String identifier){
		Resource resource = null;
		for(Resource rs: listResource){
			if (rs.getIdentifier().toLowerCase().trim().equals(identifier.toLowerCase().trim())){
				return rs;
			}
		}
		return resource;
	}

}

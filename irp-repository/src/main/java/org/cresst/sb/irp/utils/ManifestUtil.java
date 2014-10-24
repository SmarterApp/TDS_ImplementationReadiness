package org.cresst.sb.irp.utils;

import java.util.List;


import org.cresst.sb.irp.domain.manifest.Manifest;
import org.springframework.stereotype.Service;

@Service
public class ManifestUtil {
	
	public Manifest.Resources.Resource getResource(List<Manifest.Resources.Resource> listResource, String identifier){
		Manifest.Resources.Resource resource = null;
		for(Manifest.Resources.Resource rs: listResource){
			if (rs.getIdentifier().toLowerCase().trim().equals(identifier.toLowerCase().trim())){
				return rs;
			}
		}
		return resource;
	}

}

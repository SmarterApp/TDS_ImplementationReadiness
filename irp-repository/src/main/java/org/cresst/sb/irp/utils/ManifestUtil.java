package org.cresst.sb.irp.utils;

import java.util.List;


import org.cresst.sb.irp.domain.manifest.Manifest;
import org.springframework.stereotype.Service;

/**
 * Manifest Utilities to get manifest resources
 */
@Service
public class ManifestUtil {
	
    /**
     * 
     * @param listResource
     *            a list of resources that will be searched according to its
     *            identifier
     * @param identifier
     *            the target identifier to be search on.
     * @return the first resource that has its identifier equal to `identifier`.
     *         Returns null if none of the items in `listResource` match
     *         `identifier`.
     */
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

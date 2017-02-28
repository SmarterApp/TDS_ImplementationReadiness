package org.cresst.sb.irp.utils;

import org.cresst.sb.irp.domain.testpackage.Testspecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.Map;

/**
 * Utility service to retrieve files (test packages).
 */
@Service
public class RetrieveFileUtil {
	private final static Logger logger = LoggerFactory.getLogger(RetrieveFileUtil.class);

	@Autowired
	private Unmarshaller unmarshaller;

	/**
     * 
     * @param testPackagePath
     *            root to search for test packages
     * @param mapTestpackage
     *            stores results of the search in `testPackagePath` in
     *            <identifier.uniqueId, test package> map.
     * @param schemaResource
     *            schema to validate the test packages found in the walk
     * @param xmlValidate
     *            xml validation service
     */
    public void walk(String testPackagePath, Map<String, Testspecification> mapTestpackage, Resource schemaResource,
			XMLValidate xmlValidate) throws Exception {

		File root = new File(testPackagePath);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
                walk(f.getAbsolutePath(), mapTestpackage, schemaResource, xmlValidate);
			} else {
				if (f.getName().endsWith(".xml") && xmlValidate.validateXMLSchema(schemaResource, f.getCanonicalPath())) {
					Testspecification testpackage =
							(Testspecification) unmarshaller.unmarshal(new StreamSource(f.getCanonicalPath()));
					String uniqueid = testpackage.getIdentifier().getUniqueid();
					mapTestpackage.put(uniqueid, testpackage);
				} else {
					logger.info("file: " + f.getCanonicalPath() + " not valid.");
				}
			}
		}
	}

}

package org.cresst.sb.irp.utils;

import java.io.File;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.cresst.sb.irp.domain.testpackage.ObjectFactory;
import org.cresst.sb.irp.domain.testpackage.Testspecification;
import org.springframework.stereotype.Service;

@Service
public class RetrieveFileUtil {
	public void walk(String testPackagePath, Map<String, Testspecification> mapTestpackage) throws JAXBException {
		
		JAXBContext ctx = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = ctx.createUnmarshaller();
		
		File root = new File(testPackagePath);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				walk(f.getAbsolutePath(), mapTestpackage);
			} else {
				Testspecification testpackage = (Testspecification) unmarshaller.unmarshal(f);
				String uniqueid = testpackage.getIdentifier().getUniqueid();
				mapTestpackage.put(uniqueid, testpackage);
			}
		}
	}


}

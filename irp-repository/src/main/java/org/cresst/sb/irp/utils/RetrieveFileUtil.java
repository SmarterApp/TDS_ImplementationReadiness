package org.cresst.sb.irp.utils;

import java.io.File;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.cresst.sb.irp.domain.testpackage.ObjectFactory;
import org.cresst.sb.irp.domain.testpackage.Testpackage;
import org.springframework.stereotype.Service;

@Service
public class RetrieveFileUtil {
	private static Logger logger = Logger.getLogger(RetrieveFileUtil.class);
	
	public RetrieveFileUtil(){
		logger.info("initializing");
	}
	
	public void walk(String testPackagePath, Map<String, Testpackage> mapTestpackage) throws JAXBException {
		
		JAXBContext ctx = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = ctx.createUnmarshaller();
		
		File root = new File(testPackagePath);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				walk(f.getAbsolutePath(), mapTestpackage);
				System.out.println("Dir:" + f.getAbsoluteFile());
			} else {
				System.out.println("File:" + f.getAbsoluteFile());
				Testpackage testpackage = (Testpackage) unmarshaller
						.unmarshal(f);
				String uniqueid = testpackage.getIdentifier().getUniqueid();
				mapTestpackage.put(uniqueid, testpackage);
			}
		}
	}


}

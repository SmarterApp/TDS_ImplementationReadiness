package org.cresst.sb.irp.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

@Service
public class XMLValidate {
	private static Logger logger = Logger.getLogger(XMLValidate.class);

	public XMLValidate() {
		logger.info("initializing");
	}

	public boolean validateXMLSchema(String xsdPath, String xmlPath)
			throws IOException {

		try {
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(xsdPath));
			Validator validator = (Validator) schema.newValidator();
			validator.validate(new StreamSource(new File(xmlPath)), null);
		} catch (SAXException e) {
			System.out.println("Exception: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean validateXMLSchema(Resource resource, MultipartFile file)
			throws IOException {

		try {
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(resource.getFile());  //resource.getInputStream());
			Validator validator = (Validator) schema.newValidator();
			validator.validate(new StreamSource(file.getInputStream()), null);
		} catch (SAXException e) {
			System.out.println("Exception: " + e.getMessage());
			return false;
		}
		return true;
	}

}

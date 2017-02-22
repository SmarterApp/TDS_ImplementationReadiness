package org.cresst.sb.irp.utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Service to validate xml files
 */
@Service
public class XMLValidate {
	private final static Logger logger = LoggerFactory.getLogger(XMLValidate.class);

    /**
     * 
     * @param schemaResource
     * @param xmlPath
     * @return true if `xmlPath` is a path to a valid xml file according to the
     *         schema in `schemaResource`, otherwise false.
     * @throws IOException
     */
	public boolean validateXMLSchema(String xsdPath, String xmlPath)
			throws IOException {

		try {
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(new File(xsdPath));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new File(xmlPath)), null);
		} catch (SAXException e) {
			logger.error("Exception: ", e);
			return false;
		}
		return true;
	}

    /**
     * 
     * @param schemaResource
     * @param file
     * @return true if `file` is a valid xml file according to the schema in
     *         `schemaResource`, otherwise false.
     * @throws IOException
     */
	public boolean validateXMLSchema(Resource schemaResource, MultipartFile file)
			throws IOException {

		try {
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(schemaResource.getFile());  //resource.getInputStream());
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(file.getInputStream()), null);
		} catch (SAXException e) {
			logger.error("Exception: ", e);
			return false;
		}
		return true;
	}

    /**
     * 
     * @param schemaResource
     * @param file
     * @return true if `file` is a valid xml file according to the schema in
     *         `schemaResource`, otherwise false.
     * @throws IOException
     */
	public boolean validateXMLSchema(Resource schemaResource, String file)
			throws IOException {

		try {
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(schemaResource.getFile());
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(file));
		} catch (SAXException e) {
			logger.error("Exception: ", e);
			return false;
		}
		return true;
	}

    /**
     * Validates xml schema and generates a list of XmlExceptions
     * 
     * @param schemaResource
     * @param file
     * @return
     * @throws IOException
     */
	public List<SAXException> validateXMLSchemaExceptionList(Resource schemaResource, String file)
			throws IOException {
	    final List<SAXException> exceptions = new LinkedList<SAXException>();
	    try {
    		SchemaFactory factory = SchemaFactory
    				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    		Schema schema = factory.newSchema(schemaResource.getFile());
    		Validator validator = schema.newValidator();
    		// Override error handler and keep all xml errors

    		validator.setErrorHandler(new ErrorHandler()
        	{
        	  @Override
        	  public void warning(SAXParseException exception) throws SAXException
        	  {
        	    exceptions.add(exception);
        	  }

        	  @Override
        	  public void fatalError(SAXParseException exception) throws SAXException
        	  {
        	    exceptions.add(exception);
        	  }

        	  @Override
        	  public void error(SAXParseException exception) throws SAXException
        	  {
        	    exceptions.add(exception);
        	  }
        	});

            validator.validate(new StreamSource(file));
        } catch (SAXException e) {
            // Do nothing, we are returning errors any
        }
		return exceptions;
	}
}

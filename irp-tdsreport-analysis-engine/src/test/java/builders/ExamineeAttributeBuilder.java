package builders;

import org.cresst.sb.irp.domain.tdsreport.Context;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Builder for ExamineeAttribute objects
 */
public class ExamineeAttributeBuilder {
    private TDSReport.Examinee.ExamineeAttribute examineeAttribute = new TDSReport.Examinee.ExamineeAttribute();

    public ExamineeAttributeBuilder() {
        try {
            XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(2014, 1, 20, 12, 0, 0, 0, 0);
            examineeAttribute.setContextDate(calendar);
        } catch (Exception ex) {
            // noop
        }
    }

    public ExamineeAttributeBuilder name(String name) {
        examineeAttribute.setName(name);
        return this;
    }

    public ExamineeAttributeBuilder value(String value) {
        examineeAttribute.setValue(value);
        return this;
    }

    public ExamineeAttributeBuilder context(Context context) {
        examineeAttribute.setContext(context);
        return this;
    }

    public ExamineeAttributeBuilder contextDate(XMLGregorianCalendar contextDate) {
        examineeAttribute.setContextDate(contextDate);
        return this;
    }

    public TDSReport.Examinee.ExamineeAttribute toExamineeAttribute() {
        return examineeAttribute;
    }
}
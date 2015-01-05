package builders;

import org.cresst.sb.irp.domain.tdsreport.Context;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Builder for ExamineeRelationship objects
 */
public class ExamineeRelationshipBuilder {
    private TDSReport.Examinee.ExamineeRelationship examineeRelationship = new TDSReport.Examinee.ExamineeRelationship();

    public ExamineeRelationshipBuilder() {
        try {
            XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(2014, 1, 20, 12, 0, 0, 0, 0);
            examineeRelationship.setContextDate(calendar);
        } catch (Exception ex) {
            // noop
        }
    }

    public ExamineeRelationshipBuilder name(String name) {
        examineeRelationship.setName(name);
        return this;
    }

    public ExamineeRelationshipBuilder value(String value) {
        examineeRelationship.setValue(value);
        return this;
    }

    public ExamineeRelationshipBuilder context(Context context) {
        examineeRelationship.setContext(context);
        return this;
    }

    public ExamineeRelationshipBuilder contextDate(XMLGregorianCalendar contextDate) {
        examineeRelationship.setContextDate(contextDate);
        return this;
    }

    public TDSReport.Examinee.ExamineeRelationship toExamineeRelationship() {
        return examineeRelationship;
    }
}
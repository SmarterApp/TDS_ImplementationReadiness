package org.cresst.sb.irp.analysis.engine.examinee;

import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.analysis.engine.AccommodationAnalysisAction;
import org.cresst.sb.irp.domain.tdsreport.Context;
import org.cresst.sb.irp.domain.tdsreport.TDSReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Helps get accommodations and relationships from an Examinee
 */
public class ExamineeHelper {

    static public String getStudentIdentifier(TDSReport.Examinee examinee) {
        return getFinalExamineeAttributeValue(examinee, AccommodationAnalysisAction.EnumAccommodationTypeAcceptableVaues.StudentIdentifier.name());
    }

    /**
     * Gets all of the ExamineeAttributes that have the FINAL context attribute
     *
     * @param examinee Examinee containing the ExamineeAttributes
     * @return List of ExamineeAttributes marked with FINAL context attribute. Never null.
     */
    static public List<TDSReport.Examinee.ExamineeAttribute> getFinalExamineeAttributes(TDSReport.Examinee examinee) {
        List<TDSReport.Examinee.ExamineeAttribute> examineeAttributes = new ArrayList<>();

        if (examinee != null) {
            List<Object> listObject = examinee.getExamineeAttributeOrExamineeRelationship();
            for (Object object : listObject) {
                if (object instanceof TDSReport.Examinee.ExamineeAttribute) {
                    TDSReport.Examinee.ExamineeAttribute examineeAttribute = (TDSReport.Examinee.ExamineeAttribute) object;
                    if (examineeAttribute.getContext() == Context.FINAL) {
                        examineeAttributes.add(examineeAttribute);
                    }
                }
            }
        }

        return examineeAttributes;
    }

    /**
     * Returns the Examinee's attribute value if the attribute name exists. The attributes with the FINAL context
     * are searched.
     *
     * @param examinee Examinee containing the ExamineeAttributes
     * @param attributeName The name of the attribute to get the value
     * @return Returns null if the attribute name does not exists.
     */
    static public String getFinalExamineeAttributeValue(TDSReport.Examinee examinee, String attributeName) {
 
    	if (examinee != null && StringUtils.isNotBlank(attributeName)) {
            List<Object> listObject = examinee.getExamineeAttributeOrExamineeRelationship();
            for (Object object : listObject) {
                if (object instanceof TDSReport.Examinee.ExamineeAttribute) {
                    TDSReport.Examinee.ExamineeAttribute examineeAttribute = (TDSReport.Examinee.ExamineeAttribute) object;
                    if (examineeAttribute.getContext() == Context.FINAL && attributeName.equalsIgnoreCase(examineeAttribute.getName())) {
                        return examineeAttribute.getValue();
                    }
                }
            }
        }

        return null;
    }

    /**
     * Gets all of the ExamineeRelationships that have the FINAL context attribute
     *
     * @param examinee Examinee containing the ExamineeRelationships
     * @return List of ExamineeRelationships marked with FINAL context attribute. Never null.
     */
    static public List<TDSReport.Examinee.ExamineeRelationship> getFinalExamineeRelationships(TDSReport.Examinee examinee) {
        List<TDSReport.Examinee.ExamineeRelationship> listExamineeRelationship = new ArrayList<>();

        if (examinee != null) {
            List<Object> listObject = examinee.getExamineeAttributeOrExamineeRelationship();
            for (Object object : listObject) {
                if (object instanceof TDSReport.Examinee.ExamineeRelationship) {
                    TDSReport.Examinee.ExamineeRelationship examineeRelationship = (TDSReport.Examinee.ExamineeRelationship) object;
                    if (examineeRelationship.getContext() == Context.FINAL) {
                        listExamineeRelationship.add(examineeRelationship);
                    }
                }
            }
        }

        return listExamineeRelationship;
    }

    /**
     * Returns the Examinee's relationship value if the relationship name exists. The relationships with the FINAL context
     * are searched.
     *
     * @param examinee Examinee containing the ExamineeRelationships
     * @param relationshipName The name of the relationship to search for
     * @return Returns null if the name of the relationship is not found.
     */
    static public String getFinalExamineeRelationshipValue(TDSReport.Examinee examinee, String relationshipName) {

        if (examinee != null && StringUtils.isNotBlank(relationshipName)) {
            List<Object> listObject = examinee.getExamineeAttributeOrExamineeRelationship();
            for (Object object : listObject) {
                if (object instanceof TDSReport.Examinee.ExamineeRelationship) {
                    TDSReport.Examinee.ExamineeRelationship examineeRelationship = (TDSReport.Examinee.ExamineeRelationship) object;
                    if (examineeRelationship.getContext() == Context.FINAL && relationshipName.equalsIgnoreCase(examineeRelationship.getName())) {
                        return examineeRelationship.getValue();
                    }
                }
            }
        }

        return null;
    }

}

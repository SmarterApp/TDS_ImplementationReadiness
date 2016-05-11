package org.cresst.sb.irp.analysis.engine.examinee;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.cresst.sb.irp.analysis.engine.AccommodationAnalysisAction;
import org.cresst.sb.irp.analysis.engine.ExamineeRelationshipAnalysisAction;
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
     * Given an Examinee and an Examinee Attribute to lookup, this method returns the Examinee Attribute for the
     * given Examinee.
     * @param examinee The Examinee to get the Attribute from
     * @param examineeAttributeToGet The Attribute to get
     * @return Returns null if the Attribute does not exist in the Examinee; otherwise it returns the ExamineeAttribute
     */
    static public TDSReport.Examinee.ExamineeAttribute getFinalExamineeAttribute(TDSReport.Examinee examinee,
                                                       EnumExamineeAttributeFieldName examineeAttributeToGet) {

        if (examinee == null) {
            return null;
        }

        List<Object> listObject = examinee.getExamineeAttributeOrExamineeRelationship();
        for (Object object : listObject) {
            if (object instanceof TDSReport.Examinee.ExamineeAttribute) {

                TDSReport.Examinee.ExamineeAttribute examineeAttribute = (TDSReport.Examinee.ExamineeAttribute) object;
                if (examineeAttribute.getContext() == Context.FINAL) {

                    EnumExamineeAttributeFieldName currentExamineeAttributeFieldName =
                            convertToExamineeAttributeEnum(examineeAttribute.getName());

                    if (currentExamineeAttributeFieldName == examineeAttributeToGet) {
                        return examineeAttribute;
                    }
                }
            }
        }

        return null;
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
     * Given an Examinee and an Examinee Relationship to lookup, this method returns the Examinee Relationship for the
     * given Examinee.
     * @param examinee The Examinee to get the Relationship from
     * @param examineeRelationshipToGet The Relationship to get
     * @return Returns null if the Relationship does not exist in the Examinee; otherwise it returns the ExamineeRelationship
     */
    static public TDSReport.Examinee.ExamineeRelationship getFinalExamineeRelationship(TDSReport.Examinee examinee,
                                                                                 EnumExamineeRelationshipFieldName examineeRelationshipToGet) {

        if (examinee == null) {
            return null;
        }

        List<Object> listObject = examinee.getExamineeAttributeOrExamineeRelationship();
        for (Object object : listObject) {
            if (object instanceof TDSReport.Examinee.ExamineeRelationship) {

                TDSReport.Examinee.ExamineeRelationship examineeRelationship = (TDSReport.Examinee.ExamineeRelationship) object;
                if (examineeRelationship.getContext() == Context.FINAL) {

                    EnumExamineeRelationshipFieldName currentExamineeRelationshipFieldName =
                            convertToExamineeRelationshipEnum(examineeRelationship.getName());

                    if (currentExamineeRelationshipFieldName == examineeRelationshipToGet) {
                        return examineeRelationship;
                    }
                }
            }
        }

        return null;
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

    /**
     * Converts the given string to an {@link EnumExamineeAttributeFieldName}.
     * If it can't convert, then null is returned.
     *
     * @param nameFieldValue
     * @return The enum if string represents a valid enum. Null otherwise.
     */
    static public EnumExamineeAttributeFieldName convertToExamineeAttributeEnum(String nameFieldValue) {

        if (EnumUtils.isValidEnum(EnumExamineeAttributeFieldName.class, nameFieldValue)) {
            return EnumExamineeAttributeFieldName.valueOf(nameFieldValue);
        }

        return null;
    }

    /**
     * Converts the given string to an {@link EnumExamineeRelationshipFieldName}.
     * If it can't convert, then null is returned.
     *
     * @param nameFieldValue
     * @return The enum if string represents a valid enum. Null otherwise.
     */
    static public EnumExamineeRelationshipFieldName convertToExamineeRelationshipEnum(String nameFieldValue) {
        if (EnumUtils.isValidEnum(EnumExamineeRelationshipFieldName.class, nameFieldValue)) {
            return EnumExamineeRelationshipFieldName.valueOf(nameFieldValue);
        }

        return null;
    }
}

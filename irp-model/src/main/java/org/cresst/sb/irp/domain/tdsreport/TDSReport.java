
package org.cresst.sb.irp.domain.tdsreport;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Test">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="subject" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="testId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="bankKey" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                 &lt;attribute name="contract" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="mode" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *                       &lt;enumeration value="online"/>
 *                       &lt;enumeration value="paper"/>
 *                       &lt;enumeration value="scanned"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="grade" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="handScoreProject" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                 &lt;attribute name="assessmentType" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="academicYear" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                 &lt;attribute name="assessmentVersion" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Examinee">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element name="ExamineeAttribute">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="context" use="required" type="{}Context" />
 *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="contextDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="ExamineeRelationship">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="context" use="required" type="{}Context" />
 *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="entityKey" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *                           &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="contextDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/choice>
 *                 &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *                 &lt;attribute name="isDemo" type="{}Bit" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Opportunity">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Segment" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="position" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
 *                                 &lt;minInclusive value="1"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="formId" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="formKey" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="algorithm" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="algorithmVersion" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Accommodation" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="segment" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
 *                                 &lt;minInclusive value="0"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Score" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="measureOf" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="measureLabel" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="standardError" type="{}NullableFloat" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="GenericVariable" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="context" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Item" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Response" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;attribute name="date" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                                     &lt;attribute name="type">
 *                                       &lt;simpleType>
 *                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *                                           &lt;enumeration value="value"/>
 *                                           &lt;enumeration value="reference"/>
 *                                           &lt;enumeration value=""/>
 *                                         &lt;/restriction>
 *                                       &lt;/simpleType>
 *                                     &lt;/attribute>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="ScoreInfo" type="{}ScoreInfoType" minOccurs="0"/>
 *                           &lt;/sequence>
 *                           &lt;attribute name="position" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                           &lt;attribute name="segmentId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="bankKey" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                           &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                           &lt;attribute name="operational" use="required" type="{}Bit" />
 *                           &lt;attribute name="isSelected" use="required" type="{}Bit" />
 *                           &lt;attribute name="format" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="score" use="required" type="{}UFloatAllowNegativeOne" />
 *                           &lt;attribute name="scoreStatus">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *                                 &lt;enumeration value="NOTSCORED"/>
 *                                 &lt;enumeration value="SCORED"/>
 *                                 &lt;enumeration value="WAITINGFORMACHINESCORE"/>
 *                                 &lt;enumeration value="SCORINGERROR"/>
 *                                 &lt;enumeration value="APPEALED"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="adminDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                           &lt;attribute name="numberVisits" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                           &lt;attribute name="mimeType" use="required">
 *                             &lt;simpleType>
 *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *                                 &lt;enumeration value="text/plain"/>
 *                                 &lt;enumeration value="text/xml"/>
 *                                 &lt;enumeration value="text/html"/>
 *                                 &lt;enumeration value="audio/ogg"/>
 *                               &lt;/restriction>
 *                             &lt;/simpleType>
 *                           &lt;/attribute>
 *                           &lt;attribute name="clientId" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="strand" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="contentLevel" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="pageNumber" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                           &lt;attribute name="pageVisits" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                           &lt;attribute name="pageTime" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *                           &lt;attribute name="dropped" use="required" type="{}Bit" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="server" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="database" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="clientName" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="oppId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="startDate" type="{}NullableDateTime" />
 *                 &lt;attribute name="status" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *                       &lt;enumeration value="appeal"/>
 *                       &lt;enumeration value="completed"/>
 *                       &lt;enumeration value="expired"/>
 *                       &lt;enumeration value="handscoring"/>
 *                       &lt;enumeration value="invalidated"/>
 *                       &lt;enumeration value="paused"/>
 *                       &lt;enumeration value="reported"/>
 *                       &lt;enumeration value="reset"/>
 *                       &lt;enumeration value="scored"/>
 *                       &lt;enumeration value="submitted"/>
 *                       &lt;enumeration value="pending"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="validity">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *                       &lt;enumeration value="valid"/>
 *                       &lt;enumeration value="invalid"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="completeness">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *                       &lt;enumeration value="none"/>
 *                       &lt;enumeration value="partial"/>
 *                       &lt;enumeration value="complete"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="opportunity" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                 &lt;attribute name="statusDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                 &lt;attribute name="dateCompleted" type="{}NullableDateTime" />
 *                 &lt;attribute name="pauseCount" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                 &lt;attribute name="itemCount" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                 &lt;attribute name="ftCount" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                 &lt;attribute name="abnormalStarts" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                 &lt;attribute name="gracePeriodRestarts" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                 &lt;attribute name="taId" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="taName" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="sessionId" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="windowId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="windowOpportunity" type="{}NullableUInt" />
 *                 &lt;attribute name="completeStatus" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="administrationCondition" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="dateForceCompleted" type="{}NullableDateTime" />
 *                 &lt;attribute name="qaLevel" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="assessmentParticipantSessionPlatformUserAgent" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="effectiveDate" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="reportingVersion" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Comment" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="context" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="itemPosition" type="{}NullableUInt" />
 *                 &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ToolUsage" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ToolPage" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="page" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                           &lt;attribute name="groupId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                           &lt;attribute name="count" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "test",
    "examinee",
    "opportunity",
    "comment",
    "toolUsage"
})
@XmlRootElement(name = "TDSReport")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
public class TDSReport {

    @XmlElement(name = "Test", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    protected TDSReport.Test test;
    @XmlElement(name = "Examinee", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    protected TDSReport.Examinee examinee;
    @XmlElement(name = "Opportunity", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    protected TDSReport.Opportunity opportunity;
    @XmlElement(name = "Comment")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    protected List<TDSReport.Comment> comment;
    @XmlElement(name = "ToolUsage")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    protected List<TDSReport.ToolUsage> toolUsage;

    /**
     * Gets the value of the test property.
     * 
     * @return
     *     possible object is
     *     {@link TDSReport.Test }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public TDSReport.Test getTest() {
        return test;
    }

    /**
     * Sets the value of the test property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDSReport.Test }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public void setTest(TDSReport.Test value) {
        this.test = value;
    }

    /**
     * Gets the value of the examinee property.
     * 
     * @return
     *     possible object is
     *     {@link TDSReport.Examinee }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public TDSReport.Examinee getExaminee() {
        return examinee;
    }

    /**
     * Sets the value of the examinee property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDSReport.Examinee }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public void setExaminee(TDSReport.Examinee value) {
        this.examinee = value;
    }

    /**
     * Gets the value of the opportunity property.
     * 
     * @return
     *     possible object is
     *     {@link TDSReport.Opportunity }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public TDSReport.Opportunity getOpportunity() {
        return opportunity;
    }

    /**
     * Sets the value of the opportunity property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDSReport.Opportunity }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public void setOpportunity(TDSReport.Opportunity value) {
        this.opportunity = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the comment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TDSReport.Comment }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public List<TDSReport.Comment> getComment() {
        if (comment == null) {
            comment = new ArrayList<TDSReport.Comment>();
        }
        return this.comment;
    }

    /**
     * Gets the value of the toolUsage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the toolUsage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getToolUsage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TDSReport.ToolUsage }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public List<TDSReport.ToolUsage> getToolUsage() {
        if (toolUsage == null) {
            toolUsage = new ArrayList<TDSReport.ToolUsage>();
        }
        return this.toolUsage;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="context" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="itemPosition" type="{}NullableUInt" />
     *       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "content"
    })
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public static class Comment {

        @XmlValue
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String content;
        @XmlAttribute(name = "context", required = true)
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String context;
        @XmlAttribute(name = "itemPosition")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String itemPosition;
        @XmlAttribute(name = "date", required = true)
        @XmlSchemaType(name = "dateTime")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected XMLGregorianCalendar date;

        /**
         * Gets the value of the content property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getContent() {
            return content;
        }

        /**
         * Sets the value of the content property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setContent(String value) {
            this.content = value;
        }

        /**
         * Gets the value of the context property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getContext() {
            return context;
        }

        /**
         * Sets the value of the context property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setContext(String value) {
            this.context = value;
        }

        /**
         * Gets the value of the itemPosition property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getItemPosition() {
            return itemPosition;
        }

        /**
         * Sets the value of the itemPosition property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setItemPosition(String value) {
            this.itemPosition = value;
        }

        /**
         * Gets the value of the date property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public XMLGregorianCalendar getDate() {
            return date;
        }

        /**
         * Sets the value of the date property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setDate(XMLGregorianCalendar value) {
            this.date = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice maxOccurs="unbounded" minOccurs="0">
     *         &lt;element name="ExamineeAttribute">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="context" use="required" type="{}Context" />
     *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="contextDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="ExamineeRelationship">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="context" use="required" type="{}Context" />
     *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="entityKey" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
     *                 &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="contextDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/choice>
     *       &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
     *       &lt;attribute name="isDemo" type="{}Bit" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "examineeAttributeOrExamineeRelationship"
    })
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public static class Examinee {

        @XmlElements({
            @XmlElement(name = "ExamineeAttribute", type = TDSReport.Examinee.ExamineeAttribute.class),
            @XmlElement(name = "ExamineeRelationship", type = TDSReport.Examinee.ExamineeRelationship.class)
        })
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected List<Object> examineeAttributeOrExamineeRelationship;
        @XmlAttribute(name = "key", required = true)
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected long key;
        @XmlAttribute(name = "isDemo")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected Short isDemo;

        /**
         * Gets the value of the examineeAttributeOrExamineeRelationship property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the examineeAttributeOrExamineeRelationship property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getExamineeAttributeOrExamineeRelationship().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TDSReport.Examinee.ExamineeAttribute }
         * {@link TDSReport.Examinee.ExamineeRelationship }
         * 
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public List<Object> getExamineeAttributeOrExamineeRelationship() {
            if (examineeAttributeOrExamineeRelationship == null) {
                examineeAttributeOrExamineeRelationship = new ArrayList<Object>();
            }
            return this.examineeAttributeOrExamineeRelationship;
        }

        /**
         * Gets the value of the key property.
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public long getKey() {
            return key;
        }

        /**
         * Sets the value of the key property.
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setKey(long value) {
            this.key = value;
        }

        /**
         * Gets the value of the isDemo property.
         * 
         * @return
         *     possible object is
         *     {@link Short }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public Short getIsDemo() {
            return isDemo;
        }

        /**
         * Sets the value of the isDemo property.
         * 
         * @param value
         *     allowed object is
         *     {@link Short }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setIsDemo(Short value) {
            this.isDemo = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="context" use="required" type="{}Context" />
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="contextDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public static class ExamineeAttribute {

            @XmlAttribute(name = "context", required = true)
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected Context context;
            @XmlAttribute(name = "name", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String name;
            @XmlAttribute(name = "value")
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String value;
            @XmlAttribute(name = "contextDate", required = true)
            @XmlSchemaType(name = "dateTime")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected XMLGregorianCalendar contextDate;

            /**
             * Gets the value of the context property.
             * 
             * @return
             *     possible object is
             *     {@link Context }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public Context getContext() {
                return context;
            }

            /**
             * Sets the value of the context property.
             * 
             * @param value
             *     allowed object is
             *     {@link Context }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setContext(Context value) {
                this.context = value;
            }

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setValue(String value) {
                this.value = value;
            }

            /**
             * Gets the value of the contextDate property.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public XMLGregorianCalendar getContextDate() {
                return contextDate;
            }

            /**
             * Sets the value of the contextDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setContextDate(XMLGregorianCalendar value) {
                this.contextDate = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="context" use="required" type="{}Context" />
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="entityKey" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
         *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="contextDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public static class ExamineeRelationship {

            @XmlAttribute(name = "context", required = true)
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected Context context;
            @XmlAttribute(name = "name", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String name;
            @XmlAttribute(name = "entityKey")
            @XmlSchemaType(name = "unsignedLong")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected BigInteger entityKey;
            @XmlAttribute(name = "value")
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String value;
            @XmlAttribute(name = "contextDate", required = true)
            @XmlSchemaType(name = "dateTime")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected XMLGregorianCalendar contextDate;

            /**
             * Gets the value of the context property.
             * 
             * @return
             *     possible object is
             *     {@link Context }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public Context getContext() {
                return context;
            }

            /**
             * Sets the value of the context property.
             * 
             * @param value
             *     allowed object is
             *     {@link Context }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setContext(Context value) {
                this.context = value;
            }

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Gets the value of the entityKey property.
             * 
             * @return
             *     possible object is
             *     {@link BigInteger }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public BigInteger getEntityKey() {
                return entityKey;
            }

            /**
             * Sets the value of the entityKey property.
             * 
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setEntityKey(BigInteger value) {
                this.entityKey = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setValue(String value) {
                this.value = value;
            }

            /**
             * Gets the value of the contextDate property.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public XMLGregorianCalendar getContextDate() {
                return contextDate;
            }

            /**
             * Sets the value of the contextDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setContextDate(XMLGregorianCalendar value) {
                this.contextDate = value;
            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Segment" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="position" use="required">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
     *                       &lt;minInclusive value="1"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="formId" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="formKey" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="algorithm" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="algorithmVersion" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Accommodation" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="segment" use="required">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
     *                       &lt;minInclusive value="0"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Score" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="measureOf" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="measureLabel" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="standardError" type="{}NullableFloat" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="GenericVariable" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="context" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Item" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Response" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="date" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *                           &lt;attribute name="type">
     *                             &lt;simpleType>
     *                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
     *                                 &lt;enumeration value="value"/>
     *                                 &lt;enumeration value="reference"/>
     *                                 &lt;enumeration value=""/>
     *                               &lt;/restriction>
     *                             &lt;/simpleType>
     *                           &lt;/attribute>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="ScoreInfo" type="{}ScoreInfoType" minOccurs="0"/>
     *                 &lt;/sequence>
     *                 &lt;attribute name="position" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *                 &lt;attribute name="segmentId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="bankKey" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *                 &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *                 &lt;attribute name="operational" use="required" type="{}Bit" />
     *                 &lt;attribute name="isSelected" use="required" type="{}Bit" />
     *                 &lt;attribute name="format" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="score" use="required" type="{}UFloatAllowNegativeOne" />
     *                 &lt;attribute name="scoreStatus">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
     *                       &lt;enumeration value="NOTSCORED"/>
     *                       &lt;enumeration value="SCORED"/>
     *                       &lt;enumeration value="WAITINGFORMACHINESCORE"/>
     *                       &lt;enumeration value="SCORINGERROR"/>
     *                       &lt;enumeration value="APPEALED"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="adminDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *                 &lt;attribute name="numberVisits" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *                 &lt;attribute name="mimeType" use="required">
     *                   &lt;simpleType>
     *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
     *                       &lt;enumeration value="text/plain"/>
     *                       &lt;enumeration value="text/xml"/>
     *                       &lt;enumeration value="text/html"/>
     *                       &lt;enumeration value="audio/ogg"/>
     *                     &lt;/restriction>
     *                   &lt;/simpleType>
     *                 &lt;/attribute>
     *                 &lt;attribute name="clientId" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="strand" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="contentLevel" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="pageNumber" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *                 &lt;attribute name="pageVisits" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *                 &lt;attribute name="pageTime" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
     *                 &lt;attribute name="dropped" use="required" type="{}Bit" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="server" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="database" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="clientName" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="oppId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="startDate" type="{}NullableDateTime" />
     *       &lt;attribute name="status" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
     *             &lt;enumeration value="appeal"/>
     *             &lt;enumeration value="completed"/>
     *             &lt;enumeration value="expired"/>
     *             &lt;enumeration value="handscoring"/>
     *             &lt;enumeration value="invalidated"/>
     *             &lt;enumeration value="paused"/>
     *             &lt;enumeration value="reported"/>
     *             &lt;enumeration value="reset"/>
     *             &lt;enumeration value="scored"/>
     *             &lt;enumeration value="submitted"/>
     *             &lt;enumeration value="pending"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="validity">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
     *             &lt;enumeration value="valid"/>
     *             &lt;enumeration value="invalid"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="completeness">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
     *             &lt;enumeration value="none"/>
     *             &lt;enumeration value="partial"/>
     *             &lt;enumeration value="complete"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="opportunity" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *       &lt;attribute name="statusDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *       &lt;attribute name="dateCompleted" type="{}NullableDateTime" />
     *       &lt;attribute name="pauseCount" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *       &lt;attribute name="itemCount" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *       &lt;attribute name="ftCount" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *       &lt;attribute name="abnormalStarts" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *       &lt;attribute name="gracePeriodRestarts" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *       &lt;attribute name="taId" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="taName" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="sessionId" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="windowId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="windowOpportunity" type="{}NullableUInt" />
     *       &lt;attribute name="completeStatus" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="administrationCondition" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="dateForceCompleted" type="{}NullableDateTime" />
     *       &lt;attribute name="qaLevel" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="assessmentParticipantSessionPlatformUserAgent" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="effectiveDate" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="reportingVersion" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "segment",
        "accommodation",
        "score",
        "genericVariable",
        "item"
    })
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public static class Opportunity {

        @XmlElement(name = "Segment")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected List<TDSReport.Opportunity.Segment> segment;
        @XmlElement(name = "Accommodation")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected List<TDSReport.Opportunity.Accommodation> accommodation;
        @XmlElement(name = "Score")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected List<TDSReport.Opportunity.Score> score;
        @XmlElement(name = "GenericVariable")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected List<TDSReport.Opportunity.GenericVariable> genericVariable;
        @XmlElement(name = "Item")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected List<TDSReport.Opportunity.Item> item;
        @XmlAttribute(name = "server", required = true)
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String server;
        @XmlAttribute(name = "database")
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String database;
        @XmlAttribute(name = "clientName", required = true)
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String clientName;
        @XmlAttribute(name = "key", required = true)
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String key;
        @XmlAttribute(name = "oppId", required = true)
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String oppId;
        @XmlAttribute(name = "startDate")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String startDate;
        @XmlAttribute(name = "status", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String status;
        @XmlAttribute(name = "validity")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String validity;
        @XmlAttribute(name = "completeness")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String completeness;
        @XmlAttribute(name = "opportunity", required = true)
        @XmlSchemaType(name = "unsignedInt")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected long opportunity;
        @XmlAttribute(name = "statusDate", required = true)
        @XmlSchemaType(name = "dateTime")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected XMLGregorianCalendar statusDate;
        @XmlAttribute(name = "dateCompleted")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String dateCompleted;
        @XmlAttribute(name = "pauseCount", required = true)
        @XmlSchemaType(name = "unsignedInt")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected long pauseCount;
        @XmlAttribute(name = "itemCount", required = true)
        @XmlSchemaType(name = "unsignedInt")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected long itemCount;
        @XmlAttribute(name = "ftCount", required = true)
        @XmlSchemaType(name = "unsignedInt")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected long ftCount;
        @XmlAttribute(name = "abnormalStarts", required = true)
        @XmlSchemaType(name = "unsignedInt")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected long abnormalStarts;
        @XmlAttribute(name = "gracePeriodRestarts", required = true)
        @XmlSchemaType(name = "unsignedInt")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected long gracePeriodRestarts;
        @XmlAttribute(name = "taId")
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String taId;
        @XmlAttribute(name = "taName")
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String taName;
        @XmlAttribute(name = "sessionId")
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String sessionId;
        @XmlAttribute(name = "windowId", required = true)
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String windowId;
        @XmlAttribute(name = "windowOpportunity")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String windowOpportunity;
        @XmlAttribute(name = "completeStatus")
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String completeStatus;
        @XmlAttribute(name = "administrationCondition")
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String administrationCondition;
        @XmlAttribute(name = "dateForceCompleted")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String dateForceCompleted;
        @XmlAttribute(name = "qaLevel")
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String qaLevel;
        @XmlAttribute(name = "assessmentParticipantSessionPlatformUserAgent")
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String assessmentParticipantSessionPlatformUserAgent;
        @XmlAttribute(name = "effectiveDate")
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String effectiveDate;
        @XmlAttribute(name = "reportingVersion")
        @XmlSchemaType(name = "unsignedInt")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected Long reportingVersion;

        /**
         * Gets the value of the segment property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the segment property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSegment().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TDSReport.Opportunity.Segment }
         * 
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public List<TDSReport.Opportunity.Segment> getSegment() {
            if (segment == null) {
                segment = new ArrayList<TDSReport.Opportunity.Segment>();
            }
            return this.segment;
        }

        /**
         * Gets the value of the accommodation property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the accommodation property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAccommodation().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TDSReport.Opportunity.Accommodation }
         * 
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public List<TDSReport.Opportunity.Accommodation> getAccommodation() {
            if (accommodation == null) {
                accommodation = new ArrayList<TDSReport.Opportunity.Accommodation>();
            }
            return this.accommodation;
        }

        /**
         * Gets the value of the score property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the score property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getScore().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TDSReport.Opportunity.Score }
         * 
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public List<TDSReport.Opportunity.Score> getScore() {
            if (score == null) {
                score = new ArrayList<TDSReport.Opportunity.Score>();
            }
            return this.score;
        }

        /**
         * Gets the value of the genericVariable property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the genericVariable property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGenericVariable().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TDSReport.Opportunity.GenericVariable }
         * 
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public List<TDSReport.Opportunity.GenericVariable> getGenericVariable() {
            if (genericVariable == null) {
                genericVariable = new ArrayList<TDSReport.Opportunity.GenericVariable>();
            }
            return this.genericVariable;
        }

        /**
         * Gets the value of the item property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the item property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getItem().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TDSReport.Opportunity.Item }
         * 
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public List<TDSReport.Opportunity.Item> getItem() {
            if (item == null) {
                item = new ArrayList<TDSReport.Opportunity.Item>();
            }
            return this.item;
        }

        /**
         * Gets the value of the server property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getServer() {
            return server;
        }

        /**
         * Sets the value of the server property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setServer(String value) {
            this.server = value;
        }

        /**
         * Gets the value of the database property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getDatabase() {
            return database;
        }

        /**
         * Sets the value of the database property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setDatabase(String value) {
            this.database = value;
        }

        /**
         * Gets the value of the clientName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getClientName() {
            return clientName;
        }

        /**
         * Sets the value of the clientName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setClientName(String value) {
            this.clientName = value;
        }

        /**
         * Gets the value of the key property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getKey() {
            return key;
        }

        /**
         * Sets the value of the key property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setKey(String value) {
            this.key = value;
        }

        /**
         * Gets the value of the oppId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getOppId() {
            return oppId;
        }

        /**
         * Sets the value of the oppId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setOppId(String value) {
            this.oppId = value;
        }

        /**
         * Gets the value of the startDate property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getStartDate() {
            return startDate;
        }

        /**
         * Sets the value of the startDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setStartDate(String value) {
            this.startDate = value;
        }

        /**
         * Gets the value of the status property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getStatus() {
            return status;
        }

        /**
         * Sets the value of the status property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setStatus(String value) {
            this.status = value;
        }

        /**
         * Gets the value of the validity property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getValidity() {
            return validity;
        }

        /**
         * Sets the value of the validity property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setValidity(String value) {
            this.validity = value;
        }

        /**
         * Gets the value of the completeness property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getCompleteness() {
            return completeness;
        }

        /**
         * Sets the value of the completeness property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setCompleteness(String value) {
            this.completeness = value;
        }

        /**
         * Gets the value of the opportunity property.
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public long getOpportunity() {
            return opportunity;
        }

        /**
         * Sets the value of the opportunity property.
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setOpportunity(long value) {
            this.opportunity = value;
        }

        /**
         * Gets the value of the statusDate property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public XMLGregorianCalendar getStatusDate() {
            return statusDate;
        }

        /**
         * Sets the value of the statusDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setStatusDate(XMLGregorianCalendar value) {
            this.statusDate = value;
        }

        /**
         * Gets the value of the dateCompleted property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getDateCompleted() {
            return dateCompleted;
        }

        /**
         * Sets the value of the dateCompleted property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setDateCompleted(String value) {
            this.dateCompleted = value;
        }

        /**
         * Gets the value of the pauseCount property.
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public long getPauseCount() {
            return pauseCount;
        }

        /**
         * Sets the value of the pauseCount property.
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setPauseCount(long value) {
            this.pauseCount = value;
        }

        /**
         * Gets the value of the itemCount property.
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public long getItemCount() {
            return itemCount;
        }

        /**
         * Sets the value of the itemCount property.
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setItemCount(long value) {
            this.itemCount = value;
        }

        /**
         * Gets the value of the ftCount property.
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public long getFtCount() {
            return ftCount;
        }

        /**
         * Sets the value of the ftCount property.
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setFtCount(long value) {
            this.ftCount = value;
        }

        /**
         * Gets the value of the abnormalStarts property.
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public long getAbnormalStarts() {
            return abnormalStarts;
        }

        /**
         * Sets the value of the abnormalStarts property.
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setAbnormalStarts(long value) {
            this.abnormalStarts = value;
        }

        /**
         * Gets the value of the gracePeriodRestarts property.
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public long getGracePeriodRestarts() {
            return gracePeriodRestarts;
        }

        /**
         * Sets the value of the gracePeriodRestarts property.
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setGracePeriodRestarts(long value) {
            this.gracePeriodRestarts = value;
        }

        /**
         * Gets the value of the taId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getTaId() {
            return taId;
        }

        /**
         * Sets the value of the taId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setTaId(String value) {
            this.taId = value;
        }

        /**
         * Gets the value of the taName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getTaName() {
            return taName;
        }

        /**
         * Sets the value of the taName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setTaName(String value) {
            this.taName = value;
        }

        /**
         * Gets the value of the sessionId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getSessionId() {
            return sessionId;
        }

        /**
         * Sets the value of the sessionId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setSessionId(String value) {
            this.sessionId = value;
        }

        /**
         * Gets the value of the windowId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getWindowId() {
            return windowId;
        }

        /**
         * Sets the value of the windowId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setWindowId(String value) {
            this.windowId = value;
        }

        /**
         * Gets the value of the windowOpportunity property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getWindowOpportunity() {
            return windowOpportunity;
        }

        /**
         * Sets the value of the windowOpportunity property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setWindowOpportunity(String value) {
            this.windowOpportunity = value;
        }

        /**
         * Gets the value of the completeStatus property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getCompleteStatus() {
            return completeStatus;
        }

        /**
         * Sets the value of the completeStatus property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setCompleteStatus(String value) {
            this.completeStatus = value;
        }

        /**
         * Gets the value of the administrationCondition property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getAdministrationCondition() {
            return administrationCondition;
        }

        /**
         * Sets the value of the administrationCondition property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setAdministrationCondition(String value) {
            this.administrationCondition = value;
        }

        /**
         * Gets the value of the dateForceCompleted property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getDateForceCompleted() {
            return dateForceCompleted;
        }

        /**
         * Sets the value of the dateForceCompleted property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setDateForceCompleted(String value) {
            this.dateForceCompleted = value;
        }

        /**
         * Gets the value of the qaLevel property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getQaLevel() {
            return qaLevel;
        }

        /**
         * Sets the value of the qaLevel property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setQaLevel(String value) {
            this.qaLevel = value;
        }

        /**
         * Gets the value of the assessmentParticipantSessionPlatformUserAgent property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getAssessmentParticipantSessionPlatformUserAgent() {
            return assessmentParticipantSessionPlatformUserAgent;
        }

        /**
         * Sets the value of the assessmentParticipantSessionPlatformUserAgent property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setAssessmentParticipantSessionPlatformUserAgent(String value) {
            this.assessmentParticipantSessionPlatformUserAgent = value;
        }

        /**
         * Gets the value of the effectiveDate property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getEffectiveDate() {
            return effectiveDate;
        }

        /**
         * Sets the value of the effectiveDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setEffectiveDate(String value) {
            this.effectiveDate = value;
        }

        /**
         * Gets the value of the reportingVersion property.
         * 
         * @return
         *     possible object is
         *     {@link Long }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public Long getReportingVersion() {
            return reportingVersion;
        }

        /**
         * Sets the value of the reportingVersion property.
         * 
         * @param value
         *     allowed object is
         *     {@link Long }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setReportingVersion(Long value) {
            this.reportingVersion = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="segment" use="required">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedInt">
         *             &lt;minInclusive value="0"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public static class Accommodation {

            @XmlAttribute(name = "type", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String type;
            @XmlAttribute(name = "value", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String value;
            @XmlAttribute(name = "code", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String code;
            @XmlAttribute(name = "segment", required = true)
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected long segment;

            /**
             * Gets the value of the type property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getType() {
                return type;
            }

            /**
             * Sets the value of the type property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setType(String value) {
                this.type = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setValue(String value) {
                this.value = value;
            }

            /**
             * Gets the value of the code property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getCode() {
                return code;
            }

            /**
             * Sets the value of the code property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setCode(String value) {
                this.code = value;
            }

            /**
             * Gets the value of the segment property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public long getSegment() {
                return segment;
            }

            /**
             * Sets the value of the segment property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setSegment(long value) {
                this.segment = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="context" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public static class GenericVariable {

            @XmlAttribute(name = "context", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String context;
            @XmlAttribute(name = "name", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String name;
            @XmlAttribute(name = "value", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String value;

            /**
             * Gets the value of the context property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getContext() {
                return context;
            }

            /**
             * Sets the value of the context property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setContext(String value) {
                this.context = value;
            }

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setValue(String value) {
                this.value = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="Response" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="date" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
         *                 &lt;attribute name="type">
         *                   &lt;simpleType>
         *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
         *                       &lt;enumeration value="value"/>
         *                       &lt;enumeration value="reference"/>
         *                       &lt;enumeration value=""/>
         *                     &lt;/restriction>
         *                   &lt;/simpleType>
         *                 &lt;/attribute>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="ScoreInfo" type="{}ScoreInfoType" minOccurs="0"/>
         *       &lt;/sequence>
         *       &lt;attribute name="position" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
         *       &lt;attribute name="segmentId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="bankKey" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
         *       &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
         *       &lt;attribute name="operational" use="required" type="{}Bit" />
         *       &lt;attribute name="isSelected" use="required" type="{}Bit" />
         *       &lt;attribute name="format" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="score" use="required" type="{}UFloatAllowNegativeOne" />
         *       &lt;attribute name="scoreStatus">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
         *             &lt;enumeration value="NOTSCORED"/>
         *             &lt;enumeration value="SCORED"/>
         *             &lt;enumeration value="WAITINGFORMACHINESCORE"/>
         *             &lt;enumeration value="SCORINGERROR"/>
         *             &lt;enumeration value="APPEALED"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="adminDate" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
         *       &lt;attribute name="numberVisits" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
         *       &lt;attribute name="mimeType" use="required">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
         *             &lt;enumeration value="text/plain"/>
         *             &lt;enumeration value="text/xml"/>
         *             &lt;enumeration value="text/html"/>
         *             &lt;enumeration value="audio/ogg"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="clientId" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="strand" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="contentLevel" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="pageNumber" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
         *       &lt;attribute name="pageVisits" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
         *       &lt;attribute name="pageTime" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
         *       &lt;attribute name="dropped" use="required" type="{}Bit" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "response",
            "scoreInfo"
        })
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public static class Item {

            @XmlElement(name = "Response")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected TDSReport.Opportunity.Item.Response response;
            @XmlElement(name = "ScoreInfo")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected ScoreInfoType scoreInfo;
            @XmlAttribute(name = "position", required = true)
            @XmlSchemaType(name = "unsignedInt")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected long position;
            @XmlAttribute(name = "segmentId", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String segmentId;
            @XmlAttribute(name = "bankKey", required = true)
            @XmlSchemaType(name = "unsignedInt")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected long bankKey;
            @XmlAttribute(name = "key", required = true)
            @XmlSchemaType(name = "unsignedInt")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected long key;
            @XmlAttribute(name = "operational", required = true)
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected short operational;
            @XmlAttribute(name = "isSelected", required = true)
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected short isSelected;
            @XmlAttribute(name = "format", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String format;
            @XmlAttribute(name = "score", required = true)
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String score;
            @XmlAttribute(name = "scoreStatus")
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String scoreStatus;
            @XmlAttribute(name = "adminDate", required = true)
            @XmlSchemaType(name = "dateTime")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected XMLGregorianCalendar adminDate;
            @XmlAttribute(name = "numberVisits", required = true)
            @XmlSchemaType(name = "unsignedInt")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected long numberVisits;
            @XmlAttribute(name = "mimeType", required = true)
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String mimeType;
            @XmlAttribute(name = "clientId")
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String clientId;
            @XmlAttribute(name = "strand", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String strand;
            @XmlAttribute(name = "contentLevel", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String contentLevel;
            @XmlAttribute(name = "pageNumber", required = true)
            @XmlSchemaType(name = "unsignedInt")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected long pageNumber;
            @XmlAttribute(name = "pageVisits", required = true)
            @XmlSchemaType(name = "unsignedInt")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected long pageVisits;
            @XmlAttribute(name = "pageTime", required = true)
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected int pageTime;
            @XmlAttribute(name = "dropped", required = true)
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected short dropped;

            /**
             * Gets the value of the response property.
             * 
             * @return
             *     possible object is
             *     {@link TDSReport.Opportunity.Item.Response }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public TDSReport.Opportunity.Item.Response getResponse() {
                return response;
            }

            /**
             * Sets the value of the response property.
             * 
             * @param value
             *     allowed object is
             *     {@link TDSReport.Opportunity.Item.Response }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setResponse(TDSReport.Opportunity.Item.Response value) {
                this.response = value;
            }

            /**
             * Gets the value of the scoreInfo property.
             * 
             * @return
             *     possible object is
             *     {@link ScoreInfoType }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public ScoreInfoType getScoreInfo() {
                return scoreInfo;
            }

            /**
             * Sets the value of the scoreInfo property.
             * 
             * @param value
             *     allowed object is
             *     {@link ScoreInfoType }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setScoreInfo(ScoreInfoType value) {
                this.scoreInfo = value;
            }

            /**
             * Gets the value of the position property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public long getPosition() {
                return position;
            }

            /**
             * Sets the value of the position property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setPosition(long value) {
                this.position = value;
            }

            /**
             * Gets the value of the segmentId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getSegmentId() {
                return segmentId;
            }

            /**
             * Sets the value of the segmentId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setSegmentId(String value) {
                this.segmentId = value;
            }

            /**
             * Gets the value of the bankKey property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public long getBankKey() {
                return bankKey;
            }

            /**
             * Sets the value of the bankKey property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setBankKey(long value) {
                this.bankKey = value;
            }

            /**
             * Gets the value of the key property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public long getKey() {
                return key;
            }

            /**
             * Sets the value of the key property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setKey(long value) {
                this.key = value;
            }

            /**
             * Gets the value of the operational property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public short getOperational() {
                return operational;
            }

            /**
             * Sets the value of the operational property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setOperational(short value) {
                this.operational = value;
            }

            /**
             * Gets the value of the isSelected property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public short getIsSelected() {
                return isSelected;
            }

            /**
             * Sets the value of the isSelected property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setIsSelected(short value) {
                this.isSelected = value;
            }

            /**
             * Gets the value of the format property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getFormat() {
                return format;
            }

            /**
             * Sets the value of the format property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setFormat(String value) {
                this.format = value;
            }

            /**
             * Gets the value of the score property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getScore() {
                return score;
            }

            /**
             * Sets the value of the score property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setScore(String value) {
                this.score = value;
            }

            /**
             * Gets the value of the scoreStatus property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getScoreStatus() {
                return scoreStatus;
            }

            /**
             * Sets the value of the scoreStatus property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setScoreStatus(String value) {
                this.scoreStatus = value;
            }

            /**
             * Gets the value of the adminDate property.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public XMLGregorianCalendar getAdminDate() {
                return adminDate;
            }

            /**
             * Sets the value of the adminDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setAdminDate(XMLGregorianCalendar value) {
                this.adminDate = value;
            }

            /**
             * Gets the value of the numberVisits property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public long getNumberVisits() {
                return numberVisits;
            }

            /**
             * Sets the value of the numberVisits property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setNumberVisits(long value) {
                this.numberVisits = value;
            }

            /**
             * Gets the value of the mimeType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getMimeType() {
                return mimeType;
            }

            /**
             * Sets the value of the mimeType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setMimeType(String value) {
                this.mimeType = value;
            }

            /**
             * Gets the value of the clientId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getClientId() {
                return clientId;
            }

            /**
             * Sets the value of the clientId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setClientId(String value) {
                this.clientId = value;
            }

            /**
             * Gets the value of the strand property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getStrand() {
                return strand;
            }

            /**
             * Sets the value of the strand property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setStrand(String value) {
                this.strand = value;
            }

            /**
             * Gets the value of the contentLevel property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getContentLevel() {
                return contentLevel;
            }

            /**
             * Sets the value of the contentLevel property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setContentLevel(String value) {
                this.contentLevel = value;
            }

            /**
             * Gets the value of the pageNumber property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public long getPageNumber() {
                return pageNumber;
            }

            /**
             * Sets the value of the pageNumber property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setPageNumber(long value) {
                this.pageNumber = value;
            }

            /**
             * Gets the value of the pageVisits property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public long getPageVisits() {
                return pageVisits;
            }

            /**
             * Sets the value of the pageVisits property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setPageVisits(long value) {
                this.pageVisits = value;
            }

            /**
             * Gets the value of the pageTime property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public int getPageTime() {
                return pageTime;
            }

            /**
             * Sets the value of the pageTime property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setPageTime(int value) {
                this.pageTime = value;
            }

            /**
             * Gets the value of the dropped property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public short getDropped() {
                return dropped;
            }

            /**
             * Sets the value of the dropped property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setDropped(short value) {
                this.dropped = value;
            }


            /**
             * <p>Java class for anonymous complex type.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="date" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
             *       &lt;attribute name="type">
             *         &lt;simpleType>
             *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
             *             &lt;enumeration value="value"/>
             *             &lt;enumeration value="reference"/>
             *             &lt;enumeration value=""/>
             *           &lt;/restriction>
             *         &lt;/simpleType>
             *       &lt;/attribute>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "content"
            })
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public static class Response {

                @XmlValue
                @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
                protected String content;
                @XmlAttribute(name = "date")
                @XmlSchemaType(name = "dateTime")
                @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
                protected XMLGregorianCalendar date;
                @XmlAttribute(name = "type")
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
                protected String type;

                /**
                 * Gets the value of the content property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
                public String getContent() {
                    return content;
                }

                /**
                 * Sets the value of the content property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
                public void setContent(String value) {
                    this.content = value;
                }

                /**
                 * Gets the value of the date property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
                public XMLGregorianCalendar getDate() {
                    return date;
                }

                /**
                 * Sets the value of the date property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
                public void setDate(XMLGregorianCalendar value) {
                    this.date = value;
                }

                /**
                 * Gets the value of the type property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
                public String getType() {
                    return type;
                }

                /**
                 * Sets the value of the type property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
                public void setType(String value) {
                    this.type = value;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="measureOf" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="measureLabel" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="value" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="standardError" type="{}NullableFloat" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public static class Score {

            @XmlAttribute(name = "measureOf", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String measureOf;
            @XmlAttribute(name = "measureLabel", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String measureLabel;
            @XmlAttribute(name = "value", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String value;
            @XmlAttribute(name = "standardError")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String standardError;

            /**
             * Gets the value of the measureOf property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getMeasureOf() {
                return measureOf;
            }

            /**
             * Sets the value of the measureOf property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setMeasureOf(String value) {
                this.measureOf = value;
            }

            /**
             * Gets the value of the measureLabel property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getMeasureLabel() {
                return measureLabel;
            }

            /**
             * Sets the value of the measureLabel property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setMeasureLabel(String value) {
                this.measureLabel = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setValue(String value) {
                this.value = value;
            }

            /**
             * Gets the value of the standardError property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getStandardError() {
                return standardError;
            }

            /**
             * Sets the value of the standardError property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setStandardError(String value) {
                this.standardError = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="position" use="required">
         *         &lt;simpleType>
         *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
         *             &lt;minInclusive value="1"/>
         *           &lt;/restriction>
         *         &lt;/simpleType>
         *       &lt;/attribute>
         *       &lt;attribute name="formId" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="formKey" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="algorithm" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="algorithmVersion" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public static class Segment {

            @XmlAttribute(name = "id", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String id;
            @XmlAttribute(name = "position", required = true)
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected short position;
            @XmlAttribute(name = "formId")
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String formId;
            @XmlAttribute(name = "formKey")
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String formKey;
            @XmlAttribute(name = "algorithm", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String algorithm;
            @XmlAttribute(name = "algorithmVersion")
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String algorithmVersion;

            /**
             * Gets the value of the id property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getId() {
                return id;
            }

            /**
             * Sets the value of the id property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setId(String value) {
                this.id = value;
            }

            /**
             * Gets the value of the position property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public short getPosition() {
                return position;
            }

            /**
             * Sets the value of the position property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setPosition(short value) {
                this.position = value;
            }

            /**
             * Gets the value of the formId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getFormId() {
                return formId;
            }

            /**
             * Sets the value of the formId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setFormId(String value) {
                this.formId = value;
            }

            /**
             * Gets the value of the formKey property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getFormKey() {
                return formKey;
            }

            /**
             * Sets the value of the formKey property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setFormKey(String value) {
                this.formKey = value;
            }

            /**
             * Gets the value of the algorithm property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getAlgorithm() {
                return algorithm;
            }

            /**
             * Sets the value of the algorithm property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setAlgorithm(String value) {
                this.algorithm = value;
            }

            /**
             * Gets the value of the algorithmVersion property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getAlgorithmVersion() {
                return algorithmVersion;
            }

            /**
             * Sets the value of the algorithmVersion property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setAlgorithmVersion(String value) {
                this.algorithmVersion = value;
            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="subject" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="testId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="bankKey" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *       &lt;attribute name="contract" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="mode" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
     *             &lt;enumeration value="online"/>
     *             &lt;enumeration value="paper"/>
     *             &lt;enumeration value="scanned"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="grade" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="handScoreProject" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *       &lt;attribute name="assessmentType" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="academicYear" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *       &lt;attribute name="assessmentVersion" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public static class Test {

        @XmlAttribute(name = "name", required = true)
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String name;
        @XmlAttribute(name = "subject", required = true)
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String subject;
        @XmlAttribute(name = "testId", required = true)
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String testId;
        @XmlAttribute(name = "bankKey")
        @XmlSchemaType(name = "unsignedInt")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected Long bankKey;
        @XmlAttribute(name = "contract", required = true)
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String contract;
        @XmlAttribute(name = "mode", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String mode;
        @XmlAttribute(name = "grade", required = true)
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String grade;
        @XmlAttribute(name = "handScoreProject")
        @XmlSchemaType(name = "unsignedInt")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected Long handScoreProject;
        @XmlAttribute(name = "assessmentType")
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String assessmentType;
        @XmlAttribute(name = "academicYear")
        @XmlSchemaType(name = "unsignedInt")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected Long academicYear;
        @XmlAttribute(name = "assessmentVersion")
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String assessmentVersion;

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the subject property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getSubject() {
            return subject;
        }

        /**
         * Sets the value of the subject property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setSubject(String value) {
            this.subject = value;
        }

        /**
         * Gets the value of the testId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getTestId() {
            return testId;
        }

        /**
         * Sets the value of the testId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setTestId(String value) {
            this.testId = value;
        }

        /**
         * Gets the value of the bankKey property.
         * 
         * @return
         *     possible object is
         *     {@link Long }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public Long getBankKey() {
            return bankKey;
        }

        /**
         * Sets the value of the bankKey property.
         * 
         * @param value
         *     allowed object is
         *     {@link Long }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setBankKey(Long value) {
            this.bankKey = value;
        }

        /**
         * Gets the value of the contract property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getContract() {
            return contract;
        }

        /**
         * Sets the value of the contract property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setContract(String value) {
            this.contract = value;
        }

        /**
         * Gets the value of the mode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getMode() {
            return mode;
        }

        /**
         * Sets the value of the mode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setMode(String value) {
            this.mode = value;
        }

        /**
         * Gets the value of the grade property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getGrade() {
            return grade;
        }

        /**
         * Sets the value of the grade property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setGrade(String value) {
            this.grade = value;
        }

        /**
         * Gets the value of the handScoreProject property.
         * 
         * @return
         *     possible object is
         *     {@link Long }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public Long getHandScoreProject() {
            return handScoreProject;
        }

        /**
         * Sets the value of the handScoreProject property.
         * 
         * @param value
         *     allowed object is
         *     {@link Long }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setHandScoreProject(Long value) {
            this.handScoreProject = value;
        }

        /**
         * Gets the value of the assessmentType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getAssessmentType() {
            return assessmentType;
        }

        /**
         * Sets the value of the assessmentType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setAssessmentType(String value) {
            this.assessmentType = value;
        }

        /**
         * Gets the value of the academicYear property.
         * 
         * @return
         *     possible object is
         *     {@link Long }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public Long getAcademicYear() {
            return academicYear;
        }

        /**
         * Sets the value of the academicYear property.
         * 
         * @param value
         *     allowed object is
         *     {@link Long }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setAcademicYear(Long value) {
            this.academicYear = value;
        }

        /**
         * Gets the value of the assessmentVersion property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getAssessmentVersion() {
            return assessmentVersion;
        }

        /**
         * Sets the value of the assessmentVersion property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setAssessmentVersion(String value) {
            this.assessmentVersion = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="ToolPage" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="page" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *                 &lt;attribute name="groupId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                 &lt;attribute name="count" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "toolPage"
    })
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public static class ToolUsage {

        @XmlElement(name = "ToolPage", required = true)
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected List<TDSReport.ToolUsage.ToolPage> toolPage;
        @XmlAttribute(name = "type", required = true)
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String type;
        @XmlAttribute(name = "code", required = true)
        @XmlSchemaType(name = "anySimpleType")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected String code;

        /**
         * Gets the value of the toolPage property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the toolPage property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getToolPage().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TDSReport.ToolUsage.ToolPage }
         * 
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public List<TDSReport.ToolUsage.ToolPage> getToolPage() {
            if (toolPage == null) {
                toolPage = new ArrayList<TDSReport.ToolUsage.ToolPage>();
            }
            return this.toolPage;
        }

        /**
         * Gets the value of the type property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setType(String value) {
            this.type = value;
        }

        /**
         * Gets the value of the code property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public String getCode() {
            return code;
        }

        /**
         * Sets the value of the code property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setCode(String value) {
            this.code = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;attribute name="page" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
         *       &lt;attribute name="groupId" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *       &lt;attribute name="count" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public static class ToolPage {

            @XmlAttribute(name = "page", required = true)
            @XmlSchemaType(name = "unsignedInt")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected long page;
            @XmlAttribute(name = "groupId", required = true)
            @XmlSchemaType(name = "anySimpleType")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected String groupId;
            @XmlAttribute(name = "count", required = true)
            @XmlSchemaType(name = "unsignedInt")
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            protected long count;

            /**
             * Gets the value of the page property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public long getPage() {
                return page;
            }

            /**
             * Sets the value of the page property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setPage(long value) {
                this.page = value;
            }

            /**
             * Gets the value of the groupId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public String getGroupId() {
                return groupId;
            }

            /**
             * Sets the value of the groupId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setGroupId(String value) {
                this.groupId = value;
            }

            /**
             * Gets the value of the count property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public long getCount() {
                return count;
            }

            /**
             * Sets the value of the count property.
             * 
             */
            @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
            public void setCount(long value) {
                this.count = value;
            }

        }

    }

}

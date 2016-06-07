
package org.cresst.sb.irp.domain.tdsreport;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for ScoreInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ScoreInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ScoreRationale" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="SubScoreList" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ScoreInfo" type="{}ScoreInfoType" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="scorePoint" type="{}UFloatAllowNegativeOne" />
 *       &lt;attribute name="maxScore" type="{}UFloatAllowNegativeOne" />
 *       &lt;attribute name="scoreDimension" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="scoreStatus">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="Scored"/>
 *             &lt;enumeration value="NotScored"/>
 *             &lt;enumeration value="WaitingForMachineScore"/>
 *             &lt;enumeration value="ScoringError"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="confLevel" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScoreInfoType", propOrder = {
    "scoreRationale",
    "subScoreList"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
public class ScoreInfoType {

    @XmlElement(name = "ScoreRationale")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    protected ScoreInfoType.ScoreRationale scoreRationale;
    @XmlElement(name = "SubScoreList")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    protected ScoreInfoType.SubScoreList subScoreList;
    @XmlAttribute(name = "scorePoint")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    protected String scorePoint;
    @XmlAttribute(name = "maxScore")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    protected String maxScore;
    @XmlAttribute(name = "scoreDimension")
    @XmlSchemaType(name = "anySimpleType")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    protected String scoreDimension;
    @XmlAttribute(name = "scoreStatus")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    protected String scoreStatus;
    @XmlAttribute(name = "confLevel")
    @XmlSchemaType(name = "anySimpleType")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    protected String confLevel;

    /**
     * Gets the value of the scoreRationale property.
     * 
     * @return
     *     possible object is
     *     {@link ScoreInfoType.ScoreRationale }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public ScoreInfoType.ScoreRationale getScoreRationale() {
        return scoreRationale;
    }

    /**
     * Sets the value of the scoreRationale property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScoreInfoType.ScoreRationale }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public void setScoreRationale(ScoreInfoType.ScoreRationale value) {
        this.scoreRationale = value;
    }

    /**
     * Gets the value of the subScoreList property.
     * 
     * @return
     *     possible object is
     *     {@link ScoreInfoType.SubScoreList }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public ScoreInfoType.SubScoreList getSubScoreList() {
        return subScoreList;
    }

    /**
     * Sets the value of the subScoreList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScoreInfoType.SubScoreList }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public void setSubScoreList(ScoreInfoType.SubScoreList value) {
        this.subScoreList = value;
    }

    /**
     * Gets the value of the scorePoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public String getScorePoint() {
        return scorePoint;
    }

    /**
     * Sets the value of the scorePoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public void setScorePoint(String value) {
        this.scorePoint = value;
    }

    /**
     * Gets the value of the maxScore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public String getMaxScore() {
        return maxScore;
    }

    /**
     * Sets the value of the maxScore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public void setMaxScore(String value) {
        this.maxScore = value;
    }

    /**
     * Gets the value of the scoreDimension property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public String getScoreDimension() {
        return scoreDimension;
    }

    /**
     * Sets the value of the scoreDimension property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public void setScoreDimension(String value) {
        this.scoreDimension = value;
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
     * Gets the value of the confLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public String getConfLevel() {
        return confLevel;
    }

    /**
     * Sets the value of the confLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public void setConfLevel(String value) {
        this.confLevel = value;
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
     *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
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
        "message"
    })
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public static class ScoreRationale {

        @XmlElement(name = "Message")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected Object message;

        /**
         * Gets the value of the message property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public Object getMessage() {
            return message;
        }

        /**
         * Sets the value of the message property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public void setMessage(Object value) {
            this.message = value;
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
     *         &lt;element name="ScoreInfo" type="{}ScoreInfoType" maxOccurs="unbounded" minOccurs="0"/>
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
        "scoreInfo"
    })
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
    public static class SubScoreList {

        @XmlElement(name = "ScoreInfo")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        protected List<ScoreInfoType> scoreInfo;

        /**
         * Gets the value of the scoreInfo property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the scoreInfo property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getScoreInfo().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ScoreInfoType }
         * 
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
        public List<ScoreInfoType> getScoreInfo() {
            if (scoreInfo == null) {
                scoreInfo = new ArrayList<ScoreInfoType>();
            }
            return this.scoreInfo;
        }

    }

}

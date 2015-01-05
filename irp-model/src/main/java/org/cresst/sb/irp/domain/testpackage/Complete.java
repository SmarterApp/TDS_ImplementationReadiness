
package org.cresst.sb.irp.domain.testpackage;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "testblueprint",
    "poolproperty",
    "itempool",
    "testform",
    "adminsegment",
    "performancelevels",
    "scoringrules",
    "reportingmeasures",
    "comment"
})
@XmlRootElement(name = "complete")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
public class Complete {

    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    protected Testblueprint testblueprint;
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    protected List<Poolproperty> poolproperty;
    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    protected Itempool itempool;
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    protected List<Testform> testform;
    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    protected List<Adminsegment> adminsegment;
    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    protected Performancelevels performancelevels;
    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    protected Scoringrules scoringrules;
    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    protected Reportingmeasures reportingmeasures;
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    protected String comment;

    /**
     * Gets the value of the testblueprint property.
     * 
     * @return
     *     possible object is
     *     {@link Testblueprint }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public Testblueprint getTestblueprint() {
        return testblueprint;
    }

    /**
     * Sets the value of the testblueprint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Testblueprint }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public void setTestblueprint(Testblueprint value) {
        this.testblueprint = value;
    }

    /**
     * Gets the value of the poolproperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the poolproperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPoolproperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Poolproperty }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public List<Poolproperty> getPoolproperty() {
        if (poolproperty == null) {
            poolproperty = new ArrayList<Poolproperty>();
        }
        return this.poolproperty;
    }

    /**
     * Gets the value of the itempool property.
     * 
     * @return
     *     possible object is
     *     {@link Itempool }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public Itempool getItempool() {
        return itempool;
    }

    /**
     * Sets the value of the itempool property.
     * 
     * @param value
     *     allowed object is
     *     {@link Itempool }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public void setItempool(Itempool value) {
        this.itempool = value;
    }

    /**
     * Gets the value of the testform property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the testform property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTestform().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Testform }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public List<Testform> getTestform() {
        if (testform == null) {
            testform = new ArrayList<Testform>();
        }
        return this.testform;
    }

    /**
     * Gets the value of the adminsegment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the adminsegment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdminsegment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Adminsegment }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public List<Adminsegment> getAdminsegment() {
        if (adminsegment == null) {
            adminsegment = new ArrayList<Adminsegment>();
        }
        return this.adminsegment;
    }

    /**
     * Gets the value of the performancelevels property.
     * 
     * @return
     *     possible object is
     *     {@link Performancelevels }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public Performancelevels getPerformancelevels() {
        return performancelevels;
    }

    /**
     * Sets the value of the performancelevels property.
     * 
     * @param value
     *     allowed object is
     *     {@link Performancelevels }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public void setPerformancelevels(Performancelevels value) {
        this.performancelevels = value;
    }

    /**
     * Gets the value of the scoringrules property.
     * 
     * @return
     *     possible object is
     *     {@link Scoringrules }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public Scoringrules getScoringrules() {
        return scoringrules;
    }

    /**
     * Sets the value of the scoringrules property.
     * 
     * @param value
     *     allowed object is
     *     {@link Scoringrules }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public void setScoringrules(Scoringrules value) {
        this.scoringrules = value;
    }

    /**
     * Gets the value of the reportingmeasures property.
     * 
     * @return
     *     possible object is
     *     {@link Reportingmeasures }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public Reportingmeasures getReportingmeasures() {
        return reportingmeasures;
    }

    /**
     * Sets the value of the reportingmeasures property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reportingmeasures }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public void setReportingmeasures(Reportingmeasures value) {
        this.reportingmeasures = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public void setComment(String value) {
        this.comment = value;
    }

}

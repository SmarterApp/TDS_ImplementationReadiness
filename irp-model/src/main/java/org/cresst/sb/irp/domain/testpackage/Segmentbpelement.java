//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.19 at 04:40:30 PM PST 
//


package org.cresst.sb.irp.domain.testpackage;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{}property" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="bpelementid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="minopitems" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="maxopitems" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="minftitems" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="maxftitems" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "property"
})
@XmlRootElement(name = "segmentbpelement")
public class Segmentbpelement {

    protected List<Property> property;
    @XmlAttribute(name = "bpelementid")
    protected String bpelementid;
    @XmlAttribute(name = "minopitems")
    protected String minopitems;
    @XmlAttribute(name = "maxopitems")
    protected String maxopitems;
    @XmlAttribute(name = "minftitems")
    protected String minftitems;
    @XmlAttribute(name = "maxftitems")
    protected String maxftitems;

    /**
     * Gets the value of the property property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the property property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Property }
     * 
     * 
     */
    public List<Property> getProperty() {
        if (property == null) {
            property = new ArrayList<Property>();
        }
        return this.property;
    }

    /**
     * Gets the value of the bpelementid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBpelementid() {
        return bpelementid;
    }

    /**
     * Sets the value of the bpelementid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBpelementid(String value) {
        this.bpelementid = value;
    }

    /**
     * Gets the value of the minopitems property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinopitems() {
        return minopitems;
    }

    /**
     * Sets the value of the minopitems property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinopitems(String value) {
        this.minopitems = value;
    }

    /**
     * Gets the value of the maxopitems property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxopitems() {
        return maxopitems;
    }

    /**
     * Sets the value of the maxopitems property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxopitems(String value) {
        this.maxopitems = value;
    }

    /**
     * Gets the value of the minftitems property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMinftitems() {
        return minftitems;
    }

    /**
     * Sets the value of the minftitems property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMinftitems(String value) {
        this.minftitems = value;
    }

    /**
     * Gets the value of the maxftitems property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxftitems() {
        return maxftitems;
    }

    /**
     * Sets the value of the maxftitems property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxftitems(String value) {
        this.maxftitems = value;
    }

}

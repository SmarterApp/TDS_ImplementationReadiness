//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.25 at 01:59:39 PM PDT 
//


package org.cresst.sb.irp.domain.testpackage;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element ref="{}identifier"/>
 *         &lt;element ref="{}property" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="elementtype" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="parentid" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="minopitems" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="maxopitems" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="minftitems" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="maxftitems" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="opitemcount" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ftitemcount" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "identifier",
    "property"
})
@XmlRootElement(name = "bpelement")
public class Bpelement {

    @XmlElement(required = true)
    protected Identifier identifier;
    protected List<Property> property;
    @XmlAttribute(name = "elementtype", required = true)
    protected String elementtype;
    @XmlAttribute(name = "parentid")
    protected String parentid;
    @XmlAttribute(name = "minopitems")
    protected String minopitems;
    @XmlAttribute(name = "maxopitems")
    protected String maxopitems;
    @XmlAttribute(name = "minftitems")
    protected String minftitems;
    @XmlAttribute(name = "maxftitems")
    protected String maxftitems;
    @XmlAttribute(name = "opitemcount")
    protected String opitemcount;
    @XmlAttribute(name = "ftitemcount")
    protected String ftitemcount;

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link Identifier }
     *     
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link Identifier }
     *     
     */
    public void setIdentifier(Identifier value) {
        this.identifier = value;
    }

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
     * Gets the value of the elementtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElementtype() {
        return elementtype;
    }

    /**
     * Sets the value of the elementtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElementtype(String value) {
        this.elementtype = value;
    }

    /**
     * Gets the value of the parentid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentid() {
        return parentid;
    }

    /**
     * Sets the value of the parentid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentid(String value) {
        this.parentid = value;
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

    /**
     * Gets the value of the opitemcount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpitemcount() {
        return opitemcount;
    }

    /**
     * Sets the value of the opitemcount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpitemcount(String value) {
        this.opitemcount = value;
    }

    /**
     * Gets the value of the ftitemcount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFtitemcount() {
        return ftitemcount;
    }

    /**
     * Sets the value of the ftitemcount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFtitemcount(String value) {
        this.ftitemcount = value;
    }

}

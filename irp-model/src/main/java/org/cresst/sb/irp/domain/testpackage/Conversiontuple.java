//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.25 at 01:59:39 PM PDT 
//


package org.cresst.sb.irp.domain.testpackage;

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
 *       &lt;attribute name="invalue" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="outvalue" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "conversiontuple")
public class Conversiontuple {

    @XmlAttribute(name = "invalue", required = true)
    protected String invalue;
    @XmlAttribute(name = "outvalue", required = true)
    protected String outvalue;

    /**
     * Gets the value of the invalue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvalue() {
        return invalue;
    }

    /**
     * Sets the value of the invalue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvalue(String value) {
        this.invalue = value;
    }

    /**
     * Gets the value of the outvalue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutvalue() {
        return outvalue;
    }

    /**
     * Sets the value of the outvalue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutvalue(String value) {
        this.outvalue = value;
    }

}

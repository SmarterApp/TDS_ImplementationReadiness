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
 *         &lt;element ref="{}computationrule" maxOccurs="unbounded"/>
 *         &lt;element ref="{}conversiontable" maxOccurs="unbounded" minOccurs="0"/>
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
    "computationrule",
    "conversiontable"
})
@XmlRootElement(name = "scoringrules")
public class Scoringrules {

    @XmlElement(required = true)
    protected List<Computationrule> computationrule;
    protected List<Conversiontable> conversiontable;

    /**
     * Gets the value of the computationrule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the computationrule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComputationrule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Computationrule }
     * 
     * 
     */
    public List<Computationrule> getComputationrule() {
        if (computationrule == null) {
            computationrule = new ArrayList<Computationrule>();
        }
        return this.computationrule;
    }

    /**
     * Gets the value of the conversiontable property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the conversiontable property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConversiontable().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Conversiontable }
     * 
     * 
     */
    public List<Conversiontable> getConversiontable() {
        if (conversiontable == null) {
            conversiontable = new ArrayList<Conversiontable>();
        }
        return this.conversiontable;
    }

}

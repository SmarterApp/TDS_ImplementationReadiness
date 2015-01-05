
package org.cresst.sb.irp.domain.testpackage;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "conversiontuple")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
public class Conversiontuple {

    @XmlAttribute(name = "invalue", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    protected String invalue;
    @XmlAttribute(name = "outvalue", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    protected String outvalue;

    /**
     * Gets the value of the invalue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
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
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
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
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
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
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public void setOutvalue(String value) {
        this.outvalue = value;
    }

}


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
    "computationrule",
    "conversiontable"
})
@XmlRootElement(name = "scoringrules")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
public class Scoringrules {

    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    protected List<Computationrule> computationrule;
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
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
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
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
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public List<Conversiontable> getConversiontable() {
        if (conversiontable == null) {
            conversiontable = new ArrayList<Conversiontable>();
        }
        return this.conversiontable;
    }

}

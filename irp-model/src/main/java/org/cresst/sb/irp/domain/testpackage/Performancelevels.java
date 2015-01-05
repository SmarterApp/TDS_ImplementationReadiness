
package org.cresst.sb.irp.domain.testpackage;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "performancelevel"
})
@XmlRootElement(name = "performancelevels")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
public class Performancelevels {

    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    protected List<Performancelevel> performancelevel;

    /**
     * Gets the value of the performancelevel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the performancelevel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPerformancelevel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Performancelevel }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public List<Performancelevel> getPerformancelevel() {
        if (performancelevel == null) {
            performancelevel = new ArrayList<Performancelevel>();
        }
        return this.performancelevel;
    }

}

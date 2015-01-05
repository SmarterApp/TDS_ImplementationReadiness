
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
    "segmentbpelement"
})
@XmlRootElement(name = "segmentblueprint")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
public class Segmentblueprint {

    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    protected List<Segmentbpelement> segmentbpelement;

    /**
     * Gets the value of the segmentbpelement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the segmentbpelement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSegmentbpelement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Segmentbpelement }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-01-02T07:35:40-08:00", comments = "JAXB RI v2.2.4-2")
    public List<Segmentbpelement> getSegmentbpelement() {
        if (segmentbpelement == null) {
            segmentbpelement = new ArrayList<Segmentbpelement>();
        }
        return this.segmentbpelement;
    }

}

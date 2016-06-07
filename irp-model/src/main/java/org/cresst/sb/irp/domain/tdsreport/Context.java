
package org.cresst.sb.irp.domain.tdsreport;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Context.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Context">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="INITIAL"/>
 *     &lt;enumeration value="FINAL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Context")
@XmlEnum
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2016-06-07T04:53:02+09:00", comments = "JAXB RI v2.2.4-2")
public enum Context {

    INITIAL,
    FINAL;

    public String value() {
        return name();
    }

    public static Context fromValue(String v) {
        return valueOf(v);
    }

}

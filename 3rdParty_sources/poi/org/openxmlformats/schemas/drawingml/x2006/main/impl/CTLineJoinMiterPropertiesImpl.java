/*
 * XML Type:  CT_LineJoinMiterProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinMiterProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_LineJoinMiterProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTLineJoinMiterPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTLineJoinMiterProperties {
    private static final long serialVersionUID = 1L;

    public CTLineJoinMiterPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "lim"),
    };


    /**
     * Gets the "lim" attribute
     */
    @Override
    public java.lang.Object getLim() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "lim" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage xgetLim() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "lim" attribute
     */
    @Override
    public boolean isSetLim() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "lim" attribute
     */
    @Override
    public void setLim(java.lang.Object lim) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setObjectValue(lim);
        }
    }

    /**
     * Sets (as xml) the "lim" attribute
     */
    @Override
    public void xsetLim(org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage lim) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(lim);
        }
    }

    /**
     * Unsets the "lim" attribute
     */
    @Override
    public void unsetLim() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }
}

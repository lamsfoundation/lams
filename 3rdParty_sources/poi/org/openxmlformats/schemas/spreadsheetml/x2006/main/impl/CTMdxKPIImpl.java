/*
 * XML Type:  CT_MdxKPI
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxKPI
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_MdxKPI(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTMdxKPIImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxKPI {
    private static final long serialVersionUID = 1L;

    public CTMdxKPIImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "n"),
        new QName("", "np"),
        new QName("", "p"),
    };


    /**
     * Gets the "n" attribute
     */
    @Override
    public long getN() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "n" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetN() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Sets the "n" attribute
     */
    @Override
    public void setN(long n) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setLongValue(n);
        }
    }

    /**
     * Sets (as xml) the "n" attribute
     */
    @Override
    public void xsetN(org.apache.xmlbeans.XmlUnsignedInt n) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(n);
        }
    }

    /**
     * Gets the "np" attribute
     */
    @Override
    public long getNp() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "np" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetNp() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "np" attribute
     */
    @Override
    public void setNp(long np) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setLongValue(np);
        }
    }

    /**
     * Sets (as xml) the "np" attribute
     */
    @Override
    public void xsetNp(org.apache.xmlbeans.XmlUnsignedInt np) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(np);
        }
    }

    /**
     * Gets the "p" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxKPIProperty.Enum getP() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxKPIProperty.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "p" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxKPIProperty xgetP() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxKPIProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxKPIProperty)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "p" attribute
     */
    @Override
    public void setP(org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxKPIProperty.Enum p) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(p);
        }
    }

    /**
     * Sets (as xml) the "p" attribute
     */
    @Override
    public void xsetP(org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxKPIProperty p) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxKPIProperty target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxKPIProperty)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxKPIProperty)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(p);
        }
    }
}

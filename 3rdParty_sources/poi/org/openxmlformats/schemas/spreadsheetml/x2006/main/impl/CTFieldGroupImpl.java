/*
 * XML Type:  CT_FieldGroup
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldGroup
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_FieldGroup(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTFieldGroupImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldGroup {
    private static final long serialVersionUID = 1L;

    public CTFieldGroupImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rangePr"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "discretePr"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "groupItems"),
        new QName("", "par"),
        new QName("", "base"),
    };


    /**
     * Gets the "rangePr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangePr getRangePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangePr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangePr)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rangePr" element
     */
    @Override
    public boolean isSetRangePr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "rangePr" element
     */
    @Override
    public void setRangePr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangePr rangePr) {
        generatedSetterHelperImpl(rangePr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rangePr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangePr addNewRangePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangePr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangePr)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "rangePr" element
     */
    @Override
    public void unsetRangePr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "discretePr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDiscretePr getDiscretePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDiscretePr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDiscretePr)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "discretePr" element
     */
    @Override
    public boolean isSetDiscretePr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "discretePr" element
     */
    @Override
    public void setDiscretePr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDiscretePr discretePr) {
        generatedSetterHelperImpl(discretePr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "discretePr" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDiscretePr addNewDiscretePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDiscretePr target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDiscretePr)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "discretePr" element
     */
    @Override
    public void unsetDiscretePr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "groupItems" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupItems getGroupItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupItems target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupItems)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "groupItems" element
     */
    @Override
    public boolean isSetGroupItems() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "groupItems" element
     */
    @Override
    public void setGroupItems(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupItems groupItems) {
        generatedSetterHelperImpl(groupItems, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "groupItems" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupItems addNewGroupItems() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupItems target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupItems)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "groupItems" element
     */
    @Override
    public void unsetGroupItems() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "par" attribute
     */
    @Override
    public long getPar() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "par" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetPar() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "par" attribute
     */
    @Override
    public boolean isSetPar() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "par" attribute
     */
    @Override
    public void setPar(long par) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setLongValue(par);
        }
    }

    /**
     * Sets (as xml) the "par" attribute
     */
    @Override
    public void xsetPar(org.apache.xmlbeans.XmlUnsignedInt par) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(par);
        }
    }

    /**
     * Unsets the "par" attribute
     */
    @Override
    public void unsetPar() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "base" attribute
     */
    @Override
    public long getBase() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "base" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetBase() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "base" attribute
     */
    @Override
    public boolean isSetBase() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "base" attribute
     */
    @Override
    public void setBase(long base) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setLongValue(base);
        }
    }

    /**
     * Sets (as xml) the "base" attribute
     */
    @Override
    public void xsetBase(org.apache.xmlbeans.XmlUnsignedInt base) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(base);
        }
    }

    /**
     * Unsets the "base" attribute
     */
    @Override
    public void unsetBase() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }
}

/*
 * XML Type:  CT_Consolidation
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConsolidation
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Consolidation(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTConsolidationImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConsolidation {
    private static final long serialVersionUID = 1L;

    public CTConsolidationImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pages"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "rangeSets"),
        new QName("", "autoPage"),
    };


    /**
     * Gets the "pages" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPages getPages() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPages target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPages)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pages" element
     */
    @Override
    public boolean isSetPages() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "pages" element
     */
    @Override
    public void setPages(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPages pages) {
        generatedSetterHelperImpl(pages, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pages" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPages addNewPages() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPages target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPages)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "pages" element
     */
    @Override
    public void unsetPages() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "rangeSets" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangeSets getRangeSets() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangeSets target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangeSets)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "rangeSets" element
     */
    @Override
    public void setRangeSets(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangeSets rangeSets) {
        generatedSetterHelperImpl(rangeSets, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rangeSets" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangeSets addNewRangeSets() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangeSets target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangeSets)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "autoPage" attribute
     */
    @Override
    public boolean getAutoPage() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "autoPage" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetAutoPage() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "autoPage" attribute
     */
    @Override
    public boolean isSetAutoPage() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "autoPage" attribute
     */
    @Override
    public void setAutoPage(boolean autoPage) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBooleanValue(autoPage);
        }
    }

    /**
     * Sets (as xml) the "autoPage" attribute
     */
    @Override
    public void xsetAutoPage(org.apache.xmlbeans.XmlBoolean autoPage) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(autoPage);
        }
    }

    /**
     * Unsets the "autoPage" attribute
     */
    @Override
    public void unsetAutoPage() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}

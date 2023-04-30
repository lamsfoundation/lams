/*
 * XML Type:  CT_PrintSettings
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTPrintSettings
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PrintSettings(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTPrintSettingsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTPrintSettings {
    private static final long serialVersionUID = 1L;

    public CTPrintSettingsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "headerFooter"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "pageMargins"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "pageSetup"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "legacyDrawingHF"),
    };


    /**
     * Gets the "headerFooter" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTHeaderFooter getHeaderFooter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTHeaderFooter target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTHeaderFooter)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "headerFooter" element
     */
    @Override
    public boolean isSetHeaderFooter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "headerFooter" element
     */
    @Override
    public void setHeaderFooter(org.openxmlformats.schemas.drawingml.x2006.chart.CTHeaderFooter headerFooter) {
        generatedSetterHelperImpl(headerFooter, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "headerFooter" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTHeaderFooter addNewHeaderFooter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTHeaderFooter target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTHeaderFooter)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "headerFooter" element
     */
    @Override
    public void unsetHeaderFooter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "pageMargins" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPageMargins getPageMargins() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPageMargins target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPageMargins)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pageMargins" element
     */
    @Override
    public boolean isSetPageMargins() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "pageMargins" element
     */
    @Override
    public void setPageMargins(org.openxmlformats.schemas.drawingml.x2006.chart.CTPageMargins pageMargins) {
        generatedSetterHelperImpl(pageMargins, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pageMargins" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPageMargins addNewPageMargins() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPageMargins target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPageMargins)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "pageMargins" element
     */
    @Override
    public void unsetPageMargins() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "pageSetup" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup getPageSetup() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pageSetup" element
     */
    @Override
    public boolean isSetPageSetup() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "pageSetup" element
     */
    @Override
    public void setPageSetup(org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup pageSetup) {
        generatedSetterHelperImpl(pageSetup, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pageSetup" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup addNewPageSetup() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPageSetup)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "pageSetup" element
     */
    @Override
    public void unsetPageSetup() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "legacyDrawingHF" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId getLegacyDrawingHF() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "legacyDrawingHF" element
     */
    @Override
    public boolean isSetLegacyDrawingHF() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "legacyDrawingHF" element
     */
    @Override
    public void setLegacyDrawingHF(org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId legacyDrawingHF) {
        generatedSetterHelperImpl(legacyDrawingHF, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "legacyDrawingHF" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId addNewLegacyDrawingHF() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "legacyDrawingHF" element
     */
    @Override
    public void unsetLegacyDrawingHF() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}

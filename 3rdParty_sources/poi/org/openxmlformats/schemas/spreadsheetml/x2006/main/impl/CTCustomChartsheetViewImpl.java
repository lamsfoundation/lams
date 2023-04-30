/*
 * XML Type:  CT_CustomChartsheetView
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetView
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CustomChartsheetView(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTCustomChartsheetViewImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCustomChartsheetView {
    private static final long serialVersionUID = 1L;

    public CTCustomChartsheetViewImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pageMargins"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "pageSetup"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "headerFooter"),
        new QName("", "guid"),
        new QName("", "scale"),
        new QName("", "state"),
        new QName("", "zoomToFit"),
    };


    /**
     * Gets the "pageMargins" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins getPageMargins() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins)get_store().find_element_user(PROPERTY_QNAME[0], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "pageMargins" element
     */
    @Override
    public void setPageMargins(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins pageMargins) {
        generatedSetterHelperImpl(pageMargins, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pageMargins" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins addNewPageMargins() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins)get_store().add_element_user(PROPERTY_QNAME[0]);
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
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "pageSetup" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCsPageSetup getPageSetup() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCsPageSetup target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCsPageSetup)get_store().find_element_user(PROPERTY_QNAME[1], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "pageSetup" element
     */
    @Override
    public void setPageSetup(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCsPageSetup pageSetup) {
        generatedSetterHelperImpl(pageSetup, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pageSetup" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCsPageSetup addNewPageSetup() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCsPageSetup target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCsPageSetup)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "headerFooter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter getHeaderFooter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter)get_store().find_element_user(PROPERTY_QNAME[2], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "headerFooter" element
     */
    @Override
    public void setHeaderFooter(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter headerFooter) {
        generatedSetterHelperImpl(headerFooter, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "headerFooter" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter addNewHeaderFooter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter)get_store().add_element_user(PROPERTY_QNAME[2]);
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
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "guid" attribute
     */
    @Override
    public java.lang.String getGuid() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "guid" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid xgetGuid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Sets the "guid" attribute
     */
    @Override
    public void setGuid(java.lang.String guid) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setStringValue(guid);
        }
    }

    /**
     * Sets (as xml) the "guid" attribute
     */
    @Override
    public void xsetGuid(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid guid) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(guid);
        }
    }

    /**
     * Gets the "scale" attribute
     */
    @Override
    public long getScale() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "scale" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetScale() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "scale" attribute
     */
    @Override
    public boolean isSetScale() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "scale" attribute
     */
    @Override
    public void setScale(long scale) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setLongValue(scale);
        }
    }

    /**
     * Sets (as xml) the "scale" attribute
     */
    @Override
    public void xsetScale(org.apache.xmlbeans.XmlUnsignedInt scale) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(scale);
        }
    }

    /**
     * Unsets the "scale" attribute
     */
    @Override
    public void unsetScale() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "state" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState.Enum getState() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "state" attribute
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState xgetState() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return target;
        }
    }

    /**
     * True if has "state" attribute
     */
    @Override
    public boolean isSetState() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "state" attribute
     */
    @Override
    public void setState(org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState.Enum state) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(state);
        }
    }

    /**
     * Sets (as xml) the "state" attribute
     */
    @Override
    public void xsetState(org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState state) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.STSheetState)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(state);
        }
    }

    /**
     * Unsets the "state" attribute
     */
    @Override
    public void unsetState() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "zoomToFit" attribute
     */
    @Override
    public boolean getZoomToFit() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "zoomToFit" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetZoomToFit() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return target;
        }
    }

    /**
     * True if has "zoomToFit" attribute
     */
    @Override
    public boolean isSetZoomToFit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "zoomToFit" attribute
     */
    @Override
    public void setZoomToFit(boolean zoomToFit) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setBooleanValue(zoomToFit);
        }
    }

    /**
     * Sets (as xml) the "zoomToFit" attribute
     */
    @Override
    public void xsetZoomToFit(org.apache.xmlbeans.XmlBoolean zoomToFit) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(zoomToFit);
        }
    }

    /**
     * Unsets the "zoomToFit" attribute
     */
    @Override
    public void unsetZoomToFit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }
}

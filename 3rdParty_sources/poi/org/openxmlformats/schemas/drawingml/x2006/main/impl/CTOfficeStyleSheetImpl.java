/*
 * XML Type:  CT_OfficeStyleSheet
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_OfficeStyleSheet(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTOfficeStyleSheetImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeStyleSheet {
    private static final long serialVersionUID = 1L;

    public CTOfficeStyleSheetImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "themeElements"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "objectDefaults"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extraClrSchemeLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "custClrLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
        new QName("", "name"),
    };


    /**
     * Gets the "themeElements" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles getThemeElements() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "themeElements" element
     */
    @Override
    public void setThemeElements(org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles themeElements) {
        generatedSetterHelperImpl(themeElements, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "themeElements" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles addNewThemeElements() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBaseStyles)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "objectDefaults" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTObjectStyleDefaults getObjectDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTObjectStyleDefaults target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTObjectStyleDefaults)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "objectDefaults" element
     */
    @Override
    public boolean isSetObjectDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "objectDefaults" element
     */
    @Override
    public void setObjectDefaults(org.openxmlformats.schemas.drawingml.x2006.main.CTObjectStyleDefaults objectDefaults) {
        generatedSetterHelperImpl(objectDefaults, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "objectDefaults" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTObjectStyleDefaults addNewObjectDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTObjectStyleDefaults target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTObjectStyleDefaults)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "objectDefaults" element
     */
    @Override
    public void unsetObjectDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "extraClrSchemeLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeList getExtraClrSchemeLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeList)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extraClrSchemeLst" element
     */
    @Override
    public boolean isSetExtraClrSchemeLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "extraClrSchemeLst" element
     */
    @Override
    public void setExtraClrSchemeLst(org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeList extraClrSchemeLst) {
        generatedSetterHelperImpl(extraClrSchemeLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extraClrSchemeLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeList addNewExtraClrSchemeLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorSchemeList)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "extraClrSchemeLst" element
     */
    @Override
    public void unsetExtraClrSchemeLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "custClrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColorList getCustClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColorList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColorList)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "custClrLst" element
     */
    @Override
    public boolean isSetCustClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "custClrLst" element
     */
    @Override
    public void setCustClrLst(org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColorList custClrLst) {
        generatedSetterHelperImpl(custClrLst, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "custClrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColorList addNewCustClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColorList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTCustomColorList)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "custClrLst" element
     */
    @Override
    public void unsetCustClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extLst" element
     */
    @Override
    public boolean isSetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "extLst" element
     */
    @Override
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "name" attribute
     */
    @Override
    public java.lang.String getName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "name" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return target;
        }
    }

    /**
     * True if has "name" attribute
     */
    @Override
    public boolean isSetName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "name" attribute
     */
    @Override
    public void setName(java.lang.String name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setStringValue(name);
        }
    }

    /**
     * Sets (as xml) the "name" attribute
     */
    @Override
    public void xsetName(org.apache.xmlbeans.XmlString name) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(name);
        }
    }

    /**
     * Unsets the "name" attribute
     */
    @Override
    public void unsetName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }
}

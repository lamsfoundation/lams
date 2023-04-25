/*
 * XML Type:  CT_ObjectStyleDefaults
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTObjectStyleDefaults
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ObjectStyleDefaults(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTObjectStyleDefaultsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTObjectStyleDefaults {
    private static final long serialVersionUID = 1L;

    public CTObjectStyleDefaultsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "spDef"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lnDef"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "txDef"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
    };


    /**
     * Gets the "spDef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition getSpDef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "spDef" element
     */
    @Override
    public boolean isSetSpDef() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "spDef" element
     */
    @Override
    public void setSpDef(org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition spDef) {
        generatedSetterHelperImpl(spDef, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spDef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition addNewSpDef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "spDef" element
     */
    @Override
    public void unsetSpDef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "lnDef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition getLnDef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lnDef" element
     */
    @Override
    public boolean isSetLnDef() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "lnDef" element
     */
    @Override
    public void setLnDef(org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition lnDef) {
        generatedSetterHelperImpl(lnDef, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lnDef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition addNewLnDef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "lnDef" element
     */
    @Override
    public void unsetLnDef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "txDef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition getTxDef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "txDef" element
     */
    @Override
    public boolean isSetTxDef() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "txDef" element
     */
    @Override
    public void setTxDef(org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition txDef) {
        generatedSetterHelperImpl(txDef, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "txDef" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition addNewTxDef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "txDef" element
     */
    @Override
    public void unsetTxDef() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}

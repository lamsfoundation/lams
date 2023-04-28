/*
 * XML Type:  CT_CTStyleLabel
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CTStyleLabel(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTCTStyleLabelImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTCTStyleLabel {
    private static final long serialVersionUID = 1L;

    public CTCTStyleLabelImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "fillClrLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "linClrLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "effectClrLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "txLinClrLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "txFillClrLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "txEffectClrLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "extLst"),
        new QName("", "name"),
    };


    /**
     * Gets the "fillClrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors getFillClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fillClrLst" element
     */
    @Override
    public boolean isSetFillClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "fillClrLst" element
     */
    @Override
    public void setFillClrLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors fillClrLst) {
        generatedSetterHelperImpl(fillClrLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fillClrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors addNewFillClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "fillClrLst" element
     */
    @Override
    public void unsetFillClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "linClrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors getLinClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "linClrLst" element
     */
    @Override
    public boolean isSetLinClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "linClrLst" element
     */
    @Override
    public void setLinClrLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors linClrLst) {
        generatedSetterHelperImpl(linClrLst, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "linClrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors addNewLinClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "linClrLst" element
     */
    @Override
    public void unsetLinClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "effectClrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors getEffectClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "effectClrLst" element
     */
    @Override
    public boolean isSetEffectClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "effectClrLst" element
     */
    @Override
    public void setEffectClrLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors effectClrLst) {
        generatedSetterHelperImpl(effectClrLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "effectClrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors addNewEffectClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "effectClrLst" element
     */
    @Override
    public void unsetEffectClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "txLinClrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors getTxLinClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "txLinClrLst" element
     */
    @Override
    public boolean isSetTxLinClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "txLinClrLst" element
     */
    @Override
    public void setTxLinClrLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors txLinClrLst) {
        generatedSetterHelperImpl(txLinClrLst, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "txLinClrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors addNewTxLinClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "txLinClrLst" element
     */
    @Override
    public void unsetTxLinClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "txFillClrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors getTxFillClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "txFillClrLst" element
     */
    @Override
    public boolean isSetTxFillClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "txFillClrLst" element
     */
    @Override
    public void setTxFillClrLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors txFillClrLst) {
        generatedSetterHelperImpl(txFillClrLst, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "txFillClrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors addNewTxFillClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "txFillClrLst" element
     */
    @Override
    public void unsetTxFillClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "txEffectClrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors getTxEffectClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "txEffectClrLst" element
     */
    @Override
    public boolean isSetTxEffectClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "txEffectClrLst" element
     */
    @Override
    public void setTxEffectClrLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors txEffectClrLst) {
        generatedSetterHelperImpl(txEffectClrLst, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "txEffectClrLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors addNewTxEffectClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTColors)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "txEffectClrLst" element
     */
    @Override
    public void unsetTxEffectClrLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[6], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[6]);
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
            get_store().remove_element(PROPERTY_QNAME[6], 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(name);
        }
    }
}

/*
 * XML Type:  CT_WordprocessingContentPart
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_WordprocessingContentPart(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public class CTWordprocessingContentPartImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPart {
    private static final long serialVersionUID = 1L;

    public CTWordprocessingContentPartImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "nvContentPartPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "xfrm"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "extLst"),
        new QName("", "bwMode"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/relationships", "id"),
    };


    /**
     * Gets the "nvContentPartPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPartNonVisual getNvContentPartPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPartNonVisual target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPartNonVisual)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "nvContentPartPr" element
     */
    @Override
    public boolean isSetNvContentPartPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "nvContentPartPr" element
     */
    @Override
    public void setNvContentPartPr(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPartNonVisual nvContentPartPr) {
        generatedSetterHelperImpl(nvContentPartPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "nvContentPartPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPartNonVisual addNewNvContentPartPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPartNonVisual target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingContentPartNonVisual)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "nvContentPartPr" element
     */
    @Override
    public void unsetNvContentPartPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "xfrm" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D getXfrm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "xfrm" element
     */
    @Override
    public boolean isSetXfrm() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "xfrm" element
     */
    @Override
    public void setXfrm(org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D xfrm) {
        generatedSetterHelperImpl(xfrm, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "xfrm" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D addNewXfrm() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "xfrm" element
     */
    @Override
    public void unsetXfrm() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[2], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[2]);
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
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "bwMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum getBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "bwMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode xgetBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "bwMode" attribute
     */
    @Override
    public boolean isSetBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "bwMode" attribute
     */
    @Override
    public void setBwMode(org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum bwMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setEnumValue(bwMode);
        }
    }

    /**
     * Sets (as xml) the "bwMode" attribute
     */
    @Override
    public void xsetBwMode(org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode bwMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(bwMode);
        }
    }

    /**
     * Unsets the "bwMode" attribute
     */
    @Override
    public void unsetBwMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "id" attribute
     */
    @Override
    public java.lang.String getId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "id" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Sets the "id" attribute
     */
    @Override
    public void setId(java.lang.String id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setStringValue(id);
        }
    }

    /**
     * Sets (as xml) the "id" attribute
     */
    @Override
    public void xsetId(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId id) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(id);
        }
    }
}

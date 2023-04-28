/*
 * XML Type:  CT_DataModel
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DataModel(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTDataModelImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel {
    private static final long serialVersionUID = 1L;

    public CTDataModelImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "ptLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "cxnLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "bg"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "whole"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "extLst"),
    };


    /**
     * Gets the "ptLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTPtList getPtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTPtList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTPtList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "ptLst" element
     */
    @Override
    public void setPtLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTPtList ptLst) {
        generatedSetterHelperImpl(ptLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ptLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTPtList addNewPtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTPtList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTPtList)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "cxnLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxnList getCxnLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxnList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxnList)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cxnLst" element
     */
    @Override
    public boolean isSetCxnLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "cxnLst" element
     */
    @Override
    public void setCxnLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxnList cxnLst) {
        generatedSetterHelperImpl(cxnLst, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cxnLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxnList addNewCxnLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxnList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxnList)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "cxnLst" element
     */
    @Override
    public void unsetCxnLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "bg" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting getBg() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bg" element
     */
    @Override
    public boolean isSetBg() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "bg" element
     */
    @Override
    public void setBg(org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting bg) {
        generatedSetterHelperImpl(bg, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bg" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting addNewBg() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "bg" element
     */
    @Override
    public void unsetBg() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "whole" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting getWhole() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "whole" element
     */
    @Override
    public boolean isSetWhole() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "whole" element
     */
    @Override
    public void setWhole(org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting whole) {
        generatedSetterHelperImpl(whole, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "whole" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting addNewWhole() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "whole" element
     */
    @Override
    public void unsetWhole() {
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
}

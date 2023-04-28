/*
 * XML Type:  CT_ElemPropSet
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTElemPropSet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ElemPropSet(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTElemPropSetImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTElemPropSet {
    private static final long serialVersionUID = 1L;

    public CTElemPropSetImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "presLayoutVars"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "style"),
        new QName("", "presAssocID"),
        new QName("", "presName"),
        new QName("", "presStyleLbl"),
        new QName("", "presStyleIdx"),
        new QName("", "presStyleCnt"),
        new QName("", "loTypeId"),
        new QName("", "loCatId"),
        new QName("", "qsTypeId"),
        new QName("", "qsCatId"),
        new QName("", "csTypeId"),
        new QName("", "csCatId"),
        new QName("", "coherent3DOff"),
        new QName("", "phldrT"),
        new QName("", "phldr"),
        new QName("", "custAng"),
        new QName("", "custFlipVert"),
        new QName("", "custFlipHor"),
        new QName("", "custSzX"),
        new QName("", "custSzY"),
        new QName("", "custScaleX"),
        new QName("", "custScaleY"),
        new QName("", "custT"),
        new QName("", "custLinFactX"),
        new QName("", "custLinFactY"),
        new QName("", "custLinFactNeighborX"),
        new QName("", "custLinFactNeighborY"),
        new QName("", "custRadScaleRad"),
        new QName("", "custRadScaleInc"),
    };


    /**
     * Gets the "presLayoutVars" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet getPresLayoutVars() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "presLayoutVars" element
     */
    @Override
    public boolean isSetPresLayoutVars() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "presLayoutVars" element
     */
    @Override
    public void setPresLayoutVars(org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet presLayoutVars) {
        generatedSetterHelperImpl(presLayoutVars, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "presLayoutVars" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet addNewPresLayoutVars() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutVariablePropertySet)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "presLayoutVars" element
     */
    @Override
    public void unsetPresLayoutVars() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "style" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle getStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "style" element
     */
    @Override
    public boolean isSetStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "style" element
     */
    @Override
    public void setStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle style) {
        generatedSetterHelperImpl(style, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "style" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle addNewStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "style" element
     */
    @Override
    public void unsetStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "presAssocID" attribute
     */
    @Override
    public java.lang.Object getPresAssocID() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "presAssocID" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId xgetPresAssocID() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "presAssocID" attribute
     */
    @Override
    public boolean isSetPresAssocID() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "presAssocID" attribute
     */
    @Override
    public void setPresAssocID(java.lang.Object presAssocID) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setObjectValue(presAssocID);
        }
    }

    /**
     * Sets (as xml) the "presAssocID" attribute
     */
    @Override
    public void xsetPresAssocID(org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId presAssocID) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(presAssocID);
        }
    }

    /**
     * Unsets the "presAssocID" attribute
     */
    @Override
    public void unsetPresAssocID() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "presName" attribute
     */
    @Override
    public java.lang.String getPresName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "presName" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetPresName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "presName" attribute
     */
    @Override
    public boolean isSetPresName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "presName" attribute
     */
    @Override
    public void setPresName(java.lang.String presName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setStringValue(presName);
        }
    }

    /**
     * Sets (as xml) the "presName" attribute
     */
    @Override
    public void xsetPresName(org.apache.xmlbeans.XmlString presName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(presName);
        }
    }

    /**
     * Unsets the "presName" attribute
     */
    @Override
    public void unsetPresName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "presStyleLbl" attribute
     */
    @Override
    public java.lang.String getPresStyleLbl() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "presStyleLbl" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetPresStyleLbl() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "presStyleLbl" attribute
     */
    @Override
    public boolean isSetPresStyleLbl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "presStyleLbl" attribute
     */
    @Override
    public void setPresStyleLbl(java.lang.String presStyleLbl) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setStringValue(presStyleLbl);
        }
    }

    /**
     * Sets (as xml) the "presStyleLbl" attribute
     */
    @Override
    public void xsetPresStyleLbl(org.apache.xmlbeans.XmlString presStyleLbl) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(presStyleLbl);
        }
    }

    /**
     * Unsets the "presStyleLbl" attribute
     */
    @Override
    public void unsetPresStyleLbl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "presStyleIdx" attribute
     */
    @Override
    public int getPresStyleIdx() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "presStyleIdx" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetPresStyleIdx() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "presStyleIdx" attribute
     */
    @Override
    public boolean isSetPresStyleIdx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "presStyleIdx" attribute
     */
    @Override
    public void setPresStyleIdx(int presStyleIdx) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setIntValue(presStyleIdx);
        }
    }

    /**
     * Sets (as xml) the "presStyleIdx" attribute
     */
    @Override
    public void xsetPresStyleIdx(org.apache.xmlbeans.XmlInt presStyleIdx) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(presStyleIdx);
        }
    }

    /**
     * Unsets the "presStyleIdx" attribute
     */
    @Override
    public void unsetPresStyleIdx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "presStyleCnt" attribute
     */
    @Override
    public int getPresStyleCnt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "presStyleCnt" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetPresStyleCnt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "presStyleCnt" attribute
     */
    @Override
    public boolean isSetPresStyleCnt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "presStyleCnt" attribute
     */
    @Override
    public void setPresStyleCnt(int presStyleCnt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setIntValue(presStyleCnt);
        }
    }

    /**
     * Sets (as xml) the "presStyleCnt" attribute
     */
    @Override
    public void xsetPresStyleCnt(org.apache.xmlbeans.XmlInt presStyleCnt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(presStyleCnt);
        }
    }

    /**
     * Unsets the "presStyleCnt" attribute
     */
    @Override
    public void unsetPresStyleCnt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "loTypeId" attribute
     */
    @Override
    public java.lang.String getLoTypeId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "loTypeId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetLoTypeId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * True if has "loTypeId" attribute
     */
    @Override
    public boolean isSetLoTypeId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "loTypeId" attribute
     */
    @Override
    public void setLoTypeId(java.lang.String loTypeId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setStringValue(loTypeId);
        }
    }

    /**
     * Sets (as xml) the "loTypeId" attribute
     */
    @Override
    public void xsetLoTypeId(org.apache.xmlbeans.XmlString loTypeId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(loTypeId);
        }
    }

    /**
     * Unsets the "loTypeId" attribute
     */
    @Override
    public void unsetLoTypeId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "loCatId" attribute
     */
    @Override
    public java.lang.String getLoCatId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "loCatId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetLoCatId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * True if has "loCatId" attribute
     */
    @Override
    public boolean isSetLoCatId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "loCatId" attribute
     */
    @Override
    public void setLoCatId(java.lang.String loCatId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setStringValue(loCatId);
        }
    }

    /**
     * Sets (as xml) the "loCatId" attribute
     */
    @Override
    public void xsetLoCatId(org.apache.xmlbeans.XmlString loCatId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(loCatId);
        }
    }

    /**
     * Unsets the "loCatId" attribute
     */
    @Override
    public void unsetLoCatId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "qsTypeId" attribute
     */
    @Override
    public java.lang.String getQsTypeId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "qsTypeId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetQsTypeId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * True if has "qsTypeId" attribute
     */
    @Override
    public boolean isSetQsTypeId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "qsTypeId" attribute
     */
    @Override
    public void setQsTypeId(java.lang.String qsTypeId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setStringValue(qsTypeId);
        }
    }

    /**
     * Sets (as xml) the "qsTypeId" attribute
     */
    @Override
    public void xsetQsTypeId(org.apache.xmlbeans.XmlString qsTypeId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(qsTypeId);
        }
    }

    /**
     * Unsets the "qsTypeId" attribute
     */
    @Override
    public void unsetQsTypeId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "qsCatId" attribute
     */
    @Override
    public java.lang.String getQsCatId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "qsCatId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetQsCatId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * True if has "qsCatId" attribute
     */
    @Override
    public boolean isSetQsCatId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "qsCatId" attribute
     */
    @Override
    public void setQsCatId(java.lang.String qsCatId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setStringValue(qsCatId);
        }
    }

    /**
     * Sets (as xml) the "qsCatId" attribute
     */
    @Override
    public void xsetQsCatId(org.apache.xmlbeans.XmlString qsCatId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(qsCatId);
        }
    }

    /**
     * Unsets the "qsCatId" attribute
     */
    @Override
    public void unsetQsCatId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Gets the "csTypeId" attribute
     */
    @Override
    public java.lang.String getCsTypeId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "csTypeId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetCsTypeId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * True if has "csTypeId" attribute
     */
    @Override
    public boolean isSetCsTypeId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "csTypeId" attribute
     */
    @Override
    public void setCsTypeId(java.lang.String csTypeId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setStringValue(csTypeId);
        }
    }

    /**
     * Sets (as xml) the "csTypeId" attribute
     */
    @Override
    public void xsetCsTypeId(org.apache.xmlbeans.XmlString csTypeId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(csTypeId);
        }
    }

    /**
     * Unsets the "csTypeId" attribute
     */
    @Override
    public void unsetCsTypeId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Gets the "csCatId" attribute
     */
    @Override
    public java.lang.String getCsCatId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "csCatId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetCsCatId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * True if has "csCatId" attribute
     */
    @Override
    public boolean isSetCsCatId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[12]) != null;
        }
    }

    /**
     * Sets the "csCatId" attribute
     */
    @Override
    public void setCsCatId(java.lang.String csCatId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setStringValue(csCatId);
        }
    }

    /**
     * Sets (as xml) the "csCatId" attribute
     */
    @Override
    public void xsetCsCatId(org.apache.xmlbeans.XmlString csCatId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(csCatId);
        }
    }

    /**
     * Unsets the "csCatId" attribute
     */
    @Override
    public void unsetCsCatId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Gets the "coherent3DOff" attribute
     */
    @Override
    public boolean getCoherent3DOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "coherent3DOff" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetCoherent3DOff() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * True if has "coherent3DOff" attribute
     */
    @Override
    public boolean isSetCoherent3DOff() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
        }
    }

    /**
     * Sets the "coherent3DOff" attribute
     */
    @Override
    public void setCoherent3DOff(boolean coherent3DOff) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.setBooleanValue(coherent3DOff);
        }
    }

    /**
     * Sets (as xml) the "coherent3DOff" attribute
     */
    @Override
    public void xsetCoherent3DOff(org.apache.xmlbeans.XmlBoolean coherent3DOff) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.set(coherent3DOff);
        }
    }

    /**
     * Unsets the "coherent3DOff" attribute
     */
    @Override
    public void unsetCoherent3DOff() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Gets the "phldrT" attribute
     */
    @Override
    public java.lang.String getPhldrT() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "phldrT" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetPhldrT() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * True if has "phldrT" attribute
     */
    @Override
    public boolean isSetPhldrT() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[14]) != null;
        }
    }

    /**
     * Sets the "phldrT" attribute
     */
    @Override
    public void setPhldrT(java.lang.String phldrT) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.setStringValue(phldrT);
        }
    }

    /**
     * Sets (as xml) the "phldrT" attribute
     */
    @Override
    public void xsetPhldrT(org.apache.xmlbeans.XmlString phldrT) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.set(phldrT);
        }
    }

    /**
     * Unsets the "phldrT" attribute
     */
    @Override
    public void unsetPhldrT() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Gets the "phldr" attribute
     */
    @Override
    public boolean getPhldr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "phldr" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetPhldr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * True if has "phldr" attribute
     */
    @Override
    public boolean isSetPhldr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[15]) != null;
        }
    }

    /**
     * Sets the "phldr" attribute
     */
    @Override
    public void setPhldr(boolean phldr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.setBooleanValue(phldr);
        }
    }

    /**
     * Sets (as xml) the "phldr" attribute
     */
    @Override
    public void xsetPhldr(org.apache.xmlbeans.XmlBoolean phldr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.set(phldr);
        }
    }

    /**
     * Unsets the "phldr" attribute
     */
    @Override
    public void unsetPhldr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Gets the "custAng" attribute
     */
    @Override
    public int getCustAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "custAng" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetCustAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * True if has "custAng" attribute
     */
    @Override
    public boolean isSetCustAng() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[16]) != null;
        }
    }

    /**
     * Sets the "custAng" attribute
     */
    @Override
    public void setCustAng(int custAng) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.setIntValue(custAng);
        }
    }

    /**
     * Sets (as xml) the "custAng" attribute
     */
    @Override
    public void xsetCustAng(org.apache.xmlbeans.XmlInt custAng) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.set(custAng);
        }
    }

    /**
     * Unsets the "custAng" attribute
     */
    @Override
    public void unsetCustAng() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Gets the "custFlipVert" attribute
     */
    @Override
    public boolean getCustFlipVert() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "custFlipVert" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetCustFlipVert() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * True if has "custFlipVert" attribute
     */
    @Override
    public boolean isSetCustFlipVert() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[17]) != null;
        }
    }

    /**
     * Sets the "custFlipVert" attribute
     */
    @Override
    public void setCustFlipVert(boolean custFlipVert) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.setBooleanValue(custFlipVert);
        }
    }

    /**
     * Sets (as xml) the "custFlipVert" attribute
     */
    @Override
    public void xsetCustFlipVert(org.apache.xmlbeans.XmlBoolean custFlipVert) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.set(custFlipVert);
        }
    }

    /**
     * Unsets the "custFlipVert" attribute
     */
    @Override
    public void unsetCustFlipVert() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Gets the "custFlipHor" attribute
     */
    @Override
    public boolean getCustFlipHor() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "custFlipHor" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetCustFlipHor() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * True if has "custFlipHor" attribute
     */
    @Override
    public boolean isSetCustFlipHor() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[18]) != null;
        }
    }

    /**
     * Sets the "custFlipHor" attribute
     */
    @Override
    public void setCustFlipHor(boolean custFlipHor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.setBooleanValue(custFlipHor);
        }
    }

    /**
     * Sets (as xml) the "custFlipHor" attribute
     */
    @Override
    public void xsetCustFlipHor(org.apache.xmlbeans.XmlBoolean custFlipHor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.set(custFlipHor);
        }
    }

    /**
     * Unsets the "custFlipHor" attribute
     */
    @Override
    public void unsetCustFlipHor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Gets the "custSzX" attribute
     */
    @Override
    public int getCustSzX() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "custSzX" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetCustSzX() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * True if has "custSzX" attribute
     */
    @Override
    public boolean isSetCustSzX() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[19]) != null;
        }
    }

    /**
     * Sets the "custSzX" attribute
     */
    @Override
    public void setCustSzX(int custSzX) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.setIntValue(custSzX);
        }
    }

    /**
     * Sets (as xml) the "custSzX" attribute
     */
    @Override
    public void xsetCustSzX(org.apache.xmlbeans.XmlInt custSzX) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.set(custSzX);
        }
    }

    /**
     * Unsets the "custSzX" attribute
     */
    @Override
    public void unsetCustSzX() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Gets the "custSzY" attribute
     */
    @Override
    public int getCustSzY() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "custSzY" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetCustSzY() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * True if has "custSzY" attribute
     */
    @Override
    public boolean isSetCustSzY() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[20]) != null;
        }
    }

    /**
     * Sets the "custSzY" attribute
     */
    @Override
    public void setCustSzY(int custSzY) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.setIntValue(custSzY);
        }
    }

    /**
     * Sets (as xml) the "custSzY" attribute
     */
    @Override
    public void xsetCustSzY(org.apache.xmlbeans.XmlInt custSzY) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.set(custSzY);
        }
    }

    /**
     * Unsets the "custSzY" attribute
     */
    @Override
    public void unsetCustSzY() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Gets the "custScaleX" attribute
     */
    @Override
    public java.lang.Object getCustScaleX() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "custScaleX" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal xgetCustScaleX() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * True if has "custScaleX" attribute
     */
    @Override
    public boolean isSetCustScaleX() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[21]) != null;
        }
    }

    /**
     * Sets the "custScaleX" attribute
     */
    @Override
    public void setCustScaleX(java.lang.Object custScaleX) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.setObjectValue(custScaleX);
        }
    }

    /**
     * Sets (as xml) the "custScaleX" attribute
     */
    @Override
    public void xsetCustScaleX(org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal custScaleX) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.set(custScaleX);
        }
    }

    /**
     * Unsets the "custScaleX" attribute
     */
    @Override
    public void unsetCustScaleX() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Gets the "custScaleY" attribute
     */
    @Override
    public java.lang.Object getCustScaleY() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "custScaleY" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal xgetCustScaleY() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * True if has "custScaleY" attribute
     */
    @Override
    public boolean isSetCustScaleY() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[22]) != null;
        }
    }

    /**
     * Sets the "custScaleY" attribute
     */
    @Override
    public void setCustScaleY(java.lang.Object custScaleY) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.setObjectValue(custScaleY);
        }
    }

    /**
     * Sets (as xml) the "custScaleY" attribute
     */
    @Override
    public void xsetCustScaleY(org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal custScaleY) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.set(custScaleY);
        }
    }

    /**
     * Unsets the "custScaleY" attribute
     */
    @Override
    public void unsetCustScaleY() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Gets the "custT" attribute
     */
    @Override
    public boolean getCustT() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "custT" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetCustT() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * True if has "custT" attribute
     */
    @Override
    public boolean isSetCustT() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[23]) != null;
        }
    }

    /**
     * Sets the "custT" attribute
     */
    @Override
    public void setCustT(boolean custT) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.setBooleanValue(custT);
        }
    }

    /**
     * Sets (as xml) the "custT" attribute
     */
    @Override
    public void xsetCustT(org.apache.xmlbeans.XmlBoolean custT) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.set(custT);
        }
    }

    /**
     * Unsets the "custT" attribute
     */
    @Override
    public void unsetCustT() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Gets the "custLinFactX" attribute
     */
    @Override
    public java.lang.Object getCustLinFactX() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "custLinFactX" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal xgetCustLinFactX() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * True if has "custLinFactX" attribute
     */
    @Override
    public boolean isSetCustLinFactX() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[24]) != null;
        }
    }

    /**
     * Sets the "custLinFactX" attribute
     */
    @Override
    public void setCustLinFactX(java.lang.Object custLinFactX) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.setObjectValue(custLinFactX);
        }
    }

    /**
     * Sets (as xml) the "custLinFactX" attribute
     */
    @Override
    public void xsetCustLinFactX(org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal custLinFactX) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.set(custLinFactX);
        }
    }

    /**
     * Unsets the "custLinFactX" attribute
     */
    @Override
    public void unsetCustLinFactX() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Gets the "custLinFactY" attribute
     */
    @Override
    public java.lang.Object getCustLinFactY() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "custLinFactY" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal xgetCustLinFactY() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * True if has "custLinFactY" attribute
     */
    @Override
    public boolean isSetCustLinFactY() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[25]) != null;
        }
    }

    /**
     * Sets the "custLinFactY" attribute
     */
    @Override
    public void setCustLinFactY(java.lang.Object custLinFactY) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.setObjectValue(custLinFactY);
        }
    }

    /**
     * Sets (as xml) the "custLinFactY" attribute
     */
    @Override
    public void xsetCustLinFactY(org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal custLinFactY) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.set(custLinFactY);
        }
    }

    /**
     * Unsets the "custLinFactY" attribute
     */
    @Override
    public void unsetCustLinFactY() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Gets the "custLinFactNeighborX" attribute
     */
    @Override
    public java.lang.Object getCustLinFactNeighborX() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "custLinFactNeighborX" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal xgetCustLinFactNeighborX() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * True if has "custLinFactNeighborX" attribute
     */
    @Override
    public boolean isSetCustLinFactNeighborX() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[26]) != null;
        }
    }

    /**
     * Sets the "custLinFactNeighborX" attribute
     */
    @Override
    public void setCustLinFactNeighborX(java.lang.Object custLinFactNeighborX) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.setObjectValue(custLinFactNeighborX);
        }
    }

    /**
     * Sets (as xml) the "custLinFactNeighborX" attribute
     */
    @Override
    public void xsetCustLinFactNeighborX(org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal custLinFactNeighborX) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.set(custLinFactNeighborX);
        }
    }

    /**
     * Unsets the "custLinFactNeighborX" attribute
     */
    @Override
    public void unsetCustLinFactNeighborX() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[26]);
        }
    }

    /**
     * Gets the "custLinFactNeighborY" attribute
     */
    @Override
    public java.lang.Object getCustLinFactNeighborY() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "custLinFactNeighborY" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal xgetCustLinFactNeighborY() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            return target;
        }
    }

    /**
     * True if has "custLinFactNeighborY" attribute
     */
    @Override
    public boolean isSetCustLinFactNeighborY() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[27]) != null;
        }
    }

    /**
     * Sets the "custLinFactNeighborY" attribute
     */
    @Override
    public void setCustLinFactNeighborY(java.lang.Object custLinFactNeighborY) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[27]);
            }
            target.setObjectValue(custLinFactNeighborY);
        }
    }

    /**
     * Sets (as xml) the "custLinFactNeighborY" attribute
     */
    @Override
    public void xsetCustLinFactNeighborY(org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal custLinFactNeighborY) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[27]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().add_attribute_user(PROPERTY_QNAME[27]);
            }
            target.set(custLinFactNeighborY);
        }
    }

    /**
     * Unsets the "custLinFactNeighborY" attribute
     */
    @Override
    public void unsetCustLinFactNeighborY() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[27]);
        }
    }

    /**
     * Gets the "custRadScaleRad" attribute
     */
    @Override
    public java.lang.Object getCustRadScaleRad() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "custRadScaleRad" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal xgetCustRadScaleRad() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            return target;
        }
    }

    /**
     * True if has "custRadScaleRad" attribute
     */
    @Override
    public boolean isSetCustRadScaleRad() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[28]) != null;
        }
    }

    /**
     * Sets the "custRadScaleRad" attribute
     */
    @Override
    public void setCustRadScaleRad(java.lang.Object custRadScaleRad) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[28]);
            }
            target.setObjectValue(custRadScaleRad);
        }
    }

    /**
     * Sets (as xml) the "custRadScaleRad" attribute
     */
    @Override
    public void xsetCustRadScaleRad(org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal custRadScaleRad) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[28]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().add_attribute_user(PROPERTY_QNAME[28]);
            }
            target.set(custRadScaleRad);
        }
    }

    /**
     * Unsets the "custRadScaleRad" attribute
     */
    @Override
    public void unsetCustRadScaleRad() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[28]);
        }
    }

    /**
     * Gets the "custRadScaleInc" attribute
     */
    @Override
    public java.lang.Object getCustRadScaleInc() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "custRadScaleInc" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal xgetCustRadScaleInc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            return target;
        }
    }

    /**
     * True if has "custRadScaleInc" attribute
     */
    @Override
    public boolean isSetCustRadScaleInc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[29]) != null;
        }
    }

    /**
     * Sets the "custRadScaleInc" attribute
     */
    @Override
    public void setCustRadScaleInc(java.lang.Object custRadScaleInc) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[29]);
            }
            target.setObjectValue(custRadScaleInc);
        }
    }

    /**
     * Sets (as xml) the "custRadScaleInc" attribute
     */
    @Override
    public void xsetCustRadScaleInc(org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal custRadScaleInc) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().find_attribute_user(PROPERTY_QNAME[29]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STPrSetCustVal)get_store().add_attribute_user(PROPERTY_QNAME[29]);
            }
            target.set(custRadScaleInc);
        }
    }

    /**
     * Unsets the "custRadScaleInc" attribute
     */
    @Override
    public void unsetCustRadScaleInc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[29]);
        }
    }
}

/*
 * XML Type:  CT_TLAnimateMotionBehavior
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLAnimateMotionBehavior(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLAnimateMotionBehaviorImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateMotionBehavior {
    private static final long serialVersionUID = 1L;

    public CTTLAnimateMotionBehaviorImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cBhvr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "by"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "from"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "to"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "rCtr"),
        new QName("", "origin"),
        new QName("", "path"),
        new QName("", "pathEditMode"),
        new QName("", "rAng"),
        new QName("", "ptsTypes"),
    };


    /**
     * Gets the "cBhvr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData getCBhvr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cBhvr" element
     */
    @Override
    public void setCBhvr(org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData cBhvr) {
        generatedSetterHelperImpl(cBhvr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cBhvr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData addNewCBhvr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "by" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint getBy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "by" element
     */
    @Override
    public boolean isSetBy() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "by" element
     */
    @Override
    public void setBy(org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint by) {
        generatedSetterHelperImpl(by, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "by" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint addNewBy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "by" element
     */
    @Override
    public void unsetBy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "from" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint getFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "from" element
     */
    @Override
    public boolean isSetFrom() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "from" element
     */
    @Override
    public void setFrom(org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint from) {
        generatedSetterHelperImpl(from, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "from" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint addNewFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "from" element
     */
    @Override
    public void unsetFrom() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "to" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint getTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "to" element
     */
    @Override
    public boolean isSetTo() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "to" element
     */
    @Override
    public void setTo(org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint to) {
        generatedSetterHelperImpl(to, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "to" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint addNewTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "to" element
     */
    @Override
    public void unsetTo() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "rCtr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint getRCtr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rCtr" element
     */
    @Override
    public boolean isSetRCtr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "rCtr" element
     */
    @Override
    public void setRCtr(org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint rCtr) {
        generatedSetterHelperImpl(rCtr, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rCtr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint addNewRCtr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLPoint)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "rCtr" element
     */
    @Override
    public void unsetRCtr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "origin" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionBehaviorOrigin.Enum getOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionBehaviorOrigin.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "origin" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionBehaviorOrigin xgetOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionBehaviorOrigin target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionBehaviorOrigin)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "origin" attribute
     */
    @Override
    public boolean isSetOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "origin" attribute
     */
    @Override
    public void setOrigin(org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionBehaviorOrigin.Enum origin) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(origin);
        }
    }

    /**
     * Sets (as xml) the "origin" attribute
     */
    @Override
    public void xsetOrigin(org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionBehaviorOrigin origin) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionBehaviorOrigin target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionBehaviorOrigin)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionBehaviorOrigin)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(origin);
        }
    }

    /**
     * Unsets the "origin" attribute
     */
    @Override
    public void unsetOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "path" attribute
     */
    @Override
    public java.lang.String getPath() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "path" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetPath() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "path" attribute
     */
    @Override
    public boolean isSetPath() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "path" attribute
     */
    @Override
    public void setPath(java.lang.String path) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setStringValue(path);
        }
    }

    /**
     * Sets (as xml) the "path" attribute
     */
    @Override
    public void xsetPath(org.apache.xmlbeans.XmlString path) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(path);
        }
    }

    /**
     * Unsets the "path" attribute
     */
    @Override
    public void unsetPath() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "pathEditMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionPathEditMode.Enum getPathEditMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionPathEditMode.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "pathEditMode" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionPathEditMode xgetPathEditMode() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionPathEditMode target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionPathEditMode)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * True if has "pathEditMode" attribute
     */
    @Override
    public boolean isSetPathEditMode() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "pathEditMode" attribute
     */
    @Override
    public void setPathEditMode(org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionPathEditMode.Enum pathEditMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setEnumValue(pathEditMode);
        }
    }

    /**
     * Sets (as xml) the "pathEditMode" attribute
     */
    @Override
    public void xsetPathEditMode(org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionPathEditMode pathEditMode) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionPathEditMode target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionPathEditMode)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateMotionPathEditMode)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(pathEditMode);
        }
    }

    /**
     * Unsets the "pathEditMode" attribute
     */
    @Override
    public void unsetPathEditMode() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "rAng" attribute
     */
    @Override
    public int getRAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "rAng" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAngle xgetRAng() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAngle)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * True if has "rAng" attribute
     */
    @Override
    public boolean isSetRAng() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "rAng" attribute
     */
    @Override
    public void setRAng(int rAng) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setIntValue(rAng);
        }
    }

    /**
     * Sets (as xml) the "rAng" attribute
     */
    @Override
    public void xsetRAng(org.openxmlformats.schemas.drawingml.x2006.main.STAngle rAng) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAngle)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAngle)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(rAng);
        }
    }

    /**
     * Unsets the "rAng" attribute
     */
    @Override
    public void unsetRAng() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "ptsTypes" attribute
     */
    @Override
    public java.lang.String getPtsTypes() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "ptsTypes" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetPtsTypes() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * True if has "ptsTypes" attribute
     */
    @Override
    public boolean isSetPtsTypes() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "ptsTypes" attribute
     */
    @Override
    public void setPtsTypes(java.lang.String ptsTypes) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setStringValue(ptsTypes);
        }
    }

    /**
     * Sets (as xml) the "ptsTypes" attribute
     */
    @Override
    public void xsetPtsTypes(org.apache.xmlbeans.XmlString ptsTypes) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(ptsTypes);
        }
    }

    /**
     * Unsets the "ptsTypes" attribute
     */
    @Override
    public void unsetPtsTypes() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }
}

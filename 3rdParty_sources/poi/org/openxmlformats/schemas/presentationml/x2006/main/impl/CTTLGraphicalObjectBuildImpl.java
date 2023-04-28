/*
 * XML Type:  CT_TLGraphicalObjectBuild
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLGraphicalObjectBuild(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLGraphicalObjectBuildImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild {
    private static final long serialVersionUID = 1L;

    public CTTLGraphicalObjectBuildImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "bldAsOne"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "bldSub"),
        new QName("", "spid"),
        new QName("", "grpId"),
        new QName("", "uiExpand"),
    };


    /**
     * Gets the "bldAsOne" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty getBldAsOne() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bldAsOne" element
     */
    @Override
    public boolean isSetBldAsOne() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "bldAsOne" element
     */
    @Override
    public void setBldAsOne(org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty bldAsOne) {
        generatedSetterHelperImpl(bldAsOne, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bldAsOne" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty addNewBldAsOne() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "bldAsOne" element
     */
    @Override
    public void unsetBldAsOne() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "bldSub" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationGraphicalObjectBuildProperties getBldSub() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationGraphicalObjectBuildProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationGraphicalObjectBuildProperties)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bldSub" element
     */
    @Override
    public boolean isSetBldSub() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "bldSub" element
     */
    @Override
    public void setBldSub(org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationGraphicalObjectBuildProperties bldSub) {
        generatedSetterHelperImpl(bldSub, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bldSub" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationGraphicalObjectBuildProperties addNewBldSub() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationGraphicalObjectBuildProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationGraphicalObjectBuildProperties)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "bldSub" element
     */
    @Override
    public void unsetBldSub() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "spid" attribute
     */
    @Override
    public long getSpid() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "spid" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId xgetSpid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "spid" attribute
     */
    @Override
    public void setSpid(long spid) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setLongValue(spid);
        }
    }

    /**
     * Sets (as xml) the "spid" attribute
     */
    @Override
    public void xsetSpid(org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId spid) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(spid);
        }
    }

    /**
     * Gets the "grpId" attribute
     */
    @Override
    public long getGrpId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "grpId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetGrpId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Sets the "grpId" attribute
     */
    @Override
    public void setGrpId(long grpId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setLongValue(grpId);
        }
    }

    /**
     * Sets (as xml) the "grpId" attribute
     */
    @Override
    public void xsetGrpId(org.apache.xmlbeans.XmlUnsignedInt grpId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(grpId);
        }
    }

    /**
     * Gets the "uiExpand" attribute
     */
    @Override
    public boolean getUiExpand() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "uiExpand" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetUiExpand() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[4]);
            }
            return target;
        }
    }

    /**
     * True if has "uiExpand" attribute
     */
    @Override
    public boolean isSetUiExpand() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "uiExpand" attribute
     */
    @Override
    public void setUiExpand(boolean uiExpand) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setBooleanValue(uiExpand);
        }
    }

    /**
     * Sets (as xml) the "uiExpand" attribute
     */
    @Override
    public void xsetUiExpand(org.apache.xmlbeans.XmlBoolean uiExpand) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(uiExpand);
        }
    }

    /**
     * Unsets the "uiExpand" attribute
     */
    @Override
    public void unsetUiExpand() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }
}

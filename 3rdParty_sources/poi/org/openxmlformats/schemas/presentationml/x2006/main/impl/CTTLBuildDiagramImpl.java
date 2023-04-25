/*
 * XML Type:  CT_TLBuildDiagram
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLBuildDiagram(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLBuildDiagramImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLBuildDiagram {
    private static final long serialVersionUID = 1L;

    public CTTLBuildDiagramImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("", "spid"),
        new QName("", "grpId"),
        new QName("", "uiExpand"),
        new QName("", "bld"),
    };


    /**
     * Gets the "spid" attribute
     */
    @Override
    public long getSpid() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId)get_store().find_attribute_user(PROPERTY_QNAME[0]);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId)get_store().add_attribute_user(PROPERTY_QNAME[0]);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
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
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
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
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[1]);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
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
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[2]);
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
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
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
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[2]);
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
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "bld" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLDiagramBuildType.Enum getBld() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLDiagramBuildType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "bld" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLDiagramBuildType xgetBld() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLDiagramBuildType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLDiagramBuildType)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLDiagramBuildType)get_default_attribute_value(PROPERTY_QNAME[3]);
            }
            return target;
        }
    }

    /**
     * True if has "bld" attribute
     */
    @Override
    public boolean isSetBld() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "bld" attribute
     */
    @Override
    public void setBld(org.openxmlformats.schemas.presentationml.x2006.main.STTLDiagramBuildType.Enum bld) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setEnumValue(bld);
        }
    }

    /**
     * Sets (as xml) the "bld" attribute
     */
    @Override
    public void xsetBld(org.openxmlformats.schemas.presentationml.x2006.main.STTLDiagramBuildType bld) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLDiagramBuildType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLDiagramBuildType)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLDiagramBuildType)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(bld);
        }
    }

    /**
     * Unsets the "bld" attribute
     */
    @Override
    public void unsetBld() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}

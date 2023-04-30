/*
 * XML Type:  CT_Cxn
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Cxn(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTCxnImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxn {
    private static final long serialVersionUID = 1L;

    public CTCxnImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "extLst"),
        new QName("", "modelId"),
        new QName("", "type"),
        new QName("", "srcId"),
        new QName("", "destId"),
        new QName("", "srcOrd"),
        new QName("", "destOrd"),
        new QName("", "parTransId"),
        new QName("", "sibTransId"),
        new QName("", "presId"),
    };


    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[0]);
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
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "modelId" attribute
     */
    @Override
    public java.lang.Object getModelId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "modelId" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId xgetModelId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "modelId" attribute
     */
    @Override
    public void setModelId(java.lang.Object modelId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(modelId);
        }
    }

    /**
     * Sets (as xml) the "modelId" attribute
     */
    @Override
    public void xsetModelId(org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId modelId) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(modelId);
        }
    }

    /**
     * Gets the "type" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STCxnType.Enum getType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.diagram.STCxnType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "type" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STCxnType xgetType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STCxnType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STCxnType)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STCxnType)get_default_attribute_value(PROPERTY_QNAME[2]);
            }
            return target;
        }
    }

    /**
     * True if has "type" attribute
     */
    @Override
    public boolean isSetType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "type" attribute
     */
    @Override
    public void setType(org.openxmlformats.schemas.drawingml.x2006.diagram.STCxnType.Enum type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(type);
        }
    }

    /**
     * Sets (as xml) the "type" attribute
     */
    @Override
    public void xsetType(org.openxmlformats.schemas.drawingml.x2006.diagram.STCxnType type) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STCxnType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STCxnType)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STCxnType)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(type);
        }
    }

    /**
     * Unsets the "type" attribute
     */
    @Override
    public void unsetType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "srcId" attribute
     */
    @Override
    public java.lang.Object getSrcId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "srcId" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId xgetSrcId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Sets the "srcId" attribute
     */
    @Override
    public void setSrcId(java.lang.Object srcId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setObjectValue(srcId);
        }
    }

    /**
     * Sets (as xml) the "srcId" attribute
     */
    @Override
    public void xsetSrcId(org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId srcId) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(srcId);
        }
    }

    /**
     * Gets the "destId" attribute
     */
    @Override
    public java.lang.Object getDestId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "destId" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId xgetDestId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Sets the "destId" attribute
     */
    @Override
    public void setDestId(java.lang.Object destId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setObjectValue(destId);
        }
    }

    /**
     * Sets (as xml) the "destId" attribute
     */
    @Override
    public void xsetDestId(org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId destId) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(destId);
        }
    }

    /**
     * Gets the "srcOrd" attribute
     */
    @Override
    public long getSrcOrd() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "srcOrd" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetSrcOrd() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Sets the "srcOrd" attribute
     */
    @Override
    public void setSrcOrd(long srcOrd) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setLongValue(srcOrd);
        }
    }

    /**
     * Sets (as xml) the "srcOrd" attribute
     */
    @Override
    public void xsetSrcOrd(org.apache.xmlbeans.XmlUnsignedInt srcOrd) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(srcOrd);
        }
    }

    /**
     * Gets the "destOrd" attribute
     */
    @Override
    public long getDestOrd() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "destOrd" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetDestOrd() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Sets the "destOrd" attribute
     */
    @Override
    public void setDestOrd(long destOrd) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setLongValue(destOrd);
        }
    }

    /**
     * Sets (as xml) the "destOrd" attribute
     */
    @Override
    public void xsetDestOrd(org.apache.xmlbeans.XmlUnsignedInt destOrd) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(destOrd);
        }
    }

    /**
     * Gets the "parTransId" attribute
     */
    @Override
    public java.lang.Object getParTransId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[7]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "parTransId" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId xgetParTransId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_default_attribute_value(PROPERTY_QNAME[7]);
            }
            return target;
        }
    }

    /**
     * True if has "parTransId" attribute
     */
    @Override
    public boolean isSetParTransId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "parTransId" attribute
     */
    @Override
    public void setParTransId(java.lang.Object parTransId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setObjectValue(parTransId);
        }
    }

    /**
     * Sets (as xml) the "parTransId" attribute
     */
    @Override
    public void xsetParTransId(org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId parTransId) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(parTransId);
        }
    }

    /**
     * Unsets the "parTransId" attribute
     */
    @Override
    public void unsetParTransId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "sibTransId" attribute
     */
    @Override
    public java.lang.Object getSibTransId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "sibTransId" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId xgetSibTransId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return target;
        }
    }

    /**
     * True if has "sibTransId" attribute
     */
    @Override
    public boolean isSetSibTransId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "sibTransId" attribute
     */
    @Override
    public void setSibTransId(java.lang.Object sibTransId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setObjectValue(sibTransId);
        }
    }

    /**
     * Sets (as xml) the "sibTransId" attribute
     */
    @Override
    public void xsetSibTransId(org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId sibTransId) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.diagram.STModelId)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(sibTransId);
        }
    }

    /**
     * Unsets the "sibTransId" attribute
     */
    @Override
    public void unsetSibTransId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "presId" attribute
     */
    @Override
    public java.lang.String getPresId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "presId" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetPresId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return target;
        }
    }

    /**
     * True if has "presId" attribute
     */
    @Override
    public boolean isSetPresId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "presId" attribute
     */
    @Override
    public void setPresId(java.lang.String presId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setStringValue(presId);
        }
    }

    /**
     * Sets (as xml) the "presId" attribute
     */
    @Override
    public void xsetPresId(org.apache.xmlbeans.XmlString presId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(presId);
        }
    }

    /**
     * Unsets the "presId" attribute
     */
    @Override
    public void unsetPresId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }
}

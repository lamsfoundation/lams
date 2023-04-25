/*
 * XML Type:  CT_TLCommonBehaviorData
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLCommonBehaviorData(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLCommonBehaviorDataImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData {
    private static final long serialVersionUID = 1L;

    public CTTLCommonBehaviorDataImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cTn"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "tgtEl"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "attrNameLst"),
        new QName("", "additive"),
        new QName("", "accumulate"),
        new QName("", "xfrmType"),
        new QName("", "from"),
        new QName("", "to"),
        new QName("", "by"),
        new QName("", "rctx"),
        new QName("", "override"),
    };


    /**
     * Gets the "cTn" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData getCTn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cTn" element
     */
    @Override
    public void setCTn(org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData cTn) {
        generatedSetterHelperImpl(cTn, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cTn" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData addNewCTn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonTimeNodeData)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "tgtEl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement getTgtEl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "tgtEl" element
     */
    @Override
    public void setTgtEl(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement tgtEl) {
        generatedSetterHelperImpl(tgtEl, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tgtEl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement addNewTgtEl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "attrNameLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLBehaviorAttributeNameList getAttrNameLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLBehaviorAttributeNameList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLBehaviorAttributeNameList)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "attrNameLst" element
     */
    @Override
    public boolean isSetAttrNameLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "attrNameLst" element
     */
    @Override
    public void setAttrNameLst(org.openxmlformats.schemas.presentationml.x2006.main.CTTLBehaviorAttributeNameList attrNameLst) {
        generatedSetterHelperImpl(attrNameLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "attrNameLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLBehaviorAttributeNameList addNewAttrNameLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLBehaviorAttributeNameList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLBehaviorAttributeNameList)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "attrNameLst" element
     */
    @Override
    public void unsetAttrNameLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "additive" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAdditiveType.Enum getAdditive() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAdditiveType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "additive" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAdditiveType xgetAdditive() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAdditiveType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAdditiveType)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "additive" attribute
     */
    @Override
    public boolean isSetAdditive() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "additive" attribute
     */
    @Override
    public void setAdditive(org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAdditiveType.Enum additive) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setEnumValue(additive);
        }
    }

    /**
     * Sets (as xml) the "additive" attribute
     */
    @Override
    public void xsetAdditive(org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAdditiveType additive) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAdditiveType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAdditiveType)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAdditiveType)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(additive);
        }
    }

    /**
     * Unsets the "additive" attribute
     */
    @Override
    public void unsetAdditive() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "accumulate" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAccumulateType.Enum getAccumulate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAccumulateType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "accumulate" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAccumulateType xgetAccumulate() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAccumulateType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAccumulateType)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "accumulate" attribute
     */
    @Override
    public boolean isSetAccumulate() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "accumulate" attribute
     */
    @Override
    public void setAccumulate(org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAccumulateType.Enum accumulate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setEnumValue(accumulate);
        }
    }

    /**
     * Sets (as xml) the "accumulate" attribute
     */
    @Override
    public void xsetAccumulate(org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAccumulateType accumulate) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAccumulateType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAccumulateType)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorAccumulateType)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(accumulate);
        }
    }

    /**
     * Unsets the "accumulate" attribute
     */
    @Override
    public void unsetAccumulate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Gets the "xfrmType" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorTransformType.Enum getXfrmType() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorTransformType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "xfrmType" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorTransformType xgetXfrmType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorTransformType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorTransformType)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "xfrmType" attribute
     */
    @Override
    public boolean isSetXfrmType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "xfrmType" attribute
     */
    @Override
    public void setXfrmType(org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorTransformType.Enum xfrmType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setEnumValue(xfrmType);
        }
    }

    /**
     * Sets (as xml) the "xfrmType" attribute
     */
    @Override
    public void xsetXfrmType(org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorTransformType xfrmType) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorTransformType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorTransformType)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorTransformType)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(xfrmType);
        }
    }

    /**
     * Unsets the "xfrmType" attribute
     */
    @Override
    public void unsetXfrmType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "from" attribute
     */
    @Override
    public java.lang.String getFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "from" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "from" attribute
     */
    @Override
    public boolean isSetFrom() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "from" attribute
     */
    @Override
    public void setFrom(java.lang.String from) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setStringValue(from);
        }
    }

    /**
     * Sets (as xml) the "from" attribute
     */
    @Override
    public void xsetFrom(org.apache.xmlbeans.XmlString from) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(from);
        }
    }

    /**
     * Unsets the "from" attribute
     */
    @Override
    public void unsetFrom() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "to" attribute
     */
    @Override
    public java.lang.String getTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "to" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * True if has "to" attribute
     */
    @Override
    public boolean isSetTo() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "to" attribute
     */
    @Override
    public void setTo(java.lang.String to) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setStringValue(to);
        }
    }

    /**
     * Sets (as xml) the "to" attribute
     */
    @Override
    public void xsetTo(org.apache.xmlbeans.XmlString to) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(to);
        }
    }

    /**
     * Unsets the "to" attribute
     */
    @Override
    public void unsetTo() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "by" attribute
     */
    @Override
    public java.lang.String getBy() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "by" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetBy() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * True if has "by" attribute
     */
    @Override
    public boolean isSetBy() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "by" attribute
     */
    @Override
    public void setBy(java.lang.String by) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setStringValue(by);
        }
    }

    /**
     * Sets (as xml) the "by" attribute
     */
    @Override
    public void xsetBy(org.apache.xmlbeans.XmlString by) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(by);
        }
    }

    /**
     * Unsets the "by" attribute
     */
    @Override
    public void unsetBy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "rctx" attribute
     */
    @Override
    public java.lang.String getRctx() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "rctx" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetRctx() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * True if has "rctx" attribute
     */
    @Override
    public boolean isSetRctx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "rctx" attribute
     */
    @Override
    public void setRctx(java.lang.String rctx) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setStringValue(rctx);
        }
    }

    /**
     * Sets (as xml) the "rctx" attribute
     */
    @Override
    public void xsetRctx(org.apache.xmlbeans.XmlString rctx) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(rctx);
        }
    }

    /**
     * Unsets the "rctx" attribute
     */
    @Override
    public void unsetRctx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "override" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorOverrideType.Enum getOverride() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorOverrideType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "override" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorOverrideType xgetOverride() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorOverrideType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorOverrideType)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * True if has "override" attribute
     */
    @Override
    public boolean isSetOverride() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "override" attribute
     */
    @Override
    public void setOverride(org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorOverrideType.Enum override) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setEnumValue(override);
        }
    }

    /**
     * Sets (as xml) the "override" attribute
     */
    @Override
    public void xsetOverride(org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorOverrideType override) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorOverrideType target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorOverrideType)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTLBehaviorOverrideType)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(override);
        }
    }

    /**
     * Unsets the "override" attribute
     */
    @Override
    public void unsetOverride() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }
}

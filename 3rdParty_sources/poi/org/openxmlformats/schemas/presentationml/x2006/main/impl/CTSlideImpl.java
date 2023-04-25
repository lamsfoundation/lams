/*
 * XML Type:  CT_Slide
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlide
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Slide(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTSlideImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTSlide {
    private static final long serialVersionUID = 1L;

    public CTSlideImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cSld"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "clrMapOvr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "transition"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "timing"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "extLst"),
        new QName("", "showMasterSp"),
        new QName("", "showMasterPhAnim"),
        new QName("", "show"),
    };


    /**
     * Gets the "cSld" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData getCSld() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "cSld" element
     */
    @Override
    public void setCSld(org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData cSld) {
        generatedSetterHelperImpl(cSld, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cSld" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData addNewCSld() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "clrMapOvr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride getClrMapOvr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "clrMapOvr" element
     */
    @Override
    public boolean isSetClrMapOvr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "clrMapOvr" element
     */
    @Override
    public void setClrMapOvr(org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride clrMapOvr) {
        generatedSetterHelperImpl(clrMapOvr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "clrMapOvr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride addNewClrMapOvr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "clrMapOvr" element
     */
    @Override
    public void unsetClrMapOvr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "transition" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition getTransition() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "transition" element
     */
    @Override
    public boolean isSetTransition() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "transition" element
     */
    @Override
    public void setTransition(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition transition) {
        generatedSetterHelperImpl(transition, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "transition" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition addNewTransition() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "transition" element
     */
    @Override
    public void unsetTransition() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "timing" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming getTiming() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "timing" element
     */
    @Override
    public boolean isSetTiming() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "timing" element
     */
    @Override
    public void setTiming(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming timing) {
        generatedSetterHelperImpl(timing, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "timing" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming addNewTiming() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "timing" element
     */
    @Override
    public void unsetTiming() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify)get_store().find_element_user(PROPERTY_QNAME[4], 0);
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
    public void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify)get_store().add_element_user(PROPERTY_QNAME[4]);
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

    /**
     * Gets the "showMasterSp" attribute
     */
    @Override
    public boolean getShowMasterSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showMasterSp" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowMasterSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[5]);
            }
            return target;
        }
    }

    /**
     * True if has "showMasterSp" attribute
     */
    @Override
    public boolean isSetShowMasterSp() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "showMasterSp" attribute
     */
    @Override
    public void setShowMasterSp(boolean showMasterSp) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setBooleanValue(showMasterSp);
        }
    }

    /**
     * Sets (as xml) the "showMasterSp" attribute
     */
    @Override
    public void xsetShowMasterSp(org.apache.xmlbeans.XmlBoolean showMasterSp) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(showMasterSp);
        }
    }

    /**
     * Unsets the "showMasterSp" attribute
     */
    @Override
    public void unsetShowMasterSp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "showMasterPhAnim" attribute
     */
    @Override
    public boolean getShowMasterPhAnim() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "showMasterPhAnim" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShowMasterPhAnim() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[6]);
            }
            return target;
        }
    }

    /**
     * True if has "showMasterPhAnim" attribute
     */
    @Override
    public boolean isSetShowMasterPhAnim() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "showMasterPhAnim" attribute
     */
    @Override
    public void setShowMasterPhAnim(boolean showMasterPhAnim) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setBooleanValue(showMasterPhAnim);
        }
    }

    /**
     * Sets (as xml) the "showMasterPhAnim" attribute
     */
    @Override
    public void xsetShowMasterPhAnim(org.apache.xmlbeans.XmlBoolean showMasterPhAnim) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(showMasterPhAnim);
        }
    }

    /**
     * Unsets the "showMasterPhAnim" attribute
     */
    @Override
    public void unsetShowMasterPhAnim() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "show" attribute
     */
    @Override
    public boolean getShow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[7]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "show" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetShow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[7]);
            }
            return target;
        }
    }

    /**
     * True if has "show" attribute
     */
    @Override
    public boolean isSetShow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "show" attribute
     */
    @Override
    public void setShow(boolean show) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setBooleanValue(show);
        }
    }

    /**
     * Sets (as xml) the "show" attribute
     */
    @Override
    public void xsetShow(org.apache.xmlbeans.XmlBoolean show) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(show);
        }
    }

    /**
     * Unsets the "show" attribute
     */
    @Override
    public void unsetShow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }
}

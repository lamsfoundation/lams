/*
 * XML Type:  CT_SlideMaster
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SlideMaster(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTSlideMasterImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMaster {
    private static final long serialVersionUID = 1L;

    public CTSlideMasterImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cSld"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "clrMap"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sldLayoutIdLst"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "transition"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "timing"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "hf"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "txStyles"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "extLst"),
        new QName("", "preserve"),
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
     * Gets the "clrMap" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping getClrMap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "clrMap" element
     */
    @Override
    public void setClrMap(org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping clrMap) {
        generatedSetterHelperImpl(clrMap, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "clrMap" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping addNewClrMap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "sldLayoutIdLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdList getSldLayoutIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdList)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sldLayoutIdLst" element
     */
    @Override
    public boolean isSetSldLayoutIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "sldLayoutIdLst" element
     */
    @Override
    public void setSldLayoutIdLst(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdList sldLayoutIdLst) {
        generatedSetterHelperImpl(sldLayoutIdLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sldLayoutIdLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdList addNewSldLayoutIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideLayoutIdList)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "sldLayoutIdLst" element
     */
    @Override
    public void unsetSldLayoutIdLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
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
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "transition" element
     */
    @Override
    public void setTransition(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition transition) {
        generatedSetterHelperImpl(transition, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "transition" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition addNewTransition() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], 0);
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
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming)get_store().find_element_user(PROPERTY_QNAME[4], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "timing" element
     */
    @Override
    public void setTiming(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming timing) {
        generatedSetterHelperImpl(timing, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "timing" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming addNewTiming() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "hf" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter getHf() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hf" element
     */
    @Override
    public boolean isSetHf() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "hf" element
     */
    @Override
    public void setHf(org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter hf) {
        generatedSetterHelperImpl(hf, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hf" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter addNewHf() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "hf" element
     */
    @Override
    public void unsetHf() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "txStyles" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles getTxStyles() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "txStyles" element
     */
    @Override
    public boolean isSetTxStyles() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "txStyles" element
     */
    @Override
    public void setTxStyles(org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles txStyles) {
        generatedSetterHelperImpl(txStyles, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "txStyles" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles addNewTxStyles() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSlideMasterTextStyles)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "txStyles" element
     */
    @Override
    public void unsetTxStyles() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
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
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify)get_store().find_element_user(PROPERTY_QNAME[7], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify)get_store().add_element_user(PROPERTY_QNAME[7]);
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
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "preserve" attribute
     */
    @Override
    public boolean getPreserve() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "preserve" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetPreserve() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[8]);
            }
            return target;
        }
    }

    /**
     * True if has "preserve" attribute
     */
    @Override
    public boolean isSetPreserve() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "preserve" attribute
     */
    @Override
    public void setPreserve(boolean preserve) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setBooleanValue(preserve);
        }
    }

    /**
     * Sets (as xml) the "preserve" attribute
     */
    @Override
    public void xsetPreserve(org.apache.xmlbeans.XmlBoolean preserve) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(preserve);
        }
    }

    /**
     * Unsets the "preserve" attribute
     */
    @Override
    public void unsetPreserve() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }
}

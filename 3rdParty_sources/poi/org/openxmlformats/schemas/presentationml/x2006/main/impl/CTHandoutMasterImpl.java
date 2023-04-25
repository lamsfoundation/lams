/*
 * XML Type:  CT_HandoutMaster
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMaster
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_HandoutMaster(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTHandoutMasterImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMaster {
    private static final long serialVersionUID = 1L;

    public CTHandoutMasterImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cSld"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "clrMap"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "hf"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "extLst"),
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
     * Gets the "hf" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter getHf() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter)get_store().find_element_user(PROPERTY_QNAME[2], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "hf" element
     */
    @Override
    public void setHf(org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter hf) {
        generatedSetterHelperImpl(hf, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hf" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter addNewHf() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter)get_store().add_element_user(PROPERTY_QNAME[2]);
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
            get_store().remove_element(PROPERTY_QNAME[2], 0);
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
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify)get_store().add_element_user(PROPERTY_QNAME[3]);
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
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}

/*
 * XML Type:  CT_SdtCell
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SdtCell(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTSdtCellImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtCell {
    private static final long serialVersionUID = 1L;

    public CTSdtCellImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sdtPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sdtEndPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "sdtContent"),
    };


    /**
     * Gets the "sdtPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr getSdtPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sdtPr" element
     */
    @Override
    public boolean isSetSdtPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "sdtPr" element
     */
    @Override
    public void setSdtPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr sdtPr) {
        generatedSetterHelperImpl(sdtPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sdtPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr addNewSdtPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "sdtPr" element
     */
    @Override
    public void unsetSdtPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "sdtEndPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtEndPr getSdtEndPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtEndPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtEndPr)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sdtEndPr" element
     */
    @Override
    public boolean isSetSdtEndPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "sdtEndPr" element
     */
    @Override
    public void setSdtEndPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtEndPr sdtEndPr) {
        generatedSetterHelperImpl(sdtEndPr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sdtEndPr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtEndPr addNewSdtEndPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtEndPr target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtEndPr)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "sdtEndPr" element
     */
    @Override
    public void unsetSdtEndPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "sdtContent" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentCell getSdtContent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentCell target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentCell)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sdtContent" element
     */
    @Override
    public boolean isSetSdtContent() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "sdtContent" element
     */
    @Override
    public void setSdtContent(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentCell sdtContent) {
        generatedSetterHelperImpl(sdtContent, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sdtContent" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentCell addNewSdtContent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentCell target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentCell)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "sdtContent" element
     */
    @Override
    public void unsetSdtContent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }
}

/*
 * XML Type:  CT_PresentationProperties
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTPresentationProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_PresentationProperties(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTPresentationPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTPresentationProperties {
    private static final long serialVersionUID = 1L;

    public CTPresentationPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "htmlPubPr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "webPr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "prnPr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "showPr"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "clrMru"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "extLst"),
    };


    /**
     * Gets the "htmlPubPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTHtmlPublishProperties getHtmlPubPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTHtmlPublishProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTHtmlPublishProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "htmlPubPr" element
     */
    @Override
    public boolean isSetHtmlPubPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "htmlPubPr" element
     */
    @Override
    public void setHtmlPubPr(org.openxmlformats.schemas.presentationml.x2006.main.CTHtmlPublishProperties htmlPubPr) {
        generatedSetterHelperImpl(htmlPubPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "htmlPubPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTHtmlPublishProperties addNewHtmlPubPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTHtmlPublishProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTHtmlPublishProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "htmlPubPr" element
     */
    @Override
    public void unsetHtmlPubPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "webPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTWebProperties getWebPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTWebProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTWebProperties)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "webPr" element
     */
    @Override
    public boolean isSetWebPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "webPr" element
     */
    @Override
    public void setWebPr(org.openxmlformats.schemas.presentationml.x2006.main.CTWebProperties webPr) {
        generatedSetterHelperImpl(webPr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "webPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTWebProperties addNewWebPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTWebProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTWebProperties)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "webPr" element
     */
    @Override
    public void unsetWebPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "prnPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTPrintProperties getPrnPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTPrintProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTPrintProperties)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "prnPr" element
     */
    @Override
    public boolean isSetPrnPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "prnPr" element
     */
    @Override
    public void setPrnPr(org.openxmlformats.schemas.presentationml.x2006.main.CTPrintProperties prnPr) {
        generatedSetterHelperImpl(prnPr, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "prnPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTPrintProperties addNewPrnPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTPrintProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTPrintProperties)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "prnPr" element
     */
    @Override
    public void unsetPrnPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "showPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTShowProperties getShowPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTShowProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTShowProperties)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "showPr" element
     */
    @Override
    public boolean isSetShowPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "showPr" element
     */
    @Override
    public void setShowPr(org.openxmlformats.schemas.presentationml.x2006.main.CTShowProperties showPr) {
        generatedSetterHelperImpl(showPr, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "showPr" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTShowProperties addNewShowPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTShowProperties target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTShowProperties)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "showPr" element
     */
    @Override
    public void unsetShowPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "clrMru" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorMRU getClrMru() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorMRU target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorMRU)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "clrMru" element
     */
    @Override
    public boolean isSetClrMru() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "clrMru" element
     */
    @Override
    public void setClrMru(org.openxmlformats.schemas.drawingml.x2006.main.CTColorMRU clrMru) {
        generatedSetterHelperImpl(clrMru, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "clrMru" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorMRU addNewClrMru() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorMRU target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorMRU)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "clrMru" element
     */
    @Override
    public void unsetClrMru() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[5], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[5]);
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
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }
}

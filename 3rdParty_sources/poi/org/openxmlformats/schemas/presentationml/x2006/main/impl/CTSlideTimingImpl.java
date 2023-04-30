/*
 * XML Type:  CT_SlideTiming
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SlideTiming(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTSlideTimingImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTiming {
    private static final long serialVersionUID = 1L;

    public CTSlideTimingImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "tnLst"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "bldLst"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "extLst"),
    };


    /**
     * Gets the "tnLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList getTnLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tnLst" element
     */
    @Override
    public boolean isSetTnLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "tnLst" element
     */
    @Override
    public void setTnLst(org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList tnLst) {
        generatedSetterHelperImpl(tnLst, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tnLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList addNewTnLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTimeNodeList)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "tnLst" element
     */
    @Override
    public void unsetTnLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "bldLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTBuildList getBldLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTBuildList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTBuildList)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bldLst" element
     */
    @Override
    public boolean isSetBldLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "bldLst" element
     */
    @Override
    public void setBldLst(org.openxmlformats.schemas.presentationml.x2006.main.CTBuildList bldLst) {
        generatedSetterHelperImpl(bldLst, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bldLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTBuildList addNewBldLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTBuildList target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTBuildList)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "bldLst" element
     */
    @Override
    public void unsetBldLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
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
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify)get_store().find_element_user(PROPERTY_QNAME[2], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify)get_store().add_element_user(PROPERTY_QNAME[2]);
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
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }
}

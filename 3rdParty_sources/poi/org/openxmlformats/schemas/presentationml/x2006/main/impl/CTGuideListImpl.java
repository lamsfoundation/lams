/*
 * XML Type:  CT_GuideList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTGuideList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_GuideList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTGuideListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTGuideList {
    private static final long serialVersionUID = 1L;

    public CTGuideListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "guide"),
    };


    /**
     * Gets a List of "guide" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTGuide> getGuideList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGuideArray,
                this::setGuideArray,
                this::insertNewGuide,
                this::removeGuide,
                this::sizeOfGuideArray
            );
        }
    }

    /**
     * Gets array of all "guide" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTGuide[] getGuideArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTGuide[0]);
    }

    /**
     * Gets ith "guide" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTGuide getGuideArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTGuide target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTGuide)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "guide" element
     */
    @Override
    public int sizeOfGuideArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "guide" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGuideArray(org.openxmlformats.schemas.presentationml.x2006.main.CTGuide[] guideArray) {
        check_orphaned();
        arraySetterHelper(guideArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "guide" element
     */
    @Override
    public void setGuideArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTGuide guide) {
        generatedSetterHelperImpl(guide, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "guide" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTGuide insertNewGuide(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTGuide target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTGuide)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "guide" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTGuide addNewGuide() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTGuide target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTGuide)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "guide" element
     */
    @Override
    public void removeGuide(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

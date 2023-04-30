/*
 * XML Type:  CT_TagList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTagList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TagList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTagListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTagList {
    private static final long serialVersionUID = 1L;

    public CTTagListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "tag"),
    };


    /**
     * Gets a List of "tag" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag> getTagList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTagArray,
                this::setTagArray,
                this::insertNewTag,
                this::removeTag,
                this::sizeOfTagArray
            );
        }
    }

    /**
     * Gets array of all "tag" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag[] getTagArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag[0]);
    }

    /**
     * Gets ith "tag" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag getTagArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "tag" element
     */
    @Override
    public int sizeOfTagArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "tag" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTagArray(org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag[] tagArray) {
        check_orphaned();
        arraySetterHelper(tagArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "tag" element
     */
    @Override
    public void setTagArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag tag) {
        generatedSetterHelperImpl(tag, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tag" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag insertNewTag(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tag" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag addNewTag() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTStringTag)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "tag" element
     */
    @Override
    public void removeTag(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

/*
 * XML Type:  CT_CustomShowList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShowList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_CustomShowList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTCustomShowListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShowList {
    private static final long serialVersionUID = 1L;

    public CTCustomShowListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "custShow"),
    };


    /**
     * Gets a List of "custShow" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow> getCustShowList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCustShowArray,
                this::setCustShowArray,
                this::insertNewCustShow,
                this::removeCustShow,
                this::sizeOfCustShowArray
            );
        }
    }

    /**
     * Gets array of all "custShow" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow[] getCustShowArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow[0]);
    }

    /**
     * Gets ith "custShow" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow getCustShowArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "custShow" element
     */
    @Override
    public int sizeOfCustShowArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "custShow" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCustShowArray(org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow[] custShowArray) {
        check_orphaned();
        arraySetterHelper(custShowArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "custShow" element
     */
    @Override
    public void setCustShowArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow custShow) {
        generatedSetterHelperImpl(custShow, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "custShow" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow insertNewCustShow(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "custShow" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow addNewCustShow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "custShow" element
     */
    @Override
    public void removeCustShow(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

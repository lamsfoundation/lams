/*
 * XML Type:  CT_Authors
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAuthors
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Authors(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTAuthorsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAuthors {
    private static final long serialVersionUID = 1L;

    public CTAuthorsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "author"),
    };


    /**
     * Gets a List of "author" elements
     */
    @Override
    public java.util.List<java.lang.String> getAuthorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getAuthorArray,
                this::setAuthorArray,
                this::insertAuthor,
                this::removeAuthor,
                this::sizeOfAuthorArray
            );
        }
    }

    /**
     * Gets array of all "author" elements
     */
    @Override
    public java.lang.String[] getAuthorArray() {
        return getObjectArray(PROPERTY_QNAME[0], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "author" element
     */
    @Override
    public java.lang.String getAuthorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "author" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring> xgetAuthorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetAuthorArray,
                this::xsetAuthorArray,
                this::insertNewAuthor,
                this::removeAuthor,
                this::sizeOfAuthorArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "author" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring[] xgetAuthorArray() {
        return xgetArray(PROPERTY_QNAME[0], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring[]::new);
    }

    /**
     * Gets (as xml) ith "author" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetAuthorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "author" element
     */
    @Override
    public int sizeOfAuthorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "author" element
     */
    @Override
    public void setAuthorArray(java.lang.String[] authorArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(authorArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets ith "author" element
     */
    @Override
    public void setAuthorArray(int i, java.lang.String author) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(author);
        }
    }

    /**
     * Sets (as xml) array of all "author" element
     */
    @Override
    public void xsetAuthorArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring[]authorArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(authorArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets (as xml) ith "author" element
     */
    @Override
    public void xsetAuthorArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring author) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(author);
        }
    }

    /**
     * Inserts the value as the ith "author" element
     */
    @Override
    public void insertAuthor(int i, java.lang.String author) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            target.setStringValue(author);
        }
    }

    /**
     * Appends the value as the last "author" element
     */
    @Override
    public void addAuthor(java.lang.String author) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            target.setStringValue(author);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "author" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring insertNewAuthor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "author" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring addNewAuthor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "author" element
     */
    @Override
    public void removeAuthor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

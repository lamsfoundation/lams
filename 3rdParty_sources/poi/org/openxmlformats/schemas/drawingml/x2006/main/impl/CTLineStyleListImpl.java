/*
 * XML Type:  CT_LineStyleList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_LineStyleList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTLineStyleListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList {
    private static final long serialVersionUID = 1L;

    public CTLineStyleListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "ln"),
    };


    /**
     * Gets a List of "ln" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties> getLnList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLnArray,
                this::setLnArray,
                this::insertNewLn,
                this::removeLn,
                this::sizeOfLnArray
            );
        }
    }

    /**
     * Gets array of all "ln" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties[] getLnArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties[0]);
    }

    /**
     * Gets ith "ln" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties getLnArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ln" element
     */
    @Override
    public int sizeOfLnArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "ln" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLnArray(org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties[] lnArray) {
        check_orphaned();
        arraySetterHelper(lnArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "ln" element
     */
    @Override
    public void setLnArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties ln) {
        generatedSetterHelperImpl(ln, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ln" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties insertNewLn(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ln" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties addNewLn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "ln" element
     */
    @Override
    public void removeLn(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

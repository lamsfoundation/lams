/*
 * XML Type:  CT_SDCategories
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategories
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SDCategories(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public class CTSDCategoriesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategories {
    private static final long serialVersionUID = 1L;

    public CTSDCategoriesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/diagram", "cat"),
    };


    /**
     * Gets a List of "cat" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategory> getCatList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCatArray,
                this::setCatArray,
                this::insertNewCat,
                this::removeCat,
                this::sizeOfCatArray
            );
        }
    }

    /**
     * Gets array of all "cat" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategory[] getCatArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategory[0]);
    }

    /**
     * Gets ith "cat" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategory getCatArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategory target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategory)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "cat" element
     */
    @Override
    public int sizeOfCatArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "cat" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCatArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategory[] catArray) {
        check_orphaned();
        arraySetterHelper(catArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "cat" element
     */
    @Override
    public void setCatArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategory cat) {
        generatedSetterHelperImpl(cat, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cat" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategory insertNewCat(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategory target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategory)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "cat" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategory addNewCat() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategory target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.diagram.CTSDCategory)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "cat" element
     */
    @Override
    public void removeCat(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

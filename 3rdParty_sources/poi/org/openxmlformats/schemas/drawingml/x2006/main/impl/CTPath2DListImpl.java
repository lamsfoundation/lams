/*
 * XML Type:  CT_Path2DList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Path2DList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTPath2DListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTPath2DList {
    private static final long serialVersionUID = 1L;

    public CTPath2DListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "path"),
    };


    /**
     * Gets a List of "path" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D> getPathList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPathArray,
                this::setPathArray,
                this::insertNewPath,
                this::removePath,
                this::sizeOfPathArray
            );
        }
    }

    /**
     * Gets array of all "path" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D[] getPathArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D[0]);
    }

    /**
     * Gets ith "path" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D getPathArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "path" element
     */
    @Override
    public int sizeOfPathArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "path" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPathArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D[] pathArray) {
        check_orphaned();
        arraySetterHelper(pathArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "path" element
     */
    @Override
    public void setPathArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D path) {
        generatedSetterHelperImpl(path, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "path" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D insertNewPath(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "path" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D addNewPath() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPath2D)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "path" element
     */
    @Override
    public void removePath(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}

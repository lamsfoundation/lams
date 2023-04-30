/*
 * XML Type:  CT_WrapPath
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWrapPath
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_WrapPath(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public class CTWrapPathImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWrapPath {
    private static final long serialVersionUID = 1L;

    public CTWrapPathImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "start"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "lineTo"),
        new QName("", "edited"),
    };


    /**
     * Gets the "start" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D getStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "start" element
     */
    @Override
    public void setStart(org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D start) {
        generatedSetterHelperImpl(start, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "start" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D addNewStart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets a List of "lineTo" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D> getLineToList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getLineToArray,
                this::setLineToArray,
                this::insertNewLineTo,
                this::removeLineTo,
                this::sizeOfLineToArray
            );
        }
    }

    /**
     * Gets array of all "lineTo" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D[] getLineToArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D[0]);
    }

    /**
     * Gets ith "lineTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D getLineToArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "lineTo" element
     */
    @Override
    public int sizeOfLineToArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "lineTo" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setLineToArray(org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D[] lineToArray) {
        check_orphaned();
        arraySetterHelper(lineToArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "lineTo" element
     */
    @Override
    public void setLineToArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D lineTo) {
        generatedSetterHelperImpl(lineTo, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lineTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D insertNewLineTo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "lineTo" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D addNewLineTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "lineTo" element
     */
    @Override
    public void removeLineTo(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets the "edited" attribute
     */
    @Override
    public boolean getEdited() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "edited" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetEdited() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "edited" attribute
     */
    @Override
    public boolean isSetEdited() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "edited" attribute
     */
    @Override
    public void setEdited(boolean edited) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBooleanValue(edited);
        }
    }

    /**
     * Sets (as xml) the "edited" attribute
     */
    @Override
    public void xsetEdited(org.apache.xmlbeans.XmlBoolean edited) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(edited);
        }
    }

    /**
     * Unsets the "edited" attribute
     */
    @Override
    public void unsetEdited() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }
}

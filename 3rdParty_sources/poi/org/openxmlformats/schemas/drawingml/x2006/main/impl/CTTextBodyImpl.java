/*
 * XML Type:  CT_TextBody
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TextBody(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTextBodyImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody {
    private static final long serialVersionUID = 1L;

    public CTTextBodyImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "bodyPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "lstStyle"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "p"),
    };


    /**
     * Gets the "bodyPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties getBodyPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "bodyPr" element
     */
    @Override
    public void setBodyPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties bodyPr) {
        generatedSetterHelperImpl(bodyPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bodyPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties addNewBodyPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "lstStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle getLstStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lstStyle" element
     */
    @Override
    public boolean isSetLstStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "lstStyle" element
     */
    @Override
    public void setLstStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle lstStyle) {
        generatedSetterHelperImpl(lstStyle, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lstStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle addNewLstStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "lstStyle" element
     */
    @Override
    public void unsetLstStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets a List of "p" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph> getPList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPArray,
                this::setPArray,
                this::insertNewP,
                this::removeP,
                this::sizeOfPArray
            );
        }
    }

    /**
     * Gets array of all "p" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph[] getPArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph[0]);
    }

    /**
     * Gets ith "p" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph getPArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "p" element
     */
    @Override
    public int sizeOfPArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "p" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPArray(org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph[] pArray) {
        check_orphaned();
        arraySetterHelper(pArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "p" element
     */
    @Override
    public void setPArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph p) {
        generatedSetterHelperImpl(p, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "p" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph insertNewP(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "p" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph addNewP() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "p" element
     */
    @Override
    public void removeP(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }
}

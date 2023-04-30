/*
 * XML Type:  CT_TableStyleList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TableStyleList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTableStyleListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleList {
    private static final long serialVersionUID = 1L;

    public CTTableStyleListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "tblStyle"),
        new QName("", "def"),
    };


    /**
     * Gets a List of "tblStyle" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle> getTblStyleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTblStyleArray,
                this::setTblStyleArray,
                this::insertNewTblStyle,
                this::removeTblStyle,
                this::sizeOfTblStyleArray
            );
        }
    }

    /**
     * Gets array of all "tblStyle" elements
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle[] getTblStyleArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle[0]);
    }

    /**
     * Gets ith "tblStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle getTblStyleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "tblStyle" element
     */
    @Override
    public int sizeOfTblStyleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "tblStyle" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTblStyleArray(org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle[] tblStyleArray) {
        check_orphaned();
        arraySetterHelper(tblStyleArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "tblStyle" element
     */
    @Override
    public void setTblStyleArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle tblStyle) {
        generatedSetterHelperImpl(tblStyle, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tblStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle insertNewTblStyle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tblStyle" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle addNewTblStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyle)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "tblStyle" element
     */
    @Override
    public void removeTblStyle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "def" attribute
     */
    @Override
    public java.lang.String getDef() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "def" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid xgetDef() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "def" attribute
     */
    @Override
    public void setDef(java.lang.String def) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(def);
        }
    }

    /**
     * Sets (as xml) the "def" attribute
     */
    @Override
    public void xsetDef(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid def) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(def);
        }
    }
}

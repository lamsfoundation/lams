/*
 * XML Type:  CT_ConditionalFormats
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormats
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ConditionalFormats(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTConditionalFormatsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormats {
    private static final long serialVersionUID = 1L;

    public CTConditionalFormatsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "conditionalFormat"),
        new QName("", "count"),
    };


    /**
     * Gets a List of "conditionalFormat" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat> getConditionalFormatList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getConditionalFormatArray,
                this::setConditionalFormatArray,
                this::insertNewConditionalFormat,
                this::removeConditionalFormat,
                this::sizeOfConditionalFormatArray
            );
        }
    }

    /**
     * Gets array of all "conditionalFormat" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat[] getConditionalFormatArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat[0]);
    }

    /**
     * Gets ith "conditionalFormat" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat getConditionalFormatArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "conditionalFormat" element
     */
    @Override
    public int sizeOfConditionalFormatArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "conditionalFormat" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setConditionalFormatArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat[] conditionalFormatArray) {
        check_orphaned();
        arraySetterHelper(conditionalFormatArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "conditionalFormat" element
     */
    @Override
    public void setConditionalFormatArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat conditionalFormat) {
        generatedSetterHelperImpl(conditionalFormat, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "conditionalFormat" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat insertNewConditionalFormat(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "conditionalFormat" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat addNewConditionalFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "conditionalFormat" element
     */
    @Override
    public void removeConditionalFormat(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "count" attribute
     */
    @Override
    public long getCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "count" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetCount() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "count" attribute
     */
    @Override
    public boolean isSetCount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "count" attribute
     */
    @Override
    public void setCount(long count) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setLongValue(count);
        }
    }

    /**
     * Sets (as xml) the "count" attribute
     */
    @Override
    public void xsetCount(org.apache.xmlbeans.XmlUnsignedInt count) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(count);
        }
    }

    /**
     * Unsets the "count" attribute
     */
    @Override
    public void unsetCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}

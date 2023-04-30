/*
 * XML Type:  CT_Tuples
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuples
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Tuples(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTTuplesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuples {
    private static final long serialVersionUID = 1L;

    public CTTuplesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "tpl"),
        new QName("", "c"),
    };


    /**
     * Gets a List of "tpl" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple> getTplList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTplArray,
                this::setTplArray,
                this::insertNewTpl,
                this::removeTpl,
                this::sizeOfTplArray
            );
        }
    }

    /**
     * Gets array of all "tpl" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple[] getTplArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple[0]);
    }

    /**
     * Gets ith "tpl" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple getTplArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "tpl" element
     */
    @Override
    public int sizeOfTplArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "tpl" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTplArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple[] tplArray) {
        check_orphaned();
        arraySetterHelper(tplArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "tpl" element
     */
    @Override
    public void setTplArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple tpl) {
        generatedSetterHelperImpl(tpl, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tpl" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple insertNewTpl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tpl" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple addNewTpl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "tpl" element
     */
    @Override
    public void removeTpl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "c" attribute
     */
    @Override
    public long getC() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "c" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetC() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "c" attribute
     */
    @Override
    public boolean isSetC() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "c" attribute
     */
    @Override
    public void setC(long c) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setLongValue(c);
        }
    }

    /**
     * Sets (as xml) the "c" attribute
     */
    @Override
    public void xsetC(org.apache.xmlbeans.XmlUnsignedInt c) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(c);
        }
    }

    /**
     * Unsets the "c" attribute
     */
    @Override
    public void unsetC() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}

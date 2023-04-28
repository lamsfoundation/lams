/*
 * XML Type:  CT_FunctionGroups
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroups
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_FunctionGroups(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTFunctionGroupsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroups {
    private static final long serialVersionUID = 1L;

    public CTFunctionGroupsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "functionGroup"),
        new QName("", "builtInGroupCount"),
    };


    /**
     * Gets a List of "functionGroup" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup> getFunctionGroupList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getFunctionGroupArray,
                this::setFunctionGroupArray,
                this::insertNewFunctionGroup,
                this::removeFunctionGroup,
                this::sizeOfFunctionGroupArray
            );
        }
    }

    /**
     * Gets array of all "functionGroup" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup[] getFunctionGroupArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup[0]);
    }

    /**
     * Gets ith "functionGroup" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup getFunctionGroupArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "functionGroup" element
     */
    @Override
    public int sizeOfFunctionGroupArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "functionGroup" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setFunctionGroupArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup[] functionGroupArray) {
        check_orphaned();
        arraySetterHelper(functionGroupArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "functionGroup" element
     */
    @Override
    public void setFunctionGroupArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup functionGroup) {
        generatedSetterHelperImpl(functionGroup, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "functionGroup" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup insertNewFunctionGroup(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "functionGroup" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup addNewFunctionGroup() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFunctionGroup)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "functionGroup" element
     */
    @Override
    public void removeFunctionGroup(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "builtInGroupCount" attribute
     */
    @Override
    public long getBuiltInGroupCount() {
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
     * Gets (as xml) the "builtInGroupCount" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetBuiltInGroupCount() {
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
     * True if has "builtInGroupCount" attribute
     */
    @Override
    public boolean isSetBuiltInGroupCount() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "builtInGroupCount" attribute
     */
    @Override
    public void setBuiltInGroupCount(long builtInGroupCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setLongValue(builtInGroupCount);
        }
    }

    /**
     * Sets (as xml) the "builtInGroupCount" attribute
     */
    @Override
    public void xsetBuiltInGroupCount(org.apache.xmlbeans.XmlUnsignedInt builtInGroupCount) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(builtInGroupCount);
        }
    }

    /**
     * Unsets the "builtInGroupCount" attribute
     */
    @Override
    public void unsetBuiltInGroupCount() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}

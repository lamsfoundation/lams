/*
 * XML Type:  CT_GroupLevels
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevels
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_GroupLevels(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTGroupLevelsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevels {
    private static final long serialVersionUID = 1L;

    public CTGroupLevelsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "groupLevel"),
        new QName("", "count"),
    };


    /**
     * Gets a List of "groupLevel" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel> getGroupLevelList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getGroupLevelArray,
                this::setGroupLevelArray,
                this::insertNewGroupLevel,
                this::removeGroupLevel,
                this::sizeOfGroupLevelArray
            );
        }
    }

    /**
     * Gets array of all "groupLevel" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel[] getGroupLevelArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel[0]);
    }

    /**
     * Gets ith "groupLevel" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel getGroupLevelArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "groupLevel" element
     */
    @Override
    public int sizeOfGroupLevelArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "groupLevel" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setGroupLevelArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel[] groupLevelArray) {
        check_orphaned();
        arraySetterHelper(groupLevelArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "groupLevel" element
     */
    @Override
    public void setGroupLevelArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel groupLevel) {
        generatedSetterHelperImpl(groupLevel, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "groupLevel" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel insertNewGroupLevel(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "groupLevel" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel addNewGroupLevel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "groupLevel" element
     */
    @Override
    public void removeGroupLevel(int i) {
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

/*
 * XML Type:  CT_MapInfo
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_MapInfo(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTMapInfoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMapInfo {
    private static final long serialVersionUID = 1L;

    public CTMapInfoImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "Schema"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "Map"),
        new QName("", "SelectionNamespaces"),
    };


    /**
     * Gets a List of "Schema" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema> getSchemaList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSchemaArray,
                this::setSchemaArray,
                this::insertNewSchema,
                this::removeSchema,
                this::sizeOfSchemaArray
            );
        }
    }

    /**
     * Gets array of all "Schema" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema[] getSchemaArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema[0]);
    }

    /**
     * Gets ith "Schema" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema getSchemaArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Schema" element
     */
    @Override
    public int sizeOfSchemaArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "Schema" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSchemaArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema[] schemaArray) {
        check_orphaned();
        arraySetterHelper(schemaArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "Schema" element
     */
    @Override
    public void setSchemaArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema schema) {
        generatedSetterHelperImpl(schema, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Schema" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema insertNewSchema(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Schema" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema addNewSchema() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSchema)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "Schema" element
     */
    @Override
    public void removeSchema(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "Map" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap> getMapList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getMapArray,
                this::setMapArray,
                this::insertNewMap,
                this::removeMap,
                this::sizeOfMapArray
            );
        }
    }

    /**
     * Gets array of all "Map" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap[] getMapArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap[0]);
    }

    /**
     * Gets ith "Map" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap getMapArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Map" element
     */
    @Override
    public int sizeOfMapArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "Map" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setMapArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap[] mapArray) {
        check_orphaned();
        arraySetterHelper(mapArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "Map" element
     */
    @Override
    public void setMapArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap map) {
        generatedSetterHelperImpl(map, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Map" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap insertNewMap(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Map" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap addNewMap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMap)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "Map" element
     */
    @Override
    public void removeMap(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets the "SelectionNamespaces" attribute
     */
    @Override
    public java.lang.String getSelectionNamespaces() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "SelectionNamespaces" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetSelectionNamespaces() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "SelectionNamespaces" attribute
     */
    @Override
    public void setSelectionNamespaces(java.lang.String selectionNamespaces) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(selectionNamespaces);
        }
    }

    /**
     * Sets (as xml) the "SelectionNamespaces" attribute
     */
    @Override
    public void xsetSelectionNamespaces(org.apache.xmlbeans.XmlString selectionNamespaces) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(selectionNamespaces);
        }
    }
}

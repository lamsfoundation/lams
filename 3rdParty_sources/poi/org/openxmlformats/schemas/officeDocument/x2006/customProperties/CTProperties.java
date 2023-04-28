/*
 * XML Type:  CT_Properties
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/custom-properties
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.customProperties;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Properties(@http://schemas.openxmlformats.org/officeDocument/2006/custom-properties).
 *
 * This is a complex type.
 */
public interface CTProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctproperties2c18type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "property" elements
     */
    java.util.List<org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty> getPropertyList();

    /**
     * Gets array of all "property" elements
     */
    org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty[] getPropertyArray();

    /**
     * Gets ith "property" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty getPropertyArray(int i);

    /**
     * Returns number of "property" element
     */
    int sizeOfPropertyArray();

    /**
     * Sets array of all "property" element
     */
    void setPropertyArray(org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty[] propertyArray);

    /**
     * Sets ith "property" element
     */
    void setPropertyArray(int i, org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty property);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "property" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty insertNewProperty(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "property" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty addNewProperty();

    /**
     * Removes the ith "property" element
     */
    void removeProperty(int i);
}

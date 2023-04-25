/*
 * XML Type:  UnsignedDataObjectPropertiesType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML UnsignedDataObjectPropertiesType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface UnsignedDataObjectPropertiesType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.UnsignedDataObjectPropertiesType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "unsigneddataobjectpropertiestype61fftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "UnsignedDataObjectProperty" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.AnyType> getUnsignedDataObjectPropertyList();

    /**
     * Gets array of all "UnsignedDataObjectProperty" elements
     */
    org.etsi.uri.x01903.v13.AnyType[] getUnsignedDataObjectPropertyArray();

    /**
     * Gets ith "UnsignedDataObjectProperty" element
     */
    org.etsi.uri.x01903.v13.AnyType getUnsignedDataObjectPropertyArray(int i);

    /**
     * Returns number of "UnsignedDataObjectProperty" element
     */
    int sizeOfUnsignedDataObjectPropertyArray();

    /**
     * Sets array of all "UnsignedDataObjectProperty" element
     */
    void setUnsignedDataObjectPropertyArray(org.etsi.uri.x01903.v13.AnyType[] unsignedDataObjectPropertyArray);

    /**
     * Sets ith "UnsignedDataObjectProperty" element
     */
    void setUnsignedDataObjectPropertyArray(int i, org.etsi.uri.x01903.v13.AnyType unsignedDataObjectProperty);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "UnsignedDataObjectProperty" element
     */
    org.etsi.uri.x01903.v13.AnyType insertNewUnsignedDataObjectProperty(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "UnsignedDataObjectProperty" element
     */
    org.etsi.uri.x01903.v13.AnyType addNewUnsignedDataObjectProperty();

    /**
     * Removes the ith "UnsignedDataObjectProperty" element
     */
    void removeUnsignedDataObjectProperty(int i);

    /**
     * Gets the "Id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "Id" attribute
     */
    org.apache.xmlbeans.XmlID xgetId();

    /**
     * True if has "Id" attribute
     */
    boolean isSetId();

    /**
     * Sets the "Id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "Id" attribute
     */
    void xsetId(org.apache.xmlbeans.XmlID id);

    /**
     * Unsets the "Id" attribute
     */
    void unsetId();
}

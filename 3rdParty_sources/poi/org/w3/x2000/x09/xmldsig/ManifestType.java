/*
 * XML Type:  ManifestType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.ManifestType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ManifestType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface ManifestType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.ManifestType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "manifesttype26b6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "Reference" elements
     */
    java.util.List<org.w3.x2000.x09.xmldsig.ReferenceType> getReferenceList();

    /**
     * Gets array of all "Reference" elements
     */
    org.w3.x2000.x09.xmldsig.ReferenceType[] getReferenceArray();

    /**
     * Gets ith "Reference" element
     */
    org.w3.x2000.x09.xmldsig.ReferenceType getReferenceArray(int i);

    /**
     * Returns number of "Reference" element
     */
    int sizeOfReferenceArray();

    /**
     * Sets array of all "Reference" element
     */
    void setReferenceArray(org.w3.x2000.x09.xmldsig.ReferenceType[] referenceArray);

    /**
     * Sets ith "Reference" element
     */
    void setReferenceArray(int i, org.w3.x2000.x09.xmldsig.ReferenceType reference);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Reference" element
     */
    org.w3.x2000.x09.xmldsig.ReferenceType insertNewReference(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "Reference" element
     */
    org.w3.x2000.x09.xmldsig.ReferenceType addNewReference();

    /**
     * Removes the ith "Reference" element
     */
    void removeReference(int i);

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

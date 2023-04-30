/*
 * XML Type:  CT_ExternalReferences
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReferences
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ExternalReferences(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTExternalReferences extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReferences> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctexternalreferencesd77ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "externalReference" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference> getExternalReferenceList();

    /**
     * Gets array of all "externalReference" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference[] getExternalReferenceArray();

    /**
     * Gets ith "externalReference" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference getExternalReferenceArray(int i);

    /**
     * Returns number of "externalReference" element
     */
    int sizeOfExternalReferenceArray();

    /**
     * Sets array of all "externalReference" element
     */
    void setExternalReferenceArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference[] externalReferenceArray);

    /**
     * Sets ith "externalReference" element
     */
    void setExternalReferenceArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference externalReference);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "externalReference" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference insertNewExternalReference(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "externalReference" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExternalReference addNewExternalReference();

    /**
     * Removes the ith "externalReference" element
     */
    void removeExternalReference(int i);
}

/*
 * XML Type:  CT_Headers
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeaders
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Headers(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTHeaders extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeaders> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctheaders8acctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "header" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString> getHeaderList();

    /**
     * Gets array of all "header" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString[] getHeaderArray();

    /**
     * Gets ith "header" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getHeaderArray(int i);

    /**
     * Returns number of "header" element
     */
    int sizeOfHeaderArray();

    /**
     * Sets array of all "header" element
     */
    void setHeaderArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString[] headerArray);

    /**
     * Sets ith "header" element
     */
    void setHeaderArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString header);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "header" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString insertNewHeader(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "header" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewHeader();

    /**
     * Removes the ith "header" element
     */
    void removeHeader(int i);
}

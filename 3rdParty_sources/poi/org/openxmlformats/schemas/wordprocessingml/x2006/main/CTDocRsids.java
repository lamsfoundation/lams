/*
 * XML Type:  CT_DocRsids
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocRsids
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DocRsids(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDocRsids extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocRsids> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdocrsids0dc9type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rsidRoot" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber getRsidRoot();

    /**
     * True if has "rsidRoot" element
     */
    boolean isSetRsidRoot();

    /**
     * Sets the "rsidRoot" element
     */
    void setRsidRoot(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber rsidRoot);

    /**
     * Appends and returns a new empty "rsidRoot" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber addNewRsidRoot();

    /**
     * Unsets the "rsidRoot" element
     */
    void unsetRsidRoot();

    /**
     * Gets a List of "rsid" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber> getRsidList();

    /**
     * Gets array of all "rsid" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber[] getRsidArray();

    /**
     * Gets ith "rsid" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber getRsidArray(int i);

    /**
     * Returns number of "rsid" element
     */
    int sizeOfRsidArray();

    /**
     * Sets array of all "rsid" element
     */
    void setRsidArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber[] rsidArray);

    /**
     * Sets ith "rsid" element
     */
    void setRsidArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber rsid);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rsid" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber insertNewRsid(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "rsid" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLongHexNumber addNewRsid();

    /**
     * Removes the ith "rsid" element
     */
    void removeRsid(int i);
}

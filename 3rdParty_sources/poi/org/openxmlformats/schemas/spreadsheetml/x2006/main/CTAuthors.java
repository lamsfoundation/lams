/*
 * XML Type:  CT_Authors
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAuthors
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Authors(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAuthors extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAuthors> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctauthorsb8a7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "author" elements
     */
    java.util.List<java.lang.String> getAuthorList();

    /**
     * Gets array of all "author" elements
     */
    java.lang.String[] getAuthorArray();

    /**
     * Gets ith "author" element
     */
    java.lang.String getAuthorArray(int i);

    /**
     * Gets (as xml) a List of "author" elements
     */
    java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring> xgetAuthorList();

    /**
     * Gets (as xml) array of all "author" elements
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring[] xgetAuthorArray();

    /**
     * Gets (as xml) ith "author" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetAuthorArray(int i);

    /**
     * Returns number of "author" element
     */
    int sizeOfAuthorArray();

    /**
     * Sets array of all "author" element
     */
    void setAuthorArray(java.lang.String[] authorArray);

    /**
     * Sets ith "author" element
     */
    void setAuthorArray(int i, java.lang.String author);

    /**
     * Sets (as xml) array of all "author" element
     */
    void xsetAuthorArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring[] authorArray);

    /**
     * Sets (as xml) ith "author" element
     */
    void xsetAuthorArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring author);

    /**
     * Inserts the value as the ith "author" element
     */
    void insertAuthor(int i, java.lang.String author);

    /**
     * Appends the value as the last "author" element
     */
    void addAuthor(java.lang.String author);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "author" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring insertNewAuthor(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "author" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring addNewAuthor();

    /**
     * Removes the ith "author" element
     */
    void removeAuthor(int i);
}

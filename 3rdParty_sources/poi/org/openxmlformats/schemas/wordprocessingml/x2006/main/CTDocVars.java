/*
 * XML Type:  CT_DocVars
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVars
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DocVars(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDocVars extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVars> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdocvarsdf8etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "docVar" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar> getDocVarList();

    /**
     * Gets array of all "docVar" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar[] getDocVarArray();

    /**
     * Gets ith "docVar" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar getDocVarArray(int i);

    /**
     * Returns number of "docVar" element
     */
    int sizeOfDocVarArray();

    /**
     * Sets array of all "docVar" element
     */
    void setDocVarArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar[] docVarArray);

    /**
     * Sets ith "docVar" element
     */
    void setDocVarArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar docVar);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "docVar" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar insertNewDocVar(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "docVar" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVar addNewDocVar();

    /**
     * Removes the ith "docVar" element
     */
    void removeDocVar(int i);
}

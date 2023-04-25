/*
 * XML Type:  CT_EdnDocProps
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnDocProps
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_EdnDocProps(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTEdnDocProps extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnProps {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnDocProps> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctedndocprops478btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "endnote" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnSepRef> getEndnoteList();

    /**
     * Gets array of all "endnote" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnSepRef[] getEndnoteArray();

    /**
     * Gets ith "endnote" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnSepRef getEndnoteArray(int i);

    /**
     * Returns number of "endnote" element
     */
    int sizeOfEndnoteArray();

    /**
     * Sets array of all "endnote" element
     */
    void setEndnoteArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnSepRef[] endnoteArray);

    /**
     * Sets ith "endnote" element
     */
    void setEndnoteArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnSepRef endnote);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "endnote" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnSepRef insertNewEndnote(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "endnote" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnSepRef addNewEndnote();

    /**
     * Removes the ith "endnote" element
     */
    void removeEndnote(int i);
}

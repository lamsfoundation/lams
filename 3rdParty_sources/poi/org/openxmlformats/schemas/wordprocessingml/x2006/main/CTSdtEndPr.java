/*
 * XML Type:  CT_SdtEndPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtEndPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SdtEndPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSdtEndPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtEndPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsdtendprbc6etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "rPr" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr> getRPrList();

    /**
     * Gets array of all "rPr" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr[] getRPrArray();

    /**
     * Gets ith "rPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr getRPrArray(int i);

    /**
     * Returns number of "rPr" element
     */
    int sizeOfRPrArray();

    /**
     * Sets array of all "rPr" element
     */
    void setRPrArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr[] rPrArray);

    /**
     * Sets ith "rPr" element
     */
    void setRPrArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr rPr);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr insertNewRPr(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "rPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr addNewRPr();

    /**
     * Removes the ith "rPr" element
     */
    void removeRPr(int i);
}

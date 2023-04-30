/*
 * XML Type:  CT_TextBody
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TextBody(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTextBody extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttextbodya3catype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "bodyPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties getBodyPr();

    /**
     * Sets the "bodyPr" element
     */
    void setBodyPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties bodyPr);

    /**
     * Appends and returns a new empty "bodyPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties addNewBodyPr();

    /**
     * Gets the "lstStyle" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle getLstStyle();

    /**
     * True if has "lstStyle" element
     */
    boolean isSetLstStyle();

    /**
     * Sets the "lstStyle" element
     */
    void setLstStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle lstStyle);

    /**
     * Appends and returns a new empty "lstStyle" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextListStyle addNewLstStyle();

    /**
     * Unsets the "lstStyle" element
     */
    void unsetLstStyle();

    /**
     * Gets a List of "p" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph> getPList();

    /**
     * Gets array of all "p" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph[] getPArray();

    /**
     * Gets ith "p" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph getPArray(int i);

    /**
     * Returns number of "p" element
     */
    int sizeOfPArray();

    /**
     * Sets array of all "p" element
     */
    void setPArray(org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph[] pArray);

    /**
     * Sets ith "p" element
     */
    void setPArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph p);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "p" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph insertNewP(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "p" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph addNewP();

    /**
     * Removes the ith "p" element
     */
    void removeP(int i);
}

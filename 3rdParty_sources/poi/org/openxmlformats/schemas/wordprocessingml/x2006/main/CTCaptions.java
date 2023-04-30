/*
 * XML Type:  CT_Captions
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaptions
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Captions(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCaptions extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaptions> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcaptions9fbbtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "caption" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption> getCaptionList();

    /**
     * Gets array of all "caption" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption[] getCaptionArray();

    /**
     * Gets ith "caption" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption getCaptionArray(int i);

    /**
     * Returns number of "caption" element
     */
    int sizeOfCaptionArray();

    /**
     * Sets array of all "caption" element
     */
    void setCaptionArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption[] captionArray);

    /**
     * Sets ith "caption" element
     */
    void setCaptionArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption caption);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "caption" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption insertNewCaption(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "caption" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaption addNewCaption();

    /**
     * Removes the ith "caption" element
     */
    void removeCaption(int i);

    /**
     * Gets the "autoCaptions" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaptions getAutoCaptions();

    /**
     * True if has "autoCaptions" element
     */
    boolean isSetAutoCaptions();

    /**
     * Sets the "autoCaptions" element
     */
    void setAutoCaptions(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaptions autoCaptions);

    /**
     * Appends and returns a new empty "autoCaptions" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAutoCaptions addNewAutoCaptions();

    /**
     * Unsets the "autoCaptions" element
     */
    void unsetAutoCaptions();
}

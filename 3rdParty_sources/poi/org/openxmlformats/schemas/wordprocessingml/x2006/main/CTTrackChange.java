/*
 * XML Type:  CT_TrackChange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TrackChange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTrackChange extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkup {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttrackchangec317type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "author" attribute
     */
    java.lang.String getAuthor();

    /**
     * Gets (as xml) the "author" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetAuthor();

    /**
     * Sets the "author" attribute
     */
    void setAuthor(java.lang.String author);

    /**
     * Sets (as xml) the "author" attribute
     */
    void xsetAuthor(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString author);

    /**
     * Gets the "date" attribute
     */
    java.util.Calendar getDate();

    /**
     * Gets (as xml) the "date" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime xgetDate();

    /**
     * True if has "date" attribute
     */
    boolean isSetDate();

    /**
     * Sets the "date" attribute
     */
    void setDate(java.util.Calendar date);

    /**
     * Sets (as xml) the "date" attribute
     */
    void xsetDate(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDateTime date);

    /**
     * Unsets the "date" attribute
     */
    void unsetDate();
}

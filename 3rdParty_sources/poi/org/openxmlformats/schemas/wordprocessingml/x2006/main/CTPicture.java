/*
 * XML Type:  CT_Picture
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Picture(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPicture extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpicture1054type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "movie" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel getMovie();

    /**
     * True if has "movie" element
     */
    boolean isSetMovie();

    /**
     * Sets the "movie" element
     */
    void setMovie(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel movie);

    /**
     * Appends and returns a new empty "movie" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel addNewMovie();

    /**
     * Unsets the "movie" element
     */
    void unsetMovie();

    /**
     * Gets the "control" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTControl getControl();

    /**
     * True if has "control" element
     */
    boolean isSetControl();

    /**
     * Sets the "control" element
     */
    void setControl(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTControl control);

    /**
     * Appends and returns a new empty "control" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTControl addNewControl();

    /**
     * Unsets the "control" element
     */
    void unsetControl();
}

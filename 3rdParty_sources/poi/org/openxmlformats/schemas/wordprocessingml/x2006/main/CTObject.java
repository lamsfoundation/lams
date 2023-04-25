/*
 * XML Type:  CT_Object
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Object(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTObject extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctobject47c9type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "drawing" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing getDrawing();

    /**
     * True if has "drawing" element
     */
    boolean isSetDrawing();

    /**
     * Sets the "drawing" element
     */
    void setDrawing(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing drawing);

    /**
     * Appends and returns a new empty "drawing" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing addNewDrawing();

    /**
     * Unsets the "drawing" element
     */
    void unsetDrawing();

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

    /**
     * Gets the "objectLink" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectLink getObjectLink();

    /**
     * True if has "objectLink" element
     */
    boolean isSetObjectLink();

    /**
     * Sets the "objectLink" element
     */
    void setObjectLink(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectLink objectLink);

    /**
     * Appends and returns a new empty "objectLink" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectLink addNewObjectLink();

    /**
     * Unsets the "objectLink" element
     */
    void unsetObjectLink();

    /**
     * Gets the "objectEmbed" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectEmbed getObjectEmbed();

    /**
     * True if has "objectEmbed" element
     */
    boolean isSetObjectEmbed();

    /**
     * Sets the "objectEmbed" element
     */
    void setObjectEmbed(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectEmbed objectEmbed);

    /**
     * Appends and returns a new empty "objectEmbed" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectEmbed addNewObjectEmbed();

    /**
     * Unsets the "objectEmbed" element
     */
    void unsetObjectEmbed();

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
     * Gets the "dxaOrig" attribute
     */
    java.lang.Object getDxaOrig();

    /**
     * Gets (as xml) the "dxaOrig" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetDxaOrig();

    /**
     * True if has "dxaOrig" attribute
     */
    boolean isSetDxaOrig();

    /**
     * Sets the "dxaOrig" attribute
     */
    void setDxaOrig(java.lang.Object dxaOrig);

    /**
     * Sets (as xml) the "dxaOrig" attribute
     */
    void xsetDxaOrig(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure dxaOrig);

    /**
     * Unsets the "dxaOrig" attribute
     */
    void unsetDxaOrig();

    /**
     * Gets the "dyaOrig" attribute
     */
    java.lang.Object getDyaOrig();

    /**
     * Gets (as xml) the "dyaOrig" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetDyaOrig();

    /**
     * True if has "dyaOrig" attribute
     */
    boolean isSetDyaOrig();

    /**
     * Sets the "dyaOrig" attribute
     */
    void setDyaOrig(java.lang.Object dyaOrig);

    /**
     * Sets (as xml) the "dyaOrig" attribute
     */
    void xsetDyaOrig(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure dyaOrig);

    /**
     * Unsets the "dyaOrig" attribute
     */
    void unsetDyaOrig();
}

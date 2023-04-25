/*
 * An XML document type.
 * Localname: videoFile
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.VideoFileDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one videoFile(@http://schemas.openxmlformats.org/drawingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface VideoFileDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.VideoFileDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "videofile1df4doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "videoFile" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile getVideoFile();

    /**
     * Sets the "videoFile" element
     */
    void setVideoFile(org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile videoFile);

    /**
     * Appends and returns a new empty "videoFile" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTVideoFile addNewVideoFile();
}

/*
 * XML Type:  CT_TextBlipBullet
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextBlipBullet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TextBlipBullet(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTextBlipBullet extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTextBlipBullet> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttextblipbullet853btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "blip" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBlip getBlip();

    /**
     * Sets the "blip" element
     */
    void setBlip(org.openxmlformats.schemas.drawingml.x2006.main.CTBlip blip);

    /**
     * Appends and returns a new empty "blip" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBlip addNewBlip();
}

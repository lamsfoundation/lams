/*
 * XML Type:  CT_WheelTransition
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTWheelTransition
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_WheelTransition(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTWheelTransition extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTWheelTransition> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctwheeltransition34bftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "spokes" attribute
     */
    long getSpokes();

    /**
     * Gets (as xml) the "spokes" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetSpokes();

    /**
     * True if has "spokes" attribute
     */
    boolean isSetSpokes();

    /**
     * Sets the "spokes" attribute
     */
    void setSpokes(long spokes);

    /**
     * Sets (as xml) the "spokes" attribute
     */
    void xsetSpokes(org.apache.xmlbeans.XmlUnsignedInt spokes);

    /**
     * Unsets the "spokes" attribute
     */
    void unsetSpokes();
}

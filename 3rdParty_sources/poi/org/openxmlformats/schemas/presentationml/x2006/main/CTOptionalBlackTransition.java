/*
 * XML Type:  CT_OptionalBlackTransition
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_OptionalBlackTransition(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTOptionalBlackTransition extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctoptionalblacktransition811btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "thruBlk" attribute
     */
    boolean getThruBlk();

    /**
     * Gets (as xml) the "thruBlk" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetThruBlk();

    /**
     * True if has "thruBlk" attribute
     */
    boolean isSetThruBlk();

    /**
     * Sets the "thruBlk" attribute
     */
    void setThruBlk(boolean thruBlk);

    /**
     * Sets (as xml) the "thruBlk" attribute
     */
    void xsetThruBlk(org.apache.xmlbeans.XmlBoolean thruBlk);

    /**
     * Unsets the "thruBlk" attribute
     */
    void unsetThruBlk();
}

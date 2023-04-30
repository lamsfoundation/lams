/*
 * XML Type:  CT_Text
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTText
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Text(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.officeDocument.x2006.math.CTText.
 */
public interface CTText extends org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.math.CTText> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttext8965type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "space" attribute
     */
    org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute.Space.Enum getSpace();

    /**
     * Gets (as xml) the "space" attribute
     */
    org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute.Space xgetSpace();

    /**
     * True if has "space" attribute
     */
    boolean isSetSpace();

    /**
     * Sets the "space" attribute
     */
    void setSpace(org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute.Space.Enum space);

    /**
     * Sets (as xml) the "space" attribute
     */
    void xsetSpace(org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute.Space space);

    /**
     * Unsets the "space" attribute
     */
    void unsetSpace();
}

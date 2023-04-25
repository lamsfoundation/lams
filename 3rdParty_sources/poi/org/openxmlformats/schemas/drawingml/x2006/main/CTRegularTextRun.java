/*
 * XML Type:  CT_RegularTextRun
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_RegularTextRun(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTRegularTextRun extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctregulartextrun7e3dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties getRPr();

    /**
     * True if has "rPr" element
     */
    boolean isSetRPr();

    /**
     * Sets the "rPr" element
     */
    void setRPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties rPr);

    /**
     * Appends and returns a new empty "rPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties addNewRPr();

    /**
     * Unsets the "rPr" element
     */
    void unsetRPr();

    /**
     * Gets the "t" element
     */
    java.lang.String getT();

    /**
     * Gets (as xml) the "t" element
     */
    org.apache.xmlbeans.XmlString xgetT();

    /**
     * Sets the "t" element
     */
    void setT(java.lang.String t);

    /**
     * Sets (as xml) the "t" element
     */
    void xsetT(org.apache.xmlbeans.XmlString t);
}

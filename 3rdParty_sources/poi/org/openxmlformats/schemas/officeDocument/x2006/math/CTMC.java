/*
 * XML Type:  CT_MC
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTMC
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MC(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is a complex type.
 */
public interface CTMC extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.math.CTMC> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmc923ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "mcPr" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTMCPr getMcPr();

    /**
     * True if has "mcPr" element
     */
    boolean isSetMcPr();

    /**
     * Sets the "mcPr" element
     */
    void setMcPr(org.openxmlformats.schemas.officeDocument.x2006.math.CTMCPr mcPr);

    /**
     * Appends and returns a new empty "mcPr" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTMCPr addNewMcPr();

    /**
     * Unsets the "mcPr" element
     */
    void unsetMcPr();
}

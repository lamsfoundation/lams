/*
 * XML Type:  CT_TextSpacing
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TextSpacing(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTextSpacing extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacing> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttextspacingef87type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "spcPct" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent getSpcPct();

    /**
     * True if has "spcPct" element
     */
    boolean isSetSpcPct();

    /**
     * Sets the "spcPct" element
     */
    void setSpcPct(org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent spcPct);

    /**
     * Appends and returns a new empty "spcPct" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPercent addNewSpcPct();

    /**
     * Unsets the "spcPct" element
     */
    void unsetSpcPct();

    /**
     * Gets the "spcPts" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPoint getSpcPts();

    /**
     * True if has "spcPts" element
     */
    boolean isSetSpcPts();

    /**
     * Sets the "spcPts" element
     */
    void setSpcPts(org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPoint spcPts);

    /**
     * Appends and returns a new empty "spcPts" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextSpacingPoint addNewSpcPts();

    /**
     * Unsets the "spcPts" element
     */
    void unsetSpcPts();
}

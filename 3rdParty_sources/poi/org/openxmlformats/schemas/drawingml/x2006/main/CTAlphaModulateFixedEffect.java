/*
 * XML Type:  CT_AlphaModulateFixedEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AlphaModulateFixedEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAlphaModulateFixedEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTAlphaModulateFixedEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctalphamodulatefixedeffect9769type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "amt" attribute
     */
    java.lang.Object getAmt();

    /**
     * Gets (as xml) the "amt" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage xgetAmt();

    /**
     * True if has "amt" attribute
     */
    boolean isSetAmt();

    /**
     * Sets the "amt" attribute
     */
    void setAmt(java.lang.Object amt);

    /**
     * Sets (as xml) the "amt" attribute
     */
    void xsetAmt(org.openxmlformats.schemas.drawingml.x2006.main.STPositivePercentage amt);

    /**
     * Unsets the "amt" attribute
     */
    void unsetAmt();
}

/*
 * XML Type:  CT_PPrGeneral
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PPrGeneral(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPPrGeneral extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrBase {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpprgenerald6f2type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pPrChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrChange getPPrChange();

    /**
     * True if has "pPrChange" element
     */
    boolean isSetPPrChange();

    /**
     * Sets the "pPrChange" element
     */
    void setPPrChange(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrChange pPrChange);

    /**
     * Appends and returns a new empty "pPrChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrChange addNewPPrChange();

    /**
     * Unsets the "pPrChange" element
     */
    void unsetPPrChange();
}

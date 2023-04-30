/*
 * XML Type:  CT_PPrDefault
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PPrDefault(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPPrDefault extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrDefault> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpprdefaultf839type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral getPPr();

    /**
     * True if has "pPr" element
     */
    boolean isSetPPr();

    /**
     * Sets the "pPr" element
     */
    void setPPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral pPr);

    /**
     * Appends and returns a new empty "pPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrGeneral addNewPPr();

    /**
     * Unsets the "pPr" element
     */
    void unsetPPr();
}

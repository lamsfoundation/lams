/*
 * XML Type:  CT_MathCtrlIns
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMathCtrlIns
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MathCtrlIns(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTMathCtrlIns extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMathCtrlIns> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmathctrlinsc697type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "del" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrChange getDel();

    /**
     * True if has "del" element
     */
    boolean isSetDel();

    /**
     * Sets the "del" element
     */
    void setDel(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrChange del);

    /**
     * Appends and returns a new empty "del" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPrChange addNewDel();

    /**
     * Unsets the "del" element
     */
    void unsetDel();

    /**
     * Gets the "rPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr getRPr();

    /**
     * True if has "rPr" element
     */
    boolean isSetRPr();

    /**
     * Sets the "rPr" element
     */
    void setRPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr rPr);

    /**
     * Appends and returns a new empty "rPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr addNewRPr();

    /**
     * Unsets the "rPr" element
     */
    void unsetRPr();
}

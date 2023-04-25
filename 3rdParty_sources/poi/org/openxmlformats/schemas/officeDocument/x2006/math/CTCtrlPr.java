/*
 * XML Type:  CT_CtrlPr
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CtrlPr(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is a complex type.
 */
public interface CTCtrlPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.math.CTCtrlPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctctrlprea05type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


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

    /**
     * Gets the "ins" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMathCtrlIns getIns();

    /**
     * True if has "ins" element
     */
    boolean isSetIns();

    /**
     * Sets the "ins" element
     */
    void setIns(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMathCtrlIns ins);

    /**
     * Appends and returns a new empty "ins" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMathCtrlIns addNewIns();

    /**
     * Unsets the "ins" element
     */
    void unsetIns();

    /**
     * Gets the "del" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMathCtrlDel getDel();

    /**
     * True if has "del" element
     */
    boolean isSetDel();

    /**
     * Sets the "del" element
     */
    void setDel(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMathCtrlDel del);

    /**
     * Appends and returns a new empty "del" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMathCtrlDel addNewDel();

    /**
     * Unsets the "del" element
     */
    void unsetDel();
}

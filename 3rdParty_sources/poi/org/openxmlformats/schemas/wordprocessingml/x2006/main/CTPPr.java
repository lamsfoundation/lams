/*
 * XML Type:  CT_PPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPPr extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPrBase {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctppr01c0type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr getRPr();

    /**
     * True if has "rPr" element
     */
    boolean isSetRPr();

    /**
     * Sets the "rPr" element
     */
    void setRPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr rPr);

    /**
     * Appends and returns a new empty "rPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr addNewRPr();

    /**
     * Unsets the "rPr" element
     */
    void unsetRPr();

    /**
     * Gets the "sectPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr getSectPr();

    /**
     * True if has "sectPr" element
     */
    boolean isSetSectPr();

    /**
     * Sets the "sectPr" element
     */
    void setSectPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr sectPr);

    /**
     * Appends and returns a new empty "sectPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr addNewSectPr();

    /**
     * Unsets the "sectPr" element
     */
    void unsetSectPr();

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

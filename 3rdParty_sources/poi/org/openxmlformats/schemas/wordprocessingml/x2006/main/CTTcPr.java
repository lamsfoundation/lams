/*
 * XML Type:  CT_TcPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TcPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTcPr extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrInner {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttcpree37type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tcPrChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrChange getTcPrChange();

    /**
     * True if has "tcPrChange" element
     */
    boolean isSetTcPrChange();

    /**
     * Sets the "tcPrChange" element
     */
    void setTcPrChange(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrChange tcPrChange);

    /**
     * Appends and returns a new empty "tcPrChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPrChange addNewTcPrChange();

    /**
     * Unsets the "tcPrChange" element
     */
    void unsetTcPrChange();
}

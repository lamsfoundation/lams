/*
 * XML Type:  CT_NumFmt
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NumFmt(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTNumFmt extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnumfmt00e1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "val" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat.Enum getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat xgetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat.Enum val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat val);

    /**
     * Gets the "format" attribute
     */
    java.lang.String getFormat();

    /**
     * Gets (as xml) the "format" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetFormat();

    /**
     * True if has "format" attribute
     */
    boolean isSetFormat();

    /**
     * Sets the "format" attribute
     */
    void setFormat(java.lang.String format);

    /**
     * Sets (as xml) the "format" attribute
     */
    void xsetFormat(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString format);

    /**
     * Unsets the "format" attribute
     */
    void unsetFormat();
}

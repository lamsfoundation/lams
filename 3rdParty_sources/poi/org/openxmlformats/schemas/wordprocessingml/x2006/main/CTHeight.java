/*
 * XML Type:  CT_Height
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Height(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTHeight extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctheighta2e1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "val" attribute
     */
    java.lang.Object getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetVal();

    /**
     * True if has "val" attribute
     */
    boolean isSetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(java.lang.Object val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure val);

    /**
     * Unsets the "val" attribute
     */
    void unsetVal();

    /**
     * Gets the "hRule" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule.Enum getHRule();

    /**
     * Gets (as xml) the "hRule" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule xgetHRule();

    /**
     * True if has "hRule" attribute
     */
    boolean isSetHRule();

    /**
     * Sets the "hRule" attribute
     */
    void setHRule(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule.Enum hRule);

    /**
     * Sets (as xml) the "hRule" attribute
     */
    void xsetHRule(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule hRule);

    /**
     * Unsets the "hRule" attribute
     */
    void unsetHRule();
}

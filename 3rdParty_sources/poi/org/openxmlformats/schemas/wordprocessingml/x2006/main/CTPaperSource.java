/*
 * XML Type:  CT_PaperSource
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPaperSource
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PaperSource(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPaperSource extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPaperSource> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpapersource8aabtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "first" attribute
     */
    java.math.BigInteger getFirst();

    /**
     * Gets (as xml) the "first" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetFirst();

    /**
     * True if has "first" attribute
     */
    boolean isSetFirst();

    /**
     * Sets the "first" attribute
     */
    void setFirst(java.math.BigInteger first);

    /**
     * Sets (as xml) the "first" attribute
     */
    void xsetFirst(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber first);

    /**
     * Unsets the "first" attribute
     */
    void unsetFirst();

    /**
     * Gets the "other" attribute
     */
    java.math.BigInteger getOther();

    /**
     * Gets (as xml) the "other" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetOther();

    /**
     * True if has "other" attribute
     */
    boolean isSetOther();

    /**
     * Sets the "other" attribute
     */
    void setOther(java.math.BigInteger other);

    /**
     * Sets (as xml) the "other" attribute
     */
    void xsetOther(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber other);

    /**
     * Unsets the "other" attribute
     */
    void unsetOther();
}

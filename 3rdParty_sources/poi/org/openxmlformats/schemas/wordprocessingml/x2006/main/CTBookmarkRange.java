/*
 * XML Type:  CT_BookmarkRange
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmarkRange
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_BookmarkRange(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTBookmarkRange extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMarkupRange {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmarkRange> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctbookmarkranged88btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "colFirst" attribute
     */
    java.math.BigInteger getColFirst();

    /**
     * Gets (as xml) the "colFirst" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetColFirst();

    /**
     * True if has "colFirst" attribute
     */
    boolean isSetColFirst();

    /**
     * Sets the "colFirst" attribute
     */
    void setColFirst(java.math.BigInteger colFirst);

    /**
     * Sets (as xml) the "colFirst" attribute
     */
    void xsetColFirst(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber colFirst);

    /**
     * Unsets the "colFirst" attribute
     */
    void unsetColFirst();

    /**
     * Gets the "colLast" attribute
     */
    java.math.BigInteger getColLast();

    /**
     * Gets (as xml) the "colLast" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetColLast();

    /**
     * True if has "colLast" attribute
     */
    boolean isSetColLast();

    /**
     * Sets the "colLast" attribute
     */
    void setColLast(java.math.BigInteger colLast);

    /**
     * Sets (as xml) the "colLast" attribute
     */
    void xsetColLast(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber colLast);

    /**
     * Unsets the "colLast" attribute
     */
    void unsetColLast();
}

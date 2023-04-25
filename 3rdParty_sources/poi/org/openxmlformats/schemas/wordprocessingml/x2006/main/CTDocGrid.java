/*
 * XML Type:  CT_DocGrid
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocGrid
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DocGrid(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDocGrid extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocGrid> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdocgride8b4type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocGrid.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocGrid xgetType();

    /**
     * True if has "type" attribute
     */
    boolean isSetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocGrid.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocGrid type);

    /**
     * Unsets the "type" attribute
     */
    void unsetType();

    /**
     * Gets the "linePitch" attribute
     */
    java.math.BigInteger getLinePitch();

    /**
     * Gets (as xml) the "linePitch" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetLinePitch();

    /**
     * True if has "linePitch" attribute
     */
    boolean isSetLinePitch();

    /**
     * Sets the "linePitch" attribute
     */
    void setLinePitch(java.math.BigInteger linePitch);

    /**
     * Sets (as xml) the "linePitch" attribute
     */
    void xsetLinePitch(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber linePitch);

    /**
     * Unsets the "linePitch" attribute
     */
    void unsetLinePitch();

    /**
     * Gets the "charSpace" attribute
     */
    java.math.BigInteger getCharSpace();

    /**
     * Gets (as xml) the "charSpace" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetCharSpace();

    /**
     * True if has "charSpace" attribute
     */
    boolean isSetCharSpace();

    /**
     * Sets the "charSpace" attribute
     */
    void setCharSpace(java.math.BigInteger charSpace);

    /**
     * Sets (as xml) the "charSpace" attribute
     */
    void xsetCharSpace(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber charSpace);

    /**
     * Unsets the "charSpace" attribute
     */
    void unsetCharSpace();
}

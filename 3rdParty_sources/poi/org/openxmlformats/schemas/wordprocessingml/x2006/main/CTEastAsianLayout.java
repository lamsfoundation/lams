/*
 * XML Type:  CT_EastAsianLayout
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_EastAsianLayout(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTEastAsianLayout extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cteastasianlayout0841type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "id" attribute
     */
    java.math.BigInteger getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetId();

    /**
     * True if has "id" attribute
     */
    boolean isSetId();

    /**
     * Sets the "id" attribute
     */
    void setId(java.math.BigInteger id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber id);

    /**
     * Unsets the "id" attribute
     */
    void unsetId();

    /**
     * Gets the "combine" attribute
     */
    java.lang.Object getCombine();

    /**
     * Gets (as xml) the "combine" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetCombine();

    /**
     * True if has "combine" attribute
     */
    boolean isSetCombine();

    /**
     * Sets the "combine" attribute
     */
    void setCombine(java.lang.Object combine);

    /**
     * Sets (as xml) the "combine" attribute
     */
    void xsetCombine(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff combine);

    /**
     * Unsets the "combine" attribute
     */
    void unsetCombine();

    /**
     * Gets the "combineBrackets" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STCombineBrackets.Enum getCombineBrackets();

    /**
     * Gets (as xml) the "combineBrackets" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STCombineBrackets xgetCombineBrackets();

    /**
     * True if has "combineBrackets" attribute
     */
    boolean isSetCombineBrackets();

    /**
     * Sets the "combineBrackets" attribute
     */
    void setCombineBrackets(org.openxmlformats.schemas.wordprocessingml.x2006.main.STCombineBrackets.Enum combineBrackets);

    /**
     * Sets (as xml) the "combineBrackets" attribute
     */
    void xsetCombineBrackets(org.openxmlformats.schemas.wordprocessingml.x2006.main.STCombineBrackets combineBrackets);

    /**
     * Unsets the "combineBrackets" attribute
     */
    void unsetCombineBrackets();

    /**
     * Gets the "vert" attribute
     */
    java.lang.Object getVert();

    /**
     * Gets (as xml) the "vert" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetVert();

    /**
     * True if has "vert" attribute
     */
    boolean isSetVert();

    /**
     * Sets the "vert" attribute
     */
    void setVert(java.lang.Object vert);

    /**
     * Sets (as xml) the "vert" attribute
     */
    void xsetVert(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff vert);

    /**
     * Unsets the "vert" attribute
     */
    void unsetVert();

    /**
     * Gets the "vertCompress" attribute
     */
    java.lang.Object getVertCompress();

    /**
     * Gets (as xml) the "vertCompress" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetVertCompress();

    /**
     * True if has "vertCompress" attribute
     */
    boolean isSetVertCompress();

    /**
     * Sets the "vertCompress" attribute
     */
    void setVertCompress(java.lang.Object vertCompress);

    /**
     * Sets (as xml) the "vertCompress" attribute
     */
    void xsetVertCompress(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff vertCompress);

    /**
     * Unsets the "vertCompress" attribute
     */
    void unsetVertCompress();
}

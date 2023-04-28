/*
 * XML Type:  CT_NumPicBullet
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NumPicBullet(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTNumPicBullet extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnumpicbullet61e2type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pict" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture getPict();

    /**
     * True if has "pict" element
     */
    boolean isSetPict();

    /**
     * Sets the "pict" element
     */
    void setPict(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture pict);

    /**
     * Appends and returns a new empty "pict" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture addNewPict();

    /**
     * Unsets the "pict" element
     */
    void unsetPict();

    /**
     * Gets the "drawing" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing getDrawing();

    /**
     * True if has "drawing" element
     */
    boolean isSetDrawing();

    /**
     * Sets the "drawing" element
     */
    void setDrawing(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing drawing);

    /**
     * Appends and returns a new empty "drawing" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing addNewDrawing();

    /**
     * Unsets the "drawing" element
     */
    void unsetDrawing();

    /**
     * Gets the "numPicBulletId" attribute
     */
    java.math.BigInteger getNumPicBulletId();

    /**
     * Gets (as xml) the "numPicBulletId" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetNumPicBulletId();

    /**
     * Sets the "numPicBulletId" attribute
     */
    void setNumPicBulletId(java.math.BigInteger numPicBulletId);

    /**
     * Sets (as xml) the "numPicBulletId" attribute
     */
    void xsetNumPicBulletId(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber numPicBulletId);
}

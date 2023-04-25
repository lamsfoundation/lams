/*
 * XML Type:  CT_PermStart
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_PermStart(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPermStart extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPermStart> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpermstart0140type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "edGrp" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STEdGrp.Enum getEdGrp();

    /**
     * Gets (as xml) the "edGrp" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STEdGrp xgetEdGrp();

    /**
     * True if has "edGrp" attribute
     */
    boolean isSetEdGrp();

    /**
     * Sets the "edGrp" attribute
     */
    void setEdGrp(org.openxmlformats.schemas.wordprocessingml.x2006.main.STEdGrp.Enum edGrp);

    /**
     * Sets (as xml) the "edGrp" attribute
     */
    void xsetEdGrp(org.openxmlformats.schemas.wordprocessingml.x2006.main.STEdGrp edGrp);

    /**
     * Unsets the "edGrp" attribute
     */
    void unsetEdGrp();

    /**
     * Gets the "ed" attribute
     */
    java.lang.String getEd();

    /**
     * Gets (as xml) the "ed" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetEd();

    /**
     * True if has "ed" attribute
     */
    boolean isSetEd();

    /**
     * Sets the "ed" attribute
     */
    void setEd(java.lang.String ed);

    /**
     * Sets (as xml) the "ed" attribute
     */
    void xsetEd(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString ed);

    /**
     * Unsets the "ed" attribute
     */
    void unsetEd();

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

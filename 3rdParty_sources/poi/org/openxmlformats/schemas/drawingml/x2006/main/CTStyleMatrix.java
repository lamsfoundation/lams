/*
 * XML Type:  CT_StyleMatrix
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_StyleMatrix(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTStyleMatrix extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctstylematrix1903type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "fillStyleLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTFillStyleList getFillStyleLst();

    /**
     * Sets the "fillStyleLst" element
     */
    void setFillStyleLst(org.openxmlformats.schemas.drawingml.x2006.main.CTFillStyleList fillStyleLst);

    /**
     * Appends and returns a new empty "fillStyleLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTFillStyleList addNewFillStyleLst();

    /**
     * Gets the "lnStyleLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList getLnStyleLst();

    /**
     * Sets the "lnStyleLst" element
     */
    void setLnStyleLst(org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList lnStyleLst);

    /**
     * Appends and returns a new empty "lnStyleLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTLineStyleList addNewLnStyleLst();

    /**
     * Gets the "effectStyleLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleList getEffectStyleLst();

    /**
     * Sets the "effectStyleLst" element
     */
    void setEffectStyleLst(org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleList effectStyleLst);

    /**
     * Appends and returns a new empty "effectStyleLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleList addNewEffectStyleLst();

    /**
     * Gets the "bgFillStyleLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFillStyleList getBgFillStyleLst();

    /**
     * Sets the "bgFillStyleLst" element
     */
    void setBgFillStyleLst(org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFillStyleList bgFillStyleLst);

    /**
     * Appends and returns a new empty "bgFillStyleLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFillStyleList addNewBgFillStyleLst();

    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.apache.xmlbeans.XmlString xgetName();

    /**
     * True if has "name" attribute
     */
    boolean isSetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.apache.xmlbeans.XmlString name);

    /**
     * Unsets the "name" attribute
     */
    void unsetName();
}

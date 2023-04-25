/*
 * XML Type:  CT_ShapeStyle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ShapeStyle(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTShapeStyle extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctshapestyle81ebtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "lnRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference getLnRef();

    /**
     * Sets the "lnRef" element
     */
    void setLnRef(org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference lnRef);

    /**
     * Appends and returns a new empty "lnRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference addNewLnRef();

    /**
     * Gets the "fillRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference getFillRef();

    /**
     * Sets the "fillRef" element
     */
    void setFillRef(org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference fillRef);

    /**
     * Appends and returns a new empty "fillRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference addNewFillRef();

    /**
     * Gets the "effectRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference getEffectRef();

    /**
     * Sets the "effectRef" element
     */
    void setEffectRef(org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference effectRef);

    /**
     * Appends and returns a new empty "effectRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference addNewEffectRef();

    /**
     * Gets the "fontRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference getFontRef();

    /**
     * Sets the "fontRef" element
     */
    void setFontRef(org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference fontRef);

    /**
     * Appends and returns a new empty "fontRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTFontReference addNewFontRef();
}

/*
 * XML Type:  CT_TableBackgroundStyle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTableBackgroundStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TableBackgroundStyle(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTableBackgroundStyle extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTableBackgroundStyle> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttablebackgroundstyleead0type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "fill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTFillProperties getFill();

    /**
     * True if has "fill" element
     */
    boolean isSetFill();

    /**
     * Sets the "fill" element
     */
    void setFill(org.openxmlformats.schemas.drawingml.x2006.main.CTFillProperties fill);

    /**
     * Appends and returns a new empty "fill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTFillProperties addNewFill();

    /**
     * Unsets the "fill" element
     */
    void unsetFill();

    /**
     * Gets the "fillRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference getFillRef();

    /**
     * True if has "fillRef" element
     */
    boolean isSetFillRef();

    /**
     * Sets the "fillRef" element
     */
    void setFillRef(org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference fillRef);

    /**
     * Appends and returns a new empty "fillRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference addNewFillRef();

    /**
     * Unsets the "fillRef" element
     */
    void unsetFillRef();

    /**
     * Gets the "effect" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEffectProperties getEffect();

    /**
     * True if has "effect" element
     */
    boolean isSetEffect();

    /**
     * Sets the "effect" element
     */
    void setEffect(org.openxmlformats.schemas.drawingml.x2006.main.CTEffectProperties effect);

    /**
     * Appends and returns a new empty "effect" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEffectProperties addNewEffect();

    /**
     * Unsets the "effect" element
     */
    void unsetEffect();

    /**
     * Gets the "effectRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference getEffectRef();

    /**
     * True if has "effectRef" element
     */
    boolean isSetEffectRef();

    /**
     * Sets the "effectRef" element
     */
    void setEffectRef(org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference effectRef);

    /**
     * Appends and returns a new empty "effectRef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrixReference addNewEffectRef();

    /**
     * Unsets the "effectRef" element
     */
    void unsetEffectRef();
}

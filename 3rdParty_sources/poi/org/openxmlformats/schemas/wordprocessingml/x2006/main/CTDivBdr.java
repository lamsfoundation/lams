/*
 * XML Type:  CT_DivBdr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivBdr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DivBdr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDivBdr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDivBdr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdivbdr6ce9type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "top" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder getTop();

    /**
     * True if has "top" element
     */
    boolean isSetTop();

    /**
     * Sets the "top" element
     */
    void setTop(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder top);

    /**
     * Appends and returns a new empty "top" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder addNewTop();

    /**
     * Unsets the "top" element
     */
    void unsetTop();

    /**
     * Gets the "left" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder getLeft();

    /**
     * True if has "left" element
     */
    boolean isSetLeft();

    /**
     * Sets the "left" element
     */
    void setLeft(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder left);

    /**
     * Appends and returns a new empty "left" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder addNewLeft();

    /**
     * Unsets the "left" element
     */
    void unsetLeft();

    /**
     * Gets the "bottom" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder getBottom();

    /**
     * True if has "bottom" element
     */
    boolean isSetBottom();

    /**
     * Sets the "bottom" element
     */
    void setBottom(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder bottom);

    /**
     * Appends and returns a new empty "bottom" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder addNewBottom();

    /**
     * Unsets the "bottom" element
     */
    void unsetBottom();

    /**
     * Gets the "right" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder getRight();

    /**
     * True if has "right" element
     */
    boolean isSetRight();

    /**
     * Sets the "right" element
     */
    void setRight(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder right);

    /**
     * Appends and returns a new empty "right" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder addNewRight();

    /**
     * Unsets the "right" element
     */
    void unsetRight();
}

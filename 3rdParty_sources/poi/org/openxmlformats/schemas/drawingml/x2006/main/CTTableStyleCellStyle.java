/*
 * XML Type:  CT_TableStyleCellStyle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TableStyleCellStyle(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTableStyleCellStyle extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTTableStyleCellStyle> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttablestylecellstyle1fddtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tcBdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellBorderStyle getTcBdr();

    /**
     * True if has "tcBdr" element
     */
    boolean isSetTcBdr();

    /**
     * Sets the "tcBdr" element
     */
    void setTcBdr(org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellBorderStyle tcBdr);

    /**
     * Appends and returns a new empty "tcBdr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTableCellBorderStyle addNewTcBdr();

    /**
     * Unsets the "tcBdr" element
     */
    void unsetTcBdr();

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
     * Gets the "cell3D" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D getCell3D();

    /**
     * True if has "cell3D" element
     */
    boolean isSetCell3D();

    /**
     * Sets the "cell3D" element
     */
    void setCell3D(org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D cell3D);

    /**
     * Appends and returns a new empty "cell3D" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTCell3D addNewCell3D();

    /**
     * Unsets the "cell3D" element
     */
    void unsetCell3D();
}

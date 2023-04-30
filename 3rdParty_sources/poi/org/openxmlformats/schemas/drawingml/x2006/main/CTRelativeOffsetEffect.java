/*
 * XML Type:  CT_RelativeOffsetEffect
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeOffsetEffect
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_RelativeOffsetEffect(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTRelativeOffsetEffect extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTRelativeOffsetEffect> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrelativeoffseteffect220btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tx" attribute
     */
    java.lang.Object getTx();

    /**
     * Gets (as xml) the "tx" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetTx();

    /**
     * True if has "tx" attribute
     */
    boolean isSetTx();

    /**
     * Sets the "tx" attribute
     */
    void setTx(java.lang.Object tx);

    /**
     * Sets (as xml) the "tx" attribute
     */
    void xsetTx(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage tx);

    /**
     * Unsets the "tx" attribute
     */
    void unsetTx();

    /**
     * Gets the "ty" attribute
     */
    java.lang.Object getTy();

    /**
     * Gets (as xml) the "ty" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STPercentage xgetTy();

    /**
     * True if has "ty" attribute
     */
    boolean isSetTy();

    /**
     * Sets the "ty" attribute
     */
    void setTy(java.lang.Object ty);

    /**
     * Sets (as xml) the "ty" attribute
     */
    void xsetTy(org.openxmlformats.schemas.drawingml.x2006.main.STPercentage ty);

    /**
     * Unsets the "ty" attribute
     */
    void unsetTy();
}

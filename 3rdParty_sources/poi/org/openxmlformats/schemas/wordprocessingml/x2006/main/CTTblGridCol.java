/*
 * XML Type:  CT_TblGridCol
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TblGridCol(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTblGridCol extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttblgridcolbfectype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "w" attribute
     */
    java.lang.Object getW();

    /**
     * Gets (as xml) the "w" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetW();

    /**
     * True if has "w" attribute
     */
    boolean isSetW();

    /**
     * Sets the "w" attribute
     */
    void setW(java.lang.Object w);

    /**
     * Sets (as xml) the "w" attribute
     */
    void xsetW(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure w);

    /**
     * Unsets the "w" attribute
     */
    void unsetW();
}

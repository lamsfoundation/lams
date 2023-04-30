/*
 * XML Type:  CT_SdtText
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtText
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SdtText(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSdtText extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtText> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsdttext0a82type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "multiLine" attribute
     */
    java.lang.Object getMultiLine();

    /**
     * Gets (as xml) the "multiLine" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetMultiLine();

    /**
     * True if has "multiLine" attribute
     */
    boolean isSetMultiLine();

    /**
     * Sets the "multiLine" attribute
     */
    void setMultiLine(java.lang.Object multiLine);

    /**
     * Sets (as xml) the "multiLine" attribute
     */
    void xsetMultiLine(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff multiLine);

    /**
     * Unsets the "multiLine" attribute
     */
    void unsetMultiLine();
}

/*
 * XML Type:  CT_Vstream
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVstream
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Vstream(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVstream.
 */
public interface CTVstream extends org.apache.xmlbeans.XmlBase64Binary {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVstream> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctvstream1579type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "version" attribute
     */
    java.lang.String getVersion();

    /**
     * Gets (as xml) the "version" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid xgetVersion();

    /**
     * True if has "version" attribute
     */
    boolean isSetVersion();

    /**
     * Sets the "version" attribute
     */
    void setVersion(java.lang.String version);

    /**
     * Sets (as xml) the "version" attribute
     */
    void xsetVersion(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid version);

    /**
     * Unsets the "version" attribute
     */
    void unsetVersion();
}

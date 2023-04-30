/*
 * XML Type:  CT_SmartTagType
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SmartTagType(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSmartTagType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSmartTagType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsmarttagtypedc74type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "namespaceUri" attribute
     */
    java.lang.String getNamespaceUri();

    /**
     * Gets (as xml) the "namespaceUri" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetNamespaceUri();

    /**
     * True if has "namespaceUri" attribute
     */
    boolean isSetNamespaceUri();

    /**
     * Sets the "namespaceUri" attribute
     */
    void setNamespaceUri(java.lang.String namespaceUri);

    /**
     * Sets (as xml) the "namespaceUri" attribute
     */
    void xsetNamespaceUri(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring namespaceUri);

    /**
     * Unsets the "namespaceUri" attribute
     */
    void unsetNamespaceUri();

    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetName();

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
    void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring name);

    /**
     * Unsets the "name" attribute
     */
    void unsetName();

    /**
     * Gets the "url" attribute
     */
    java.lang.String getUrl();

    /**
     * Gets (as xml) the "url" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetUrl();

    /**
     * True if has "url" attribute
     */
    boolean isSetUrl();

    /**
     * Sets the "url" attribute
     */
    void setUrl(java.lang.String url);

    /**
     * Sets (as xml) the "url" attribute
     */
    void xsetUrl(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring url);

    /**
     * Unsets the "url" attribute
     */
    void unsetUrl();
}

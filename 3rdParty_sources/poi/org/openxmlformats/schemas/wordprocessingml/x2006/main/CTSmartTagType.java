/*
 * XML Type:  CT_SmartTagType
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SmartTagType(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSmartTagType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsmarttagtypeae5dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "namespaceuri" attribute
     */
    java.lang.String getNamespaceuri();

    /**
     * Gets (as xml) the "namespaceuri" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetNamespaceuri();

    /**
     * True if has "namespaceuri" attribute
     */
    boolean isSetNamespaceuri();

    /**
     * Sets the "namespaceuri" attribute
     */
    void setNamespaceuri(java.lang.String namespaceuri);

    /**
     * Sets (as xml) the "namespaceuri" attribute
     */
    void xsetNamespaceuri(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString namespaceuri);

    /**
     * Unsets the "namespaceuri" attribute
     */
    void unsetNamespaceuri();

    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetName();

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
    void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString name);

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
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetUrl();

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
    void xsetUrl(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString url);

    /**
     * Unsets the "url" attribute
     */
    void unsetUrl();
}

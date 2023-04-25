/*
 * XML Type:  IdentifierType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.IdentifierType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML IdentifierType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is an atomic type that is a restriction of org.etsi.uri.x01903.v13.IdentifierType.
 */
public interface IdentifierType extends org.apache.xmlbeans.XmlAnyURI {
    DocumentFactory<org.etsi.uri.x01903.v13.IdentifierType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "identifiertype2cb7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Qualifier" attribute
     */
    org.etsi.uri.x01903.v13.QualifierType.Enum getQualifier();

    /**
     * Gets (as xml) the "Qualifier" attribute
     */
    org.etsi.uri.x01903.v13.QualifierType xgetQualifier();

    /**
     * True if has "Qualifier" attribute
     */
    boolean isSetQualifier();

    /**
     * Sets the "Qualifier" attribute
     */
    void setQualifier(org.etsi.uri.x01903.v13.QualifierType.Enum qualifier);

    /**
     * Sets (as xml) the "Qualifier" attribute
     */
    void xsetQualifier(org.etsi.uri.x01903.v13.QualifierType qualifier);

    /**
     * Unsets the "Qualifier" attribute
     */
    void unsetQualifier();
}

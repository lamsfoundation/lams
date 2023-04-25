/*
 * XML Type:  CRLIdentifierType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CRLIdentifierType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CRLIdentifierType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface CRLIdentifierType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CRLIdentifierType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "crlidentifiertypeb702type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Issuer" element
     */
    java.lang.String getIssuer();

    /**
     * Gets (as xml) the "Issuer" element
     */
    org.apache.xmlbeans.XmlString xgetIssuer();

    /**
     * Sets the "Issuer" element
     */
    void setIssuer(java.lang.String issuer);

    /**
     * Sets (as xml) the "Issuer" element
     */
    void xsetIssuer(org.apache.xmlbeans.XmlString issuer);

    /**
     * Gets the "IssueTime" element
     */
    java.util.Calendar getIssueTime();

    /**
     * Gets (as xml) the "IssueTime" element
     */
    org.apache.xmlbeans.XmlDateTime xgetIssueTime();

    /**
     * Sets the "IssueTime" element
     */
    void setIssueTime(java.util.Calendar issueTime);

    /**
     * Sets (as xml) the "IssueTime" element
     */
    void xsetIssueTime(org.apache.xmlbeans.XmlDateTime issueTime);

    /**
     * Gets the "Number" element
     */
    java.math.BigInteger getNumber();

    /**
     * Gets (as xml) the "Number" element
     */
    org.apache.xmlbeans.XmlInteger xgetNumber();

    /**
     * True if has "Number" element
     */
    boolean isSetNumber();

    /**
     * Sets the "Number" element
     */
    void setNumber(java.math.BigInteger number);

    /**
     * Sets (as xml) the "Number" element
     */
    void xsetNumber(org.apache.xmlbeans.XmlInteger number);

    /**
     * Unsets the "Number" element
     */
    void unsetNumber();

    /**
     * Gets the "URI" attribute
     */
    java.lang.String getURI();

    /**
     * Gets (as xml) the "URI" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetURI();

    /**
     * True if has "URI" attribute
     */
    boolean isSetURI();

    /**
     * Sets the "URI" attribute
     */
    void setURI(java.lang.String uri);

    /**
     * Sets (as xml) the "URI" attribute
     */
    void xsetURI(org.apache.xmlbeans.XmlAnyURI uri);

    /**
     * Unsets the "URI" attribute
     */
    void unsetURI();
}

/*
 * XML Type:  CRLRefsType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CRLRefsType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CRLRefsType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface CRLRefsType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CRLRefsType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "crlrefstype2a59type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "CRLRef" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.CRLRefType> getCRLRefList();

    /**
     * Gets array of all "CRLRef" elements
     */
    org.etsi.uri.x01903.v13.CRLRefType[] getCRLRefArray();

    /**
     * Gets ith "CRLRef" element
     */
    org.etsi.uri.x01903.v13.CRLRefType getCRLRefArray(int i);

    /**
     * Returns number of "CRLRef" element
     */
    int sizeOfCRLRefArray();

    /**
     * Sets array of all "CRLRef" element
     */
    void setCRLRefArray(org.etsi.uri.x01903.v13.CRLRefType[] crlRefArray);

    /**
     * Sets ith "CRLRef" element
     */
    void setCRLRefArray(int i, org.etsi.uri.x01903.v13.CRLRefType crlRef);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CRLRef" element
     */
    org.etsi.uri.x01903.v13.CRLRefType insertNewCRLRef(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "CRLRef" element
     */
    org.etsi.uri.x01903.v13.CRLRefType addNewCRLRef();

    /**
     * Removes the ith "CRLRef" element
     */
    void removeCRLRef(int i);
}

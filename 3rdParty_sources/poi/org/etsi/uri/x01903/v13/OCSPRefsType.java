/*
 * XML Type:  OCSPRefsType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.OCSPRefsType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML OCSPRefsType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface OCSPRefsType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.OCSPRefsType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ocsprefstypef13ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "OCSPRef" elements
     */
    java.util.List<org.etsi.uri.x01903.v13.OCSPRefType> getOCSPRefList();

    /**
     * Gets array of all "OCSPRef" elements
     */
    org.etsi.uri.x01903.v13.OCSPRefType[] getOCSPRefArray();

    /**
     * Gets ith "OCSPRef" element
     */
    org.etsi.uri.x01903.v13.OCSPRefType getOCSPRefArray(int i);

    /**
     * Returns number of "OCSPRef" element
     */
    int sizeOfOCSPRefArray();

    /**
     * Sets array of all "OCSPRef" element
     */
    void setOCSPRefArray(org.etsi.uri.x01903.v13.OCSPRefType[] ocspRefArray);

    /**
     * Sets ith "OCSPRef" element
     */
    void setOCSPRefArray(int i, org.etsi.uri.x01903.v13.OCSPRefType ocspRef);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "OCSPRef" element
     */
    org.etsi.uri.x01903.v13.OCSPRefType insertNewOCSPRef(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "OCSPRef" element
     */
    org.etsi.uri.x01903.v13.OCSPRefType addNewOCSPRef();

    /**
     * Removes the ith "OCSPRef" element
     */
    void removeOCSPRef(int i);
}

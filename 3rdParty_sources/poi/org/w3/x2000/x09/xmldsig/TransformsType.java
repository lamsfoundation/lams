/*
 * XML Type:  TransformsType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.TransformsType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML TransformsType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface TransformsType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.TransformsType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "transformstype659etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "Transform" elements
     */
    java.util.List<org.w3.x2000.x09.xmldsig.TransformType> getTransformList();

    /**
     * Gets array of all "Transform" elements
     */
    org.w3.x2000.x09.xmldsig.TransformType[] getTransformArray();

    /**
     * Gets ith "Transform" element
     */
    org.w3.x2000.x09.xmldsig.TransformType getTransformArray(int i);

    /**
     * Returns number of "Transform" element
     */
    int sizeOfTransformArray();

    /**
     * Sets array of all "Transform" element
     */
    void setTransformArray(org.w3.x2000.x09.xmldsig.TransformType[] transformArray);

    /**
     * Sets ith "Transform" element
     */
    void setTransformArray(int i, org.w3.x2000.x09.xmldsig.TransformType transform);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Transform" element
     */
    org.w3.x2000.x09.xmldsig.TransformType insertNewTransform(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "Transform" element
     */
    org.w3.x2000.x09.xmldsig.TransformType addNewTransform();

    /**
     * Removes the ith "Transform" element
     */
    void removeTransform(int i);
}

/*
 * XML Type:  CT_RelationshipReference
 * Namespace: http://schemas.openxmlformats.org/package/2006/digital-signature
 * Java type: org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.digitalSignature;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_RelationshipReference(@http://schemas.openxmlformats.org/package/2006/digital-signature).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference.
 */
public interface CTRelationshipReference extends org.apache.xmlbeans.XmlString {
    DocumentFactory<org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipReference> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrelationshipreferencee68ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SourceId" attribute
     */
    java.lang.String getSourceId();

    /**
     * Gets (as xml) the "SourceId" attribute
     */
    org.apache.xmlbeans.XmlString xgetSourceId();

    /**
     * Sets the "SourceId" attribute
     */
    void setSourceId(java.lang.String sourceId);

    /**
     * Sets (as xml) the "SourceId" attribute
     */
    void xsetSourceId(org.apache.xmlbeans.XmlString sourceId);
}

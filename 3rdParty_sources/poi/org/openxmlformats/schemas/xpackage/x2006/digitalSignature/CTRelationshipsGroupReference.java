/*
 * XML Type:  CT_RelationshipsGroupReference
 * Namespace: http://schemas.openxmlformats.org/package/2006/digital-signature
 * Java type: org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.digitalSignature;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_RelationshipsGroupReference(@http://schemas.openxmlformats.org/package/2006/digital-signature).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference.
 */
public interface CTRelationshipsGroupReference extends org.apache.xmlbeans.XmlString {
    DocumentFactory<org.openxmlformats.schemas.xpackage.x2006.digitalSignature.CTRelationshipsGroupReference> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrelationshipsgroupreferencea39btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SourceType" attribute
     */
    java.lang.String getSourceType();

    /**
     * Gets (as xml) the "SourceType" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetSourceType();

    /**
     * Sets the "SourceType" attribute
     */
    void setSourceType(java.lang.String sourceType);

    /**
     * Sets (as xml) the "SourceType" attribute
     */
    void xsetSourceType(org.apache.xmlbeans.XmlAnyURI sourceType);
}

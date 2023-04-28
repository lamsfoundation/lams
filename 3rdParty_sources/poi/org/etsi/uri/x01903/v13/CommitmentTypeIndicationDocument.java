/*
 * An XML document type.
 * Localname: CommitmentTypeIndication
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.CommitmentTypeIndicationDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one CommitmentTypeIndication(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface CommitmentTypeIndicationDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.CommitmentTypeIndicationDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "commitmenttypeindication48afdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "CommitmentTypeIndication" element
     */
    org.etsi.uri.x01903.v13.CommitmentTypeIndicationType getCommitmentTypeIndication();

    /**
     * Sets the "CommitmentTypeIndication" element
     */
    void setCommitmentTypeIndication(org.etsi.uri.x01903.v13.CommitmentTypeIndicationType commitmentTypeIndication);

    /**
     * Appends and returns a new empty "CommitmentTypeIndication" element
     */
    org.etsi.uri.x01903.v13.CommitmentTypeIndicationType addNewCommitmentTypeIndication();
}

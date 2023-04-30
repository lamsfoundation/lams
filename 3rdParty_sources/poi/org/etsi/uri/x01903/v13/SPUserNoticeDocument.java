/*
 * An XML document type.
 * Localname: SPUserNotice
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SPUserNoticeDocument
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one SPUserNotice(@http://uri.etsi.org/01903/v1.3.2#) element.
 *
 * This is a complex type.
 */
public interface SPUserNoticeDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SPUserNoticeDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "spusernotice0ee4doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "SPUserNotice" element
     */
    org.etsi.uri.x01903.v13.SPUserNoticeType getSPUserNotice();

    /**
     * Sets the "SPUserNotice" element
     */
    void setSPUserNotice(org.etsi.uri.x01903.v13.SPUserNoticeType spUserNotice);

    /**
     * Appends and returns a new empty "SPUserNotice" element
     */
    org.etsi.uri.x01903.v13.SPUserNoticeType addNewSPUserNotice();
}

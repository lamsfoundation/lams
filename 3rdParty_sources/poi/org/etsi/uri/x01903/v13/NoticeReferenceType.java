/*
 * XML Type:  NoticeReferenceType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.NoticeReferenceType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML NoticeReferenceType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface NoticeReferenceType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.NoticeReferenceType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "noticereferencetype286ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "Organization" element
     */
    java.lang.String getOrganization();

    /**
     * Gets (as xml) the "Organization" element
     */
    org.apache.xmlbeans.XmlString xgetOrganization();

    /**
     * Sets the "Organization" element
     */
    void setOrganization(java.lang.String organization);

    /**
     * Sets (as xml) the "Organization" element
     */
    void xsetOrganization(org.apache.xmlbeans.XmlString organization);

    /**
     * Gets the "NoticeNumbers" element
     */
    org.etsi.uri.x01903.v13.IntegerListType getNoticeNumbers();

    /**
     * Sets the "NoticeNumbers" element
     */
    void setNoticeNumbers(org.etsi.uri.x01903.v13.IntegerListType noticeNumbers);

    /**
     * Appends and returns a new empty "NoticeNumbers" element
     */
    org.etsi.uri.x01903.v13.IntegerListType addNewNoticeNumbers();
}

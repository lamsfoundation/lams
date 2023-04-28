/*
 * XML Type:  SPUserNoticeType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.SPUserNoticeType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML SPUserNoticeType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is a complex type.
 */
public interface SPUserNoticeType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.etsi.uri.x01903.v13.SPUserNoticeType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "spusernoticetypefd2etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "NoticeRef" element
     */
    org.etsi.uri.x01903.v13.NoticeReferenceType getNoticeRef();

    /**
     * True if has "NoticeRef" element
     */
    boolean isSetNoticeRef();

    /**
     * Sets the "NoticeRef" element
     */
    void setNoticeRef(org.etsi.uri.x01903.v13.NoticeReferenceType noticeRef);

    /**
     * Appends and returns a new empty "NoticeRef" element
     */
    org.etsi.uri.x01903.v13.NoticeReferenceType addNewNoticeRef();

    /**
     * Unsets the "NoticeRef" element
     */
    void unsetNoticeRef();

    /**
     * Gets the "ExplicitText" element
     */
    java.lang.String getExplicitText();

    /**
     * Gets (as xml) the "ExplicitText" element
     */
    org.apache.xmlbeans.XmlString xgetExplicitText();

    /**
     * True if has "ExplicitText" element
     */
    boolean isSetExplicitText();

    /**
     * Sets the "ExplicitText" element
     */
    void setExplicitText(java.lang.String explicitText);

    /**
     * Sets (as xml) the "ExplicitText" element
     */
    void xsetExplicitText(org.apache.xmlbeans.XmlString explicitText);

    /**
     * Unsets the "ExplicitText" element
     */
    void unsetExplicitText();
}

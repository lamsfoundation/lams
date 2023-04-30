/*
 * XML Type:  CT_CommentAuthor
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CommentAuthor(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCommentAuthor extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTCommentAuthor> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcommentauthora405type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "id" attribute
     */
    long getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetId();

    /**
     * Sets the "id" attribute
     */
    void setId(long id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.apache.xmlbeans.XmlUnsignedInt id);

    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STName xgetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.openxmlformats.schemas.presentationml.x2006.main.STName name);

    /**
     * Gets the "initials" attribute
     */
    java.lang.String getInitials();

    /**
     * Gets (as xml) the "initials" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STName xgetInitials();

    /**
     * Sets the "initials" attribute
     */
    void setInitials(java.lang.String initials);

    /**
     * Sets (as xml) the "initials" attribute
     */
    void xsetInitials(org.openxmlformats.schemas.presentationml.x2006.main.STName initials);

    /**
     * Gets the "lastIdx" attribute
     */
    long getLastIdx();

    /**
     * Gets (as xml) the "lastIdx" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetLastIdx();

    /**
     * Sets the "lastIdx" attribute
     */
    void setLastIdx(long lastIdx);

    /**
     * Sets (as xml) the "lastIdx" attribute
     */
    void xsetLastIdx(org.apache.xmlbeans.XmlUnsignedInt lastIdx);

    /**
     * Gets the "clrIdx" attribute
     */
    long getClrIdx();

    /**
     * Gets (as xml) the "clrIdx" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetClrIdx();

    /**
     * Sets the "clrIdx" attribute
     */
    void setClrIdx(long clrIdx);

    /**
     * Sets (as xml) the "clrIdx" attribute
     */
    void xsetClrIdx(org.apache.xmlbeans.XmlUnsignedInt clrIdx);
}

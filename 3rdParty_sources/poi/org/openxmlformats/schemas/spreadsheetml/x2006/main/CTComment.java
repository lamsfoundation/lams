/*
 * XML Type:  CT_Comment
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComment
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Comment(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTComment extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComment> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcomment7bfetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "text" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst getText();

    /**
     * Sets the "text" element
     */
    void setText(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst text);

    /**
     * Appends and returns a new empty "text" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst addNewText();

    /**
     * Gets the "commentPr" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentPr getCommentPr();

    /**
     * True if has "commentPr" element
     */
    boolean isSetCommentPr();

    /**
     * Sets the "commentPr" element
     */
    void setCommentPr(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentPr commentPr);

    /**
     * Appends and returns a new empty "commentPr" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCommentPr addNewCommentPr();

    /**
     * Unsets the "commentPr" element
     */
    void unsetCommentPr();

    /**
     * Gets the "ref" attribute
     */
    java.lang.String getRef();

    /**
     * Gets (as xml) the "ref" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef xgetRef();

    /**
     * Sets the "ref" attribute
     */
    void setRef(java.lang.String ref);

    /**
     * Sets (as xml) the "ref" attribute
     */
    void xsetRef(org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef ref);

    /**
     * Gets the "authorId" attribute
     */
    long getAuthorId();

    /**
     * Gets (as xml) the "authorId" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetAuthorId();

    /**
     * Sets the "authorId" attribute
     */
    void setAuthorId(long authorId);

    /**
     * Sets (as xml) the "authorId" attribute
     */
    void xsetAuthorId(org.apache.xmlbeans.XmlUnsignedInt authorId);

    /**
     * Gets the "guid" attribute
     */
    java.lang.String getGuid();

    /**
     * Gets (as xml) the "guid" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid xgetGuid();

    /**
     * True if has "guid" attribute
     */
    boolean isSetGuid();

    /**
     * Sets the "guid" attribute
     */
    void setGuid(java.lang.String guid);

    /**
     * Sets (as xml) the "guid" attribute
     */
    void xsetGuid(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid guid);

    /**
     * Unsets the "guid" attribute
     */
    void unsetGuid();

    /**
     * Gets the "shapeId" attribute
     */
    long getShapeId();

    /**
     * Gets (as xml) the "shapeId" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetShapeId();

    /**
     * True if has "shapeId" attribute
     */
    boolean isSetShapeId();

    /**
     * Sets the "shapeId" attribute
     */
    void setShapeId(long shapeId);

    /**
     * Sets (as xml) the "shapeId" attribute
     */
    void xsetShapeId(org.apache.xmlbeans.XmlUnsignedInt shapeId);

    /**
     * Unsets the "shapeId" attribute
     */
    void unsetShapeId();
}

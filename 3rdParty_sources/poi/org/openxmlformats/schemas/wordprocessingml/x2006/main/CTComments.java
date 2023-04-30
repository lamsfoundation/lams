/*
 * XML Type:  CT_Comments
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComments
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Comments(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTComments extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComments> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcomments7674type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "comment" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment> getCommentList();

    /**
     * Gets array of all "comment" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment[] getCommentArray();

    /**
     * Gets ith "comment" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment getCommentArray(int i);

    /**
     * Returns number of "comment" element
     */
    int sizeOfCommentArray();

    /**
     * Sets array of all "comment" element
     */
    void setCommentArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment[] commentArray);

    /**
     * Sets ith "comment" element
     */
    void setCommentArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment comment);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "comment" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment insertNewComment(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "comment" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment addNewComment();

    /**
     * Removes the ith "comment" element
     */
    void removeComment(int i);
}

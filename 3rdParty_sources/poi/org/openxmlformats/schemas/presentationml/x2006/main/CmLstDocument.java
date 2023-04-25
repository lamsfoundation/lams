/*
 * An XML document type.
 * Localname: cmLst
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CmLstDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one cmLst(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public interface CmLstDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CmLstDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cmlst3880doctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cmLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList getCmLst();

    /**
     * Sets the "cmLst" element
     */
    void setCmLst(org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList cmLst);

    /**
     * Appends and returns a new empty "cmLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCommentList addNewCmLst();
}

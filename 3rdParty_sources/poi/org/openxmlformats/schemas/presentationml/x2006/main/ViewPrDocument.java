/*
 * An XML document type.
 * Localname: viewPr
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.ViewPrDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one viewPr(@http://schemas.openxmlformats.org/presentationml/2006/main) element.
 *
 * This is a complex type.
 */
public interface ViewPrDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.ViewPrDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "viewpr43aadoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "viewPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTViewProperties getViewPr();

    /**
     * Sets the "viewPr" element
     */
    void setViewPr(org.openxmlformats.schemas.presentationml.x2006.main.CTViewProperties viewPr);

    /**
     * Appends and returns a new empty "viewPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTViewProperties addNewViewPr();
}

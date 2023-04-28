/*
 * An XML document type.
 * Localname: recipients
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.RecipientsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one recipients(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface RecipientsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.RecipientsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "recipients6a1adoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "recipients" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipients getRecipients();

    /**
     * Sets the "recipients" element
     */
    void setRecipients(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipients recipients);

    /**
     * Appends and returns a new empty "recipients" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRecipients addNewRecipients();
}

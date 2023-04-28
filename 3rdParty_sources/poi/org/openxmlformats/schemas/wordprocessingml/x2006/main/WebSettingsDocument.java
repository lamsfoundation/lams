/*
 * An XML document type.
 * Localname: webSettings
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.WebSettingsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * A document containing one webSettings(@http://schemas.openxmlformats.org/wordprocessingml/2006/main) element.
 *
 * This is a complex type.
 */
public interface WebSettingsDocument extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.WebSettingsDocument> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "websettings0b2fdoctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "webSettings" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWebSettings getWebSettings();

    /**
     * Sets the "webSettings" element
     */
    void setWebSettings(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWebSettings webSettings);

    /**
     * Appends and returns a new empty "webSettings" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWebSettings addNewWebSettings();
}

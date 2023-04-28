/*
 * XML Type:  CT_Placeholder
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPlaceholder
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Placeholder(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPlaceholder extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPlaceholder> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctplaceholder117ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "docPart" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getDocPart();

    /**
     * Sets the "docPart" element
     */
    void setDocPart(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString docPart);

    /**
     * Appends and returns a new empty "docPart" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewDocPart();
}

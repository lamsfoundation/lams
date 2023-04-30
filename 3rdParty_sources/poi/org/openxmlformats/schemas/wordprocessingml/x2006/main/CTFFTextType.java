/*
 * XML Type:  CT_FFTextType
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FFTextType(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFFTextType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFFTextType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfftexttype07c1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "val" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STFFTextType.Enum getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STFFTextType xgetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STFFTextType.Enum val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STFFTextType val);
}

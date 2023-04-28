/*
 * XML Type:  CT_Kinsoku
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTKinsoku
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Kinsoku(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTKinsoku extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTKinsoku> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctkinsoku0179type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "lang" attribute
     */
    java.lang.String getLang();

    /**
     * Gets (as xml) the "lang" attribute
     */
    org.apache.xmlbeans.XmlString xgetLang();

    /**
     * True if has "lang" attribute
     */
    boolean isSetLang();

    /**
     * Sets the "lang" attribute
     */
    void setLang(java.lang.String lang);

    /**
     * Sets (as xml) the "lang" attribute
     */
    void xsetLang(org.apache.xmlbeans.XmlString lang);

    /**
     * Unsets the "lang" attribute
     */
    void unsetLang();

    /**
     * Gets the "invalStChars" attribute
     */
    java.lang.String getInvalStChars();

    /**
     * Gets (as xml) the "invalStChars" attribute
     */
    org.apache.xmlbeans.XmlString xgetInvalStChars();

    /**
     * Sets the "invalStChars" attribute
     */
    void setInvalStChars(java.lang.String invalStChars);

    /**
     * Sets (as xml) the "invalStChars" attribute
     */
    void xsetInvalStChars(org.apache.xmlbeans.XmlString invalStChars);

    /**
     * Gets the "invalEndChars" attribute
     */
    java.lang.String getInvalEndChars();

    /**
     * Gets (as xml) the "invalEndChars" attribute
     */
    org.apache.xmlbeans.XmlString xgetInvalEndChars();

    /**
     * Sets the "invalEndChars" attribute
     */
    void setInvalEndChars(java.lang.String invalEndChars);

    /**
     * Sets (as xml) the "invalEndChars" attribute
     */
    void xsetInvalEndChars(org.apache.xmlbeans.XmlString invalEndChars);
}

/*
 * XML Type:  CT_ShowInfoBrowse
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTShowInfoBrowse
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ShowInfoBrowse(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTShowInfoBrowse extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTShowInfoBrowse> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctshowinfobrowse7a90type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "showScrollbar" attribute
     */
    boolean getShowScrollbar();

    /**
     * Gets (as xml) the "showScrollbar" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetShowScrollbar();

    /**
     * True if has "showScrollbar" attribute
     */
    boolean isSetShowScrollbar();

    /**
     * Sets the "showScrollbar" attribute
     */
    void setShowScrollbar(boolean showScrollbar);

    /**
     * Sets (as xml) the "showScrollbar" attribute
     */
    void xsetShowScrollbar(org.apache.xmlbeans.XmlBoolean showScrollbar);

    /**
     * Unsets the "showScrollbar" attribute
     */
    void unsetShowScrollbar();
}

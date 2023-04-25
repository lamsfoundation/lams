/*
 * XML Type:  CT_ShowInfoKiosk
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTShowInfoKiosk
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ShowInfoKiosk(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTShowInfoKiosk extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTShowInfoKiosk> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctshowinfokiosk99f1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "restart" attribute
     */
    long getRestart();

    /**
     * Gets (as xml) the "restart" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetRestart();

    /**
     * True if has "restart" attribute
     */
    boolean isSetRestart();

    /**
     * Sets the "restart" attribute
     */
    void setRestart(long restart);

    /**
     * Sets (as xml) the "restart" attribute
     */
    void xsetRestart(org.apache.xmlbeans.XmlUnsignedInt restart);

    /**
     * Unsets the "restart" attribute
     */
    void unsetRestart();
}

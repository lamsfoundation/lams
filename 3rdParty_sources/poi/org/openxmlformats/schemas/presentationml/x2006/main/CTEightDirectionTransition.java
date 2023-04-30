/*
 * XML Type:  CT_EightDirectionTransition
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_EightDirectionTransition(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTEightDirectionTransition extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cteightdirectiontransition1d60type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "dir" attribute
     */
    java.lang.String getDir();

    /**
     * Gets (as xml) the "dir" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTransitionEightDirectionType xgetDir();

    /**
     * True if has "dir" attribute
     */
    boolean isSetDir();

    /**
     * Sets the "dir" attribute
     */
    void setDir(java.lang.String dir);

    /**
     * Sets (as xml) the "dir" attribute
     */
    void xsetDir(org.openxmlformats.schemas.presentationml.x2006.main.STTransitionEightDirectionType dir);

    /**
     * Unsets the "dir" attribute
     */
    void unsetDir();
}

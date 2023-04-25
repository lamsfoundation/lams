/*
 * XML Type:  CT_SideDirectionTransition
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SideDirectionTransition(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSideDirectionTransition extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsidedirectiontransition1752type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "dir" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSideDirectionType.Enum getDir();

    /**
     * Gets (as xml) the "dir" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSideDirectionType xgetDir();

    /**
     * True if has "dir" attribute
     */
    boolean isSetDir();

    /**
     * Sets the "dir" attribute
     */
    void setDir(org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSideDirectionType.Enum dir);

    /**
     * Sets (as xml) the "dir" attribute
     */
    void xsetDir(org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSideDirectionType dir);

    /**
     * Unsets the "dir" attribute
     */
    void unsetDir();
}

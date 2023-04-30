/*
 * XML Type:  CT_InOutTransition
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTInOutTransition
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_InOutTransition(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTInOutTransition extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTInOutTransition> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctinouttransitionacf1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "dir" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTransitionInOutDirectionType.Enum getDir();

    /**
     * Gets (as xml) the "dir" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTransitionInOutDirectionType xgetDir();

    /**
     * True if has "dir" attribute
     */
    boolean isSetDir();

    /**
     * Sets the "dir" attribute
     */
    void setDir(org.openxmlformats.schemas.presentationml.x2006.main.STTransitionInOutDirectionType.Enum dir);

    /**
     * Sets (as xml) the "dir" attribute
     */
    void xsetDir(org.openxmlformats.schemas.presentationml.x2006.main.STTransitionInOutDirectionType dir);

    /**
     * Unsets the "dir" attribute
     */
    void unsetDir();
}

/*
 * XML Type:  CT_TrackChangeNumbering
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TrackChangeNumbering(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTrackChangeNumbering extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangeNumbering> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttrackchangenumberinge6catype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "original" attribute
     */
    java.lang.String getOriginal();

    /**
     * Gets (as xml) the "original" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetOriginal();

    /**
     * True if has "original" attribute
     */
    boolean isSetOriginal();

    /**
     * Sets the "original" attribute
     */
    void setOriginal(java.lang.String original);

    /**
     * Sets (as xml) the "original" attribute
     */
    void xsetOriginal(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString original);

    /**
     * Unsets the "original" attribute
     */
    void unsetOriginal();
}

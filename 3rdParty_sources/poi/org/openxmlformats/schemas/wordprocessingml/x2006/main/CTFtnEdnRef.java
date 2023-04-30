/*
 * XML Type:  CT_FtnEdnRef
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FtnEdnRef(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFtnEdnRef extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnEdnRef> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctftnednref89eetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "customMarkFollows" attribute
     */
    java.lang.Object getCustomMarkFollows();

    /**
     * Gets (as xml) the "customMarkFollows" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetCustomMarkFollows();

    /**
     * True if has "customMarkFollows" attribute
     */
    boolean isSetCustomMarkFollows();

    /**
     * Sets the "customMarkFollows" attribute
     */
    void setCustomMarkFollows(java.lang.Object customMarkFollows);

    /**
     * Sets (as xml) the "customMarkFollows" attribute
     */
    void xsetCustomMarkFollows(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff customMarkFollows);

    /**
     * Unsets the "customMarkFollows" attribute
     */
    void unsetCustomMarkFollows();

    /**
     * Gets the "id" attribute
     */
    java.math.BigInteger getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetId();

    /**
     * Sets the "id" attribute
     */
    void setId(java.math.BigInteger id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber id);
}

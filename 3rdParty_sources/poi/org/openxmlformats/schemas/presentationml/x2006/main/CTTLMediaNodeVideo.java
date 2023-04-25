/*
 * XML Type:  CT_TLMediaNodeVideo
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLMediaNodeVideo(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLMediaNodeVideo extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeVideo> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlmedianodevideoe3f8type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cMediaNode" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonMediaNodeData getCMediaNode();

    /**
     * Sets the "cMediaNode" element
     */
    void setCMediaNode(org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonMediaNodeData cMediaNode);

    /**
     * Appends and returns a new empty "cMediaNode" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonMediaNodeData addNewCMediaNode();

    /**
     * Gets the "fullScrn" attribute
     */
    boolean getFullScrn();

    /**
     * Gets (as xml) the "fullScrn" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetFullScrn();

    /**
     * True if has "fullScrn" attribute
     */
    boolean isSetFullScrn();

    /**
     * Sets the "fullScrn" attribute
     */
    void setFullScrn(boolean fullScrn);

    /**
     * Sets (as xml) the "fullScrn" attribute
     */
    void xsetFullScrn(org.apache.xmlbeans.XmlBoolean fullScrn);

    /**
     * Unsets the "fullScrn" attribute
     */
    void unsetFullScrn();
}

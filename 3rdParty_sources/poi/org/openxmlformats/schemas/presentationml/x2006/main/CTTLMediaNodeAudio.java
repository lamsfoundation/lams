/*
 * XML Type:  CT_TLMediaNodeAudio
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLMediaNodeAudio(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLMediaNodeAudio extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLMediaNodeAudio> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlmedianodeaudio47bdtype");
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
     * Gets the "isNarration" attribute
     */
    boolean getIsNarration();

    /**
     * Gets (as xml) the "isNarration" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetIsNarration();

    /**
     * True if has "isNarration" attribute
     */
    boolean isSetIsNarration();

    /**
     * Sets the "isNarration" attribute
     */
    void setIsNarration(boolean isNarration);

    /**
     * Sets (as xml) the "isNarration" attribute
     */
    void xsetIsNarration(org.apache.xmlbeans.XmlBoolean isNarration);

    /**
     * Unsets the "isNarration" attribute
     */
    void unsetIsNarration();
}

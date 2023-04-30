/*
 * XML Type:  CT_TransitionStartSoundAction
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionStartSoundAction
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TransitionStartSoundAction(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTransitionStartSoundAction extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionStartSoundAction> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttransitionstartsoundaction3e0dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "snd" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile getSnd();

    /**
     * Sets the "snd" element
     */
    void setSnd(org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile snd);

    /**
     * Appends and returns a new empty "snd" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile addNewSnd();

    /**
     * Gets the "loop" attribute
     */
    boolean getLoop();

    /**
     * Gets (as xml) the "loop" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetLoop();

    /**
     * True if has "loop" attribute
     */
    boolean isSetLoop();

    /**
     * Sets the "loop" attribute
     */
    void setLoop(boolean loop);

    /**
     * Sets (as xml) the "loop" attribute
     */
    void xsetLoop(org.apache.xmlbeans.XmlBoolean loop);

    /**
     * Unsets the "loop" attribute
     */
    void unsetLoop();
}

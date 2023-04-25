/*
 * XML Type:  CT_TransitionSoundAction
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionSoundAction
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TransitionSoundAction(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTransitionSoundAction extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionSoundAction> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttransitionsoundactionc47ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "stSnd" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionStartSoundAction getStSnd();

    /**
     * True if has "stSnd" element
     */
    boolean isSetStSnd();

    /**
     * Sets the "stSnd" element
     */
    void setStSnd(org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionStartSoundAction stSnd);

    /**
     * Appends and returns a new empty "stSnd" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionStartSoundAction addNewStSnd();

    /**
     * Unsets the "stSnd" element
     */
    void unsetStSnd();

    /**
     * Gets the "endSnd" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty getEndSnd();

    /**
     * True if has "endSnd" element
     */
    boolean isSetEndSnd();

    /**
     * Sets the "endSnd" element
     */
    void setEndSnd(org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty endSnd);

    /**
     * Appends and returns a new empty "endSnd" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty addNewEndSnd();

    /**
     * Unsets the "endSnd" element
     */
    void unsetEndSnd();
}

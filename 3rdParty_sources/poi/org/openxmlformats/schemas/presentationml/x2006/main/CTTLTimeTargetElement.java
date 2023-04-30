/*
 * XML Type:  CT_TLTimeTargetElement
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLTimeTargetElement(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLTimeTargetElement extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttltimetargetelementdca9type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "sldTgt" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty getSldTgt();

    /**
     * True if has "sldTgt" element
     */
    boolean isSetSldTgt();

    /**
     * Sets the "sldTgt" element
     */
    void setSldTgt(org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty sldTgt);

    /**
     * Appends and returns a new empty "sldTgt" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty addNewSldTgt();

    /**
     * Unsets the "sldTgt" element
     */
    void unsetSldTgt();

    /**
     * Gets the "sndTgt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile getSndTgt();

    /**
     * True if has "sndTgt" element
     */
    boolean isSetSndTgt();

    /**
     * Sets the "sndTgt" element
     */
    void setSndTgt(org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile sndTgt);

    /**
     * Appends and returns a new empty "sndTgt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile addNewSndTgt();

    /**
     * Unsets the "sndTgt" element
     */
    void unsetSndTgt();

    /**
     * Gets the "spTgt" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLShapeTargetElement getSpTgt();

    /**
     * True if has "spTgt" element
     */
    boolean isSetSpTgt();

    /**
     * Sets the "spTgt" element
     */
    void setSpTgt(org.openxmlformats.schemas.presentationml.x2006.main.CTTLShapeTargetElement spTgt);

    /**
     * Appends and returns a new empty "spTgt" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLShapeTargetElement addNewSpTgt();

    /**
     * Unsets the "spTgt" element
     */
    void unsetSpTgt();

    /**
     * Gets the "inkTgt" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId getInkTgt();

    /**
     * True if has "inkTgt" element
     */
    boolean isSetInkTgt();

    /**
     * Sets the "inkTgt" element
     */
    void setInkTgt(org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId inkTgt);

    /**
     * Appends and returns a new empty "inkTgt" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId addNewInkTgt();

    /**
     * Unsets the "inkTgt" element
     */
    void unsetInkTgt();
}

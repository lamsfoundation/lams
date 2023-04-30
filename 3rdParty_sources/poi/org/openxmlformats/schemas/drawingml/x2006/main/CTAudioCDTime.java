/*
 * XML Type:  CT_AudioCDTime
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCDTime
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AudioCDTime(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAudioCDTime extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTAudioCDTime> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctaudiocdtime68d5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "track" attribute
     */
    short getTrack();

    /**
     * Gets (as xml) the "track" attribute
     */
    org.apache.xmlbeans.XmlUnsignedByte xgetTrack();

    /**
     * Sets the "track" attribute
     */
    void setTrack(short track);

    /**
     * Sets (as xml) the "track" attribute
     */
    void xsetTrack(org.apache.xmlbeans.XmlUnsignedByte track);

    /**
     * Gets the "time" attribute
     */
    long getTime();

    /**
     * Gets (as xml) the "time" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetTime();

    /**
     * True if has "time" attribute
     */
    boolean isSetTime();

    /**
     * Sets the "time" attribute
     */
    void setTime(long time);

    /**
     * Sets (as xml) the "time" attribute
     */
    void xsetTime(org.apache.xmlbeans.XmlUnsignedInt time);

    /**
     * Unsets the "time" attribute
     */
    void unsetTime();
}

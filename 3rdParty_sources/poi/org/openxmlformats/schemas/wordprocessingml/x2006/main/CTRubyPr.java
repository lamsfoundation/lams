/*
 * XML Type:  CT_RubyPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_RubyPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTRubyPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrubyprb2actype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rubyAlign" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign getRubyAlign();

    /**
     * Sets the "rubyAlign" element
     */
    void setRubyAlign(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign rubyAlign);

    /**
     * Appends and returns a new empty "rubyAlign" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRubyAlign addNewRubyAlign();

    /**
     * Gets the "hps" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure getHps();

    /**
     * Sets the "hps" element
     */
    void setHps(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure hps);

    /**
     * Appends and returns a new empty "hps" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure addNewHps();

    /**
     * Gets the "hpsRaise" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure getHpsRaise();

    /**
     * Sets the "hpsRaise" element
     */
    void setHpsRaise(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure hpsRaise);

    /**
     * Appends and returns a new empty "hpsRaise" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure addNewHpsRaise();

    /**
     * Gets the "hpsBaseText" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure getHpsBaseText();

    /**
     * Sets the "hpsBaseText" element
     */
    void setHpsBaseText(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure hpsBaseText);

    /**
     * Appends and returns a new empty "hpsBaseText" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure addNewHpsBaseText();

    /**
     * Gets the "lid" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang getLid();

    /**
     * Sets the "lid" element
     */
    void setLid(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang lid);

    /**
     * Appends and returns a new empty "lid" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLang addNewLid();

    /**
     * Gets the "dirty" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDirty();

    /**
     * True if has "dirty" element
     */
    boolean isSetDirty();

    /**
     * Sets the "dirty" element
     */
    void setDirty(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff dirty);

    /**
     * Appends and returns a new empty "dirty" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDirty();

    /**
     * Unsets the "dirty" element
     */
    void unsetDirty();
}

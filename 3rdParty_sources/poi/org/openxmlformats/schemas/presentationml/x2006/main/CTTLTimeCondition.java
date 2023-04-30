/*
 * XML Type:  CT_TLTimeCondition
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLTimeCondition(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLTimeCondition extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttltimecondition1eb9type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tgtEl" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement getTgtEl();

    /**
     * True if has "tgtEl" element
     */
    boolean isSetTgtEl();

    /**
     * Sets the "tgtEl" element
     */
    void setTgtEl(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement tgtEl);

    /**
     * Appends and returns a new empty "tgtEl" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement addNewTgtEl();

    /**
     * Unsets the "tgtEl" element
     */
    void unsetTgtEl();

    /**
     * Gets the "tn" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerTimeNodeID getTn();

    /**
     * True if has "tn" element
     */
    boolean isSetTn();

    /**
     * Sets the "tn" element
     */
    void setTn(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerTimeNodeID tn);

    /**
     * Appends and returns a new empty "tn" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerTimeNodeID addNewTn();

    /**
     * Unsets the "tn" element
     */
    void unsetTn();

    /**
     * Gets the "rtn" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerRuntimeNode getRtn();

    /**
     * True if has "rtn" element
     */
    boolean isSetRtn();

    /**
     * Sets the "rtn" element
     */
    void setRtn(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerRuntimeNode rtn);

    /**
     * Appends and returns a new empty "rtn" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTriggerRuntimeNode addNewRtn();

    /**
     * Unsets the "rtn" element
     */
    void unsetRtn();

    /**
     * Gets the "evt" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTLTriggerEvent.Enum getEvt();

    /**
     * Gets (as xml) the "evt" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTLTriggerEvent xgetEvt();

    /**
     * True if has "evt" attribute
     */
    boolean isSetEvt();

    /**
     * Sets the "evt" attribute
     */
    void setEvt(org.openxmlformats.schemas.presentationml.x2006.main.STTLTriggerEvent.Enum evt);

    /**
     * Sets (as xml) the "evt" attribute
     */
    void xsetEvt(org.openxmlformats.schemas.presentationml.x2006.main.STTLTriggerEvent evt);

    /**
     * Unsets the "evt" attribute
     */
    void unsetEvt();

    /**
     * Gets the "delay" attribute
     */
    java.lang.Object getDelay();

    /**
     * Gets (as xml) the "delay" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTLTime xgetDelay();

    /**
     * True if has "delay" attribute
     */
    boolean isSetDelay();

    /**
     * Sets the "delay" attribute
     */
    void setDelay(java.lang.Object delay);

    /**
     * Sets (as xml) the "delay" attribute
     */
    void xsetDelay(org.openxmlformats.schemas.presentationml.x2006.main.STTLTime delay);

    /**
     * Unsets the "delay" attribute
     */
    void unsetDelay();
}

/*
 * XML Type:  CT_TLCommandBehavior
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLCommandBehavior(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLCommandBehavior extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommandBehavior> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlcommandbehavior902atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cBhvr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData getCBhvr();

    /**
     * Sets the "cBhvr" element
     */
    void setCBhvr(org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData cBhvr);

    /**
     * Appends and returns a new empty "cBhvr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData addNewCBhvr();

    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTLCommandType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTLCommandType xgetType();

    /**
     * True if has "type" attribute
     */
    boolean isSetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.presentationml.x2006.main.STTLCommandType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.presentationml.x2006.main.STTLCommandType type);

    /**
     * Unsets the "type" attribute
     */
    void unsetType();

    /**
     * Gets the "cmd" attribute
     */
    java.lang.String getCmd();

    /**
     * Gets (as xml) the "cmd" attribute
     */
    org.apache.xmlbeans.XmlString xgetCmd();

    /**
     * True if has "cmd" attribute
     */
    boolean isSetCmd();

    /**
     * Sets the "cmd" attribute
     */
    void setCmd(java.lang.String cmd);

    /**
     * Sets (as xml) the "cmd" attribute
     */
    void xsetCmd(org.apache.xmlbeans.XmlString cmd);

    /**
     * Unsets the "cmd" attribute
     */
    void unsetCmd();
}

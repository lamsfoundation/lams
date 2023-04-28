/*
 * XML Type:  CT_DbPr
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDbPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DbPr(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDbPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDbPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdbpra069type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "connection" attribute
     */
    java.lang.String getConnection();

    /**
     * Gets (as xml) the "connection" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetConnection();

    /**
     * Sets the "connection" attribute
     */
    void setConnection(java.lang.String connection);

    /**
     * Sets (as xml) the "connection" attribute
     */
    void xsetConnection(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring connection);

    /**
     * Gets the "command" attribute
     */
    java.lang.String getCommand();

    /**
     * Gets (as xml) the "command" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetCommand();

    /**
     * True if has "command" attribute
     */
    boolean isSetCommand();

    /**
     * Sets the "command" attribute
     */
    void setCommand(java.lang.String command);

    /**
     * Sets (as xml) the "command" attribute
     */
    void xsetCommand(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring command);

    /**
     * Unsets the "command" attribute
     */
    void unsetCommand();

    /**
     * Gets the "serverCommand" attribute
     */
    java.lang.String getServerCommand();

    /**
     * Gets (as xml) the "serverCommand" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetServerCommand();

    /**
     * True if has "serverCommand" attribute
     */
    boolean isSetServerCommand();

    /**
     * Sets the "serverCommand" attribute
     */
    void setServerCommand(java.lang.String serverCommand);

    /**
     * Sets (as xml) the "serverCommand" attribute
     */
    void xsetServerCommand(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring serverCommand);

    /**
     * Unsets the "serverCommand" attribute
     */
    void unsetServerCommand();

    /**
     * Gets the "commandType" attribute
     */
    long getCommandType();

    /**
     * Gets (as xml) the "commandType" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetCommandType();

    /**
     * True if has "commandType" attribute
     */
    boolean isSetCommandType();

    /**
     * Sets the "commandType" attribute
     */
    void setCommandType(long commandType);

    /**
     * Sets (as xml) the "commandType" attribute
     */
    void xsetCommandType(org.apache.xmlbeans.XmlUnsignedInt commandType);

    /**
     * Unsets the "commandType" attribute
     */
    void unsetCommandType();
}

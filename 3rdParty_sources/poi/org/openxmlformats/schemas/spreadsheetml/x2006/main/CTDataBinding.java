/*
 * XML Type:  CT_DataBinding
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataBinding
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DataBinding(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDataBinding extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataBinding> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdatabindingc09atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "DataBindingName" attribute
     */
    java.lang.String getDataBindingName();

    /**
     * Gets (as xml) the "DataBindingName" attribute
     */
    org.apache.xmlbeans.XmlString xgetDataBindingName();

    /**
     * True if has "DataBindingName" attribute
     */
    boolean isSetDataBindingName();

    /**
     * Sets the "DataBindingName" attribute
     */
    void setDataBindingName(java.lang.String dataBindingName);

    /**
     * Sets (as xml) the "DataBindingName" attribute
     */
    void xsetDataBindingName(org.apache.xmlbeans.XmlString dataBindingName);

    /**
     * Unsets the "DataBindingName" attribute
     */
    void unsetDataBindingName();

    /**
     * Gets the "FileBinding" attribute
     */
    boolean getFileBinding();

    /**
     * Gets (as xml) the "FileBinding" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetFileBinding();

    /**
     * True if has "FileBinding" attribute
     */
    boolean isSetFileBinding();

    /**
     * Sets the "FileBinding" attribute
     */
    void setFileBinding(boolean fileBinding);

    /**
     * Sets (as xml) the "FileBinding" attribute
     */
    void xsetFileBinding(org.apache.xmlbeans.XmlBoolean fileBinding);

    /**
     * Unsets the "FileBinding" attribute
     */
    void unsetFileBinding();

    /**
     * Gets the "ConnectionID" attribute
     */
    long getConnectionID();

    /**
     * Gets (as xml) the "ConnectionID" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetConnectionID();

    /**
     * True if has "ConnectionID" attribute
     */
    boolean isSetConnectionID();

    /**
     * Sets the "ConnectionID" attribute
     */
    void setConnectionID(long connectionID);

    /**
     * Sets (as xml) the "ConnectionID" attribute
     */
    void xsetConnectionID(org.apache.xmlbeans.XmlUnsignedInt connectionID);

    /**
     * Unsets the "ConnectionID" attribute
     */
    void unsetConnectionID();

    /**
     * Gets the "FileBindingName" attribute
     */
    java.lang.String getFileBindingName();

    /**
     * Gets (as xml) the "FileBindingName" attribute
     */
    org.apache.xmlbeans.XmlString xgetFileBindingName();

    /**
     * True if has "FileBindingName" attribute
     */
    boolean isSetFileBindingName();

    /**
     * Sets the "FileBindingName" attribute
     */
    void setFileBindingName(java.lang.String fileBindingName);

    /**
     * Sets (as xml) the "FileBindingName" attribute
     */
    void xsetFileBindingName(org.apache.xmlbeans.XmlString fileBindingName);

    /**
     * Unsets the "FileBindingName" attribute
     */
    void unsetFileBindingName();

    /**
     * Gets the "DataBindingLoadMode" attribute
     */
    long getDataBindingLoadMode();

    /**
     * Gets (as xml) the "DataBindingLoadMode" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetDataBindingLoadMode();

    /**
     * Sets the "DataBindingLoadMode" attribute
     */
    void setDataBindingLoadMode(long dataBindingLoadMode);

    /**
     * Sets (as xml) the "DataBindingLoadMode" attribute
     */
    void xsetDataBindingLoadMode(org.apache.xmlbeans.XmlUnsignedInt dataBindingLoadMode);
}

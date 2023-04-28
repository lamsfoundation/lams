/*
 * XML Type:  CT_ServerFormats
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormats
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ServerFormats(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTServerFormats extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormats> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctserverformatsf778type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "serverFormat" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormat> getServerFormatList();

    /**
     * Gets array of all "serverFormat" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormat[] getServerFormatArray();

    /**
     * Gets ith "serverFormat" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormat getServerFormatArray(int i);

    /**
     * Returns number of "serverFormat" element
     */
    int sizeOfServerFormatArray();

    /**
     * Sets array of all "serverFormat" element
     */
    void setServerFormatArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormat[] serverFormatArray);

    /**
     * Sets ith "serverFormat" element
     */
    void setServerFormatArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormat serverFormat);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "serverFormat" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormat insertNewServerFormat(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "serverFormat" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTServerFormat addNewServerFormat();

    /**
     * Removes the ith "serverFormat" element
     */
    void removeServerFormat(int i);

    /**
     * Gets the "count" attribute
     */
    long getCount();

    /**
     * Gets (as xml) the "count" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetCount();

    /**
     * True if has "count" attribute
     */
    boolean isSetCount();

    /**
     * Sets the "count" attribute
     */
    void setCount(long count);

    /**
     * Sets (as xml) the "count" attribute
     */
    void xsetCount(org.apache.xmlbeans.XmlUnsignedInt count);

    /**
     * Unsets the "count" attribute
     */
    void unsetCount();
}

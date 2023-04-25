/*
 * XML Type:  CT_Parameters
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTParameters
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Parameters(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTParameters extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTParameters> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctparameterse2f3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "parameter" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTParameter> getParameterList();

    /**
     * Gets array of all "parameter" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTParameter[] getParameterArray();

    /**
     * Gets ith "parameter" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTParameter getParameterArray(int i);

    /**
     * Returns number of "parameter" element
     */
    int sizeOfParameterArray();

    /**
     * Sets array of all "parameter" element
     */
    void setParameterArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTParameter[] parameterArray);

    /**
     * Sets ith "parameter" element
     */
    void setParameterArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTParameter parameter);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "parameter" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTParameter insertNewParameter(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "parameter" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTParameter addNewParameter();

    /**
     * Removes the ith "parameter" element
     */
    void removeParameter(int i);

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

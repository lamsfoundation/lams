/*
 * XML Type:  CT_Tuples
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuples
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Tuples(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTuples extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuples> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttuples49f4type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "tpl" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple> getTplList();

    /**
     * Gets array of all "tpl" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple[] getTplArray();

    /**
     * Gets ith "tpl" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple getTplArray(int i);

    /**
     * Returns number of "tpl" element
     */
    int sizeOfTplArray();

    /**
     * Sets array of all "tpl" element
     */
    void setTplArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple[] tplArray);

    /**
     * Sets ith "tpl" element
     */
    void setTplArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple tpl);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tpl" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple insertNewTpl(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "tpl" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuple addNewTpl();

    /**
     * Removes the ith "tpl" element
     */
    void removeTpl(int i);

    /**
     * Gets the "c" attribute
     */
    long getC();

    /**
     * Gets (as xml) the "c" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetC();

    /**
     * True if has "c" attribute
     */
    boolean isSetC();

    /**
     * Sets the "c" attribute
     */
    void setC(long c);

    /**
     * Sets (as xml) the "c" attribute
     */
    void xsetC(org.apache.xmlbeans.XmlUnsignedInt c);

    /**
     * Unsets the "c" attribute
     */
    void unsetC();
}

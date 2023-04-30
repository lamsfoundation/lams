/*
 * XML Type:  CT_MdxMetadata
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMetadata
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MdxMetadata(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTMdxMetadata extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdxMetadata> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmdxmetadatadceftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "mdx" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdx> getMdxList();

    /**
     * Gets array of all "mdx" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdx[] getMdxArray();

    /**
     * Gets ith "mdx" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdx getMdxArray(int i);

    /**
     * Returns number of "mdx" element
     */
    int sizeOfMdxArray();

    /**
     * Sets array of all "mdx" element
     */
    void setMdxArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdx[] mdxArray);

    /**
     * Sets ith "mdx" element
     */
    void setMdxArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdx mdx);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "mdx" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdx insertNewMdx(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "mdx" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMdx addNewMdx();

    /**
     * Removes the ith "mdx" element
     */
    void removeMdx(int i);

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

/*
 * XML Type:  CT_Sources
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/bibliography
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSources
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.bibliography;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Sources(@http://schemas.openxmlformats.org/officeDocument/2006/bibliography).
 *
 * This is a complex type.
 */
public interface CTSources extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSources> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsourcesa9fetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "Source" elements
     */
    java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType> getSourceList();

    /**
     * Gets array of all "Source" elements
     */
    org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType[] getSourceArray();

    /**
     * Gets ith "Source" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType getSourceArray(int i);

    /**
     * Returns number of "Source" element
     */
    int sizeOfSourceArray();

    /**
     * Sets array of all "Source" element
     */
    void setSourceArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType[] sourceArray);

    /**
     * Sets ith "Source" element
     */
    void setSourceArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType source);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Source" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType insertNewSource(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "Source" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType addNewSource();

    /**
     * Removes the ith "Source" element
     */
    void removeSource(int i);

    /**
     * Gets the "SelectedStyle" attribute
     */
    java.lang.String getSelectedStyle();

    /**
     * Gets (as xml) the "SelectedStyle" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetSelectedStyle();

    /**
     * True if has "SelectedStyle" attribute
     */
    boolean isSetSelectedStyle();

    /**
     * Sets the "SelectedStyle" attribute
     */
    void setSelectedStyle(java.lang.String selectedStyle);

    /**
     * Sets (as xml) the "SelectedStyle" attribute
     */
    void xsetSelectedStyle(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString selectedStyle);

    /**
     * Unsets the "SelectedStyle" attribute
     */
    void unsetSelectedStyle();

    /**
     * Gets the "StyleName" attribute
     */
    java.lang.String getStyleName();

    /**
     * Gets (as xml) the "StyleName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetStyleName();

    /**
     * True if has "StyleName" attribute
     */
    boolean isSetStyleName();

    /**
     * Sets the "StyleName" attribute
     */
    void setStyleName(java.lang.String styleName);

    /**
     * Sets (as xml) the "StyleName" attribute
     */
    void xsetStyleName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString styleName);

    /**
     * Unsets the "StyleName" attribute
     */
    void unsetStyleName();

    /**
     * Gets the "URI" attribute
     */
    java.lang.String getURI();

    /**
     * Gets (as xml) the "URI" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetURI();

    /**
     * True if has "URI" attribute
     */
    boolean isSetURI();

    /**
     * Sets the "URI" attribute
     */
    void setURI(java.lang.String uri);

    /**
     * Sets (as xml) the "URI" attribute
     */
    void xsetURI(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString uri);

    /**
     * Unsets the "URI" attribute
     */
    void unsetURI();
}

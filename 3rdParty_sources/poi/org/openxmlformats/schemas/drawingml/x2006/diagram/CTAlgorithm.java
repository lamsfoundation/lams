/*
 * XML Type:  CT_Algorithm
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Algorithm(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTAlgorithm extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTAlgorithm> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctalgorithmb98ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "param" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTParameter> getParamList();

    /**
     * Gets array of all "param" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTParameter[] getParamArray();

    /**
     * Gets ith "param" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTParameter getParamArray(int i);

    /**
     * Returns number of "param" element
     */
    int sizeOfParamArray();

    /**
     * Sets array of all "param" element
     */
    void setParamArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTParameter[] paramArray);

    /**
     * Sets ith "param" element
     */
    void setParamArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTParameter param);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "param" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTParameter insertNewParam(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "param" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTParameter addNewParam();

    /**
     * Removes the ith "param" element
     */
    void removeParam(int i);

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.STAlgorithmType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.STAlgorithmType xgetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.drawingml.x2006.diagram.STAlgorithmType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.drawingml.x2006.diagram.STAlgorithmType type);

    /**
     * Gets the "rev" attribute
     */
    long getRev();

    /**
     * Gets (as xml) the "rev" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetRev();

    /**
     * True if has "rev" attribute
     */
    boolean isSetRev();

    /**
     * Sets the "rev" attribute
     */
    void setRev(long rev);

    /**
     * Sets (as xml) the "rev" attribute
     */
    void xsetRev(org.apache.xmlbeans.XmlUnsignedInt rev);

    /**
     * Unsets the "rev" attribute
     */
    void unsetRev();
}

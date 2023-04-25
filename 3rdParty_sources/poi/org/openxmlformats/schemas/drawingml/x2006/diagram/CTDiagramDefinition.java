/*
 * XML Type:  CT_DiagramDefinition
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinition
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DiagramDefinition(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTDiagramDefinition extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTDiagramDefinition> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdiagramdefinition3655type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "title" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTName> getTitleList();

    /**
     * Gets array of all "title" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTName[] getTitleArray();

    /**
     * Gets ith "title" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTName getTitleArray(int i);

    /**
     * Returns number of "title" element
     */
    int sizeOfTitleArray();

    /**
     * Sets array of all "title" element
     */
    void setTitleArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTName[] titleArray);

    /**
     * Sets ith "title" element
     */
    void setTitleArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTName title);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "title" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTName insertNewTitle(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "title" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTName addNewTitle();

    /**
     * Removes the ith "title" element
     */
    void removeTitle(int i);

    /**
     * Gets a List of "desc" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription> getDescList();

    /**
     * Gets array of all "desc" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription[] getDescArray();

    /**
     * Gets ith "desc" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription getDescArray(int i);

    /**
     * Returns number of "desc" element
     */
    int sizeOfDescArray();

    /**
     * Sets array of all "desc" element
     */
    void setDescArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription[] descArray);

    /**
     * Sets ith "desc" element
     */
    void setDescArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription desc);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "desc" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription insertNewDesc(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "desc" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTDescription addNewDesc();

    /**
     * Removes the ith "desc" element
     */
    void removeDesc(int i);

    /**
     * Gets the "catLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategories getCatLst();

    /**
     * True if has "catLst" element
     */
    boolean isSetCatLst();

    /**
     * Sets the "catLst" element
     */
    void setCatLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategories catLst);

    /**
     * Appends and returns a new empty "catLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCategories addNewCatLst();

    /**
     * Unsets the "catLst" element
     */
    void unsetCatLst();

    /**
     * Gets the "sampData" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData getSampData();

    /**
     * True if has "sampData" element
     */
    boolean isSetSampData();

    /**
     * Sets the "sampData" element
     */
    void setSampData(org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData sampData);

    /**
     * Appends and returns a new empty "sampData" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData addNewSampData();

    /**
     * Unsets the "sampData" element
     */
    void unsetSampData();

    /**
     * Gets the "styleData" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData getStyleData();

    /**
     * True if has "styleData" element
     */
    boolean isSetStyleData();

    /**
     * Sets the "styleData" element
     */
    void setStyleData(org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData styleData);

    /**
     * Appends and returns a new empty "styleData" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData addNewStyleData();

    /**
     * Unsets the "styleData" element
     */
    void unsetStyleData();

    /**
     * Gets the "clrData" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData getClrData();

    /**
     * True if has "clrData" element
     */
    boolean isSetClrData();

    /**
     * Sets the "clrData" element
     */
    void setClrData(org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData clrData);

    /**
     * Appends and returns a new empty "clrData" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTSampleData addNewClrData();

    /**
     * Unsets the "clrData" element
     */
    void unsetClrData();

    /**
     * Gets the "layoutNode" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode getLayoutNode();

    /**
     * Sets the "layoutNode" element
     */
    void setLayoutNode(org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode layoutNode);

    /**
     * Appends and returns a new empty "layoutNode" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTLayoutNode addNewLayoutNode();

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
     * Gets the "uniqueId" attribute
     */
    java.lang.String getUniqueId();

    /**
     * Gets (as xml) the "uniqueId" attribute
     */
    org.apache.xmlbeans.XmlString xgetUniqueId();

    /**
     * True if has "uniqueId" attribute
     */
    boolean isSetUniqueId();

    /**
     * Sets the "uniqueId" attribute
     */
    void setUniqueId(java.lang.String uniqueId);

    /**
     * Sets (as xml) the "uniqueId" attribute
     */
    void xsetUniqueId(org.apache.xmlbeans.XmlString uniqueId);

    /**
     * Unsets the "uniqueId" attribute
     */
    void unsetUniqueId();

    /**
     * Gets the "minVer" attribute
     */
    java.lang.String getMinVer();

    /**
     * Gets (as xml) the "minVer" attribute
     */
    org.apache.xmlbeans.XmlString xgetMinVer();

    /**
     * True if has "minVer" attribute
     */
    boolean isSetMinVer();

    /**
     * Sets the "minVer" attribute
     */
    void setMinVer(java.lang.String minVer);

    /**
     * Sets (as xml) the "minVer" attribute
     */
    void xsetMinVer(org.apache.xmlbeans.XmlString minVer);

    /**
     * Unsets the "minVer" attribute
     */
    void unsetMinVer();

    /**
     * Gets the "defStyle" attribute
     */
    java.lang.String getDefStyle();

    /**
     * Gets (as xml) the "defStyle" attribute
     */
    org.apache.xmlbeans.XmlString xgetDefStyle();

    /**
     * True if has "defStyle" attribute
     */
    boolean isSetDefStyle();

    /**
     * Sets the "defStyle" attribute
     */
    void setDefStyle(java.lang.String defStyle);

    /**
     * Sets (as xml) the "defStyle" attribute
     */
    void xsetDefStyle(org.apache.xmlbeans.XmlString defStyle);

    /**
     * Unsets the "defStyle" attribute
     */
    void unsetDefStyle();
}

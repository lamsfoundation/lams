/*
 * XML Type:  CT_DataModel
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DataModel(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTDataModel extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTDataModel> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdatamodel8e5ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ptLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTPtList getPtLst();

    /**
     * Sets the "ptLst" element
     */
    void setPtLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTPtList ptLst);

    /**
     * Appends and returns a new empty "ptLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTPtList addNewPtLst();

    /**
     * Gets the "cxnLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxnList getCxnLst();

    /**
     * True if has "cxnLst" element
     */
    boolean isSetCxnLst();

    /**
     * Sets the "cxnLst" element
     */
    void setCxnLst(org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxnList cxnLst);

    /**
     * Appends and returns a new empty "cxnLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTCxnList addNewCxnLst();

    /**
     * Unsets the "cxnLst" element
     */
    void unsetCxnLst();

    /**
     * Gets the "bg" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting getBg();

    /**
     * True if has "bg" element
     */
    boolean isSetBg();

    /**
     * Sets the "bg" element
     */
    void setBg(org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting bg);

    /**
     * Appends and returns a new empty "bg" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBackgroundFormatting addNewBg();

    /**
     * Unsets the "bg" element
     */
    void unsetBg();

    /**
     * Gets the "whole" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting getWhole();

    /**
     * True if has "whole" element
     */
    boolean isSetWhole();

    /**
     * Sets the "whole" element
     */
    void setWhole(org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting whole);

    /**
     * Appends and returns a new empty "whole" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTWholeE2OFormatting addNewWhole();

    /**
     * Unsets the "whole" element
     */
    void unsetWhole();

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
}

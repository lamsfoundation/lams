/*
 * XML Type:  CT_GroupShapeProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_GroupShapeProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGroupShapeProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTGroupShapeProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctgroupshapeproperties8690type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "xfrm" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D getXfrm();

    /**
     * True if has "xfrm" element
     */
    boolean isSetXfrm();

    /**
     * Sets the "xfrm" element
     */
    void setXfrm(org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D xfrm);

    /**
     * Appends and returns a new empty "xfrm" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGroupTransform2D addNewXfrm();

    /**
     * Unsets the "xfrm" element
     */
    void unsetXfrm();

    /**
     * Gets the "noFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties getNoFill();

    /**
     * True if has "noFill" element
     */
    boolean isSetNoFill();

    /**
     * Sets the "noFill" element
     */
    void setNoFill(org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties noFill);

    /**
     * Appends and returns a new empty "noFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTNoFillProperties addNewNoFill();

    /**
     * Unsets the "noFill" element
     */
    void unsetNoFill();

    /**
     * Gets the "solidFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties getSolidFill();

    /**
     * True if has "solidFill" element
     */
    boolean isSetSolidFill();

    /**
     * Sets the "solidFill" element
     */
    void setSolidFill(org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties solidFill);

    /**
     * Appends and returns a new empty "solidFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties addNewSolidFill();

    /**
     * Unsets the "solidFill" element
     */
    void unsetSolidFill();

    /**
     * Gets the "gradFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties getGradFill();

    /**
     * True if has "gradFill" element
     */
    boolean isSetGradFill();

    /**
     * Sets the "gradFill" element
     */
    void setGradFill(org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties gradFill);

    /**
     * Appends and returns a new empty "gradFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGradientFillProperties addNewGradFill();

    /**
     * Unsets the "gradFill" element
     */
    void unsetGradFill();

    /**
     * Gets the "blipFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties getBlipFill();

    /**
     * True if has "blipFill" element
     */
    boolean isSetBlipFill();

    /**
     * Sets the "blipFill" element
     */
    void setBlipFill(org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties blipFill);

    /**
     * Appends and returns a new empty "blipFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTBlipFillProperties addNewBlipFill();

    /**
     * Unsets the "blipFill" element
     */
    void unsetBlipFill();

    /**
     * Gets the "pattFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties getPattFill();

    /**
     * True if has "pattFill" element
     */
    boolean isSetPattFill();

    /**
     * Sets the "pattFill" element
     */
    void setPattFill(org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties pattFill);

    /**
     * Appends and returns a new empty "pattFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTPatternFillProperties addNewPattFill();

    /**
     * Unsets the "pattFill" element
     */
    void unsetPattFill();

    /**
     * Gets the "grpFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties getGrpFill();

    /**
     * True if has "grpFill" element
     */
    boolean isSetGrpFill();

    /**
     * Sets the "grpFill" element
     */
    void setGrpFill(org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties grpFill);

    /**
     * Appends and returns a new empty "grpFill" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTGroupFillProperties addNewGrpFill();

    /**
     * Unsets the "grpFill" element
     */
    void unsetGrpFill();

    /**
     * Gets the "effectLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList getEffectLst();

    /**
     * True if has "effectLst" element
     */
    boolean isSetEffectLst();

    /**
     * Sets the "effectLst" element
     */
    void setEffectLst(org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList effectLst);

    /**
     * Appends and returns a new empty "effectLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList addNewEffectLst();

    /**
     * Unsets the "effectLst" element
     */
    void unsetEffectLst();

    /**
     * Gets the "effectDag" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer getEffectDag();

    /**
     * True if has "effectDag" element
     */
    boolean isSetEffectDag();

    /**
     * Sets the "effectDag" element
     */
    void setEffectDag(org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer effectDag);

    /**
     * Appends and returns a new empty "effectDag" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEffectContainer addNewEffectDag();

    /**
     * Unsets the "effectDag" element
     */
    void unsetEffectDag();

    /**
     * Gets the "scene3d" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D getScene3D();

    /**
     * True if has "scene3d" element
     */
    boolean isSetScene3D();

    /**
     * Sets the "scene3d" element
     */
    void setScene3D(org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D scene3D);

    /**
     * Appends and returns a new empty "scene3d" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D addNewScene3D();

    /**
     * Unsets the "scene3d" element
     */
    void unsetScene3D();

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
     * Gets the "bwMode" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum getBwMode();

    /**
     * Gets (as xml) the "bwMode" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode xgetBwMode();

    /**
     * True if has "bwMode" attribute
     */
    boolean isSetBwMode();

    /**
     * Sets the "bwMode" attribute
     */
    void setBwMode(org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode.Enum bwMode);

    /**
     * Sets (as xml) the "bwMode" attribute
     */
    void xsetBwMode(org.openxmlformats.schemas.drawingml.x2006.main.STBlackWhiteMode bwMode);

    /**
     * Unsets the "bwMode" attribute
     */
    void unsetBwMode();
}

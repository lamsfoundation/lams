/*
 * XML Type:  CT_ObjectStyleDefaults
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTObjectStyleDefaults
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ObjectStyleDefaults(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTObjectStyleDefaults extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTObjectStyleDefaults> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctobjectstyledefaults61d5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "spDef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition getSpDef();

    /**
     * True if has "spDef" element
     */
    boolean isSetSpDef();

    /**
     * Sets the "spDef" element
     */
    void setSpDef(org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition spDef);

    /**
     * Appends and returns a new empty "spDef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition addNewSpDef();

    /**
     * Unsets the "spDef" element
     */
    void unsetSpDef();

    /**
     * Gets the "lnDef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition getLnDef();

    /**
     * True if has "lnDef" element
     */
    boolean isSetLnDef();

    /**
     * Sets the "lnDef" element
     */
    void setLnDef(org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition lnDef);

    /**
     * Appends and returns a new empty "lnDef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition addNewLnDef();

    /**
     * Unsets the "lnDef" element
     */
    void unsetLnDef();

    /**
     * Gets the "txDef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition getTxDef();

    /**
     * True if has "txDef" element
     */
    boolean isSetTxDef();

    /**
     * Sets the "txDef" element
     */
    void setTxDef(org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition txDef);

    /**
     * Appends and returns a new empty "txDef" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTDefaultShapeDefinition addNewTxDef();

    /**
     * Unsets the "txDef" element
     */
    void unsetTxDef();

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

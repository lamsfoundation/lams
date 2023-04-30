/*
 * XML Type:  CT_HierBranchStyle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTHierBranchStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_HierBranchStyle(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTHierBranchStyle extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTHierBranchStyle> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cthierbranchstyle439atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "val" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.STHierBranchStyle.Enum getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.STHierBranchStyle xgetVal();

    /**
     * True if has "val" attribute
     */
    boolean isSetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(org.openxmlformats.schemas.drawingml.x2006.diagram.STHierBranchStyle.Enum val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.openxmlformats.schemas.drawingml.x2006.diagram.STHierBranchStyle val);

    /**
     * Unsets the "val" attribute
     */
    void unsetVal();
}

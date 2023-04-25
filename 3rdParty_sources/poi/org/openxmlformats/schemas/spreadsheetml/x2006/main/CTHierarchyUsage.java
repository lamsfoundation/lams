/*
 * XML Type:  CT_HierarchyUsage
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHierarchyUsage
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_HierarchyUsage(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTHierarchyUsage extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHierarchyUsage> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cthierarchyusaged475type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "hierarchyUsage" attribute
     */
    int getHierarchyUsage();

    /**
     * Gets (as xml) the "hierarchyUsage" attribute
     */
    org.apache.xmlbeans.XmlInt xgetHierarchyUsage();

    /**
     * Sets the "hierarchyUsage" attribute
     */
    void setHierarchyUsage(int hierarchyUsage);

    /**
     * Sets (as xml) the "hierarchyUsage" attribute
     */
    void xsetHierarchyUsage(org.apache.xmlbeans.XmlInt hierarchyUsage);
}

/*
 * XML Type:  CT_ColorMappingOverride
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ColorMappingOverride(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTColorMappingOverride extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTColorMappingOverride> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcolormappingoverridea2b2type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "masterClrMapping" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement getMasterClrMapping();

    /**
     * True if has "masterClrMapping" element
     */
    boolean isSetMasterClrMapping();

    /**
     * Sets the "masterClrMapping" element
     */
    void setMasterClrMapping(org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement masterClrMapping);

    /**
     * Appends and returns a new empty "masterClrMapping" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTEmptyElement addNewMasterClrMapping();

    /**
     * Unsets the "masterClrMapping" element
     */
    void unsetMasterClrMapping();

    /**
     * Gets the "overrideClrMapping" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping getOverrideClrMapping();

    /**
     * True if has "overrideClrMapping" element
     */
    boolean isSetOverrideClrMapping();

    /**
     * Sets the "overrideClrMapping" element
     */
    void setOverrideClrMapping(org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping overrideClrMapping);

    /**
     * Appends and returns a new empty "overrideClrMapping" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping addNewOverrideClrMapping();

    /**
     * Unsets the "overrideClrMapping" element
     */
    void unsetOverrideClrMapping();
}

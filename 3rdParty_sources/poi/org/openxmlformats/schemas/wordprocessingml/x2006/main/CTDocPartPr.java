/*
 * XML Type:  CT_DocPartPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DocPartPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDocPartPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdocpartpra1c5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "name" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartName getName();

    /**
     * Sets the "name" element
     */
    void setName(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartName name);

    /**
     * Appends and returns a new empty "name" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartName addNewName();

    /**
     * Gets the "style" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getStyle();

    /**
     * True if has "style" element
     */
    boolean isSetStyle();

    /**
     * Sets the "style" element
     */
    void setStyle(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString style);

    /**
     * Appends and returns a new empty "style" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewStyle();

    /**
     * Unsets the "style" element
     */
    void unsetStyle();

    /**
     * Gets the "category" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartCategory getCategory();

    /**
     * True if has "category" element
     */
    boolean isSetCategory();

    /**
     * Sets the "category" element
     */
    void setCategory(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartCategory category);

    /**
     * Appends and returns a new empty "category" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartCategory addNewCategory();

    /**
     * Unsets the "category" element
     */
    void unsetCategory();

    /**
     * Gets the "types" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartTypes getTypes();

    /**
     * True if has "types" element
     */
    boolean isSetTypes();

    /**
     * Sets the "types" element
     */
    void setTypes(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartTypes types);

    /**
     * Appends and returns a new empty "types" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartTypes addNewTypes();

    /**
     * Unsets the "types" element
     */
    void unsetTypes();

    /**
     * Gets the "behaviors" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehaviors getBehaviors();

    /**
     * True if has "behaviors" element
     */
    boolean isSetBehaviors();

    /**
     * Sets the "behaviors" element
     */
    void setBehaviors(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehaviors behaviors);

    /**
     * Appends and returns a new empty "behaviors" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehaviors addNewBehaviors();

    /**
     * Unsets the "behaviors" element
     */
    void unsetBehaviors();

    /**
     * Gets the "description" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getDescription();

    /**
     * True if has "description" element
     */
    boolean isSetDescription();

    /**
     * Sets the "description" element
     */
    void setDescription(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString description);

    /**
     * Appends and returns a new empty "description" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewDescription();

    /**
     * Unsets the "description" element
     */
    void unsetDescription();

    /**
     * Gets the "guid" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGuid getGuid();

    /**
     * True if has "guid" element
     */
    boolean isSetGuid();

    /**
     * Sets the "guid" element
     */
    void setGuid(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGuid guid);

    /**
     * Appends and returns a new empty "guid" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTGuid addNewGuid();

    /**
     * Unsets the "guid" element
     */
    void unsetGuid();
}

/*
 * XML Type:  CT_NonVisualContentPartProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualContentPartProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NonVisualContentPartProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTNonVisualContentPartProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualContentPartProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnonvisualcontentpartproperties8d4dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cpLocks" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTContentPartLocking getCpLocks();

    /**
     * True if has "cpLocks" element
     */
    boolean isSetCpLocks();

    /**
     * Sets the "cpLocks" element
     */
    void setCpLocks(org.openxmlformats.schemas.drawingml.x2006.main.CTContentPartLocking cpLocks);

    /**
     * Appends and returns a new empty "cpLocks" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTContentPartLocking addNewCpLocks();

    /**
     * Unsets the "cpLocks" element
     */
    void unsetCpLocks();

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
     * Gets the "isComment" attribute
     */
    boolean getIsComment();

    /**
     * Gets (as xml) the "isComment" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetIsComment();

    /**
     * True if has "isComment" attribute
     */
    boolean isSetIsComment();

    /**
     * Sets the "isComment" attribute
     */
    void setIsComment(boolean isComment);

    /**
     * Sets (as xml) the "isComment" attribute
     */
    void xsetIsComment(org.apache.xmlbeans.XmlBoolean isComment);

    /**
     * Unsets the "isComment" attribute
     */
    void unsetIsComment();
}

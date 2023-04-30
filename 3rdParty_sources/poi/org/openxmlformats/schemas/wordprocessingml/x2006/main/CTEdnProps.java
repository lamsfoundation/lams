/*
 * XML Type:  CT_EdnProps
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnProps
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_EdnProps(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTEdnProps extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnProps> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctednpropsd887type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pos" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnPos getPos();

    /**
     * True if has "pos" element
     */
    boolean isSetPos();

    /**
     * Sets the "pos" element
     */
    void setPos(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnPos pos);

    /**
     * Appends and returns a new empty "pos" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnPos addNewPos();

    /**
     * Unsets the "pos" element
     */
    void unsetPos();

    /**
     * Gets the "numFmt" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt getNumFmt();

    /**
     * True if has "numFmt" element
     */
    boolean isSetNumFmt();

    /**
     * Sets the "numFmt" element
     */
    void setNumFmt(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt numFmt);

    /**
     * Appends and returns a new empty "numFmt" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumFmt addNewNumFmt();

    /**
     * Unsets the "numFmt" element
     */
    void unsetNumFmt();

    /**
     * Gets the "numStart" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getNumStart();

    /**
     * True if has "numStart" element
     */
    boolean isSetNumStart();

    /**
     * Sets the "numStart" element
     */
    void setNumStart(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber numStart);

    /**
     * Appends and returns a new empty "numStart" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewNumStart();

    /**
     * Unsets the "numStart" element
     */
    void unsetNumStart();

    /**
     * Gets the "numRestart" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumRestart getNumRestart();

    /**
     * True if has "numRestart" element
     */
    boolean isSetNumRestart();

    /**
     * Sets the "numRestart" element
     */
    void setNumRestart(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumRestart numRestart);

    /**
     * Appends and returns a new empty "numRestart" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumRestart addNewNumRestart();

    /**
     * Unsets the "numRestart" element
     */
    void unsetNumRestart();
}

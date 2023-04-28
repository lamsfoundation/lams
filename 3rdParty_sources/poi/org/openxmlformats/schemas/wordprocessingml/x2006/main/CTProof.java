/*
 * XML Type:  CT_Proof
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProof
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Proof(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTProof extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProof> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctproof8f0etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "spelling" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof.Enum getSpelling();

    /**
     * Gets (as xml) the "spelling" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof xgetSpelling();

    /**
     * True if has "spelling" attribute
     */
    boolean isSetSpelling();

    /**
     * Sets the "spelling" attribute
     */
    void setSpelling(org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof.Enum spelling);

    /**
     * Sets (as xml) the "spelling" attribute
     */
    void xsetSpelling(org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof spelling);

    /**
     * Unsets the "spelling" attribute
     */
    void unsetSpelling();

    /**
     * Gets the "grammar" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof.Enum getGrammar();

    /**
     * Gets (as xml) the "grammar" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof xgetGrammar();

    /**
     * True if has "grammar" attribute
     */
    boolean isSetGrammar();

    /**
     * Sets the "grammar" attribute
     */
    void setGrammar(org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof.Enum grammar);

    /**
     * Sets (as xml) the "grammar" attribute
     */
    void xsetGrammar(org.openxmlformats.schemas.wordprocessingml.x2006.main.STProof grammar);

    /**
     * Unsets the "grammar" attribute
     */
    void unsetGrammar();
}

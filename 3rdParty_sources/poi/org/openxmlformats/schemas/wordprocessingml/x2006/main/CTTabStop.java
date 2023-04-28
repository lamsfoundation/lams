/*
 * XML Type:  CT_TabStop
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TabStop(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTabStop extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttabstop5ebbtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "val" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc.Enum getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc xgetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc.Enum val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc val);

    /**
     * Gets the "leader" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc.Enum getLeader();

    /**
     * Gets (as xml) the "leader" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc xgetLeader();

    /**
     * True if has "leader" attribute
     */
    boolean isSetLeader();

    /**
     * Sets the "leader" attribute
     */
    void setLeader(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc.Enum leader);

    /**
     * Sets (as xml) the "leader" attribute
     */
    void xsetLeader(org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc leader);

    /**
     * Unsets the "leader" attribute
     */
    void unsetLeader();

    /**
     * Gets the "pos" attribute
     */
    java.lang.Object getPos();

    /**
     * Gets (as xml) the "pos" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure xgetPos();

    /**
     * Sets the "pos" attribute
     */
    void setPos(java.lang.Object pos);

    /**
     * Sets (as xml) the "pos" attribute
     */
    void xsetPos(org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure pos);
}

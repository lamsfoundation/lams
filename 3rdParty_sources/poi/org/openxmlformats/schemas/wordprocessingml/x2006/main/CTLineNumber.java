/*
 * XML Type:  CT_LineNumber
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLineNumber
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_LineNumber(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTLineNumber extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLineNumber> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctlinenumber99ebtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "countBy" attribute
     */
    java.math.BigInteger getCountBy();

    /**
     * Gets (as xml) the "countBy" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetCountBy();

    /**
     * True if has "countBy" attribute
     */
    boolean isSetCountBy();

    /**
     * Sets the "countBy" attribute
     */
    void setCountBy(java.math.BigInteger countBy);

    /**
     * Sets (as xml) the "countBy" attribute
     */
    void xsetCountBy(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber countBy);

    /**
     * Unsets the "countBy" attribute
     */
    void unsetCountBy();

    /**
     * Gets the "start" attribute
     */
    java.math.BigInteger getStart();

    /**
     * Gets (as xml) the "start" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetStart();

    /**
     * True if has "start" attribute
     */
    boolean isSetStart();

    /**
     * Sets the "start" attribute
     */
    void setStart(java.math.BigInteger start);

    /**
     * Sets (as xml) the "start" attribute
     */
    void xsetStart(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber start);

    /**
     * Unsets the "start" attribute
     */
    void unsetStart();

    /**
     * Gets the "distance" attribute
     */
    java.lang.Object getDistance();

    /**
     * Gets (as xml) the "distance" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetDistance();

    /**
     * True if has "distance" attribute
     */
    boolean isSetDistance();

    /**
     * Sets the "distance" attribute
     */
    void setDistance(java.lang.Object distance);

    /**
     * Sets (as xml) the "distance" attribute
     */
    void xsetDistance(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure distance);

    /**
     * Unsets the "distance" attribute
     */
    void unsetDistance();

    /**
     * Gets the "restart" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineNumberRestart.Enum getRestart();

    /**
     * Gets (as xml) the "restart" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineNumberRestart xgetRestart();

    /**
     * True if has "restart" attribute
     */
    boolean isSetRestart();

    /**
     * Sets the "restart" attribute
     */
    void setRestart(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineNumberRestart.Enum restart);

    /**
     * Sets (as xml) the "restart" attribute
     */
    void xsetRestart(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineNumberRestart restart);

    /**
     * Unsets the "restart" attribute
     */
    void unsetRestart();
}

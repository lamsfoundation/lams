/*
 * XML Type:  CT_Spacing
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Spacing(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSpacing extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctspacingff2ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "before" attribute
     */
    java.lang.Object getBefore();

    /**
     * Gets (as xml) the "before" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetBefore();

    /**
     * True if has "before" attribute
     */
    boolean isSetBefore();

    /**
     * Sets the "before" attribute
     */
    void setBefore(java.lang.Object before);

    /**
     * Sets (as xml) the "before" attribute
     */
    void xsetBefore(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure before);

    /**
     * Unsets the "before" attribute
     */
    void unsetBefore();

    /**
     * Gets the "beforeLines" attribute
     */
    java.math.BigInteger getBeforeLines();

    /**
     * Gets (as xml) the "beforeLines" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetBeforeLines();

    /**
     * True if has "beforeLines" attribute
     */
    boolean isSetBeforeLines();

    /**
     * Sets the "beforeLines" attribute
     */
    void setBeforeLines(java.math.BigInteger beforeLines);

    /**
     * Sets (as xml) the "beforeLines" attribute
     */
    void xsetBeforeLines(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber beforeLines);

    /**
     * Unsets the "beforeLines" attribute
     */
    void unsetBeforeLines();

    /**
     * Gets the "beforeAutospacing" attribute
     */
    java.lang.Object getBeforeAutospacing();

    /**
     * Gets (as xml) the "beforeAutospacing" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetBeforeAutospacing();

    /**
     * True if has "beforeAutospacing" attribute
     */
    boolean isSetBeforeAutospacing();

    /**
     * Sets the "beforeAutospacing" attribute
     */
    void setBeforeAutospacing(java.lang.Object beforeAutospacing);

    /**
     * Sets (as xml) the "beforeAutospacing" attribute
     */
    void xsetBeforeAutospacing(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff beforeAutospacing);

    /**
     * Unsets the "beforeAutospacing" attribute
     */
    void unsetBeforeAutospacing();

    /**
     * Gets the "after" attribute
     */
    java.lang.Object getAfter();

    /**
     * Gets (as xml) the "after" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetAfter();

    /**
     * True if has "after" attribute
     */
    boolean isSetAfter();

    /**
     * Sets the "after" attribute
     */
    void setAfter(java.lang.Object after);

    /**
     * Sets (as xml) the "after" attribute
     */
    void xsetAfter(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure after);

    /**
     * Unsets the "after" attribute
     */
    void unsetAfter();

    /**
     * Gets the "afterLines" attribute
     */
    java.math.BigInteger getAfterLines();

    /**
     * Gets (as xml) the "afterLines" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetAfterLines();

    /**
     * True if has "afterLines" attribute
     */
    boolean isSetAfterLines();

    /**
     * Sets the "afterLines" attribute
     */
    void setAfterLines(java.math.BigInteger afterLines);

    /**
     * Sets (as xml) the "afterLines" attribute
     */
    void xsetAfterLines(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber afterLines);

    /**
     * Unsets the "afterLines" attribute
     */
    void unsetAfterLines();

    /**
     * Gets the "afterAutospacing" attribute
     */
    java.lang.Object getAfterAutospacing();

    /**
     * Gets (as xml) the "afterAutospacing" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetAfterAutospacing();

    /**
     * True if has "afterAutospacing" attribute
     */
    boolean isSetAfterAutospacing();

    /**
     * Sets the "afterAutospacing" attribute
     */
    void setAfterAutospacing(java.lang.Object afterAutospacing);

    /**
     * Sets (as xml) the "afterAutospacing" attribute
     */
    void xsetAfterAutospacing(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff afterAutospacing);

    /**
     * Unsets the "afterAutospacing" attribute
     */
    void unsetAfterAutospacing();

    /**
     * Gets the "line" attribute
     */
    java.lang.Object getLine();

    /**
     * Gets (as xml) the "line" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure xgetLine();

    /**
     * True if has "line" attribute
     */
    boolean isSetLine();

    /**
     * Sets the "line" attribute
     */
    void setLine(java.lang.Object line);

    /**
     * Sets (as xml) the "line" attribute
     */
    void xsetLine(org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure line);

    /**
     * Unsets the "line" attribute
     */
    void unsetLine();

    /**
     * Gets the "lineRule" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule.Enum getLineRule();

    /**
     * Gets (as xml) the "lineRule" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule xgetLineRule();

    /**
     * True if has "lineRule" attribute
     */
    boolean isSetLineRule();

    /**
     * Sets the "lineRule" attribute
     */
    void setLineRule(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule.Enum lineRule);

    /**
     * Sets (as xml) the "lineRule" attribute
     */
    void xsetLineRule(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule lineRule);

    /**
     * Unsets the "lineRule" attribute
     */
    void unsetLineRule();
}

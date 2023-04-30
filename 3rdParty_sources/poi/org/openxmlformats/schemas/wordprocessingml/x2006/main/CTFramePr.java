/*
 * XML Type:  CT_FramePr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramePr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FramePr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFramePr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFramePr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctframepr12a3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "dropCap" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDropCap.Enum getDropCap();

    /**
     * Gets (as xml) the "dropCap" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDropCap xgetDropCap();

    /**
     * True if has "dropCap" attribute
     */
    boolean isSetDropCap();

    /**
     * Sets the "dropCap" attribute
     */
    void setDropCap(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDropCap.Enum dropCap);

    /**
     * Sets (as xml) the "dropCap" attribute
     */
    void xsetDropCap(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDropCap dropCap);

    /**
     * Unsets the "dropCap" attribute
     */
    void unsetDropCap();

    /**
     * Gets the "lines" attribute
     */
    java.math.BigInteger getLines();

    /**
     * Gets (as xml) the "lines" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetLines();

    /**
     * True if has "lines" attribute
     */
    boolean isSetLines();

    /**
     * Sets the "lines" attribute
     */
    void setLines(java.math.BigInteger lines);

    /**
     * Sets (as xml) the "lines" attribute
     */
    void xsetLines(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber lines);

    /**
     * Unsets the "lines" attribute
     */
    void unsetLines();

    /**
     * Gets the "w" attribute
     */
    java.lang.Object getW();

    /**
     * Gets (as xml) the "w" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetW();

    /**
     * True if has "w" attribute
     */
    boolean isSetW();

    /**
     * Sets the "w" attribute
     */
    void setW(java.lang.Object w);

    /**
     * Sets (as xml) the "w" attribute
     */
    void xsetW(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure w);

    /**
     * Unsets the "w" attribute
     */
    void unsetW();

    /**
     * Gets the "h" attribute
     */
    java.lang.Object getH();

    /**
     * Gets (as xml) the "h" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetH();

    /**
     * True if has "h" attribute
     */
    boolean isSetH();

    /**
     * Sets the "h" attribute
     */
    void setH(java.lang.Object h);

    /**
     * Sets (as xml) the "h" attribute
     */
    void xsetH(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure h);

    /**
     * Unsets the "h" attribute
     */
    void unsetH();

    /**
     * Gets the "vSpace" attribute
     */
    java.lang.Object getVSpace();

    /**
     * Gets (as xml) the "vSpace" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetVSpace();

    /**
     * True if has "vSpace" attribute
     */
    boolean isSetVSpace();

    /**
     * Sets the "vSpace" attribute
     */
    void setVSpace(java.lang.Object vSpace);

    /**
     * Sets (as xml) the "vSpace" attribute
     */
    void xsetVSpace(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure vSpace);

    /**
     * Unsets the "vSpace" attribute
     */
    void unsetVSpace();

    /**
     * Gets the "hSpace" attribute
     */
    java.lang.Object getHSpace();

    /**
     * Gets (as xml) the "hSpace" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetHSpace();

    /**
     * True if has "hSpace" attribute
     */
    boolean isSetHSpace();

    /**
     * Sets the "hSpace" attribute
     */
    void setHSpace(java.lang.Object hSpace);

    /**
     * Sets (as xml) the "hSpace" attribute
     */
    void xsetHSpace(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure hSpace);

    /**
     * Unsets the "hSpace" attribute
     */
    void unsetHSpace();

    /**
     * Gets the "wrap" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap.Enum getWrap();

    /**
     * Gets (as xml) the "wrap" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap xgetWrap();

    /**
     * True if has "wrap" attribute
     */
    boolean isSetWrap();

    /**
     * Sets the "wrap" attribute
     */
    void setWrap(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap.Enum wrap);

    /**
     * Sets (as xml) the "wrap" attribute
     */
    void xsetWrap(org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap wrap);

    /**
     * Unsets the "wrap" attribute
     */
    void unsetWrap();

    /**
     * Gets the "hAnchor" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor.Enum getHAnchor();

    /**
     * Gets (as xml) the "hAnchor" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor xgetHAnchor();

    /**
     * True if has "hAnchor" attribute
     */
    boolean isSetHAnchor();

    /**
     * Sets the "hAnchor" attribute
     */
    void setHAnchor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor.Enum hAnchor);

    /**
     * Sets (as xml) the "hAnchor" attribute
     */
    void xsetHAnchor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHAnchor hAnchor);

    /**
     * Unsets the "hAnchor" attribute
     */
    void unsetHAnchor();

    /**
     * Gets the "vAnchor" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor.Enum getVAnchor();

    /**
     * Gets (as xml) the "vAnchor" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor xgetVAnchor();

    /**
     * True if has "vAnchor" attribute
     */
    boolean isSetVAnchor();

    /**
     * Sets the "vAnchor" attribute
     */
    void setVAnchor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor.Enum vAnchor);

    /**
     * Sets (as xml) the "vAnchor" attribute
     */
    void xsetVAnchor(org.openxmlformats.schemas.wordprocessingml.x2006.main.STVAnchor vAnchor);

    /**
     * Unsets the "vAnchor" attribute
     */
    void unsetVAnchor();

    /**
     * Gets the "x" attribute
     */
    java.lang.Object getX();

    /**
     * Gets (as xml) the "x" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure xgetX();

    /**
     * True if has "x" attribute
     */
    boolean isSetX();

    /**
     * Sets the "x" attribute
     */
    void setX(java.lang.Object x);

    /**
     * Sets (as xml) the "x" attribute
     */
    void xsetX(org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure x);

    /**
     * Unsets the "x" attribute
     */
    void unsetX();

    /**
     * Gets the "xAlign" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign.Enum getXAlign();

    /**
     * Gets (as xml) the "xAlign" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign xgetXAlign();

    /**
     * True if has "xAlign" attribute
     */
    boolean isSetXAlign();

    /**
     * Sets the "xAlign" attribute
     */
    void setXAlign(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign.Enum xAlign);

    /**
     * Sets (as xml) the "xAlign" attribute
     */
    void xsetXAlign(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXAlign xAlign);

    /**
     * Unsets the "xAlign" attribute
     */
    void unsetXAlign();

    /**
     * Gets the "y" attribute
     */
    java.lang.Object getY();

    /**
     * Gets (as xml) the "y" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure xgetY();

    /**
     * True if has "y" attribute
     */
    boolean isSetY();

    /**
     * Sets the "y" attribute
     */
    void setY(java.lang.Object y);

    /**
     * Sets (as xml) the "y" attribute
     */
    void xsetY(org.openxmlformats.schemas.wordprocessingml.x2006.main.STSignedTwipsMeasure y);

    /**
     * Unsets the "y" attribute
     */
    void unsetY();

    /**
     * Gets the "yAlign" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign.Enum getYAlign();

    /**
     * Gets (as xml) the "yAlign" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign xgetYAlign();

    /**
     * True if has "yAlign" attribute
     */
    boolean isSetYAlign();

    /**
     * Sets the "yAlign" attribute
     */
    void setYAlign(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign.Enum yAlign);

    /**
     * Sets (as xml) the "yAlign" attribute
     */
    void xsetYAlign(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STYAlign yAlign);

    /**
     * Unsets the "yAlign" attribute
     */
    void unsetYAlign();

    /**
     * Gets the "hRule" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule.Enum getHRule();

    /**
     * Gets (as xml) the "hRule" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule xgetHRule();

    /**
     * True if has "hRule" attribute
     */
    boolean isSetHRule();

    /**
     * Sets the "hRule" attribute
     */
    void setHRule(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule.Enum hRule);

    /**
     * Sets (as xml) the "hRule" attribute
     */
    void xsetHRule(org.openxmlformats.schemas.wordprocessingml.x2006.main.STHeightRule hRule);

    /**
     * Unsets the "hRule" attribute
     */
    void unsetHRule();

    /**
     * Gets the "anchorLock" attribute
     */
    java.lang.Object getAnchorLock();

    /**
     * Gets (as xml) the "anchorLock" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetAnchorLock();

    /**
     * True if has "anchorLock" attribute
     */
    boolean isSetAnchorLock();

    /**
     * Sets the "anchorLock" attribute
     */
    void setAnchorLock(java.lang.Object anchorLock);

    /**
     * Sets (as xml) the "anchorLock" attribute
     */
    void xsetAnchorLock(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff anchorLock);

    /**
     * Unsets the "anchorLock" attribute
     */
    void unsetAnchorLock();
}

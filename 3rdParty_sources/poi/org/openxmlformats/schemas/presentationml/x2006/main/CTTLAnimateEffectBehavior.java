/*
 * XML Type:  CT_TLAnimateEffectBehavior
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLAnimateEffectBehavior(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLAnimateEffectBehavior extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimateEffectBehavior> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlanimateeffectbehavior5083type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cBhvr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData getCBhvr();

    /**
     * Sets the "cBhvr" element
     */
    void setCBhvr(org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData cBhvr);

    /**
     * Appends and returns a new empty "cBhvr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLCommonBehaviorData addNewCBhvr();

    /**
     * Gets the "progress" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant getProgress();

    /**
     * True if has "progress" element
     */
    boolean isSetProgress();

    /**
     * Sets the "progress" element
     */
    void setProgress(org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant progress);

    /**
     * Appends and returns a new empty "progress" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLAnimVariant addNewProgress();

    /**
     * Unsets the "progress" element
     */
    void unsetProgress();

    /**
     * Gets the "transition" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateEffectTransition.Enum getTransition();

    /**
     * Gets (as xml) the "transition" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateEffectTransition xgetTransition();

    /**
     * True if has "transition" attribute
     */
    boolean isSetTransition();

    /**
     * Sets the "transition" attribute
     */
    void setTransition(org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateEffectTransition.Enum transition);

    /**
     * Sets (as xml) the "transition" attribute
     */
    void xsetTransition(org.openxmlformats.schemas.presentationml.x2006.main.STTLAnimateEffectTransition transition);

    /**
     * Unsets the "transition" attribute
     */
    void unsetTransition();

    /**
     * Gets the "filter" attribute
     */
    java.lang.String getFilter();

    /**
     * Gets (as xml) the "filter" attribute
     */
    org.apache.xmlbeans.XmlString xgetFilter();

    /**
     * True if has "filter" attribute
     */
    boolean isSetFilter();

    /**
     * Sets the "filter" attribute
     */
    void setFilter(java.lang.String filter);

    /**
     * Sets (as xml) the "filter" attribute
     */
    void xsetFilter(org.apache.xmlbeans.XmlString filter);

    /**
     * Unsets the "filter" attribute
     */
    void unsetFilter();

    /**
     * Gets the "prLst" attribute
     */
    java.lang.String getPrLst();

    /**
     * Gets (as xml) the "prLst" attribute
     */
    org.apache.xmlbeans.XmlString xgetPrLst();

    /**
     * True if has "prLst" attribute
     */
    boolean isSetPrLst();

    /**
     * Sets the "prLst" attribute
     */
    void setPrLst(java.lang.String prLst);

    /**
     * Sets (as xml) the "prLst" attribute
     */
    void xsetPrLst(org.apache.xmlbeans.XmlString prLst);

    /**
     * Unsets the "prLst" attribute
     */
    void unsetPrLst();
}

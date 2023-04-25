/*
 * XML Type:  CT_SlideTransition
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SlideTransition(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTSlideTransitionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTSlideTransition {
    private static final long serialVersionUID = 1L;

    public CTSlideTransitionImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "blinds"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "checker"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "circle"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "dissolve"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "comb"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cover"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "cut"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "diamond"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "fade"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "newsflash"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "plus"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "pull"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "push"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "random"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "randomBar"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "split"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "strips"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "wedge"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "wheel"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "wipe"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "zoom"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sndAc"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "extLst"),
        new QName("", "spd"),
        new QName("", "advClick"),
        new QName("", "advTm"),
    };


    /**
     * Gets the "blinds" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition getBlinds() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "blinds" element
     */
    @Override
    public boolean isSetBlinds() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "blinds" element
     */
    @Override
    public void setBlinds(org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition blinds) {
        generatedSetterHelperImpl(blinds, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "blinds" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition addNewBlinds() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "blinds" element
     */
    @Override
    public void unsetBlinds() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "checker" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition getChecker() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "checker" element
     */
    @Override
    public boolean isSetChecker() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "checker" element
     */
    @Override
    public void setChecker(org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition checker) {
        generatedSetterHelperImpl(checker, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "checker" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition addNewChecker() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "checker" element
     */
    @Override
    public void unsetChecker() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "circle" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty getCircle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "circle" element
     */
    @Override
    public boolean isSetCircle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "circle" element
     */
    @Override
    public void setCircle(org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty circle) {
        generatedSetterHelperImpl(circle, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "circle" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty addNewCircle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "circle" element
     */
    @Override
    public void unsetCircle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "dissolve" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty getDissolve() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dissolve" element
     */
    @Override
    public boolean isSetDissolve() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "dissolve" element
     */
    @Override
    public void setDissolve(org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty dissolve) {
        generatedSetterHelperImpl(dissolve, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dissolve" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty addNewDissolve() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "dissolve" element
     */
    @Override
    public void unsetDissolve() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "comb" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition getComb() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "comb" element
     */
    @Override
    public boolean isSetComb() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "comb" element
     */
    @Override
    public void setComb(org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition comb) {
        generatedSetterHelperImpl(comb, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "comb" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition addNewComb() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "comb" element
     */
    @Override
    public void unsetComb() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "cover" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition getCover() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cover" element
     */
    @Override
    public boolean isSetCover() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "cover" element
     */
    @Override
    public void setCover(org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition cover) {
        generatedSetterHelperImpl(cover, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cover" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition addNewCover() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "cover" element
     */
    @Override
    public void unsetCover() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "cut" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition getCut() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cut" element
     */
    @Override
    public boolean isSetCut() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "cut" element
     */
    @Override
    public void setCut(org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition cut) {
        generatedSetterHelperImpl(cut, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cut" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition addNewCut() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "cut" element
     */
    @Override
    public void unsetCut() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "diamond" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty getDiamond() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "diamond" element
     */
    @Override
    public boolean isSetDiamond() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "diamond" element
     */
    @Override
    public void setDiamond(org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty diamond) {
        generatedSetterHelperImpl(diamond, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "diamond" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty addNewDiamond() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "diamond" element
     */
    @Override
    public void unsetDiamond() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "fade" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition getFade() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fade" element
     */
    @Override
    public boolean isSetFade() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "fade" element
     */
    @Override
    public void setFade(org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition fade) {
        generatedSetterHelperImpl(fade, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fade" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition addNewFade() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOptionalBlackTransition)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "fade" element
     */
    @Override
    public void unsetFade() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "newsflash" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty getNewsflash() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "newsflash" element
     */
    @Override
    public boolean isSetNewsflash() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "newsflash" element
     */
    @Override
    public void setNewsflash(org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty newsflash) {
        generatedSetterHelperImpl(newsflash, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "newsflash" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty addNewNewsflash() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "newsflash" element
     */
    @Override
    public void unsetNewsflash() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "plus" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty getPlus() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "plus" element
     */
    @Override
    public boolean isSetPlus() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "plus" element
     */
    @Override
    public void setPlus(org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty plus) {
        generatedSetterHelperImpl(plus, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "plus" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty addNewPlus() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "plus" element
     */
    @Override
    public void unsetPlus() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "pull" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition getPull() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pull" element
     */
    @Override
    public boolean isSetPull() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "pull" element
     */
    @Override
    public void setPull(org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition pull) {
        generatedSetterHelperImpl(pull, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pull" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition addNewPull() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEightDirectionTransition)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "pull" element
     */
    @Override
    public void unsetPull() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "push" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition getPush() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "push" element
     */
    @Override
    public boolean isSetPush() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "push" element
     */
    @Override
    public void setPush(org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition push) {
        generatedSetterHelperImpl(push, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "push" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition addNewPush() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "push" element
     */
    @Override
    public void unsetPush() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "random" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty getRandom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "random" element
     */
    @Override
    public boolean isSetRandom() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "random" element
     */
    @Override
    public void setRandom(org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty random) {
        generatedSetterHelperImpl(random, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "random" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty addNewRandom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "random" element
     */
    @Override
    public void unsetRandom() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "randomBar" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition getRandomBar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "randomBar" element
     */
    @Override
    public boolean isSetRandomBar() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "randomBar" element
     */
    @Override
    public void setRandomBar(org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition randomBar) {
        generatedSetterHelperImpl(randomBar, PROPERTY_QNAME[14], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "randomBar" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition addNewRandomBar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTOrientationTransition)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Unsets the "randomBar" element
     */
    @Override
    public void unsetRandomBar() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "split" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSplitTransition getSplit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSplitTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSplitTransition)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "split" element
     */
    @Override
    public boolean isSetSplit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]) != 0;
        }
    }

    /**
     * Sets the "split" element
     */
    @Override
    public void setSplit(org.openxmlformats.schemas.presentationml.x2006.main.CTSplitTransition split) {
        generatedSetterHelperImpl(split, PROPERTY_QNAME[15], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "split" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSplitTransition addNewSplit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSplitTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSplitTransition)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Unsets the "split" element
     */
    @Override
    public void unsetSplit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], 0);
        }
    }

    /**
     * Gets the "strips" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCornerDirectionTransition getStrips() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCornerDirectionTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCornerDirectionTransition)get_store().find_element_user(PROPERTY_QNAME[16], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "strips" element
     */
    @Override
    public boolean isSetStrips() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]) != 0;
        }
    }

    /**
     * Sets the "strips" element
     */
    @Override
    public void setStrips(org.openxmlformats.schemas.presentationml.x2006.main.CTCornerDirectionTransition strips) {
        generatedSetterHelperImpl(strips, PROPERTY_QNAME[16], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "strips" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTCornerDirectionTransition addNewStrips() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTCornerDirectionTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTCornerDirectionTransition)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Unsets the "strips" element
     */
    @Override
    public void unsetStrips() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], 0);
        }
    }

    /**
     * Gets the "wedge" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty getWedge() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "wedge" element
     */
    @Override
    public boolean isSetWedge() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]) != 0;
        }
    }

    /**
     * Sets the "wedge" element
     */
    @Override
    public void setWedge(org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty wedge) {
        generatedSetterHelperImpl(wedge, PROPERTY_QNAME[17], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "wedge" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty addNewWedge() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Unsets the "wedge" element
     */
    @Override
    public void unsetWedge() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], 0);
        }
    }

    /**
     * Gets the "wheel" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTWheelTransition getWheel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTWheelTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTWheelTransition)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "wheel" element
     */
    @Override
    public boolean isSetWheel() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]) != 0;
        }
    }

    /**
     * Sets the "wheel" element
     */
    @Override
    public void setWheel(org.openxmlformats.schemas.presentationml.x2006.main.CTWheelTransition wheel) {
        generatedSetterHelperImpl(wheel, PROPERTY_QNAME[18], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "wheel" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTWheelTransition addNewWheel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTWheelTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTWheelTransition)get_store().add_element_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * Unsets the "wheel" element
     */
    @Override
    public void unsetWheel() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], 0);
        }
    }

    /**
     * Gets the "wipe" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition getWipe() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition)get_store().find_element_user(PROPERTY_QNAME[19], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "wipe" element
     */
    @Override
    public boolean isSetWipe() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[19]) != 0;
        }
    }

    /**
     * Sets the "wipe" element
     */
    @Override
    public void setWipe(org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition wipe) {
        generatedSetterHelperImpl(wipe, PROPERTY_QNAME[19], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "wipe" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition addNewWipe() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTSideDirectionTransition)get_store().add_element_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * Unsets the "wipe" element
     */
    @Override
    public void unsetWipe() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[19], 0);
        }
    }

    /**
     * Gets the "zoom" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTInOutTransition getZoom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTInOutTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTInOutTransition)get_store().find_element_user(PROPERTY_QNAME[20], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "zoom" element
     */
    @Override
    public boolean isSetZoom() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[20]) != 0;
        }
    }

    /**
     * Sets the "zoom" element
     */
    @Override
    public void setZoom(org.openxmlformats.schemas.presentationml.x2006.main.CTInOutTransition zoom) {
        generatedSetterHelperImpl(zoom, PROPERTY_QNAME[20], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "zoom" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTInOutTransition addNewZoom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTInOutTransition target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTInOutTransition)get_store().add_element_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * Unsets the "zoom" element
     */
    @Override
    public void unsetZoom() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[20], 0);
        }
    }

    /**
     * Gets the "sndAc" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionSoundAction getSndAc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionSoundAction target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionSoundAction)get_store().find_element_user(PROPERTY_QNAME[21], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sndAc" element
     */
    @Override
    public boolean isSetSndAc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[21]) != 0;
        }
    }

    /**
     * Sets the "sndAc" element
     */
    @Override
    public void setSndAc(org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionSoundAction sndAc) {
        generatedSetterHelperImpl(sndAc, PROPERTY_QNAME[21], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sndAc" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionSoundAction addNewSndAc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionSoundAction target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTransitionSoundAction)get_store().add_element_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * Unsets the "sndAc" element
     */
    @Override
    public void unsetSndAc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[21], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify)get_store().find_element_user(PROPERTY_QNAME[22], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extLst" element
     */
    @Override
    public boolean isSetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[22]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[22], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify)get_store().add_element_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * Unsets the "extLst" element
     */
    @Override
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[22], 0);
        }
    }

    /**
     * Gets the "spd" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSpeed.Enum getSpd() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[23]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSpeed.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "spd" attribute
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSpeed xgetSpd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSpeed target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSpeed)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSpeed)get_default_attribute_value(PROPERTY_QNAME[23]);
            }
            return target;
        }
    }

    /**
     * True if has "spd" attribute
     */
    @Override
    public boolean isSetSpd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[23]) != null;
        }
    }

    /**
     * Sets the "spd" attribute
     */
    @Override
    public void setSpd(org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSpeed.Enum spd) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.setEnumValue(spd);
        }
    }

    /**
     * Sets (as xml) the "spd" attribute
     */
    @Override
    public void xsetSpd(org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSpeed spd) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSpeed target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSpeed)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.openxmlformats.schemas.presentationml.x2006.main.STTransitionSpeed)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.set(spd);
        }
    }

    /**
     * Unsets the "spd" attribute
     */
    @Override
    public void unsetSpd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Gets the "advClick" attribute
     */
    @Override
    public boolean getAdvClick() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[24]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "advClick" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetAdvClick() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[24]);
            }
            return target;
        }
    }

    /**
     * True if has "advClick" attribute
     */
    @Override
    public boolean isSetAdvClick() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[24]) != null;
        }
    }

    /**
     * Sets the "advClick" attribute
     */
    @Override
    public void setAdvClick(boolean advClick) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.setBooleanValue(advClick);
        }
    }

    /**
     * Sets (as xml) the "advClick" attribute
     */
    @Override
    public void xsetAdvClick(org.apache.xmlbeans.XmlBoolean advClick) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.set(advClick);
        }
    }

    /**
     * Unsets the "advClick" attribute
     */
    @Override
    public void unsetAdvClick() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Gets the "advTm" attribute
     */
    @Override
    public long getAdvTm() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "advTm" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetAdvTm() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * True if has "advTm" attribute
     */
    @Override
    public boolean isSetAdvTm() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[25]) != null;
        }
    }

    /**
     * Sets the "advTm" attribute
     */
    @Override
    public void setAdvTm(long advTm) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.setLongValue(advTm);
        }
    }

    /**
     * Sets (as xml) the "advTm" attribute
     */
    @Override
    public void xsetAdvTm(org.apache.xmlbeans.XmlUnsignedInt advTm) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.set(advTm);
        }
    }

    /**
     * Unsets the "advTm" attribute
     */
    @Override
    public void unsetAdvTm() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[25]);
        }
    }
}

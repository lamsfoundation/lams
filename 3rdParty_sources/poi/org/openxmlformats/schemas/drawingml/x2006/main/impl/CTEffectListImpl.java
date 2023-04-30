/*
 * XML Type:  CT_EffectList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_EffectList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTEffectListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTEffectList {
    private static final long serialVersionUID = 1L;

    public CTEffectListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "blur"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "fillOverlay"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "glow"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "innerShdw"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "outerShdw"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "prstShdw"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "reflection"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "softEdge"),
    };


    /**
     * Gets the "blur" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect getBlur() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "blur" element
     */
    @Override
    public boolean isSetBlur() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "blur" element
     */
    @Override
    public void setBlur(org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect blur) {
        generatedSetterHelperImpl(blur, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "blur" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect addNewBlur() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTBlurEffect)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "blur" element
     */
    @Override
    public void unsetBlur() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "fillOverlay" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect getFillOverlay() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "fillOverlay" element
     */
    @Override
    public boolean isSetFillOverlay() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "fillOverlay" element
     */
    @Override
    public void setFillOverlay(org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect fillOverlay) {
        generatedSetterHelperImpl(fillOverlay, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "fillOverlay" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect addNewFillOverlay() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFillOverlayEffect)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "fillOverlay" element
     */
    @Override
    public void unsetFillOverlay() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "glow" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGlowEffect getGlow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGlowEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGlowEffect)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "glow" element
     */
    @Override
    public boolean isSetGlow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "glow" element
     */
    @Override
    public void setGlow(org.openxmlformats.schemas.drawingml.x2006.main.CTGlowEffect glow) {
        generatedSetterHelperImpl(glow, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "glow" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGlowEffect addNewGlow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGlowEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGlowEffect)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "glow" element
     */
    @Override
    public void unsetGlow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "innerShdw" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTInnerShadowEffect getInnerShdw() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTInnerShadowEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTInnerShadowEffect)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "innerShdw" element
     */
    @Override
    public boolean isSetInnerShdw() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "innerShdw" element
     */
    @Override
    public void setInnerShdw(org.openxmlformats.schemas.drawingml.x2006.main.CTInnerShadowEffect innerShdw) {
        generatedSetterHelperImpl(innerShdw, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "innerShdw" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTInnerShadowEffect addNewInnerShdw() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTInnerShadowEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTInnerShadowEffect)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "innerShdw" element
     */
    @Override
    public void unsetInnerShdw() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "outerShdw" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect getOuterShdw() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "outerShdw" element
     */
    @Override
    public boolean isSetOuterShdw() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "outerShdw" element
     */
    @Override
    public void setOuterShdw(org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect outerShdw) {
        generatedSetterHelperImpl(outerShdw, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "outerShdw" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect addNewOuterShdw() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "outerShdw" element
     */
    @Override
    public void unsetOuterShdw() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "prstShdw" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPresetShadowEffect getPrstShdw() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPresetShadowEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPresetShadowEffect)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "prstShdw" element
     */
    @Override
    public boolean isSetPrstShdw() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "prstShdw" element
     */
    @Override
    public void setPrstShdw(org.openxmlformats.schemas.drawingml.x2006.main.CTPresetShadowEffect prstShdw) {
        generatedSetterHelperImpl(prstShdw, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "prstShdw" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPresetShadowEffect addNewPrstShdw() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPresetShadowEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPresetShadowEffect)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "prstShdw" element
     */
    @Override
    public void unsetPrstShdw() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "reflection" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTReflectionEffect getReflection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTReflectionEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTReflectionEffect)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "reflection" element
     */
    @Override
    public boolean isSetReflection() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "reflection" element
     */
    @Override
    public void setReflection(org.openxmlformats.schemas.drawingml.x2006.main.CTReflectionEffect reflection) {
        generatedSetterHelperImpl(reflection, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "reflection" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTReflectionEffect addNewReflection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTReflectionEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTReflectionEffect)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "reflection" element
     */
    @Override
    public void unsetReflection() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "softEdge" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSoftEdgesEffect getSoftEdge() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSoftEdgesEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSoftEdgesEffect)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "softEdge" element
     */
    @Override
    public boolean isSetSoftEdge() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "softEdge" element
     */
    @Override
    public void setSoftEdge(org.openxmlformats.schemas.drawingml.x2006.main.CTSoftEdgesEffect softEdge) {
        generatedSetterHelperImpl(softEdge, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "softEdge" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTSoftEdgesEffect addNewSoftEdge() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTSoftEdgesEffect target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTSoftEdgesEffect)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "softEdge" element
     */
    @Override
    public void unsetSoftEdge() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }
}

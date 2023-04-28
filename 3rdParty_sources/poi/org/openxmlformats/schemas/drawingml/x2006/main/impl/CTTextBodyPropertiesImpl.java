/*
 * XML Type:  CT_TextBodyProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TextBodyProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public class CTTextBodyPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties {
    private static final long serialVersionUID = 1L;

    public CTTextBodyPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "prstTxWarp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "noAutofit"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "normAutofit"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "spAutoFit"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "scene3d"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "sp3d"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "flatTx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "extLst"),
        new QName("", "rot"),
        new QName("", "spcFirstLastPara"),
        new QName("", "vertOverflow"),
        new QName("", "horzOverflow"),
        new QName("", "vert"),
        new QName("", "wrap"),
        new QName("", "lIns"),
        new QName("", "tIns"),
        new QName("", "rIns"),
        new QName("", "bIns"),
        new QName("", "numCol"),
        new QName("", "spcCol"),
        new QName("", "rtlCol"),
        new QName("", "fromWordArt"),
        new QName("", "anchor"),
        new QName("", "anchorCtr"),
        new QName("", "forceAA"),
        new QName("", "upright"),
        new QName("", "compatLnSpc"),
    };


    /**
     * Gets the "prstTxWarp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPresetTextShape getPrstTxWarp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPresetTextShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPresetTextShape)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "prstTxWarp" element
     */
    @Override
    public boolean isSetPrstTxWarp() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "prstTxWarp" element
     */
    @Override
    public void setPrstTxWarp(org.openxmlformats.schemas.drawingml.x2006.main.CTPresetTextShape prstTxWarp) {
        generatedSetterHelperImpl(prstTxWarp, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "prstTxWarp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPresetTextShape addNewPrstTxWarp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPresetTextShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPresetTextShape)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "prstTxWarp" element
     */
    @Override
    public void unsetPrstTxWarp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "noAutofit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextNoAutofit getNoAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextNoAutofit target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextNoAutofit)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "noAutofit" element
     */
    @Override
    public boolean isSetNoAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "noAutofit" element
     */
    @Override
    public void setNoAutofit(org.openxmlformats.schemas.drawingml.x2006.main.CTTextNoAutofit noAutofit) {
        generatedSetterHelperImpl(noAutofit, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noAutofit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextNoAutofit addNewNoAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextNoAutofit target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextNoAutofit)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "noAutofit" element
     */
    @Override
    public void unsetNoAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "normAutofit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit getNormAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "normAutofit" element
     */
    @Override
    public boolean isSetNormAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "normAutofit" element
     */
    @Override
    public void setNormAutofit(org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit normAutofit) {
        generatedSetterHelperImpl(normAutofit, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "normAutofit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit addNewNormAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextNormalAutofit)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "normAutofit" element
     */
    @Override
    public void unsetNormAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "spAutoFit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextShapeAutofit getSpAutoFit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextShapeAutofit target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextShapeAutofit)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "spAutoFit" element
     */
    @Override
    public boolean isSetSpAutoFit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "spAutoFit" element
     */
    @Override
    public void setSpAutoFit(org.openxmlformats.schemas.drawingml.x2006.main.CTTextShapeAutofit spAutoFit) {
        generatedSetterHelperImpl(spAutoFit, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spAutoFit" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextShapeAutofit addNewSpAutoFit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextShapeAutofit target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextShapeAutofit)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "spAutoFit" element
     */
    @Override
    public void unsetSpAutoFit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "scene3d" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D getScene3D() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "scene3d" element
     */
    @Override
    public boolean isSetScene3D() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "scene3d" element
     */
    @Override
    public void setScene3D(org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D scene3D) {
        generatedSetterHelperImpl(scene3D, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "scene3d" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D addNewScene3D() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTScene3D)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "scene3d" element
     */
    @Override
    public void unsetScene3D() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "sp3d" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D getSp3D() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sp3d" element
     */
    @Override
    public boolean isSetSp3D() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "sp3d" element
     */
    @Override
    public void setSp3D(org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D sp3D) {
        generatedSetterHelperImpl(sp3D, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sp3d" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D addNewSp3D() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShape3D)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "sp3d" element
     */
    @Override
    public void unsetSp3D() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "flatTx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText getFlatTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "flatTx" element
     */
    @Override
    public boolean isSetFlatTx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "flatTx" element
     */
    @Override
    public void setFlatTx(org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText flatTx) {
        generatedSetterHelperImpl(flatTx, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "flatTx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText addNewFlatTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTFlatText)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "flatTx" element
     */
    @Override
    public void unsetFlatTx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[7], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[7]);
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
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "rot" attribute
     */
    @Override
    public int getRot() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "rot" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STAngle xgetRot() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAngle)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * True if has "rot" attribute
     */
    @Override
    public boolean isSetRot() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "rot" attribute
     */
    @Override
    public void setRot(int rot) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setIntValue(rot);
        }
    }

    /**
     * Sets (as xml) the "rot" attribute
     */
    @Override
    public void xsetRot(org.openxmlformats.schemas.drawingml.x2006.main.STAngle rot) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STAngle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STAngle)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STAngle)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(rot);
        }
    }

    /**
     * Unsets the "rot" attribute
     */
    @Override
    public void unsetRot() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Gets the "spcFirstLastPara" attribute
     */
    @Override
    public boolean getSpcFirstLastPara() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "spcFirstLastPara" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetSpcFirstLastPara() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * True if has "spcFirstLastPara" attribute
     */
    @Override
    public boolean isSetSpcFirstLastPara() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "spcFirstLastPara" attribute
     */
    @Override
    public void setSpcFirstLastPara(boolean spcFirstLastPara) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setBooleanValue(spcFirstLastPara);
        }
    }

    /**
     * Sets (as xml) the "spcFirstLastPara" attribute
     */
    @Override
    public void xsetSpcFirstLastPara(org.apache.xmlbeans.XmlBoolean spcFirstLastPara) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(spcFirstLastPara);
        }
    }

    /**
     * Unsets the "spcFirstLastPara" attribute
     */
    @Override
    public void unsetSpcFirstLastPara() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Gets the "vertOverflow" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextVertOverflowType.Enum getVertOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTextVertOverflowType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "vertOverflow" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextVertOverflowType xgetVertOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextVertOverflowType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextVertOverflowType)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * True if has "vertOverflow" attribute
     */
    @Override
    public boolean isSetVertOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[10]) != null;
        }
    }

    /**
     * Sets the "vertOverflow" attribute
     */
    @Override
    public void setVertOverflow(org.openxmlformats.schemas.drawingml.x2006.main.STTextVertOverflowType.Enum vertOverflow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.setEnumValue(vertOverflow);
        }
    }

    /**
     * Sets (as xml) the "vertOverflow" attribute
     */
    @Override
    public void xsetVertOverflow(org.openxmlformats.schemas.drawingml.x2006.main.STTextVertOverflowType vertOverflow) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextVertOverflowType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextVertOverflowType)get_store().find_attribute_user(PROPERTY_QNAME[10]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextVertOverflowType)get_store().add_attribute_user(PROPERTY_QNAME[10]);
            }
            target.set(vertOverflow);
        }
    }

    /**
     * Unsets the "vertOverflow" attribute
     */
    @Override
    public void unsetVertOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Gets the "horzOverflow" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType.Enum getHorzOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "horzOverflow" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType xgetHorzOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * True if has "horzOverflow" attribute
     */
    @Override
    public boolean isSetHorzOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[11]) != null;
        }
    }

    /**
     * Sets the "horzOverflow" attribute
     */
    @Override
    public void setHorzOverflow(org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType.Enum horzOverflow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.setEnumValue(horzOverflow);
        }
    }

    /**
     * Sets (as xml) the "horzOverflow" attribute
     */
    @Override
    public void xsetHorzOverflow(org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType horzOverflow) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType)get_store().find_attribute_user(PROPERTY_QNAME[11]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextHorzOverflowType)get_store().add_attribute_user(PROPERTY_QNAME[11]);
            }
            target.set(horzOverflow);
        }
    }

    /**
     * Unsets the "horzOverflow" attribute
     */
    @Override
    public void unsetHorzOverflow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Gets the "vert" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType.Enum getVert() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "vert" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType xgetVert() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * True if has "vert" attribute
     */
    @Override
    public boolean isSetVert() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[12]) != null;
        }
    }

    /**
     * Sets the "vert" attribute
     */
    @Override
    public void setVert(org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType.Enum vert) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.setEnumValue(vert);
        }
    }

    /**
     * Sets (as xml) the "vert" attribute
     */
    @Override
    public void xsetVert(org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType vert) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType)get_store().find_attribute_user(PROPERTY_QNAME[12]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextVerticalType)get_store().add_attribute_user(PROPERTY_QNAME[12]);
            }
            target.set(vert);
        }
    }

    /**
     * Unsets the "vert" attribute
     */
    @Override
    public void unsetVert() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Gets the "wrap" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextWrappingType.Enum getWrap() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTextWrappingType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "wrap" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextWrappingType xgetWrap() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextWrappingType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextWrappingType)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * True if has "wrap" attribute
     */
    @Override
    public boolean isSetWrap() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[13]) != null;
        }
    }

    /**
     * Sets the "wrap" attribute
     */
    @Override
    public void setWrap(org.openxmlformats.schemas.drawingml.x2006.main.STTextWrappingType.Enum wrap) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.setEnumValue(wrap);
        }
    }

    /**
     * Sets (as xml) the "wrap" attribute
     */
    @Override
    public void xsetWrap(org.openxmlformats.schemas.drawingml.x2006.main.STTextWrappingType wrap) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextWrappingType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextWrappingType)get_store().find_attribute_user(PROPERTY_QNAME[13]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextWrappingType)get_store().add_attribute_user(PROPERTY_QNAME[13]);
            }
            target.set(wrap);
        }
    }

    /**
     * Unsets the "wrap" attribute
     */
    @Override
    public void unsetWrap() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Gets the "lIns" attribute
     */
    @Override
    public java.lang.Object getLIns() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "lIns" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 xgetLIns() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * True if has "lIns" attribute
     */
    @Override
    public boolean isSetLIns() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[14]) != null;
        }
    }

    /**
     * Sets the "lIns" attribute
     */
    @Override
    public void setLIns(java.lang.Object lIns) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.setObjectValue(lIns);
        }
    }

    /**
     * Sets (as xml) the "lIns" attribute
     */
    @Override
    public void xsetLIns(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 lIns) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[14]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().add_attribute_user(PROPERTY_QNAME[14]);
            }
            target.set(lIns);
        }
    }

    /**
     * Unsets the "lIns" attribute
     */
    @Override
    public void unsetLIns() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Gets the "tIns" attribute
     */
    @Override
    public java.lang.Object getTIns() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "tIns" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 xgetTIns() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * True if has "tIns" attribute
     */
    @Override
    public boolean isSetTIns() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[15]) != null;
        }
    }

    /**
     * Sets the "tIns" attribute
     */
    @Override
    public void setTIns(java.lang.Object tIns) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.setObjectValue(tIns);
        }
    }

    /**
     * Sets (as xml) the "tIns" attribute
     */
    @Override
    public void xsetTIns(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 tIns) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[15]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().add_attribute_user(PROPERTY_QNAME[15]);
            }
            target.set(tIns);
        }
    }

    /**
     * Unsets the "tIns" attribute
     */
    @Override
    public void unsetTIns() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Gets the "rIns" attribute
     */
    @Override
    public java.lang.Object getRIns() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "rIns" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 xgetRIns() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * True if has "rIns" attribute
     */
    @Override
    public boolean isSetRIns() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[16]) != null;
        }
    }

    /**
     * Sets the "rIns" attribute
     */
    @Override
    public void setRIns(java.lang.Object rIns) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.setObjectValue(rIns);
        }
    }

    /**
     * Sets (as xml) the "rIns" attribute
     */
    @Override
    public void xsetRIns(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 rIns) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[16]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().add_attribute_user(PROPERTY_QNAME[16]);
            }
            target.set(rIns);
        }
    }

    /**
     * Unsets the "rIns" attribute
     */
    @Override
    public void unsetRIns() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Gets the "bIns" attribute
     */
    @Override
    public java.lang.Object getBIns() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "bIns" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 xgetBIns() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * True if has "bIns" attribute
     */
    @Override
    public boolean isSetBIns() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[17]) != null;
        }
    }

    /**
     * Sets the "bIns" attribute
     */
    @Override
    public void setBIns(java.lang.Object bIns) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.setObjectValue(bIns);
        }
    }

    /**
     * Sets (as xml) the "bIns" attribute
     */
    @Override
    public void xsetBIns(org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 bIns) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[17]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STCoordinate32)get_store().add_attribute_user(PROPERTY_QNAME[17]);
            }
            target.set(bIns);
        }
    }

    /**
     * Unsets the "bIns" attribute
     */
    @Override
    public void unsetBIns() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Gets the "numCol" attribute
     */
    @Override
    public int getNumCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "numCol" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextColumnCount xgetNumCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextColumnCount target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextColumnCount)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * True if has "numCol" attribute
     */
    @Override
    public boolean isSetNumCol() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[18]) != null;
        }
    }

    /**
     * Sets the "numCol" attribute
     */
    @Override
    public void setNumCol(int numCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.setIntValue(numCol);
        }
    }

    /**
     * Sets (as xml) the "numCol" attribute
     */
    @Override
    public void xsetNumCol(org.openxmlformats.schemas.drawingml.x2006.main.STTextColumnCount numCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextColumnCount target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextColumnCount)get_store().find_attribute_user(PROPERTY_QNAME[18]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextColumnCount)get_store().add_attribute_user(PROPERTY_QNAME[18]);
            }
            target.set(numCol);
        }
    }

    /**
     * Unsets the "numCol" attribute
     */
    @Override
    public void unsetNumCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Gets the "spcCol" attribute
     */
    @Override
    public int getSpcCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "spcCol" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate32 xgetSpcCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * True if has "spcCol" attribute
     */
    @Override
    public boolean isSetSpcCol() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[19]) != null;
        }
    }

    /**
     * Sets the "spcCol" attribute
     */
    @Override
    public void setSpcCol(int spcCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.setIntValue(spcCol);
        }
    }

    /**
     * Sets (as xml) the "spcCol" attribute
     */
    @Override
    public void xsetSpcCol(org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate32 spcCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate32 target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate32)get_store().find_attribute_user(PROPERTY_QNAME[19]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STPositiveCoordinate32)get_store().add_attribute_user(PROPERTY_QNAME[19]);
            }
            target.set(spcCol);
        }
    }

    /**
     * Unsets the "spcCol" attribute
     */
    @Override
    public void unsetSpcCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Gets the "rtlCol" attribute
     */
    @Override
    public boolean getRtlCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "rtlCol" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetRtlCol() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * True if has "rtlCol" attribute
     */
    @Override
    public boolean isSetRtlCol() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[20]) != null;
        }
    }

    /**
     * Sets the "rtlCol" attribute
     */
    @Override
    public void setRtlCol(boolean rtlCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.setBooleanValue(rtlCol);
        }
    }

    /**
     * Sets (as xml) the "rtlCol" attribute
     */
    @Override
    public void xsetRtlCol(org.apache.xmlbeans.XmlBoolean rtlCol) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[20]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[20]);
            }
            target.set(rtlCol);
        }
    }

    /**
     * Unsets the "rtlCol" attribute
     */
    @Override
    public void unsetRtlCol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Gets the "fromWordArt" attribute
     */
    @Override
    public boolean getFromWordArt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "fromWordArt" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetFromWordArt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * True if has "fromWordArt" attribute
     */
    @Override
    public boolean isSetFromWordArt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[21]) != null;
        }
    }

    /**
     * Sets the "fromWordArt" attribute
     */
    @Override
    public void setFromWordArt(boolean fromWordArt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.setBooleanValue(fromWordArt);
        }
    }

    /**
     * Sets (as xml) the "fromWordArt" attribute
     */
    @Override
    public void xsetFromWordArt(org.apache.xmlbeans.XmlBoolean fromWordArt) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[21]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[21]);
            }
            target.set(fromWordArt);
        }
    }

    /**
     * Unsets the "fromWordArt" attribute
     */
    @Override
    public void unsetFromWordArt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Gets the "anchor" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType.Enum getAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "anchor" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType xgetAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * True if has "anchor" attribute
     */
    @Override
    public boolean isSetAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[22]) != null;
        }
    }

    /**
     * Sets the "anchor" attribute
     */
    @Override
    public void setAnchor(org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType.Enum anchor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.setEnumValue(anchor);
        }
    }

    /**
     * Sets (as xml) the "anchor" attribute
     */
    @Override
    public void xsetAnchor(org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType anchor) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType)get_store().find_attribute_user(PROPERTY_QNAME[22]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STTextAnchoringType)get_store().add_attribute_user(PROPERTY_QNAME[22]);
            }
            target.set(anchor);
        }
    }

    /**
     * Unsets the "anchor" attribute
     */
    @Override
    public void unsetAnchor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Gets the "anchorCtr" attribute
     */
    @Override
    public boolean getAnchorCtr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "anchorCtr" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetAnchorCtr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * True if has "anchorCtr" attribute
     */
    @Override
    public boolean isSetAnchorCtr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[23]) != null;
        }
    }

    /**
     * Sets the "anchorCtr" attribute
     */
    @Override
    public void setAnchorCtr(boolean anchorCtr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.setBooleanValue(anchorCtr);
        }
    }

    /**
     * Sets (as xml) the "anchorCtr" attribute
     */
    @Override
    public void xsetAnchorCtr(org.apache.xmlbeans.XmlBoolean anchorCtr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[23]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[23]);
            }
            target.set(anchorCtr);
        }
    }

    /**
     * Unsets the "anchorCtr" attribute
     */
    @Override
    public void unsetAnchorCtr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Gets the "forceAA" attribute
     */
    @Override
    public boolean getForceAA() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "forceAA" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetForceAA() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * True if has "forceAA" attribute
     */
    @Override
    public boolean isSetForceAA() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[24]) != null;
        }
    }

    /**
     * Sets the "forceAA" attribute
     */
    @Override
    public void setForceAA(boolean forceAA) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.setBooleanValue(forceAA);
        }
    }

    /**
     * Sets (as xml) the "forceAA" attribute
     */
    @Override
    public void xsetForceAA(org.apache.xmlbeans.XmlBoolean forceAA) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[24]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[24]);
            }
            target.set(forceAA);
        }
    }

    /**
     * Unsets the "forceAA" attribute
     */
    @Override
    public void unsetForceAA() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Gets the "upright" attribute
     */
    @Override
    public boolean getUpright() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[25]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "upright" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetUpright() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[25]);
            }
            return target;
        }
    }

    /**
     * True if has "upright" attribute
     */
    @Override
    public boolean isSetUpright() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[25]) != null;
        }
    }

    /**
     * Sets the "upright" attribute
     */
    @Override
    public void setUpright(boolean upright) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.setBooleanValue(upright);
        }
    }

    /**
     * Sets (as xml) the "upright" attribute
     */
    @Override
    public void xsetUpright(org.apache.xmlbeans.XmlBoolean upright) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[25]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[25]);
            }
            target.set(upright);
        }
    }

    /**
     * Unsets the "upright" attribute
     */
    @Override
    public void unsetUpright() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Gets the "compatLnSpc" attribute
     */
    @Override
    public boolean getCompatLnSpc() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "compatLnSpc" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetCompatLnSpc() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * True if has "compatLnSpc" attribute
     */
    @Override
    public boolean isSetCompatLnSpc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[26]) != null;
        }
    }

    /**
     * Sets the "compatLnSpc" attribute
     */
    @Override
    public void setCompatLnSpc(boolean compatLnSpc) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.setBooleanValue(compatLnSpc);
        }
    }

    /**
     * Sets (as xml) the "compatLnSpc" attribute
     */
    @Override
    public void xsetCompatLnSpc(org.apache.xmlbeans.XmlBoolean compatLnSpc) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[26]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[26]);
            }
            target.set(compatLnSpc);
        }
    }

    /**
     * Unsets the "compatLnSpc" attribute
     */
    @Override
    public void unsetCompatLnSpc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[26]);
        }
    }
}

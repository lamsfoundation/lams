/*
 * XML Type:  CT_Variant
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Variant(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes).
 *
 * This is a complex type.
 */
public class CTVariantImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant {
    private static final long serialVersionUID = 1L;

    public CTVariantImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "variant"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "vector"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "array"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "blob"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "oblob"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "empty"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "null"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "i1"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "i2"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "i4"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "i8"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "int"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "ui1"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "ui2"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "ui4"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "ui8"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "uint"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "r4"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "r8"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "decimal"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "lpstr"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "lpwstr"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "bstr"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "date"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "filetime"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "bool"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "cy"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "error"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "stream"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "ostream"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "storage"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "ostorage"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "vstream"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes", "clsid"),
    };


    /**
     * Gets the "variant" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant getVariant() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "variant" element
     */
    @Override
    public boolean isSetVariant() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "variant" element
     */
    @Override
    public void setVariant(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant variant) {
        generatedSetterHelperImpl(variant, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "variant" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant addNewVariant() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVariant)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "variant" element
     */
    @Override
    public void unsetVariant() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "vector" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector getVector() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "vector" element
     */
    @Override
    public boolean isSetVector() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "vector" element
     */
    @Override
    public void setVector(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector vector) {
        generatedSetterHelperImpl(vector, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "vector" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector addNewVector() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVector)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "vector" element
     */
    @Override
    public void unsetVector() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "array" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTArray getArray() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTArray target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTArray)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "array" element
     */
    @Override
    public boolean isSetArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "array" element
     */
    @Override
    public void setArray(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTArray array) {
        generatedSetterHelperImpl(array, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "array" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTArray addNewArray() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTArray target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTArray)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "array" element
     */
    @Override
    public void unsetArray() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "blob" element
     */
    @Override
    public byte[] getBlob() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "blob" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetBlob() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return target;
        }
    }

    /**
     * True if has "blob" element
     */
    @Override
    public boolean isSetBlob() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "blob" element
     */
    @Override
    public void setBlob(byte[] blob) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[3]);
            }
            target.setByteArrayValue(blob);
        }
    }

    /**
     * Sets (as xml) the "blob" element
     */
    @Override
    public void xsetBlob(org.apache.xmlbeans.XmlBase64Binary blob) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(PROPERTY_QNAME[3]);
            }
            target.set(blob);
        }
    }

    /**
     * Unsets the "blob" element
     */
    @Override
    public void unsetBlob() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "oblob" element
     */
    @Override
    public byte[] getOblob() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "oblob" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetOblob() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return target;
        }
    }

    /**
     * True if has "oblob" element
     */
    @Override
    public boolean isSetOblob() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "oblob" element
     */
    @Override
    public void setOblob(byte[] oblob) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[4]);
            }
            target.setByteArrayValue(oblob);
        }
    }

    /**
     * Sets (as xml) the "oblob" element
     */
    @Override
    public void xsetOblob(org.apache.xmlbeans.XmlBase64Binary oblob) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(PROPERTY_QNAME[4]);
            }
            target.set(oblob);
        }
    }

    /**
     * Unsets the "oblob" element
     */
    @Override
    public void unsetOblob() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "empty" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTEmpty getEmpty() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTEmpty target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "empty" element
     */
    @Override
    public boolean isSetEmpty() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "empty" element
     */
    @Override
    public void setEmpty(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTEmpty empty) {
        generatedSetterHelperImpl(empty, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "empty" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTEmpty addNewEmpty() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTEmpty target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "empty" element
     */
    @Override
    public void unsetEmpty() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "null" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTNull getNull() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTNull target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTNull)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "null" element
     */
    @Override
    public boolean isSetNull() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "null" element
     */
    @Override
    public void setNull(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTNull xnull) {
        generatedSetterHelperImpl(xnull, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "null" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTNull addNewNull() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTNull target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTNull)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "null" element
     */
    @Override
    public void unsetNull() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "i1" element
     */
    @Override
    public byte getI1() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? 0 : target.getByteValue();
        }
    }

    /**
     * Gets (as xml) the "i1" element
     */
    @Override
    public org.apache.xmlbeans.XmlByte xgetI1() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlByte target = null;
            target = (org.apache.xmlbeans.XmlByte)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return target;
        }
    }

    /**
     * True if has "i1" element
     */
    @Override
    public boolean isSetI1() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "i1" element
     */
    @Override
    public void setI1(byte i1) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[7]);
            }
            target.setByteValue(i1);
        }
    }

    /**
     * Sets (as xml) the "i1" element
     */
    @Override
    public void xsetI1(org.apache.xmlbeans.XmlByte i1) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlByte target = null;
            target = (org.apache.xmlbeans.XmlByte)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlByte)get_store().add_element_user(PROPERTY_QNAME[7]);
            }
            target.set(i1);
        }
    }

    /**
     * Unsets the "i1" element
     */
    @Override
    public void unsetI1() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "i2" element
     */
    @Override
    public short getI2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? 0 : target.getShortValue();
        }
    }

    /**
     * Gets (as xml) the "i2" element
     */
    @Override
    public org.apache.xmlbeans.XmlShort xgetI2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlShort target = null;
            target = (org.apache.xmlbeans.XmlShort)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return target;
        }
    }

    /**
     * True if has "i2" element
     */
    @Override
    public boolean isSetI2() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "i2" element
     */
    @Override
    public void setI2(short i2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[8]);
            }
            target.setShortValue(i2);
        }
    }

    /**
     * Sets (as xml) the "i2" element
     */
    @Override
    public void xsetI2(org.apache.xmlbeans.XmlShort i2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlShort target = null;
            target = (org.apache.xmlbeans.XmlShort)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlShort)get_store().add_element_user(PROPERTY_QNAME[8]);
            }
            target.set(i2);
        }
    }

    /**
     * Unsets the "i2" element
     */
    @Override
    public void unsetI2() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "i4" element
     */
    @Override
    public int getI4() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "i4" element
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetI4() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return target;
        }
    }

    /**
     * True if has "i4" element
     */
    @Override
    public boolean isSetI4() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "i4" element
     */
    @Override
    public void setI4(int i4) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[9]);
            }
            target.setIntValue(i4);
        }
    }

    /**
     * Sets (as xml) the "i4" element
     */
    @Override
    public void xsetI4(org.apache.xmlbeans.XmlInt i4) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(PROPERTY_QNAME[9]);
            }
            target.set(i4);
        }
    }

    /**
     * Unsets the "i4" element
     */
    @Override
    public void unsetI4() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "i8" element
     */
    @Override
    public long getI8() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "i8" element
     */
    @Override
    public org.apache.xmlbeans.XmlLong xgetI8() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return target;
        }
    }

    /**
     * True if has "i8" element
     */
    @Override
    public boolean isSetI8() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "i8" element
     */
    @Override
    public void setI8(long i8) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[10]);
            }
            target.setLongValue(i8);
        }
    }

    /**
     * Sets (as xml) the "i8" element
     */
    @Override
    public void xsetI8(org.apache.xmlbeans.XmlLong i8) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(PROPERTY_QNAME[10]);
            }
            target.set(i8);
        }
    }

    /**
     * Unsets the "i8" element
     */
    @Override
    public void unsetI8() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "int" element
     */
    @Override
    public int getInt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "int" element
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetInt() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return target;
        }
    }

    /**
     * True if has "int" element
     */
    @Override
    public boolean isSetInt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "int" element
     */
    @Override
    public void setInt(int xint) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[11]);
            }
            target.setIntValue(xint);
        }
    }

    /**
     * Sets (as xml) the "int" element
     */
    @Override
    public void xsetInt(org.apache.xmlbeans.XmlInt xint) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(PROPERTY_QNAME[11]);
            }
            target.set(xint);
        }
    }

    /**
     * Unsets the "int" element
     */
    @Override
    public void unsetInt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "ui1" element
     */
    @Override
    public short getUi1() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? 0 : target.getShortValue();
        }
    }

    /**
     * Gets (as xml) the "ui1" element
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedByte xgetUi1() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return target;
        }
    }

    /**
     * True if has "ui1" element
     */
    @Override
    public boolean isSetUi1() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "ui1" element
     */
    @Override
    public void setUi1(short ui1) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[12]);
            }
            target.setShortValue(ui1);
        }
    }

    /**
     * Sets (as xml) the "ui1" element
     */
    @Override
    public void xsetUi1(org.apache.xmlbeans.XmlUnsignedByte ui1) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedByte target = null;
            target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedByte)get_store().add_element_user(PROPERTY_QNAME[12]);
            }
            target.set(ui1);
        }
    }

    /**
     * Unsets the "ui1" element
     */
    @Override
    public void unsetUi1() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "ui2" element
     */
    @Override
    public int getUi2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "ui2" element
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedShort xgetUi2() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedShort target = null;
            target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return target;
        }
    }

    /**
     * True if has "ui2" element
     */
    @Override
    public boolean isSetUi2() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "ui2" element
     */
    @Override
    public void setUi2(int ui2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[13]);
            }
            target.setIntValue(ui2);
        }
    }

    /**
     * Sets (as xml) the "ui2" element
     */
    @Override
    public void xsetUi2(org.apache.xmlbeans.XmlUnsignedShort ui2) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedShort target = null;
            target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedShort)get_store().add_element_user(PROPERTY_QNAME[13]);
            }
            target.set(ui2);
        }
    }

    /**
     * Unsets the "ui2" element
     */
    @Override
    public void unsetUi2() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "ui4" element
     */
    @Override
    public long getUi4() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "ui4" element
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetUi4() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return target;
        }
    }

    /**
     * True if has "ui4" element
     */
    @Override
    public boolean isSetUi4() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "ui4" element
     */
    @Override
    public void setUi4(long ui4) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[14]);
            }
            target.setLongValue(ui4);
        }
    }

    /**
     * Sets (as xml) the "ui4" element
     */
    @Override
    public void xsetUi4(org.apache.xmlbeans.XmlUnsignedInt ui4) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_element_user(PROPERTY_QNAME[14]);
            }
            target.set(ui4);
        }
    }

    /**
     * Unsets the "ui4" element
     */
    @Override
    public void unsetUi4() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "ui8" element
     */
    @Override
    public java.math.BigInteger getUi8() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "ui8" element
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedLong xgetUi8() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedLong target = null;
            target = (org.apache.xmlbeans.XmlUnsignedLong)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return target;
        }
    }

    /**
     * True if has "ui8" element
     */
    @Override
    public boolean isSetUi8() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]) != 0;
        }
    }

    /**
     * Sets the "ui8" element
     */
    @Override
    public void setUi8(java.math.BigInteger ui8) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[15]);
            }
            target.setBigIntegerValue(ui8);
        }
    }

    /**
     * Sets (as xml) the "ui8" element
     */
    @Override
    public void xsetUi8(org.apache.xmlbeans.XmlUnsignedLong ui8) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedLong target = null;
            target = (org.apache.xmlbeans.XmlUnsignedLong)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedLong)get_store().add_element_user(PROPERTY_QNAME[15]);
            }
            target.set(ui8);
        }
    }

    /**
     * Unsets the "ui8" element
     */
    @Override
    public void unsetUi8() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], 0);
        }
    }

    /**
     * Gets the "uint" element
     */
    @Override
    public long getUint() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[16], 0);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "uint" element
     */
    @Override
    public org.apache.xmlbeans.XmlUnsignedInt xgetUint() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(PROPERTY_QNAME[16], 0);
            return target;
        }
    }

    /**
     * True if has "uint" element
     */
    @Override
    public boolean isSetUint() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]) != 0;
        }
    }

    /**
     * Sets the "uint" element
     */
    @Override
    public void setUint(long uint) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[16], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[16]);
            }
            target.setLongValue(uint);
        }
    }

    /**
     * Sets (as xml) the "uint" element
     */
    @Override
    public void xsetUint(org.apache.xmlbeans.XmlUnsignedInt uint) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlUnsignedInt target = null;
            target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().find_element_user(PROPERTY_QNAME[16], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlUnsignedInt)get_store().add_element_user(PROPERTY_QNAME[16]);
            }
            target.set(uint);
        }
    }

    /**
     * Unsets the "uint" element
     */
    @Override
    public void unsetUint() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], 0);
        }
    }

    /**
     * Gets the "r4" element
     */
    @Override
    public float getR4() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            return (target == null) ? 0.0f : target.getFloatValue();
        }
    }

    /**
     * Gets (as xml) the "r4" element
     */
    @Override
    public org.apache.xmlbeans.XmlFloat xgetR4() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            return target;
        }
    }

    /**
     * True if has "r4" element
     */
    @Override
    public boolean isSetR4() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]) != 0;
        }
    }

    /**
     * Sets the "r4" element
     */
    @Override
    public void setR4(float r4) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[17]);
            }
            target.setFloatValue(r4);
        }
    }

    /**
     * Sets (as xml) the "r4" element
     */
    @Override
    public void xsetR4(org.apache.xmlbeans.XmlFloat r4) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlFloat)get_store().add_element_user(PROPERTY_QNAME[17]);
            }
            target.set(r4);
        }
    }

    /**
     * Unsets the "r4" element
     */
    @Override
    public void unsetR4() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], 0);
        }
    }

    /**
     * Gets the "r8" element
     */
    @Override
    public double getR8() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            return (target == null) ? 0.0 : target.getDoubleValue();
        }
    }

    /**
     * Gets (as xml) the "r8" element
     */
    @Override
    public org.apache.xmlbeans.XmlDouble xgetR8() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            return target;
        }
    }

    /**
     * True if has "r8" element
     */
    @Override
    public boolean isSetR8() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]) != 0;
        }
    }

    /**
     * Sets the "r8" element
     */
    @Override
    public void setR8(double r8) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[18]);
            }
            target.setDoubleValue(r8);
        }
    }

    /**
     * Sets (as xml) the "r8" element
     */
    @Override
    public void xsetR8(org.apache.xmlbeans.XmlDouble r8) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(PROPERTY_QNAME[18]);
            }
            target.set(r8);
        }
    }

    /**
     * Unsets the "r8" element
     */
    @Override
    public void unsetR8() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], 0);
        }
    }

    /**
     * Gets the "decimal" element
     */
    @Override
    public java.math.BigDecimal getDecimal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[19], 0);
            return (target == null) ? null : target.getBigDecimalValue();
        }
    }

    /**
     * Gets (as xml) the "decimal" element
     */
    @Override
    public org.apache.xmlbeans.XmlDecimal xgetDecimal() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDecimal target = null;
            target = (org.apache.xmlbeans.XmlDecimal)get_store().find_element_user(PROPERTY_QNAME[19], 0);
            return target;
        }
    }

    /**
     * True if has "decimal" element
     */
    @Override
    public boolean isSetDecimal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[19]) != 0;
        }
    }

    /**
     * Sets the "decimal" element
     */
    @Override
    public void setDecimal(java.math.BigDecimal decimal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[19], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[19]);
            }
            target.setBigDecimalValue(decimal);
        }
    }

    /**
     * Sets (as xml) the "decimal" element
     */
    @Override
    public void xsetDecimal(org.apache.xmlbeans.XmlDecimal decimal) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDecimal target = null;
            target = (org.apache.xmlbeans.XmlDecimal)get_store().find_element_user(PROPERTY_QNAME[19], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDecimal)get_store().add_element_user(PROPERTY_QNAME[19]);
            }
            target.set(decimal);
        }
    }

    /**
     * Unsets the "decimal" element
     */
    @Override
    public void unsetDecimal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[19], 0);
        }
    }

    /**
     * Gets the "lpstr" element
     */
    @Override
    public java.lang.String getLpstr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[20], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "lpstr" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetLpstr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[20], 0);
            return target;
        }
    }

    /**
     * True if has "lpstr" element
     */
    @Override
    public boolean isSetLpstr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[20]) != 0;
        }
    }

    /**
     * Sets the "lpstr" element
     */
    @Override
    public void setLpstr(java.lang.String lpstr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[20], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[20]);
            }
            target.setStringValue(lpstr);
        }
    }

    /**
     * Sets (as xml) the "lpstr" element
     */
    @Override
    public void xsetLpstr(org.apache.xmlbeans.XmlString lpstr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[20], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[20]);
            }
            target.set(lpstr);
        }
    }

    /**
     * Unsets the "lpstr" element
     */
    @Override
    public void unsetLpstr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[20], 0);
        }
    }

    /**
     * Gets the "lpwstr" element
     */
    @Override
    public java.lang.String getLpwstr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[21], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "lpwstr" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetLpwstr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[21], 0);
            return target;
        }
    }

    /**
     * True if has "lpwstr" element
     */
    @Override
    public boolean isSetLpwstr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[21]) != 0;
        }
    }

    /**
     * Sets the "lpwstr" element
     */
    @Override
    public void setLpwstr(java.lang.String lpwstr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[21], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[21]);
            }
            target.setStringValue(lpwstr);
        }
    }

    /**
     * Sets (as xml) the "lpwstr" element
     */
    @Override
    public void xsetLpwstr(org.apache.xmlbeans.XmlString lpwstr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[21], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[21]);
            }
            target.set(lpwstr);
        }
    }

    /**
     * Unsets the "lpwstr" element
     */
    @Override
    public void unsetLpwstr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[21], 0);
        }
    }

    /**
     * Gets the "bstr" element
     */
    @Override
    public java.lang.String getBstr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[22], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "bstr" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetBstr() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[22], 0);
            return target;
        }
    }

    /**
     * True if has "bstr" element
     */
    @Override
    public boolean isSetBstr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[22]) != 0;
        }
    }

    /**
     * Sets the "bstr" element
     */
    @Override
    public void setBstr(java.lang.String bstr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[22], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[22]);
            }
            target.setStringValue(bstr);
        }
    }

    /**
     * Sets (as xml) the "bstr" element
     */
    @Override
    public void xsetBstr(org.apache.xmlbeans.XmlString bstr) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[22], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[22]);
            }
            target.set(bstr);
        }
    }

    /**
     * Unsets the "bstr" element
     */
    @Override
    public void unsetBstr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[22], 0);
        }
    }

    /**
     * Gets the "date" element
     */
    @Override
    public java.util.Calendar getDate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[23], 0);
            return (target == null) ? null : target.getCalendarValue();
        }
    }

    /**
     * Gets (as xml) the "date" element
     */
    @Override
    public org.apache.xmlbeans.XmlDateTime xgetDate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(PROPERTY_QNAME[23], 0);
            return target;
        }
    }

    /**
     * True if has "date" element
     */
    @Override
    public boolean isSetDate() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[23]) != 0;
        }
    }

    /**
     * Sets the "date" element
     */
    @Override
    public void setDate(java.util.Calendar date) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[23], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[23]);
            }
            target.setCalendarValue(date);
        }
    }

    /**
     * Sets (as xml) the "date" element
     */
    @Override
    public void xsetDate(org.apache.xmlbeans.XmlDateTime date) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(PROPERTY_QNAME[23], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDateTime)get_store().add_element_user(PROPERTY_QNAME[23]);
            }
            target.set(date);
        }
    }

    /**
     * Unsets the "date" element
     */
    @Override
    public void unsetDate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[23], 0);
        }
    }

    /**
     * Gets the "filetime" element
     */
    @Override
    public java.util.Calendar getFiletime() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[24], 0);
            return (target == null) ? null : target.getCalendarValue();
        }
    }

    /**
     * Gets (as xml) the "filetime" element
     */
    @Override
    public org.apache.xmlbeans.XmlDateTime xgetFiletime() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(PROPERTY_QNAME[24], 0);
            return target;
        }
    }

    /**
     * True if has "filetime" element
     */
    @Override
    public boolean isSetFiletime() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[24]) != 0;
        }
    }

    /**
     * Sets the "filetime" element
     */
    @Override
    public void setFiletime(java.util.Calendar filetime) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[24], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[24]);
            }
            target.setCalendarValue(filetime);
        }
    }

    /**
     * Sets (as xml) the "filetime" element
     */
    @Override
    public void xsetFiletime(org.apache.xmlbeans.XmlDateTime filetime) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlDateTime target = null;
            target = (org.apache.xmlbeans.XmlDateTime)get_store().find_element_user(PROPERTY_QNAME[24], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlDateTime)get_store().add_element_user(PROPERTY_QNAME[24]);
            }
            target.set(filetime);
        }
    }

    /**
     * Unsets the "filetime" element
     */
    @Override
    public void unsetFiletime() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[24], 0);
        }
    }

    /**
     * Gets the "bool" element
     */
    @Override
    public boolean getBool() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[25], 0);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "bool" element
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetBool() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(PROPERTY_QNAME[25], 0);
            return target;
        }
    }

    /**
     * True if has "bool" element
     */
    @Override
    public boolean isSetBool() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[25]) != 0;
        }
    }

    /**
     * Sets the "bool" element
     */
    @Override
    public void setBool(boolean bool) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[25], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[25]);
            }
            target.setBooleanValue(bool);
        }
    }

    /**
     * Sets (as xml) the "bool" element
     */
    @Override
    public void xsetBool(org.apache.xmlbeans.XmlBoolean bool) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(PROPERTY_QNAME[25], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(PROPERTY_QNAME[25]);
            }
            target.set(bool);
        }
    }

    /**
     * Unsets the "bool" element
     */
    @Override
    public void unsetBool() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[25], 0);
        }
    }

    /**
     * Gets the "cy" element
     */
    @Override
    public java.lang.String getCy() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[26], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "cy" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STCy xgetCy() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STCy target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STCy)get_store().find_element_user(PROPERTY_QNAME[26], 0);
            return target;
        }
    }

    /**
     * True if has "cy" element
     */
    @Override
    public boolean isSetCy() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[26]) != 0;
        }
    }

    /**
     * Sets the "cy" element
     */
    @Override
    public void setCy(java.lang.String cy) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[26], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[26]);
            }
            target.setStringValue(cy);
        }
    }

    /**
     * Sets (as xml) the "cy" element
     */
    @Override
    public void xsetCy(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STCy cy) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STCy target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STCy)get_store().find_element_user(PROPERTY_QNAME[26], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STCy)get_store().add_element_user(PROPERTY_QNAME[26]);
            }
            target.set(cy);
        }
    }

    /**
     * Unsets the "cy" element
     */
    @Override
    public void unsetCy() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[26], 0);
        }
    }

    /**
     * Gets the "error" element
     */
    @Override
    public java.lang.String getError() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[27], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "error" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STError xgetError() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STError target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STError)get_store().find_element_user(PROPERTY_QNAME[27], 0);
            return target;
        }
    }

    /**
     * True if has "error" element
     */
    @Override
    public boolean isSetError() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[27]) != 0;
        }
    }

    /**
     * Sets the "error" element
     */
    @Override
    public void setError(java.lang.String error) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[27], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[27]);
            }
            target.setStringValue(error);
        }
    }

    /**
     * Sets (as xml) the "error" element
     */
    @Override
    public void xsetError(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STError error) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STError target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STError)get_store().find_element_user(PROPERTY_QNAME[27], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STError)get_store().add_element_user(PROPERTY_QNAME[27]);
            }
            target.set(error);
        }
    }

    /**
     * Unsets the "error" element
     */
    @Override
    public void unsetError() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[27], 0);
        }
    }

    /**
     * Gets the "stream" element
     */
    @Override
    public byte[] getStream() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[28], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "stream" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetStream() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[28], 0);
            return target;
        }
    }

    /**
     * True if has "stream" element
     */
    @Override
    public boolean isSetStream() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[28]) != 0;
        }
    }

    /**
     * Sets the "stream" element
     */
    @Override
    public void setStream(byte[] stream) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[28], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[28]);
            }
            target.setByteArrayValue(stream);
        }
    }

    /**
     * Sets (as xml) the "stream" element
     */
    @Override
    public void xsetStream(org.apache.xmlbeans.XmlBase64Binary stream) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[28], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(PROPERTY_QNAME[28]);
            }
            target.set(stream);
        }
    }

    /**
     * Unsets the "stream" element
     */
    @Override
    public void unsetStream() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[28], 0);
        }
    }

    /**
     * Gets the "ostream" element
     */
    @Override
    public byte[] getOstream() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[29], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "ostream" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetOstream() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[29], 0);
            return target;
        }
    }

    /**
     * True if has "ostream" element
     */
    @Override
    public boolean isSetOstream() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[29]) != 0;
        }
    }

    /**
     * Sets the "ostream" element
     */
    @Override
    public void setOstream(byte[] ostream) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[29], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[29]);
            }
            target.setByteArrayValue(ostream);
        }
    }

    /**
     * Sets (as xml) the "ostream" element
     */
    @Override
    public void xsetOstream(org.apache.xmlbeans.XmlBase64Binary ostream) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[29], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(PROPERTY_QNAME[29]);
            }
            target.set(ostream);
        }
    }

    /**
     * Unsets the "ostream" element
     */
    @Override
    public void unsetOstream() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[29], 0);
        }
    }

    /**
     * Gets the "storage" element
     */
    @Override
    public byte[] getStorage() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[30], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "storage" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetStorage() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[30], 0);
            return target;
        }
    }

    /**
     * True if has "storage" element
     */
    @Override
    public boolean isSetStorage() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[30]) != 0;
        }
    }

    /**
     * Sets the "storage" element
     */
    @Override
    public void setStorage(byte[] storage) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[30], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[30]);
            }
            target.setByteArrayValue(storage);
        }
    }

    /**
     * Sets (as xml) the "storage" element
     */
    @Override
    public void xsetStorage(org.apache.xmlbeans.XmlBase64Binary storage) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[30], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(PROPERTY_QNAME[30]);
            }
            target.set(storage);
        }
    }

    /**
     * Unsets the "storage" element
     */
    @Override
    public void unsetStorage() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[30], 0);
        }
    }

    /**
     * Gets the "ostorage" element
     */
    @Override
    public byte[] getOstorage() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[31], 0);
            return (target == null) ? null : target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) the "ostorage" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetOstorage() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[31], 0);
            return target;
        }
    }

    /**
     * True if has "ostorage" element
     */
    @Override
    public boolean isSetOstorage() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[31]) != 0;
        }
    }

    /**
     * Sets the "ostorage" element
     */
    @Override
    public void setOstorage(byte[] ostorage) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[31], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[31]);
            }
            target.setByteArrayValue(ostorage);
        }
    }

    /**
     * Sets (as xml) the "ostorage" element
     */
    @Override
    public void xsetOstorage(org.apache.xmlbeans.XmlBase64Binary ostorage) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[31], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(PROPERTY_QNAME[31]);
            }
            target.set(ostorage);
        }
    }

    /**
     * Unsets the "ostorage" element
     */
    @Override
    public void unsetOstorage() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[31], 0);
        }
    }

    /**
     * Gets the "vstream" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVstream getVstream() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVstream target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVstream)get_store().find_element_user(PROPERTY_QNAME[32], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "vstream" element
     */
    @Override
    public boolean isSetVstream() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[32]) != 0;
        }
    }

    /**
     * Sets the "vstream" element
     */
    @Override
    public void setVstream(org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVstream vstream) {
        generatedSetterHelperImpl(vstream, PROPERTY_QNAME[32], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "vstream" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVstream addNewVstream() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVstream target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.CTVstream)get_store().add_element_user(PROPERTY_QNAME[32]);
            return target;
        }
    }

    /**
     * Unsets the "vstream" element
     */
    @Override
    public void unsetVstream() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[32], 0);
        }
    }

    /**
     * Gets the "clsid" element
     */
    @Override
    public java.lang.String getClsid() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[33], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "clsid" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid xgetClsid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().find_element_user(PROPERTY_QNAME[33], 0);
            return target;
        }
    }

    /**
     * True if has "clsid" element
     */
    @Override
    public boolean isSetClsid() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[33]) != 0;
        }
    }

    /**
     * Sets the "clsid" element
     */
    @Override
    public void setClsid(java.lang.String clsid) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[33], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[33]);
            }
            target.setStringValue(clsid);
        }
    }

    /**
     * Sets (as xml) the "clsid" element
     */
    @Override
    public void xsetClsid(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid clsid) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().find_element_user(PROPERTY_QNAME[33], 0);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid)get_store().add_element_user(PROPERTY_QNAME[33]);
            }
            target.set(clsid);
        }
    }

    /**
     * Unsets the "clsid" element
     */
    @Override
    public void unsetClsid() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[33], 0);
        }
    }
}

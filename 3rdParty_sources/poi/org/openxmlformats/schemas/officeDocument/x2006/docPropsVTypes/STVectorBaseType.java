/*
 * XML Type:  ST_VectorBaseType
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STVectorBaseType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_VectorBaseType(@http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STVectorBaseType.
 */
public interface STVectorBaseType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STVectorBaseType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stvectorbasetype36e7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum VARIANT = Enum.forString("variant");
    Enum I_1 = Enum.forString("i1");
    Enum I_2 = Enum.forString("i2");
    Enum I_4 = Enum.forString("i4");
    Enum I_8 = Enum.forString("i8");
    Enum UI_1 = Enum.forString("ui1");
    Enum UI_2 = Enum.forString("ui2");
    Enum UI_4 = Enum.forString("ui4");
    Enum UI_8 = Enum.forString("ui8");
    Enum R_4 = Enum.forString("r4");
    Enum R_8 = Enum.forString("r8");
    Enum LPSTR = Enum.forString("lpstr");
    Enum LPWSTR = Enum.forString("lpwstr");
    Enum BSTR = Enum.forString("bstr");
    Enum DATE = Enum.forString("date");
    Enum FILETIME = Enum.forString("filetime");
    Enum BOOL = Enum.forString("bool");
    Enum CY = Enum.forString("cy");
    Enum ERROR = Enum.forString("error");
    Enum CLSID = Enum.forString("clsid");

    int INT_VARIANT = Enum.INT_VARIANT;
    int INT_I_1 = Enum.INT_I_1;
    int INT_I_2 = Enum.INT_I_2;
    int INT_I_4 = Enum.INT_I_4;
    int INT_I_8 = Enum.INT_I_8;
    int INT_UI_1 = Enum.INT_UI_1;
    int INT_UI_2 = Enum.INT_UI_2;
    int INT_UI_4 = Enum.INT_UI_4;
    int INT_UI_8 = Enum.INT_UI_8;
    int INT_R_4 = Enum.INT_R_4;
    int INT_R_8 = Enum.INT_R_8;
    int INT_LPSTR = Enum.INT_LPSTR;
    int INT_LPWSTR = Enum.INT_LPWSTR;
    int INT_BSTR = Enum.INT_BSTR;
    int INT_DATE = Enum.INT_DATE;
    int INT_FILETIME = Enum.INT_FILETIME;
    int INT_BOOL = Enum.INT_BOOL;
    int INT_CY = Enum.INT_CY;
    int INT_ERROR = Enum.INT_ERROR;
    int INT_CLSID = Enum.INT_CLSID;

    /**
     * Enumeration value class for org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.STVectorBaseType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_VARIANT
     * Enum.forString(s); // returns the enum value for a string
     * Enum.forInt(i); // returns the enum value for an int
     * </pre>
     * Enumeration objects are immutable singleton objects that
     * can be compared using == object equality. They have no
     * public constructor. See the constants defined within this
     * class for all the valid values.
     */
    final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase {
        /**
         * Returns the enum value for a string, or null if none.
         */
        public static Enum forString(java.lang.String s) {
            return (Enum)table.forString(s);
        }

        /**
         * Returns the enum value corresponding to an int, or null if none.
         */
        public static Enum forInt(int i) {
            return (Enum)table.forInt(i);
        }

        private Enum(java.lang.String s, int i) {
            super(s, i);
        }

        static final int INT_VARIANT = 1;
        static final int INT_I_1 = 2;
        static final int INT_I_2 = 3;
        static final int INT_I_4 = 4;
        static final int INT_I_8 = 5;
        static final int INT_UI_1 = 6;
        static final int INT_UI_2 = 7;
        static final int INT_UI_4 = 8;
        static final int INT_UI_8 = 9;
        static final int INT_R_4 = 10;
        static final int INT_R_8 = 11;
        static final int INT_LPSTR = 12;
        static final int INT_LPWSTR = 13;
        static final int INT_BSTR = 14;
        static final int INT_DATE = 15;
        static final int INT_FILETIME = 16;
        static final int INT_BOOL = 17;
        static final int INT_CY = 18;
        static final int INT_ERROR = 19;
        static final int INT_CLSID = 20;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("variant", INT_VARIANT),
            new Enum("i1", INT_I_1),
            new Enum("i2", INT_I_2),
            new Enum("i4", INT_I_4),
            new Enum("i8", INT_I_8),
            new Enum("ui1", INT_UI_1),
            new Enum("ui2", INT_UI_2),
            new Enum("ui4", INT_UI_4),
            new Enum("ui8", INT_UI_8),
            new Enum("r4", INT_R_4),
            new Enum("r8", INT_R_8),
            new Enum("lpstr", INT_LPSTR),
            new Enum("lpwstr", INT_LPWSTR),
            new Enum("bstr", INT_BSTR),
            new Enum("date", INT_DATE),
            new Enum("filetime", INT_FILETIME),
            new Enum("bool", INT_BOOL),
            new Enum("cy", INT_CY),
            new Enum("error", INT_ERROR),
            new Enum("clsid", INT_CLSID),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}

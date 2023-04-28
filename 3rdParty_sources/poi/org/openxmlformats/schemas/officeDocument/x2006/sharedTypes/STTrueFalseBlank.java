/*
 * XML Type:  ST_TrueFalseBlank
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/sharedTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTrueFalseBlank
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.sharedTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_TrueFalseBlank(@http://schemas.openxmlformats.org/officeDocument/2006/sharedTypes).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTrueFalseBlank.
 */
public interface STTrueFalseBlank extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTrueFalseBlank> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sttruefalseblank5459type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum T = Enum.forString("t");
    Enum F = Enum.forString("f");
    Enum TRUE = Enum.forString("true");
    Enum FALSE = Enum.forString("false");
    Enum X = Enum.forString("");
    Enum TRUE_2 = Enum.forString("True");
    Enum FALSE_2 = Enum.forString("False");

    int INT_T = Enum.INT_T;
    int INT_F = Enum.INT_F;
    int INT_TRUE = Enum.INT_TRUE;
    int INT_FALSE = Enum.INT_FALSE;
    int INT_X = Enum.INT_X;
    int INT_TRUE_2 = Enum.INT_TRUE_2;
    int INT_FALSE_2 = Enum.INT_FALSE_2;

    /**
     * Enumeration value class for org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTrueFalseBlank.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_T
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

        static final int INT_T = 1;
        static final int INT_F = 2;
        static final int INT_TRUE = 3;
        static final int INT_FALSE = 4;
        static final int INT_X = 5;
        static final int INT_TRUE_2 = 6;
        static final int INT_FALSE_2 = 7;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("t", INT_T),
            new Enum("f", INT_F),
            new Enum("true", INT_TRUE),
            new Enum("false", INT_FALSE),
            new Enum("", INT_X),
            new Enum("True", INT_TRUE_2),
            new Enum("False", INT_FALSE_2),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}

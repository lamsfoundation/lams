/*
 * XML Type:  ST_MdxSetOrder
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxSetOrder
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_MdxSetOrder(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxSetOrder.
 */
public interface STMdxSetOrder extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxSetOrder> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stmdxsetorderb97ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum U = Enum.forString("u");
    Enum A = Enum.forString("a");
    Enum D = Enum.forString("d");
    Enum AA = Enum.forString("aa");
    Enum AD = Enum.forString("ad");
    Enum NA = Enum.forString("na");
    Enum ND = Enum.forString("nd");

    int INT_U = Enum.INT_U;
    int INT_A = Enum.INT_A;
    int INT_D = Enum.INT_D;
    int INT_AA = Enum.INT_AA;
    int INT_AD = Enum.INT_AD;
    int INT_NA = Enum.INT_NA;
    int INT_ND = Enum.INT_ND;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STMdxSetOrder.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_U
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

        static final int INT_U = 1;
        static final int INT_A = 2;
        static final int INT_D = 3;
        static final int INT_AA = 4;
        static final int INT_AD = 5;
        static final int INT_NA = 6;
        static final int INT_ND = 7;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("u", INT_U),
            new Enum("a", INT_A),
            new Enum("d", INT_D),
            new Enum("aa", INT_AA),
            new Enum("ad", INT_AD),
            new Enum("na", INT_NA),
            new Enum("nd", INT_ND),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}

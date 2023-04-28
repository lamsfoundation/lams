/*
 * XML Type:  ST_WmlColorSchemeIndex
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_WmlColorSchemeIndex(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.
 */
public interface STWmlColorSchemeIndex extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stwmlcolorschemeindex4de2type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum DARK_1 = Enum.forString("dark1");
    Enum LIGHT_1 = Enum.forString("light1");
    Enum DARK_2 = Enum.forString("dark2");
    Enum LIGHT_2 = Enum.forString("light2");
    Enum ACCENT_1 = Enum.forString("accent1");
    Enum ACCENT_2 = Enum.forString("accent2");
    Enum ACCENT_3 = Enum.forString("accent3");
    Enum ACCENT_4 = Enum.forString("accent4");
    Enum ACCENT_5 = Enum.forString("accent5");
    Enum ACCENT_6 = Enum.forString("accent6");
    Enum HYPERLINK = Enum.forString("hyperlink");
    Enum FOLLOWED_HYPERLINK = Enum.forString("followedHyperlink");

    int INT_DARK_1 = Enum.INT_DARK_1;
    int INT_LIGHT_1 = Enum.INT_LIGHT_1;
    int INT_DARK_2 = Enum.INT_DARK_2;
    int INT_LIGHT_2 = Enum.INT_LIGHT_2;
    int INT_ACCENT_1 = Enum.INT_ACCENT_1;
    int INT_ACCENT_2 = Enum.INT_ACCENT_2;
    int INT_ACCENT_3 = Enum.INT_ACCENT_3;
    int INT_ACCENT_4 = Enum.INT_ACCENT_4;
    int INT_ACCENT_5 = Enum.INT_ACCENT_5;
    int INT_ACCENT_6 = Enum.INT_ACCENT_6;
    int INT_HYPERLINK = Enum.INT_HYPERLINK;
    int INT_FOLLOWED_HYPERLINK = Enum.INT_FOLLOWED_HYPERLINK;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STWmlColorSchemeIndex.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_DARK_1
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

        static final int INT_DARK_1 = 1;
        static final int INT_LIGHT_1 = 2;
        static final int INT_DARK_2 = 3;
        static final int INT_LIGHT_2 = 4;
        static final int INT_ACCENT_1 = 5;
        static final int INT_ACCENT_2 = 6;
        static final int INT_ACCENT_3 = 7;
        static final int INT_ACCENT_4 = 8;
        static final int INT_ACCENT_5 = 9;
        static final int INT_ACCENT_6 = 10;
        static final int INT_HYPERLINK = 11;
        static final int INT_FOLLOWED_HYPERLINK = 12;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("dark1", INT_DARK_1),
            new Enum("light1", INT_LIGHT_1),
            new Enum("dark2", INT_DARK_2),
            new Enum("light2", INT_LIGHT_2),
            new Enum("accent1", INT_ACCENT_1),
            new Enum("accent2", INT_ACCENT_2),
            new Enum("accent3", INT_ACCENT_3),
            new Enum("accent4", INT_ACCENT_4),
            new Enum("accent5", INT_ACCENT_5),
            new Enum("accent6", INT_ACCENT_6),
            new Enum("hyperlink", INT_HYPERLINK),
            new Enum("followedHyperlink", INT_FOLLOWED_HYPERLINK),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}

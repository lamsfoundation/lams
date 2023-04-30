/*
 * XML Type:  ST_ColorSchemeIndex
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_ColorSchemeIndex(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.
 */
public interface STColorSchemeIndex extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stcolorschemeindexe6f5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum DK_1 = Enum.forString("dk1");
    Enum LT_1 = Enum.forString("lt1");
    Enum DK_2 = Enum.forString("dk2");
    Enum LT_2 = Enum.forString("lt2");
    Enum ACCENT_1 = Enum.forString("accent1");
    Enum ACCENT_2 = Enum.forString("accent2");
    Enum ACCENT_3 = Enum.forString("accent3");
    Enum ACCENT_4 = Enum.forString("accent4");
    Enum ACCENT_5 = Enum.forString("accent5");
    Enum ACCENT_6 = Enum.forString("accent6");
    Enum HLINK = Enum.forString("hlink");
    Enum FOL_HLINK = Enum.forString("folHlink");

    int INT_DK_1 = Enum.INT_DK_1;
    int INT_LT_1 = Enum.INT_LT_1;
    int INT_DK_2 = Enum.INT_DK_2;
    int INT_LT_2 = Enum.INT_LT_2;
    int INT_ACCENT_1 = Enum.INT_ACCENT_1;
    int INT_ACCENT_2 = Enum.INT_ACCENT_2;
    int INT_ACCENT_3 = Enum.INT_ACCENT_3;
    int INT_ACCENT_4 = Enum.INT_ACCENT_4;
    int INT_ACCENT_5 = Enum.INT_ACCENT_5;
    int INT_ACCENT_6 = Enum.INT_ACCENT_6;
    int INT_HLINK = Enum.INT_HLINK;
    int INT_FOL_HLINK = Enum.INT_FOL_HLINK;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.main.STColorSchemeIndex.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_DK_1
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

        static final int INT_DK_1 = 1;
        static final int INT_LT_1 = 2;
        static final int INT_DK_2 = 3;
        static final int INT_LT_2 = 4;
        static final int INT_ACCENT_1 = 5;
        static final int INT_ACCENT_2 = 6;
        static final int INT_ACCENT_3 = 7;
        static final int INT_ACCENT_4 = 8;
        static final int INT_ACCENT_5 = 9;
        static final int INT_ACCENT_6 = 10;
        static final int INT_HLINK = 11;
        static final int INT_FOL_HLINK = 12;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("dk1", INT_DK_1),
            new Enum("lt1", INT_LT_1),
            new Enum("dk2", INT_DK_2),
            new Enum("lt2", INT_LT_2),
            new Enum("accent1", INT_ACCENT_1),
            new Enum("accent2", INT_ACCENT_2),
            new Enum("accent3", INT_ACCENT_3),
            new Enum("accent4", INT_ACCENT_4),
            new Enum("accent5", INT_ACCENT_5),
            new Enum("accent6", INT_ACCENT_6),
            new Enum("hlink", INT_HLINK),
            new Enum("folHlink", INT_FOL_HLINK),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}

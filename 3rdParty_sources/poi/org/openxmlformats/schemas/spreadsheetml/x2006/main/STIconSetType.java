/*
 * XML Type:  ST_IconSetType
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_IconSetType(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType.
 */
public interface STIconSetType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sticonsettype6112type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum X_3_ARROWS = Enum.forString("3Arrows");
    Enum X_3_ARROWS_GRAY = Enum.forString("3ArrowsGray");
    Enum X_3_FLAGS = Enum.forString("3Flags");
    Enum X_3_TRAFFIC_LIGHTS_1 = Enum.forString("3TrafficLights1");
    Enum X_3_TRAFFIC_LIGHTS_2 = Enum.forString("3TrafficLights2");
    Enum X_3_SIGNS = Enum.forString("3Signs");
    Enum X_3_SYMBOLS = Enum.forString("3Symbols");
    Enum X_3_SYMBOLS_2 = Enum.forString("3Symbols2");
    Enum X_4_ARROWS = Enum.forString("4Arrows");
    Enum X_4_ARROWS_GRAY = Enum.forString("4ArrowsGray");
    Enum X_4_RED_TO_BLACK = Enum.forString("4RedToBlack");
    Enum X_4_RATING = Enum.forString("4Rating");
    Enum X_4_TRAFFIC_LIGHTS = Enum.forString("4TrafficLights");
    Enum X_5_ARROWS = Enum.forString("5Arrows");
    Enum X_5_ARROWS_GRAY = Enum.forString("5ArrowsGray");
    Enum X_5_RATING = Enum.forString("5Rating");
    Enum X_5_QUARTERS = Enum.forString("5Quarters");

    int INT_X_3_ARROWS = Enum.INT_X_3_ARROWS;
    int INT_X_3_ARROWS_GRAY = Enum.INT_X_3_ARROWS_GRAY;
    int INT_X_3_FLAGS = Enum.INT_X_3_FLAGS;
    int INT_X_3_TRAFFIC_LIGHTS_1 = Enum.INT_X_3_TRAFFIC_LIGHTS_1;
    int INT_X_3_TRAFFIC_LIGHTS_2 = Enum.INT_X_3_TRAFFIC_LIGHTS_2;
    int INT_X_3_SIGNS = Enum.INT_X_3_SIGNS;
    int INT_X_3_SYMBOLS = Enum.INT_X_3_SYMBOLS;
    int INT_X_3_SYMBOLS_2 = Enum.INT_X_3_SYMBOLS_2;
    int INT_X_4_ARROWS = Enum.INT_X_4_ARROWS;
    int INT_X_4_ARROWS_GRAY = Enum.INT_X_4_ARROWS_GRAY;
    int INT_X_4_RED_TO_BLACK = Enum.INT_X_4_RED_TO_BLACK;
    int INT_X_4_RATING = Enum.INT_X_4_RATING;
    int INT_X_4_TRAFFIC_LIGHTS = Enum.INT_X_4_TRAFFIC_LIGHTS;
    int INT_X_5_ARROWS = Enum.INT_X_5_ARROWS;
    int INT_X_5_ARROWS_GRAY = Enum.INT_X_5_ARROWS_GRAY;
    int INT_X_5_RATING = Enum.INT_X_5_RATING;
    int INT_X_5_QUARTERS = Enum.INT_X_5_QUARTERS;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_X_3_ARROWS
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

        static final int INT_X_3_ARROWS = 1;
        static final int INT_X_3_ARROWS_GRAY = 2;
        static final int INT_X_3_FLAGS = 3;
        static final int INT_X_3_TRAFFIC_LIGHTS_1 = 4;
        static final int INT_X_3_TRAFFIC_LIGHTS_2 = 5;
        static final int INT_X_3_SIGNS = 6;
        static final int INT_X_3_SYMBOLS = 7;
        static final int INT_X_3_SYMBOLS_2 = 8;
        static final int INT_X_4_ARROWS = 9;
        static final int INT_X_4_ARROWS_GRAY = 10;
        static final int INT_X_4_RED_TO_BLACK = 11;
        static final int INT_X_4_RATING = 12;
        static final int INT_X_4_TRAFFIC_LIGHTS = 13;
        static final int INT_X_5_ARROWS = 14;
        static final int INT_X_5_ARROWS_GRAY = 15;
        static final int INT_X_5_RATING = 16;
        static final int INT_X_5_QUARTERS = 17;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("3Arrows", INT_X_3_ARROWS),
            new Enum("3ArrowsGray", INT_X_3_ARROWS_GRAY),
            new Enum("3Flags", INT_X_3_FLAGS),
            new Enum("3TrafficLights1", INT_X_3_TRAFFIC_LIGHTS_1),
            new Enum("3TrafficLights2", INT_X_3_TRAFFIC_LIGHTS_2),
            new Enum("3Signs", INT_X_3_SIGNS),
            new Enum("3Symbols", INT_X_3_SYMBOLS),
            new Enum("3Symbols2", INT_X_3_SYMBOLS_2),
            new Enum("4Arrows", INT_X_4_ARROWS),
            new Enum("4ArrowsGray", INT_X_4_ARROWS_GRAY),
            new Enum("4RedToBlack", INT_X_4_RED_TO_BLACK),
            new Enum("4Rating", INT_X_4_RATING),
            new Enum("4TrafficLights", INT_X_4_TRAFFIC_LIGHTS),
            new Enum("5Arrows", INT_X_5_ARROWS),
            new Enum("5ArrowsGray", INT_X_5_ARROWS_GRAY),
            new Enum("5Rating", INT_X_5_RATING),
            new Enum("5Quarters", INT_X_5_QUARTERS),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}

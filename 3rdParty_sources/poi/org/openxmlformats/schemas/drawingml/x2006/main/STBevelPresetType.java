/*
 * XML Type:  ST_BevelPresetType
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.STBevelPresetType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_BevelPresetType(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.main.STBevelPresetType.
 */
public interface STBevelPresetType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.main.STBevelPresetType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stbevelpresettype48b4type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum RELAXED_INSET = Enum.forString("relaxedInset");
    Enum CIRCLE = Enum.forString("circle");
    Enum SLOPE = Enum.forString("slope");
    Enum CROSS = Enum.forString("cross");
    Enum ANGLE = Enum.forString("angle");
    Enum SOFT_ROUND = Enum.forString("softRound");
    Enum CONVEX = Enum.forString("convex");
    Enum COOL_SLANT = Enum.forString("coolSlant");
    Enum DIVOT = Enum.forString("divot");
    Enum RIBLET = Enum.forString("riblet");
    Enum HARD_EDGE = Enum.forString("hardEdge");
    Enum ART_DECO = Enum.forString("artDeco");

    int INT_RELAXED_INSET = Enum.INT_RELAXED_INSET;
    int INT_CIRCLE = Enum.INT_CIRCLE;
    int INT_SLOPE = Enum.INT_SLOPE;
    int INT_CROSS = Enum.INT_CROSS;
    int INT_ANGLE = Enum.INT_ANGLE;
    int INT_SOFT_ROUND = Enum.INT_SOFT_ROUND;
    int INT_CONVEX = Enum.INT_CONVEX;
    int INT_COOL_SLANT = Enum.INT_COOL_SLANT;
    int INT_DIVOT = Enum.INT_DIVOT;
    int INT_RIBLET = Enum.INT_RIBLET;
    int INT_HARD_EDGE = Enum.INT_HARD_EDGE;
    int INT_ART_DECO = Enum.INT_ART_DECO;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.main.STBevelPresetType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_RELAXED_INSET
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

        static final int INT_RELAXED_INSET = 1;
        static final int INT_CIRCLE = 2;
        static final int INT_SLOPE = 3;
        static final int INT_CROSS = 4;
        static final int INT_ANGLE = 5;
        static final int INT_SOFT_ROUND = 6;
        static final int INT_CONVEX = 7;
        static final int INT_COOL_SLANT = 8;
        static final int INT_DIVOT = 9;
        static final int INT_RIBLET = 10;
        static final int INT_HARD_EDGE = 11;
        static final int INT_ART_DECO = 12;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("relaxedInset", INT_RELAXED_INSET),
            new Enum("circle", INT_CIRCLE),
            new Enum("slope", INT_SLOPE),
            new Enum("cross", INT_CROSS),
            new Enum("angle", INT_ANGLE),
            new Enum("softRound", INT_SOFT_ROUND),
            new Enum("convex", INT_CONVEX),
            new Enum("coolSlant", INT_COOL_SLANT),
            new Enum("divot", INT_DIVOT),
            new Enum("riblet", INT_RIBLET),
            new Enum("hardEdge", INT_HARD_EDGE),
            new Enum("artDeco", INT_ART_DECO),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}

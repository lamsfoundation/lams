/*
 * XML Type:  ST_LightRigType
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_LightRigType(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType.
 */
public interface STLightRigType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stlightrigtypec99ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum LEGACY_FLAT_1 = Enum.forString("legacyFlat1");
    Enum LEGACY_FLAT_2 = Enum.forString("legacyFlat2");
    Enum LEGACY_FLAT_3 = Enum.forString("legacyFlat3");
    Enum LEGACY_FLAT_4 = Enum.forString("legacyFlat4");
    Enum LEGACY_NORMAL_1 = Enum.forString("legacyNormal1");
    Enum LEGACY_NORMAL_2 = Enum.forString("legacyNormal2");
    Enum LEGACY_NORMAL_3 = Enum.forString("legacyNormal3");
    Enum LEGACY_NORMAL_4 = Enum.forString("legacyNormal4");
    Enum LEGACY_HARSH_1 = Enum.forString("legacyHarsh1");
    Enum LEGACY_HARSH_2 = Enum.forString("legacyHarsh2");
    Enum LEGACY_HARSH_3 = Enum.forString("legacyHarsh3");
    Enum LEGACY_HARSH_4 = Enum.forString("legacyHarsh4");
    Enum THREE_PT = Enum.forString("threePt");
    Enum BALANCED = Enum.forString("balanced");
    Enum SOFT = Enum.forString("soft");
    Enum HARSH = Enum.forString("harsh");
    Enum FLOOD = Enum.forString("flood");
    Enum CONTRASTING = Enum.forString("contrasting");
    Enum MORNING = Enum.forString("morning");
    Enum SUNRISE = Enum.forString("sunrise");
    Enum SUNSET = Enum.forString("sunset");
    Enum CHILLY = Enum.forString("chilly");
    Enum FREEZING = Enum.forString("freezing");
    Enum FLAT = Enum.forString("flat");
    Enum TWO_PT = Enum.forString("twoPt");
    Enum GLOW = Enum.forString("glow");
    Enum BRIGHT_ROOM = Enum.forString("brightRoom");

    int INT_LEGACY_FLAT_1 = Enum.INT_LEGACY_FLAT_1;
    int INT_LEGACY_FLAT_2 = Enum.INT_LEGACY_FLAT_2;
    int INT_LEGACY_FLAT_3 = Enum.INT_LEGACY_FLAT_3;
    int INT_LEGACY_FLAT_4 = Enum.INT_LEGACY_FLAT_4;
    int INT_LEGACY_NORMAL_1 = Enum.INT_LEGACY_NORMAL_1;
    int INT_LEGACY_NORMAL_2 = Enum.INT_LEGACY_NORMAL_2;
    int INT_LEGACY_NORMAL_3 = Enum.INT_LEGACY_NORMAL_3;
    int INT_LEGACY_NORMAL_4 = Enum.INT_LEGACY_NORMAL_4;
    int INT_LEGACY_HARSH_1 = Enum.INT_LEGACY_HARSH_1;
    int INT_LEGACY_HARSH_2 = Enum.INT_LEGACY_HARSH_2;
    int INT_LEGACY_HARSH_3 = Enum.INT_LEGACY_HARSH_3;
    int INT_LEGACY_HARSH_4 = Enum.INT_LEGACY_HARSH_4;
    int INT_THREE_PT = Enum.INT_THREE_PT;
    int INT_BALANCED = Enum.INT_BALANCED;
    int INT_SOFT = Enum.INT_SOFT;
    int INT_HARSH = Enum.INT_HARSH;
    int INT_FLOOD = Enum.INT_FLOOD;
    int INT_CONTRASTING = Enum.INT_CONTRASTING;
    int INT_MORNING = Enum.INT_MORNING;
    int INT_SUNRISE = Enum.INT_SUNRISE;
    int INT_SUNSET = Enum.INT_SUNSET;
    int INT_CHILLY = Enum.INT_CHILLY;
    int INT_FREEZING = Enum.INT_FREEZING;
    int INT_FLAT = Enum.INT_FLAT;
    int INT_TWO_PT = Enum.INT_TWO_PT;
    int INT_GLOW = Enum.INT_GLOW;
    int INT_BRIGHT_ROOM = Enum.INT_BRIGHT_ROOM;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.main.STLightRigType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_LEGACY_FLAT_1
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

        static final int INT_LEGACY_FLAT_1 = 1;
        static final int INT_LEGACY_FLAT_2 = 2;
        static final int INT_LEGACY_FLAT_3 = 3;
        static final int INT_LEGACY_FLAT_4 = 4;
        static final int INT_LEGACY_NORMAL_1 = 5;
        static final int INT_LEGACY_NORMAL_2 = 6;
        static final int INT_LEGACY_NORMAL_3 = 7;
        static final int INT_LEGACY_NORMAL_4 = 8;
        static final int INT_LEGACY_HARSH_1 = 9;
        static final int INT_LEGACY_HARSH_2 = 10;
        static final int INT_LEGACY_HARSH_3 = 11;
        static final int INT_LEGACY_HARSH_4 = 12;
        static final int INT_THREE_PT = 13;
        static final int INT_BALANCED = 14;
        static final int INT_SOFT = 15;
        static final int INT_HARSH = 16;
        static final int INT_FLOOD = 17;
        static final int INT_CONTRASTING = 18;
        static final int INT_MORNING = 19;
        static final int INT_SUNRISE = 20;
        static final int INT_SUNSET = 21;
        static final int INT_CHILLY = 22;
        static final int INT_FREEZING = 23;
        static final int INT_FLAT = 24;
        static final int INT_TWO_PT = 25;
        static final int INT_GLOW = 26;
        static final int INT_BRIGHT_ROOM = 27;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("legacyFlat1", INT_LEGACY_FLAT_1),
            new Enum("legacyFlat2", INT_LEGACY_FLAT_2),
            new Enum("legacyFlat3", INT_LEGACY_FLAT_3),
            new Enum("legacyFlat4", INT_LEGACY_FLAT_4),
            new Enum("legacyNormal1", INT_LEGACY_NORMAL_1),
            new Enum("legacyNormal2", INT_LEGACY_NORMAL_2),
            new Enum("legacyNormal3", INT_LEGACY_NORMAL_3),
            new Enum("legacyNormal4", INT_LEGACY_NORMAL_4),
            new Enum("legacyHarsh1", INT_LEGACY_HARSH_1),
            new Enum("legacyHarsh2", INT_LEGACY_HARSH_2),
            new Enum("legacyHarsh3", INT_LEGACY_HARSH_3),
            new Enum("legacyHarsh4", INT_LEGACY_HARSH_4),
            new Enum("threePt", INT_THREE_PT),
            new Enum("balanced", INT_BALANCED),
            new Enum("soft", INT_SOFT),
            new Enum("harsh", INT_HARSH),
            new Enum("flood", INT_FLOOD),
            new Enum("contrasting", INT_CONTRASTING),
            new Enum("morning", INT_MORNING),
            new Enum("sunrise", INT_SUNRISE),
            new Enum("sunset", INT_SUNSET),
            new Enum("chilly", INT_CHILLY),
            new Enum("freezing", INT_FREEZING),
            new Enum("flat", INT_FLAT),
            new Enum("twoPt", INT_TWO_PT),
            new Enum("glow", INT_GLOW),
            new Enum("brightRoom", INT_BRIGHT_ROOM),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}

/*
 * XML Type:  ST_PresetMaterialType
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.STPresetMaterialType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_PresetMaterialType(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.main.STPresetMaterialType.
 */
public interface STPresetMaterialType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.main.STPresetMaterialType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stpresetmaterialtype87ebtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum LEGACY_MATTE = Enum.forString("legacyMatte");
    Enum LEGACY_PLASTIC = Enum.forString("legacyPlastic");
    Enum LEGACY_METAL = Enum.forString("legacyMetal");
    Enum LEGACY_WIREFRAME = Enum.forString("legacyWireframe");
    Enum MATTE = Enum.forString("matte");
    Enum PLASTIC = Enum.forString("plastic");
    Enum METAL = Enum.forString("metal");
    Enum WARM_MATTE = Enum.forString("warmMatte");
    Enum TRANSLUCENT_POWDER = Enum.forString("translucentPowder");
    Enum POWDER = Enum.forString("powder");
    Enum DK_EDGE = Enum.forString("dkEdge");
    Enum SOFT_EDGE = Enum.forString("softEdge");
    Enum CLEAR = Enum.forString("clear");
    Enum FLAT = Enum.forString("flat");
    Enum SOFTMETAL = Enum.forString("softmetal");

    int INT_LEGACY_MATTE = Enum.INT_LEGACY_MATTE;
    int INT_LEGACY_PLASTIC = Enum.INT_LEGACY_PLASTIC;
    int INT_LEGACY_METAL = Enum.INT_LEGACY_METAL;
    int INT_LEGACY_WIREFRAME = Enum.INT_LEGACY_WIREFRAME;
    int INT_MATTE = Enum.INT_MATTE;
    int INT_PLASTIC = Enum.INT_PLASTIC;
    int INT_METAL = Enum.INT_METAL;
    int INT_WARM_MATTE = Enum.INT_WARM_MATTE;
    int INT_TRANSLUCENT_POWDER = Enum.INT_TRANSLUCENT_POWDER;
    int INT_POWDER = Enum.INT_POWDER;
    int INT_DK_EDGE = Enum.INT_DK_EDGE;
    int INT_SOFT_EDGE = Enum.INT_SOFT_EDGE;
    int INT_CLEAR = Enum.INT_CLEAR;
    int INT_FLAT = Enum.INT_FLAT;
    int INT_SOFTMETAL = Enum.INT_SOFTMETAL;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.main.STPresetMaterialType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_LEGACY_MATTE
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

        static final int INT_LEGACY_MATTE = 1;
        static final int INT_LEGACY_PLASTIC = 2;
        static final int INT_LEGACY_METAL = 3;
        static final int INT_LEGACY_WIREFRAME = 4;
        static final int INT_MATTE = 5;
        static final int INT_PLASTIC = 6;
        static final int INT_METAL = 7;
        static final int INT_WARM_MATTE = 8;
        static final int INT_TRANSLUCENT_POWDER = 9;
        static final int INT_POWDER = 10;
        static final int INT_DK_EDGE = 11;
        static final int INT_SOFT_EDGE = 12;
        static final int INT_CLEAR = 13;
        static final int INT_FLAT = 14;
        static final int INT_SOFTMETAL = 15;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("legacyMatte", INT_LEGACY_MATTE),
            new Enum("legacyPlastic", INT_LEGACY_PLASTIC),
            new Enum("legacyMetal", INT_LEGACY_METAL),
            new Enum("legacyWireframe", INT_LEGACY_WIREFRAME),
            new Enum("matte", INT_MATTE),
            new Enum("plastic", INT_PLASTIC),
            new Enum("metal", INT_METAL),
            new Enum("warmMatte", INT_WARM_MATTE),
            new Enum("translucentPowder", INT_TRANSLUCENT_POWDER),
            new Enum("powder", INT_POWDER),
            new Enum("dkEdge", INT_DK_EDGE),
            new Enum("softEdge", INT_SOFT_EDGE),
            new Enum("clear", INT_CLEAR),
            new Enum("flat", INT_FLAT),
            new Enum("softmetal", INT_SOFTMETAL),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}

/*
 * XML Type:  ST_PresetCameraType
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.STPresetCameraType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_PresetCameraType(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.main.STPresetCameraType.
 */
public interface STPresetCameraType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.main.STPresetCameraType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stpresetcameratype9969type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum LEGACY_OBLIQUE_TOP_LEFT = Enum.forString("legacyObliqueTopLeft");
    Enum LEGACY_OBLIQUE_TOP = Enum.forString("legacyObliqueTop");
    Enum LEGACY_OBLIQUE_TOP_RIGHT = Enum.forString("legacyObliqueTopRight");
    Enum LEGACY_OBLIQUE_LEFT = Enum.forString("legacyObliqueLeft");
    Enum LEGACY_OBLIQUE_FRONT = Enum.forString("legacyObliqueFront");
    Enum LEGACY_OBLIQUE_RIGHT = Enum.forString("legacyObliqueRight");
    Enum LEGACY_OBLIQUE_BOTTOM_LEFT = Enum.forString("legacyObliqueBottomLeft");
    Enum LEGACY_OBLIQUE_BOTTOM = Enum.forString("legacyObliqueBottom");
    Enum LEGACY_OBLIQUE_BOTTOM_RIGHT = Enum.forString("legacyObliqueBottomRight");
    Enum LEGACY_PERSPECTIVE_TOP_LEFT = Enum.forString("legacyPerspectiveTopLeft");
    Enum LEGACY_PERSPECTIVE_TOP = Enum.forString("legacyPerspectiveTop");
    Enum LEGACY_PERSPECTIVE_TOP_RIGHT = Enum.forString("legacyPerspectiveTopRight");
    Enum LEGACY_PERSPECTIVE_LEFT = Enum.forString("legacyPerspectiveLeft");
    Enum LEGACY_PERSPECTIVE_FRONT = Enum.forString("legacyPerspectiveFront");
    Enum LEGACY_PERSPECTIVE_RIGHT = Enum.forString("legacyPerspectiveRight");
    Enum LEGACY_PERSPECTIVE_BOTTOM_LEFT = Enum.forString("legacyPerspectiveBottomLeft");
    Enum LEGACY_PERSPECTIVE_BOTTOM = Enum.forString("legacyPerspectiveBottom");
    Enum LEGACY_PERSPECTIVE_BOTTOM_RIGHT = Enum.forString("legacyPerspectiveBottomRight");
    Enum ORTHOGRAPHIC_FRONT = Enum.forString("orthographicFront");
    Enum ISOMETRIC_TOP_UP = Enum.forString("isometricTopUp");
    Enum ISOMETRIC_TOP_DOWN = Enum.forString("isometricTopDown");
    Enum ISOMETRIC_BOTTOM_UP = Enum.forString("isometricBottomUp");
    Enum ISOMETRIC_BOTTOM_DOWN = Enum.forString("isometricBottomDown");
    Enum ISOMETRIC_LEFT_UP = Enum.forString("isometricLeftUp");
    Enum ISOMETRIC_LEFT_DOWN = Enum.forString("isometricLeftDown");
    Enum ISOMETRIC_RIGHT_UP = Enum.forString("isometricRightUp");
    Enum ISOMETRIC_RIGHT_DOWN = Enum.forString("isometricRightDown");
    Enum ISOMETRIC_OFF_AXIS_1_LEFT = Enum.forString("isometricOffAxis1Left");
    Enum ISOMETRIC_OFF_AXIS_1_RIGHT = Enum.forString("isometricOffAxis1Right");
    Enum ISOMETRIC_OFF_AXIS_1_TOP = Enum.forString("isometricOffAxis1Top");
    Enum ISOMETRIC_OFF_AXIS_2_LEFT = Enum.forString("isometricOffAxis2Left");
    Enum ISOMETRIC_OFF_AXIS_2_RIGHT = Enum.forString("isometricOffAxis2Right");
    Enum ISOMETRIC_OFF_AXIS_2_TOP = Enum.forString("isometricOffAxis2Top");
    Enum ISOMETRIC_OFF_AXIS_3_LEFT = Enum.forString("isometricOffAxis3Left");
    Enum ISOMETRIC_OFF_AXIS_3_RIGHT = Enum.forString("isometricOffAxis3Right");
    Enum ISOMETRIC_OFF_AXIS_3_BOTTOM = Enum.forString("isometricOffAxis3Bottom");
    Enum ISOMETRIC_OFF_AXIS_4_LEFT = Enum.forString("isometricOffAxis4Left");
    Enum ISOMETRIC_OFF_AXIS_4_RIGHT = Enum.forString("isometricOffAxis4Right");
    Enum ISOMETRIC_OFF_AXIS_4_BOTTOM = Enum.forString("isometricOffAxis4Bottom");
    Enum OBLIQUE_TOP_LEFT = Enum.forString("obliqueTopLeft");
    Enum OBLIQUE_TOP = Enum.forString("obliqueTop");
    Enum OBLIQUE_TOP_RIGHT = Enum.forString("obliqueTopRight");
    Enum OBLIQUE_LEFT = Enum.forString("obliqueLeft");
    Enum OBLIQUE_RIGHT = Enum.forString("obliqueRight");
    Enum OBLIQUE_BOTTOM_LEFT = Enum.forString("obliqueBottomLeft");
    Enum OBLIQUE_BOTTOM = Enum.forString("obliqueBottom");
    Enum OBLIQUE_BOTTOM_RIGHT = Enum.forString("obliqueBottomRight");
    Enum PERSPECTIVE_FRONT = Enum.forString("perspectiveFront");
    Enum PERSPECTIVE_LEFT = Enum.forString("perspectiveLeft");
    Enum PERSPECTIVE_RIGHT = Enum.forString("perspectiveRight");
    Enum PERSPECTIVE_ABOVE = Enum.forString("perspectiveAbove");
    Enum PERSPECTIVE_BELOW = Enum.forString("perspectiveBelow");
    Enum PERSPECTIVE_ABOVE_LEFT_FACING = Enum.forString("perspectiveAboveLeftFacing");
    Enum PERSPECTIVE_ABOVE_RIGHT_FACING = Enum.forString("perspectiveAboveRightFacing");
    Enum PERSPECTIVE_CONTRASTING_LEFT_FACING = Enum.forString("perspectiveContrastingLeftFacing");
    Enum PERSPECTIVE_CONTRASTING_RIGHT_FACING = Enum.forString("perspectiveContrastingRightFacing");
    Enum PERSPECTIVE_HEROIC_LEFT_FACING = Enum.forString("perspectiveHeroicLeftFacing");
    Enum PERSPECTIVE_HEROIC_RIGHT_FACING = Enum.forString("perspectiveHeroicRightFacing");
    Enum PERSPECTIVE_HEROIC_EXTREME_LEFT_FACING = Enum.forString("perspectiveHeroicExtremeLeftFacing");
    Enum PERSPECTIVE_HEROIC_EXTREME_RIGHT_FACING = Enum.forString("perspectiveHeroicExtremeRightFacing");
    Enum PERSPECTIVE_RELAXED = Enum.forString("perspectiveRelaxed");
    Enum PERSPECTIVE_RELAXED_MODERATELY = Enum.forString("perspectiveRelaxedModerately");

    int INT_LEGACY_OBLIQUE_TOP_LEFT = Enum.INT_LEGACY_OBLIQUE_TOP_LEFT;
    int INT_LEGACY_OBLIQUE_TOP = Enum.INT_LEGACY_OBLIQUE_TOP;
    int INT_LEGACY_OBLIQUE_TOP_RIGHT = Enum.INT_LEGACY_OBLIQUE_TOP_RIGHT;
    int INT_LEGACY_OBLIQUE_LEFT = Enum.INT_LEGACY_OBLIQUE_LEFT;
    int INT_LEGACY_OBLIQUE_FRONT = Enum.INT_LEGACY_OBLIQUE_FRONT;
    int INT_LEGACY_OBLIQUE_RIGHT = Enum.INT_LEGACY_OBLIQUE_RIGHT;
    int INT_LEGACY_OBLIQUE_BOTTOM_LEFT = Enum.INT_LEGACY_OBLIQUE_BOTTOM_LEFT;
    int INT_LEGACY_OBLIQUE_BOTTOM = Enum.INT_LEGACY_OBLIQUE_BOTTOM;
    int INT_LEGACY_OBLIQUE_BOTTOM_RIGHT = Enum.INT_LEGACY_OBLIQUE_BOTTOM_RIGHT;
    int INT_LEGACY_PERSPECTIVE_TOP_LEFT = Enum.INT_LEGACY_PERSPECTIVE_TOP_LEFT;
    int INT_LEGACY_PERSPECTIVE_TOP = Enum.INT_LEGACY_PERSPECTIVE_TOP;
    int INT_LEGACY_PERSPECTIVE_TOP_RIGHT = Enum.INT_LEGACY_PERSPECTIVE_TOP_RIGHT;
    int INT_LEGACY_PERSPECTIVE_LEFT = Enum.INT_LEGACY_PERSPECTIVE_LEFT;
    int INT_LEGACY_PERSPECTIVE_FRONT = Enum.INT_LEGACY_PERSPECTIVE_FRONT;
    int INT_LEGACY_PERSPECTIVE_RIGHT = Enum.INT_LEGACY_PERSPECTIVE_RIGHT;
    int INT_LEGACY_PERSPECTIVE_BOTTOM_LEFT = Enum.INT_LEGACY_PERSPECTIVE_BOTTOM_LEFT;
    int INT_LEGACY_PERSPECTIVE_BOTTOM = Enum.INT_LEGACY_PERSPECTIVE_BOTTOM;
    int INT_LEGACY_PERSPECTIVE_BOTTOM_RIGHT = Enum.INT_LEGACY_PERSPECTIVE_BOTTOM_RIGHT;
    int INT_ORTHOGRAPHIC_FRONT = Enum.INT_ORTHOGRAPHIC_FRONT;
    int INT_ISOMETRIC_TOP_UP = Enum.INT_ISOMETRIC_TOP_UP;
    int INT_ISOMETRIC_TOP_DOWN = Enum.INT_ISOMETRIC_TOP_DOWN;
    int INT_ISOMETRIC_BOTTOM_UP = Enum.INT_ISOMETRIC_BOTTOM_UP;
    int INT_ISOMETRIC_BOTTOM_DOWN = Enum.INT_ISOMETRIC_BOTTOM_DOWN;
    int INT_ISOMETRIC_LEFT_UP = Enum.INT_ISOMETRIC_LEFT_UP;
    int INT_ISOMETRIC_LEFT_DOWN = Enum.INT_ISOMETRIC_LEFT_DOWN;
    int INT_ISOMETRIC_RIGHT_UP = Enum.INT_ISOMETRIC_RIGHT_UP;
    int INT_ISOMETRIC_RIGHT_DOWN = Enum.INT_ISOMETRIC_RIGHT_DOWN;
    int INT_ISOMETRIC_OFF_AXIS_1_LEFT = Enum.INT_ISOMETRIC_OFF_AXIS_1_LEFT;
    int INT_ISOMETRIC_OFF_AXIS_1_RIGHT = Enum.INT_ISOMETRIC_OFF_AXIS_1_RIGHT;
    int INT_ISOMETRIC_OFF_AXIS_1_TOP = Enum.INT_ISOMETRIC_OFF_AXIS_1_TOP;
    int INT_ISOMETRIC_OFF_AXIS_2_LEFT = Enum.INT_ISOMETRIC_OFF_AXIS_2_LEFT;
    int INT_ISOMETRIC_OFF_AXIS_2_RIGHT = Enum.INT_ISOMETRIC_OFF_AXIS_2_RIGHT;
    int INT_ISOMETRIC_OFF_AXIS_2_TOP = Enum.INT_ISOMETRIC_OFF_AXIS_2_TOP;
    int INT_ISOMETRIC_OFF_AXIS_3_LEFT = Enum.INT_ISOMETRIC_OFF_AXIS_3_LEFT;
    int INT_ISOMETRIC_OFF_AXIS_3_RIGHT = Enum.INT_ISOMETRIC_OFF_AXIS_3_RIGHT;
    int INT_ISOMETRIC_OFF_AXIS_3_BOTTOM = Enum.INT_ISOMETRIC_OFF_AXIS_3_BOTTOM;
    int INT_ISOMETRIC_OFF_AXIS_4_LEFT = Enum.INT_ISOMETRIC_OFF_AXIS_4_LEFT;
    int INT_ISOMETRIC_OFF_AXIS_4_RIGHT = Enum.INT_ISOMETRIC_OFF_AXIS_4_RIGHT;
    int INT_ISOMETRIC_OFF_AXIS_4_BOTTOM = Enum.INT_ISOMETRIC_OFF_AXIS_4_BOTTOM;
    int INT_OBLIQUE_TOP_LEFT = Enum.INT_OBLIQUE_TOP_LEFT;
    int INT_OBLIQUE_TOP = Enum.INT_OBLIQUE_TOP;
    int INT_OBLIQUE_TOP_RIGHT = Enum.INT_OBLIQUE_TOP_RIGHT;
    int INT_OBLIQUE_LEFT = Enum.INT_OBLIQUE_LEFT;
    int INT_OBLIQUE_RIGHT = Enum.INT_OBLIQUE_RIGHT;
    int INT_OBLIQUE_BOTTOM_LEFT = Enum.INT_OBLIQUE_BOTTOM_LEFT;
    int INT_OBLIQUE_BOTTOM = Enum.INT_OBLIQUE_BOTTOM;
    int INT_OBLIQUE_BOTTOM_RIGHT = Enum.INT_OBLIQUE_BOTTOM_RIGHT;
    int INT_PERSPECTIVE_FRONT = Enum.INT_PERSPECTIVE_FRONT;
    int INT_PERSPECTIVE_LEFT = Enum.INT_PERSPECTIVE_LEFT;
    int INT_PERSPECTIVE_RIGHT = Enum.INT_PERSPECTIVE_RIGHT;
    int INT_PERSPECTIVE_ABOVE = Enum.INT_PERSPECTIVE_ABOVE;
    int INT_PERSPECTIVE_BELOW = Enum.INT_PERSPECTIVE_BELOW;
    int INT_PERSPECTIVE_ABOVE_LEFT_FACING = Enum.INT_PERSPECTIVE_ABOVE_LEFT_FACING;
    int INT_PERSPECTIVE_ABOVE_RIGHT_FACING = Enum.INT_PERSPECTIVE_ABOVE_RIGHT_FACING;
    int INT_PERSPECTIVE_CONTRASTING_LEFT_FACING = Enum.INT_PERSPECTIVE_CONTRASTING_LEFT_FACING;
    int INT_PERSPECTIVE_CONTRASTING_RIGHT_FACING = Enum.INT_PERSPECTIVE_CONTRASTING_RIGHT_FACING;
    int INT_PERSPECTIVE_HEROIC_LEFT_FACING = Enum.INT_PERSPECTIVE_HEROIC_LEFT_FACING;
    int INT_PERSPECTIVE_HEROIC_RIGHT_FACING = Enum.INT_PERSPECTIVE_HEROIC_RIGHT_FACING;
    int INT_PERSPECTIVE_HEROIC_EXTREME_LEFT_FACING = Enum.INT_PERSPECTIVE_HEROIC_EXTREME_LEFT_FACING;
    int INT_PERSPECTIVE_HEROIC_EXTREME_RIGHT_FACING = Enum.INT_PERSPECTIVE_HEROIC_EXTREME_RIGHT_FACING;
    int INT_PERSPECTIVE_RELAXED = Enum.INT_PERSPECTIVE_RELAXED;
    int INT_PERSPECTIVE_RELAXED_MODERATELY = Enum.INT_PERSPECTIVE_RELAXED_MODERATELY;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.main.STPresetCameraType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_LEGACY_OBLIQUE_TOP_LEFT
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

        static final int INT_LEGACY_OBLIQUE_TOP_LEFT = 1;
        static final int INT_LEGACY_OBLIQUE_TOP = 2;
        static final int INT_LEGACY_OBLIQUE_TOP_RIGHT = 3;
        static final int INT_LEGACY_OBLIQUE_LEFT = 4;
        static final int INT_LEGACY_OBLIQUE_FRONT = 5;
        static final int INT_LEGACY_OBLIQUE_RIGHT = 6;
        static final int INT_LEGACY_OBLIQUE_BOTTOM_LEFT = 7;
        static final int INT_LEGACY_OBLIQUE_BOTTOM = 8;
        static final int INT_LEGACY_OBLIQUE_BOTTOM_RIGHT = 9;
        static final int INT_LEGACY_PERSPECTIVE_TOP_LEFT = 10;
        static final int INT_LEGACY_PERSPECTIVE_TOP = 11;
        static final int INT_LEGACY_PERSPECTIVE_TOP_RIGHT = 12;
        static final int INT_LEGACY_PERSPECTIVE_LEFT = 13;
        static final int INT_LEGACY_PERSPECTIVE_FRONT = 14;
        static final int INT_LEGACY_PERSPECTIVE_RIGHT = 15;
        static final int INT_LEGACY_PERSPECTIVE_BOTTOM_LEFT = 16;
        static final int INT_LEGACY_PERSPECTIVE_BOTTOM = 17;
        static final int INT_LEGACY_PERSPECTIVE_BOTTOM_RIGHT = 18;
        static final int INT_ORTHOGRAPHIC_FRONT = 19;
        static final int INT_ISOMETRIC_TOP_UP = 20;
        static final int INT_ISOMETRIC_TOP_DOWN = 21;
        static final int INT_ISOMETRIC_BOTTOM_UP = 22;
        static final int INT_ISOMETRIC_BOTTOM_DOWN = 23;
        static final int INT_ISOMETRIC_LEFT_UP = 24;
        static final int INT_ISOMETRIC_LEFT_DOWN = 25;
        static final int INT_ISOMETRIC_RIGHT_UP = 26;
        static final int INT_ISOMETRIC_RIGHT_DOWN = 27;
        static final int INT_ISOMETRIC_OFF_AXIS_1_LEFT = 28;
        static final int INT_ISOMETRIC_OFF_AXIS_1_RIGHT = 29;
        static final int INT_ISOMETRIC_OFF_AXIS_1_TOP = 30;
        static final int INT_ISOMETRIC_OFF_AXIS_2_LEFT = 31;
        static final int INT_ISOMETRIC_OFF_AXIS_2_RIGHT = 32;
        static final int INT_ISOMETRIC_OFF_AXIS_2_TOP = 33;
        static final int INT_ISOMETRIC_OFF_AXIS_3_LEFT = 34;
        static final int INT_ISOMETRIC_OFF_AXIS_3_RIGHT = 35;
        static final int INT_ISOMETRIC_OFF_AXIS_3_BOTTOM = 36;
        static final int INT_ISOMETRIC_OFF_AXIS_4_LEFT = 37;
        static final int INT_ISOMETRIC_OFF_AXIS_4_RIGHT = 38;
        static final int INT_ISOMETRIC_OFF_AXIS_4_BOTTOM = 39;
        static final int INT_OBLIQUE_TOP_LEFT = 40;
        static final int INT_OBLIQUE_TOP = 41;
        static final int INT_OBLIQUE_TOP_RIGHT = 42;
        static final int INT_OBLIQUE_LEFT = 43;
        static final int INT_OBLIQUE_RIGHT = 44;
        static final int INT_OBLIQUE_BOTTOM_LEFT = 45;
        static final int INT_OBLIQUE_BOTTOM = 46;
        static final int INT_OBLIQUE_BOTTOM_RIGHT = 47;
        static final int INT_PERSPECTIVE_FRONT = 48;
        static final int INT_PERSPECTIVE_LEFT = 49;
        static final int INT_PERSPECTIVE_RIGHT = 50;
        static final int INT_PERSPECTIVE_ABOVE = 51;
        static final int INT_PERSPECTIVE_BELOW = 52;
        static final int INT_PERSPECTIVE_ABOVE_LEFT_FACING = 53;
        static final int INT_PERSPECTIVE_ABOVE_RIGHT_FACING = 54;
        static final int INT_PERSPECTIVE_CONTRASTING_LEFT_FACING = 55;
        static final int INT_PERSPECTIVE_CONTRASTING_RIGHT_FACING = 56;
        static final int INT_PERSPECTIVE_HEROIC_LEFT_FACING = 57;
        static final int INT_PERSPECTIVE_HEROIC_RIGHT_FACING = 58;
        static final int INT_PERSPECTIVE_HEROIC_EXTREME_LEFT_FACING = 59;
        static final int INT_PERSPECTIVE_HEROIC_EXTREME_RIGHT_FACING = 60;
        static final int INT_PERSPECTIVE_RELAXED = 61;
        static final int INT_PERSPECTIVE_RELAXED_MODERATELY = 62;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("legacyObliqueTopLeft", INT_LEGACY_OBLIQUE_TOP_LEFT),
            new Enum("legacyObliqueTop", INT_LEGACY_OBLIQUE_TOP),
            new Enum("legacyObliqueTopRight", INT_LEGACY_OBLIQUE_TOP_RIGHT),
            new Enum("legacyObliqueLeft", INT_LEGACY_OBLIQUE_LEFT),
            new Enum("legacyObliqueFront", INT_LEGACY_OBLIQUE_FRONT),
            new Enum("legacyObliqueRight", INT_LEGACY_OBLIQUE_RIGHT),
            new Enum("legacyObliqueBottomLeft", INT_LEGACY_OBLIQUE_BOTTOM_LEFT),
            new Enum("legacyObliqueBottom", INT_LEGACY_OBLIQUE_BOTTOM),
            new Enum("legacyObliqueBottomRight", INT_LEGACY_OBLIQUE_BOTTOM_RIGHT),
            new Enum("legacyPerspectiveTopLeft", INT_LEGACY_PERSPECTIVE_TOP_LEFT),
            new Enum("legacyPerspectiveTop", INT_LEGACY_PERSPECTIVE_TOP),
            new Enum("legacyPerspectiveTopRight", INT_LEGACY_PERSPECTIVE_TOP_RIGHT),
            new Enum("legacyPerspectiveLeft", INT_LEGACY_PERSPECTIVE_LEFT),
            new Enum("legacyPerspectiveFront", INT_LEGACY_PERSPECTIVE_FRONT),
            new Enum("legacyPerspectiveRight", INT_LEGACY_PERSPECTIVE_RIGHT),
            new Enum("legacyPerspectiveBottomLeft", INT_LEGACY_PERSPECTIVE_BOTTOM_LEFT),
            new Enum("legacyPerspectiveBottom", INT_LEGACY_PERSPECTIVE_BOTTOM),
            new Enum("legacyPerspectiveBottomRight", INT_LEGACY_PERSPECTIVE_BOTTOM_RIGHT),
            new Enum("orthographicFront", INT_ORTHOGRAPHIC_FRONT),
            new Enum("isometricTopUp", INT_ISOMETRIC_TOP_UP),
            new Enum("isometricTopDown", INT_ISOMETRIC_TOP_DOWN),
            new Enum("isometricBottomUp", INT_ISOMETRIC_BOTTOM_UP),
            new Enum("isometricBottomDown", INT_ISOMETRIC_BOTTOM_DOWN),
            new Enum("isometricLeftUp", INT_ISOMETRIC_LEFT_UP),
            new Enum("isometricLeftDown", INT_ISOMETRIC_LEFT_DOWN),
            new Enum("isometricRightUp", INT_ISOMETRIC_RIGHT_UP),
            new Enum("isometricRightDown", INT_ISOMETRIC_RIGHT_DOWN),
            new Enum("isometricOffAxis1Left", INT_ISOMETRIC_OFF_AXIS_1_LEFT),
            new Enum("isometricOffAxis1Right", INT_ISOMETRIC_OFF_AXIS_1_RIGHT),
            new Enum("isometricOffAxis1Top", INT_ISOMETRIC_OFF_AXIS_1_TOP),
            new Enum("isometricOffAxis2Left", INT_ISOMETRIC_OFF_AXIS_2_LEFT),
            new Enum("isometricOffAxis2Right", INT_ISOMETRIC_OFF_AXIS_2_RIGHT),
            new Enum("isometricOffAxis2Top", INT_ISOMETRIC_OFF_AXIS_2_TOP),
            new Enum("isometricOffAxis3Left", INT_ISOMETRIC_OFF_AXIS_3_LEFT),
            new Enum("isometricOffAxis3Right", INT_ISOMETRIC_OFF_AXIS_3_RIGHT),
            new Enum("isometricOffAxis3Bottom", INT_ISOMETRIC_OFF_AXIS_3_BOTTOM),
            new Enum("isometricOffAxis4Left", INT_ISOMETRIC_OFF_AXIS_4_LEFT),
            new Enum("isometricOffAxis4Right", INT_ISOMETRIC_OFF_AXIS_4_RIGHT),
            new Enum("isometricOffAxis4Bottom", INT_ISOMETRIC_OFF_AXIS_4_BOTTOM),
            new Enum("obliqueTopLeft", INT_OBLIQUE_TOP_LEFT),
            new Enum("obliqueTop", INT_OBLIQUE_TOP),
            new Enum("obliqueTopRight", INT_OBLIQUE_TOP_RIGHT),
            new Enum("obliqueLeft", INT_OBLIQUE_LEFT),
            new Enum("obliqueRight", INT_OBLIQUE_RIGHT),
            new Enum("obliqueBottomLeft", INT_OBLIQUE_BOTTOM_LEFT),
            new Enum("obliqueBottom", INT_OBLIQUE_BOTTOM),
            new Enum("obliqueBottomRight", INT_OBLIQUE_BOTTOM_RIGHT),
            new Enum("perspectiveFront", INT_PERSPECTIVE_FRONT),
            new Enum("perspectiveLeft", INT_PERSPECTIVE_LEFT),
            new Enum("perspectiveRight", INT_PERSPECTIVE_RIGHT),
            new Enum("perspectiveAbove", INT_PERSPECTIVE_ABOVE),
            new Enum("perspectiveBelow", INT_PERSPECTIVE_BELOW),
            new Enum("perspectiveAboveLeftFacing", INT_PERSPECTIVE_ABOVE_LEFT_FACING),
            new Enum("perspectiveAboveRightFacing", INT_PERSPECTIVE_ABOVE_RIGHT_FACING),
            new Enum("perspectiveContrastingLeftFacing", INT_PERSPECTIVE_CONTRASTING_LEFT_FACING),
            new Enum("perspectiveContrastingRightFacing", INT_PERSPECTIVE_CONTRASTING_RIGHT_FACING),
            new Enum("perspectiveHeroicLeftFacing", INT_PERSPECTIVE_HEROIC_LEFT_FACING),
            new Enum("perspectiveHeroicRightFacing", INT_PERSPECTIVE_HEROIC_RIGHT_FACING),
            new Enum("perspectiveHeroicExtremeLeftFacing", INT_PERSPECTIVE_HEROIC_EXTREME_LEFT_FACING),
            new Enum("perspectiveHeroicExtremeRightFacing", INT_PERSPECTIVE_HEROIC_EXTREME_RIGHT_FACING),
            new Enum("perspectiveRelaxed", INT_PERSPECTIVE_RELAXED),
            new Enum("perspectiveRelaxedModerately", INT_PERSPECTIVE_RELAXED_MODERATELY),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}

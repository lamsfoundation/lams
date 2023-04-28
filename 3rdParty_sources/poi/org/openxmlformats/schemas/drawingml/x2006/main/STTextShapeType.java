/*
 * XML Type:  ST_TextShapeType
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.STTextShapeType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_TextShapeType(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.main.STTextShapeType.
 */
public interface STTextShapeType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.main.STTextShapeType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sttextshapetype3a2ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum TEXT_NO_SHAPE = Enum.forString("textNoShape");
    Enum TEXT_PLAIN = Enum.forString("textPlain");
    Enum TEXT_STOP = Enum.forString("textStop");
    Enum TEXT_TRIANGLE = Enum.forString("textTriangle");
    Enum TEXT_TRIANGLE_INVERTED = Enum.forString("textTriangleInverted");
    Enum TEXT_CHEVRON = Enum.forString("textChevron");
    Enum TEXT_CHEVRON_INVERTED = Enum.forString("textChevronInverted");
    Enum TEXT_RING_INSIDE = Enum.forString("textRingInside");
    Enum TEXT_RING_OUTSIDE = Enum.forString("textRingOutside");
    Enum TEXT_ARCH_UP = Enum.forString("textArchUp");
    Enum TEXT_ARCH_DOWN = Enum.forString("textArchDown");
    Enum TEXT_CIRCLE = Enum.forString("textCircle");
    Enum TEXT_BUTTON = Enum.forString("textButton");
    Enum TEXT_ARCH_UP_POUR = Enum.forString("textArchUpPour");
    Enum TEXT_ARCH_DOWN_POUR = Enum.forString("textArchDownPour");
    Enum TEXT_CIRCLE_POUR = Enum.forString("textCirclePour");
    Enum TEXT_BUTTON_POUR = Enum.forString("textButtonPour");
    Enum TEXT_CURVE_UP = Enum.forString("textCurveUp");
    Enum TEXT_CURVE_DOWN = Enum.forString("textCurveDown");
    Enum TEXT_CAN_UP = Enum.forString("textCanUp");
    Enum TEXT_CAN_DOWN = Enum.forString("textCanDown");
    Enum TEXT_WAVE_1 = Enum.forString("textWave1");
    Enum TEXT_WAVE_2 = Enum.forString("textWave2");
    Enum TEXT_DOUBLE_WAVE_1 = Enum.forString("textDoubleWave1");
    Enum TEXT_WAVE_4 = Enum.forString("textWave4");
    Enum TEXT_INFLATE = Enum.forString("textInflate");
    Enum TEXT_DEFLATE = Enum.forString("textDeflate");
    Enum TEXT_INFLATE_BOTTOM = Enum.forString("textInflateBottom");
    Enum TEXT_DEFLATE_BOTTOM = Enum.forString("textDeflateBottom");
    Enum TEXT_INFLATE_TOP = Enum.forString("textInflateTop");
    Enum TEXT_DEFLATE_TOP = Enum.forString("textDeflateTop");
    Enum TEXT_DEFLATE_INFLATE = Enum.forString("textDeflateInflate");
    Enum TEXT_DEFLATE_INFLATE_DEFLATE = Enum.forString("textDeflateInflateDeflate");
    Enum TEXT_FADE_RIGHT = Enum.forString("textFadeRight");
    Enum TEXT_FADE_LEFT = Enum.forString("textFadeLeft");
    Enum TEXT_FADE_UP = Enum.forString("textFadeUp");
    Enum TEXT_FADE_DOWN = Enum.forString("textFadeDown");
    Enum TEXT_SLANT_UP = Enum.forString("textSlantUp");
    Enum TEXT_SLANT_DOWN = Enum.forString("textSlantDown");
    Enum TEXT_CASCADE_UP = Enum.forString("textCascadeUp");
    Enum TEXT_CASCADE_DOWN = Enum.forString("textCascadeDown");

    int INT_TEXT_NO_SHAPE = Enum.INT_TEXT_NO_SHAPE;
    int INT_TEXT_PLAIN = Enum.INT_TEXT_PLAIN;
    int INT_TEXT_STOP = Enum.INT_TEXT_STOP;
    int INT_TEXT_TRIANGLE = Enum.INT_TEXT_TRIANGLE;
    int INT_TEXT_TRIANGLE_INVERTED = Enum.INT_TEXT_TRIANGLE_INVERTED;
    int INT_TEXT_CHEVRON = Enum.INT_TEXT_CHEVRON;
    int INT_TEXT_CHEVRON_INVERTED = Enum.INT_TEXT_CHEVRON_INVERTED;
    int INT_TEXT_RING_INSIDE = Enum.INT_TEXT_RING_INSIDE;
    int INT_TEXT_RING_OUTSIDE = Enum.INT_TEXT_RING_OUTSIDE;
    int INT_TEXT_ARCH_UP = Enum.INT_TEXT_ARCH_UP;
    int INT_TEXT_ARCH_DOWN = Enum.INT_TEXT_ARCH_DOWN;
    int INT_TEXT_CIRCLE = Enum.INT_TEXT_CIRCLE;
    int INT_TEXT_BUTTON = Enum.INT_TEXT_BUTTON;
    int INT_TEXT_ARCH_UP_POUR = Enum.INT_TEXT_ARCH_UP_POUR;
    int INT_TEXT_ARCH_DOWN_POUR = Enum.INT_TEXT_ARCH_DOWN_POUR;
    int INT_TEXT_CIRCLE_POUR = Enum.INT_TEXT_CIRCLE_POUR;
    int INT_TEXT_BUTTON_POUR = Enum.INT_TEXT_BUTTON_POUR;
    int INT_TEXT_CURVE_UP = Enum.INT_TEXT_CURVE_UP;
    int INT_TEXT_CURVE_DOWN = Enum.INT_TEXT_CURVE_DOWN;
    int INT_TEXT_CAN_UP = Enum.INT_TEXT_CAN_UP;
    int INT_TEXT_CAN_DOWN = Enum.INT_TEXT_CAN_DOWN;
    int INT_TEXT_WAVE_1 = Enum.INT_TEXT_WAVE_1;
    int INT_TEXT_WAVE_2 = Enum.INT_TEXT_WAVE_2;
    int INT_TEXT_DOUBLE_WAVE_1 = Enum.INT_TEXT_DOUBLE_WAVE_1;
    int INT_TEXT_WAVE_4 = Enum.INT_TEXT_WAVE_4;
    int INT_TEXT_INFLATE = Enum.INT_TEXT_INFLATE;
    int INT_TEXT_DEFLATE = Enum.INT_TEXT_DEFLATE;
    int INT_TEXT_INFLATE_BOTTOM = Enum.INT_TEXT_INFLATE_BOTTOM;
    int INT_TEXT_DEFLATE_BOTTOM = Enum.INT_TEXT_DEFLATE_BOTTOM;
    int INT_TEXT_INFLATE_TOP = Enum.INT_TEXT_INFLATE_TOP;
    int INT_TEXT_DEFLATE_TOP = Enum.INT_TEXT_DEFLATE_TOP;
    int INT_TEXT_DEFLATE_INFLATE = Enum.INT_TEXT_DEFLATE_INFLATE;
    int INT_TEXT_DEFLATE_INFLATE_DEFLATE = Enum.INT_TEXT_DEFLATE_INFLATE_DEFLATE;
    int INT_TEXT_FADE_RIGHT = Enum.INT_TEXT_FADE_RIGHT;
    int INT_TEXT_FADE_LEFT = Enum.INT_TEXT_FADE_LEFT;
    int INT_TEXT_FADE_UP = Enum.INT_TEXT_FADE_UP;
    int INT_TEXT_FADE_DOWN = Enum.INT_TEXT_FADE_DOWN;
    int INT_TEXT_SLANT_UP = Enum.INT_TEXT_SLANT_UP;
    int INT_TEXT_SLANT_DOWN = Enum.INT_TEXT_SLANT_DOWN;
    int INT_TEXT_CASCADE_UP = Enum.INT_TEXT_CASCADE_UP;
    int INT_TEXT_CASCADE_DOWN = Enum.INT_TEXT_CASCADE_DOWN;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.main.STTextShapeType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_TEXT_NO_SHAPE
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

        static final int INT_TEXT_NO_SHAPE = 1;
        static final int INT_TEXT_PLAIN = 2;
        static final int INT_TEXT_STOP = 3;
        static final int INT_TEXT_TRIANGLE = 4;
        static final int INT_TEXT_TRIANGLE_INVERTED = 5;
        static final int INT_TEXT_CHEVRON = 6;
        static final int INT_TEXT_CHEVRON_INVERTED = 7;
        static final int INT_TEXT_RING_INSIDE = 8;
        static final int INT_TEXT_RING_OUTSIDE = 9;
        static final int INT_TEXT_ARCH_UP = 10;
        static final int INT_TEXT_ARCH_DOWN = 11;
        static final int INT_TEXT_CIRCLE = 12;
        static final int INT_TEXT_BUTTON = 13;
        static final int INT_TEXT_ARCH_UP_POUR = 14;
        static final int INT_TEXT_ARCH_DOWN_POUR = 15;
        static final int INT_TEXT_CIRCLE_POUR = 16;
        static final int INT_TEXT_BUTTON_POUR = 17;
        static final int INT_TEXT_CURVE_UP = 18;
        static final int INT_TEXT_CURVE_DOWN = 19;
        static final int INT_TEXT_CAN_UP = 20;
        static final int INT_TEXT_CAN_DOWN = 21;
        static final int INT_TEXT_WAVE_1 = 22;
        static final int INT_TEXT_WAVE_2 = 23;
        static final int INT_TEXT_DOUBLE_WAVE_1 = 24;
        static final int INT_TEXT_WAVE_4 = 25;
        static final int INT_TEXT_INFLATE = 26;
        static final int INT_TEXT_DEFLATE = 27;
        static final int INT_TEXT_INFLATE_BOTTOM = 28;
        static final int INT_TEXT_DEFLATE_BOTTOM = 29;
        static final int INT_TEXT_INFLATE_TOP = 30;
        static final int INT_TEXT_DEFLATE_TOP = 31;
        static final int INT_TEXT_DEFLATE_INFLATE = 32;
        static final int INT_TEXT_DEFLATE_INFLATE_DEFLATE = 33;
        static final int INT_TEXT_FADE_RIGHT = 34;
        static final int INT_TEXT_FADE_LEFT = 35;
        static final int INT_TEXT_FADE_UP = 36;
        static final int INT_TEXT_FADE_DOWN = 37;
        static final int INT_TEXT_SLANT_UP = 38;
        static final int INT_TEXT_SLANT_DOWN = 39;
        static final int INT_TEXT_CASCADE_UP = 40;
        static final int INT_TEXT_CASCADE_DOWN = 41;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("textNoShape", INT_TEXT_NO_SHAPE),
            new Enum("textPlain", INT_TEXT_PLAIN),
            new Enum("textStop", INT_TEXT_STOP),
            new Enum("textTriangle", INT_TEXT_TRIANGLE),
            new Enum("textTriangleInverted", INT_TEXT_TRIANGLE_INVERTED),
            new Enum("textChevron", INT_TEXT_CHEVRON),
            new Enum("textChevronInverted", INT_TEXT_CHEVRON_INVERTED),
            new Enum("textRingInside", INT_TEXT_RING_INSIDE),
            new Enum("textRingOutside", INT_TEXT_RING_OUTSIDE),
            new Enum("textArchUp", INT_TEXT_ARCH_UP),
            new Enum("textArchDown", INT_TEXT_ARCH_DOWN),
            new Enum("textCircle", INT_TEXT_CIRCLE),
            new Enum("textButton", INT_TEXT_BUTTON),
            new Enum("textArchUpPour", INT_TEXT_ARCH_UP_POUR),
            new Enum("textArchDownPour", INT_TEXT_ARCH_DOWN_POUR),
            new Enum("textCirclePour", INT_TEXT_CIRCLE_POUR),
            new Enum("textButtonPour", INT_TEXT_BUTTON_POUR),
            new Enum("textCurveUp", INT_TEXT_CURVE_UP),
            new Enum("textCurveDown", INT_TEXT_CURVE_DOWN),
            new Enum("textCanUp", INT_TEXT_CAN_UP),
            new Enum("textCanDown", INT_TEXT_CAN_DOWN),
            new Enum("textWave1", INT_TEXT_WAVE_1),
            new Enum("textWave2", INT_TEXT_WAVE_2),
            new Enum("textDoubleWave1", INT_TEXT_DOUBLE_WAVE_1),
            new Enum("textWave4", INT_TEXT_WAVE_4),
            new Enum("textInflate", INT_TEXT_INFLATE),
            new Enum("textDeflate", INT_TEXT_DEFLATE),
            new Enum("textInflateBottom", INT_TEXT_INFLATE_BOTTOM),
            new Enum("textDeflateBottom", INT_TEXT_DEFLATE_BOTTOM),
            new Enum("textInflateTop", INT_TEXT_INFLATE_TOP),
            new Enum("textDeflateTop", INT_TEXT_DEFLATE_TOP),
            new Enum("textDeflateInflate", INT_TEXT_DEFLATE_INFLATE),
            new Enum("textDeflateInflateDeflate", INT_TEXT_DEFLATE_INFLATE_DEFLATE),
            new Enum("textFadeRight", INT_TEXT_FADE_RIGHT),
            new Enum("textFadeLeft", INT_TEXT_FADE_LEFT),
            new Enum("textFadeUp", INT_TEXT_FADE_UP),
            new Enum("textFadeDown", INT_TEXT_FADE_DOWN),
            new Enum("textSlantUp", INT_TEXT_SLANT_UP),
            new Enum("textSlantDown", INT_TEXT_SLANT_DOWN),
            new Enum("textCascadeUp", INT_TEXT_CASCADE_UP),
            new Enum("textCascadeDown", INT_TEXT_CASCADE_DOWN),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}

/*
 * XML Type:  ST_FFTextType
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STFFTextType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_FFTextType(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STFFTextType.
 */
public interface STFFTextType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STFFTextType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stfftexttype99b1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum REGULAR = Enum.forString("regular");
    Enum NUMBER = Enum.forString("number");
    Enum DATE = Enum.forString("date");
    Enum CURRENT_TIME = Enum.forString("currentTime");
    Enum CURRENT_DATE = Enum.forString("currentDate");
    Enum CALCULATED = Enum.forString("calculated");

    int INT_REGULAR = Enum.INT_REGULAR;
    int INT_NUMBER = Enum.INT_NUMBER;
    int INT_DATE = Enum.INT_DATE;
    int INT_CURRENT_TIME = Enum.INT_CURRENT_TIME;
    int INT_CURRENT_DATE = Enum.INT_CURRENT_DATE;
    int INT_CALCULATED = Enum.INT_CALCULATED;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STFFTextType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_REGULAR
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

        static final int INT_REGULAR = 1;
        static final int INT_NUMBER = 2;
        static final int INT_DATE = 3;
        static final int INT_CURRENT_TIME = 4;
        static final int INT_CURRENT_DATE = 5;
        static final int INT_CALCULATED = 6;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("regular", INT_REGULAR),
            new Enum("number", INT_NUMBER),
            new Enum("date", INT_DATE),
            new Enum("currentTime", INT_CURRENT_TIME),
            new Enum("currentDate", INT_CURRENT_DATE),
            new Enum("calculated", INT_CALCULATED),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}

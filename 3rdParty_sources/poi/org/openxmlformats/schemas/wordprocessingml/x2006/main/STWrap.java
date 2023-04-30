/*
 * XML Type:  ST_Wrap
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_Wrap(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap.
 */
public interface STWrap extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stwrap3f4etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum AUTO = Enum.forString("auto");
    Enum NOT_BESIDE = Enum.forString("notBeside");
    Enum AROUND = Enum.forString("around");
    Enum TIGHT = Enum.forString("tight");
    Enum THROUGH = Enum.forString("through");
    Enum NONE = Enum.forString("none");

    int INT_AUTO = Enum.INT_AUTO;
    int INT_NOT_BESIDE = Enum.INT_NOT_BESIDE;
    int INT_AROUND = Enum.INT_AROUND;
    int INT_TIGHT = Enum.INT_TIGHT;
    int INT_THROUGH = Enum.INT_THROUGH;
    int INT_NONE = Enum.INT_NONE;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STWrap.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_AUTO
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

        static final int INT_AUTO = 1;
        static final int INT_NOT_BESIDE = 2;
        static final int INT_AROUND = 3;
        static final int INT_TIGHT = 4;
        static final int INT_THROUGH = 5;
        static final int INT_NONE = 6;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("auto", INT_AUTO),
            new Enum("notBeside", INT_NOT_BESIDE),
            new Enum("around", INT_AROUND),
            new Enum("tight", INT_TIGHT),
            new Enum("through", INT_THROUGH),
            new Enum("none", INT_NONE),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}

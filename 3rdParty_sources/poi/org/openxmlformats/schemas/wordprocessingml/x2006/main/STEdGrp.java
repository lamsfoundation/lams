/*
 * XML Type:  ST_EdGrp
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STEdGrp
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_EdGrp(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STEdGrp.
 */
public interface STEdGrp extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STEdGrp> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stedgrp6bdctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NONE = Enum.forString("none");
    Enum EVERYONE = Enum.forString("everyone");
    Enum ADMINISTRATORS = Enum.forString("administrators");
    Enum CONTRIBUTORS = Enum.forString("contributors");
    Enum EDITORS = Enum.forString("editors");
    Enum OWNERS = Enum.forString("owners");
    Enum CURRENT = Enum.forString("current");

    int INT_NONE = Enum.INT_NONE;
    int INT_EVERYONE = Enum.INT_EVERYONE;
    int INT_ADMINISTRATORS = Enum.INT_ADMINISTRATORS;
    int INT_CONTRIBUTORS = Enum.INT_CONTRIBUTORS;
    int INT_EDITORS = Enum.INT_EDITORS;
    int INT_OWNERS = Enum.INT_OWNERS;
    int INT_CURRENT = Enum.INT_CURRENT;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STEdGrp.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_NONE
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

        static final int INT_NONE = 1;
        static final int INT_EVERYONE = 2;
        static final int INT_ADMINISTRATORS = 3;
        static final int INT_CONTRIBUTORS = 4;
        static final int INT_EDITORS = 5;
        static final int INT_OWNERS = 6;
        static final int INT_CURRENT = 7;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("none", INT_NONE),
            new Enum("everyone", INT_EVERYONE),
            new Enum("administrators", INT_ADMINISTRATORS),
            new Enum("contributors", INT_CONTRIBUTORS),
            new Enum("editors", INT_EDITORS),
            new Enum("owners", INT_OWNERS),
            new Enum("current", INT_CURRENT),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}

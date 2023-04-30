package org.apache.poi.schemas.ooxml.system.ooxml;

import org.apache.xmlbeans.impl.schema.SchemaTypeSystemImpl;

public final class TypeSystemHolder extends SchemaTypeSystemImpl {
    public static final TypeSystemHolder typeSystem = new TypeSystemHolder();

    private TypeSystemHolder() {
        super(TypeSystemHolder.class);
    }
}

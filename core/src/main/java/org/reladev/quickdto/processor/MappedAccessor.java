package org.reladev.quickdto.processor;

public class MappedAccessor implements Component {
    public Field field;
    public AccessorMethod accessorMethod;
    public ConverterMethod converterMethod;

    public MappedAccessor(Field field, AccessorMethod accessorMethod) {
        this.field = field;
        this.accessorMethod = accessorMethod;
    }
}

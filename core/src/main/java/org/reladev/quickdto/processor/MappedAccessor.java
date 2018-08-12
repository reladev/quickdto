package org.reladev.quickdto.processor;

public class MappedAccessor implements Component {
    public DtoField dtoField;
    public AccessorMethod accessorMethod;
    public Method converterMethod;

    public MappedAccessor(DtoField dtoField, AccessorMethod accessorMethod) {
        this.dtoField = dtoField;
        this.accessorMethod = accessorMethod;
    }
}

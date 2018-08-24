package org.reladev.quickdto.processor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.reladev.quickdto.feature.QuickDtoFeature;
import org.reladev.quickdto.shared.QuickDto;

public class DtoDef extends ClassDef implements FieldList {
    public boolean makeDto = true;
    public List<QuickDtoFeature> features = new ArrayList<>();

    public LinkedList<String> annotations = new LinkedList<>();
    public String extend;
    public String dtoExtend;
    public LinkedList<String> implementList = new LinkedList<>();

    public boolean fieldAnnotationsOnGetter = false;

    public List<String> sourceClasses = new ArrayList<>();

    /**
     * List of methods in the DtoDef that should be copied to the DTO.
     *
     * @see QuickDto#copyMethods()
     */
    LinkedList<ConverterMethod> methods = new LinkedList<>();

    public DtoDef() {
        //features.add(new DirtyFeature());
    }

    public AccessorDef convertToSourceDef() {
        AccessorDef accessorDef = new AccessorDef();
        accessorDef.type = getTypeString();


        for (Field field : getFields().values()) {
            accessorDef.getters.put(field.getAccessorName(), field);
            accessorDef.setters.put(field.getAccessorName(), field);
        }

        return accessorDef;
    }


}

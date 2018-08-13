package org.reladev.quickdto.processor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class ClassDef {
    ///////////////////////////////////
    // Class definition
    ///////////////////////////////////
    public String packageString;
    public String name;
    public String qualifiedName;
    public boolean strictCopy = true;

    private LinkedHashMap<String, Field> fields = new LinkedHashMap<>();
    private HashMap<String, List<ConverterMethod>> converters = new HashMap<>();
    private LinkedList<SourceCopyMap> sourceMaps = new LinkedList<>();

    public Field getField(String accessorName) {
        return fields.get(accessorName);
    }

    public ConverterMethod getConverter(String toType, String fromType) {
        List<ConverterMethod> methods = converters.get(toType);
        if (methods == null) {
            String simpleToType = toType.substring(toType.lastIndexOf(".") + 1);
            methods = converters.get(simpleToType);
        }
        if (methods != null) {
            for (ConverterMethod method : methods) {
                if (method.fromType.equals(fromType)) {
                    return method;
                } else {
                    String simpleFromType = fromType.substring(fromType.lastIndexOf(".") + 1);
                    if (method.fromType.equals(simpleFromType)) {
                        return method;
                    }
                }
            }
        }
        return null;
    }

    public void addConverter(ConverterMethod method) {
        List<ConverterMethod> methods = converters.get(method.toType);
        if (methods == null) {
            methods = new LinkedList<>();
            converters.put(method.toType, methods);
        }
        methods.add(method);
    }

    public String getPackageString() {
        return packageString;
    }

    public void setPackageString(String packageString) {
        this.packageString = packageString;
    }

    public String getName() {
        return name;
    }

    public String getTypeString() {
        return packageString + "." + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedHashMap<String, Field> getFields() {
        return fields;
    }

    public HashMap<String, List<ConverterMethod>> getConverters() {
        return converters;
    }

    public LinkedList<SourceCopyMap> getSourceMaps() {
        return sourceMaps;
    }

    public boolean isStrictCopy() {
        return strictCopy;
    }

    public void setStrictCopy(boolean strictCopy) {
        this.strictCopy = strictCopy;
    }
}

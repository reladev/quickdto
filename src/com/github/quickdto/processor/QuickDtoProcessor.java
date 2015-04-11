package com.github.quickdto.processor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.SimpleTypeVisitor7;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import com.github.quickdto.EqualsHashCode;
import com.github.quickdto.QuickDto;
import com.github.quickdto.ReadOnly;


@SupportedAnnotationTypes({"com.googlecode.slotted.client.quickdto.QuickDto"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class QuickDtoProcessor extends AbstractProcessor {
    int messageCount = 0;
    private LinkedHashMap<String, DtoDef> dtoDefs = new LinkedHashMap<String, DtoDef>();

    public void print(String message) {
        processingEnv.getMessager().printMessage(Kind.NOTE, message + "_" + messageCount++);
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        for (Element element : env.getElementsAnnotatedWith(QuickDto.class)) {
            if (!element.getSimpleName().toString().endsWith("DtoDef")) {
                processingEnv.getMessager().printMessage(Kind.ERROR, element.getSimpleName() + " DtoDef must end in 'DtoDef'");
            } else {
                processDtoDef(element);
            }
        }

        return true;
    }

    private DtoDef processDtoDef(Element defElement) {
        processingEnv.getMessager().printMessage(Kind.NOTE, "Processing:" + defElement.getSimpleName());
        DtoDef dtoDef = new DtoDef();
        PackageElement packageElement = (PackageElement) defElement.getEnclosingElement();
        dtoDef.packageString = packageElement.getQualifiedName().toString();
        dtoDef.name = defElement.getSimpleName().toString();
        dtoDef.name = dtoDef.name.substring(0, dtoDef.name.length() - 3);
        dtoDef.qualifiedName = defElement.toString();
        //for (AnnotationMirror ann: defElement.getAnnotationMirrors()) {
        //    print(ann.toString());
        //}
        //QuickDto annotation = defElement.getAnnotation(QuickDto.class);

        for (Element subelement : defElement.getEnclosedElements()) {
            if (subelement.getKind() != ElementKind.CONSTRUCTOR) {
                TypeMirror mirror = subelement.asType();
                Field field = mirror.accept(getType, subelement);
                createNames(field);

                if (subelement.getAnnotation(EqualsHashCode.class) != null) {
                    field.equalsHashCode = true;
                }
                if (subelement.getAnnotation(ReadOnly.class) != null) {
                    field.readOnly = true;
                }

                Field existing = dtoDef.fields.get(field.fieldName);
                if (existing == null) {
                    dtoDef.fields.put(field.fieldName, field);
                } else {
                    //merge
                }
            }
        }

        for (Field field : dtoDef.fields.values()) {
            print(field.toString());
        }

        writeDto(dtoDef);
        return dtoDef;
    }

    private void createNames(Field field) {
        char firstChar = field.fieldName.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            field.accessorName = field.fieldName;
            firstChar = Character.toLowerCase(firstChar);
            field.fieldName = firstChar + field.fieldName.substring(1);

        } else {
            firstChar = Character.toUpperCase(firstChar);
            field.accessorName = firstChar + field.fieldName.substring(1);
        }
        field.enumName = field.fieldName.replaceAll("(.)([A-Z])", "$1_$2").toUpperCase();
    }

    private void writeDto(DtoDef dtoDef)  {
        try {
            JavaFileObject jfo = processingEnv.getFiler().createSourceFile(dtoDef.packageString + "." + dtoDef.name);

            BufferedWriter bw = new BufferedWriter(jfo.openWriter());
            bw.append("package ").append(dtoDef.packageString).append(";\n");
            bw.newLine();
            bw.append("import java.util.Arrays;\n");
            bw.append("import java.util.List;\n");
            bw.append("import java.util.Map;\n");
            bw.append("import java.util.Set;\n");
            bw.append("import com.googlecode.slotted.client.quickdto.Dto;\n");
            bw.append("import ").append(dtoDef.packageString).append(".").append(dtoDef.name).append(".Fields;\n");
            bw.newLine();
            bw.append("public class ").append(dtoDef.name).append(" extends Dto<Fields> {\n");
            bw.newLine();
            writeFieldsEnum(dtoDef, bw);
            bw.newLine();
            writeFields(dtoDef, bw);
            bw.newLine();
            writeGettersSetters(dtoDef, bw);
            bw.newLine();
            writeEqualsHash(dtoDef, bw);
            bw.newLine();
            bw.append("}\n");

            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFieldsEnum(DtoDef dtoDef, BufferedWriter bw) throws IOException {
        bw.append("\tpublic static enum Fields {\n");
        boolean first = true;
        for (Field field: dtoDef.fields.values()) {
            if (!first) {
                bw.append(",\n");
            } else {
                first = false;
            }
            bw.append("\t\t").append(field.enumName);
        }
        bw.append("\n\t}\n");
    }

    private void writeFields(DtoDef dtoDef, BufferedWriter bw) throws IOException {
        for (Field field: dtoDef.fields.values()) {
            bw.append("\tprivate ").append(field.type).append(" ").append(field.fieldName).append(";\n");
        }
    }

    private void writeGettersSetters(DtoDef dtoDef, BufferedWriter bw) throws IOException {
        for (Field field: dtoDef.fields.values()) {
            bw.append("\tpublic ").append(field.type).append(" get").append(field.accessorName).append("() {\n");
            bw.append("\t\treturn ").append(field.fieldName).append(";\n");
            bw.append("\t}\n");
            bw.newLine();
            if (!field.readOnly) {
                bw.append("\tpublic void set").append(field.accessorName).append("(").append(field.type).append(" ").append(field.fieldName).append(") {\n");
                if (field.primitive) {
                    bw.append("\t\tif (this.").append(field.fieldName).append(" != ").append(field.fieldName).append(") {\n");
                } else {
                    bw.append("\t\tif (Dto.safeEquals(this.").append(field.fieldName).append(", ").append(field.fieldName).append(")) {\n");
                }
                bw.append("\t\t\tsetDirty(Fields.").append(field.enumName).append(", true);\n");
                bw.append("\t\t\tthis.").append(field.fieldName).append(" = ").append(field.fieldName).append(";\n");
                bw.append("\t\t}\n");
                bw.append("\t}\n");
                bw.newLine();
            }
        }
    }

    private void writeEqualsHash(DtoDef dtoDef, BufferedWriter bw) throws IOException {
        List<Field> equalsFields = new LinkedList<Field>();
        for (Field field: dtoDef.fields.values()) {
            if (field.equalsHashCode) {
                equalsFields.add(field);
            }
        }
        Collection<Field> genFields;
        if (equalsFields.isEmpty()) {
            genFields = dtoDef.fields.values();

        } else {
            genFields = equalsFields;
        }

        bw.append("\tpublic boolean equals(Object o) {\n");
        bw.append("\t\tif (this == o) {\n");
        bw.append("\t\t\treturn true;\n");
        bw.append("\t\t}\n");
        bw.newLine();
        bw.append("\t\tif (o == null || getClass() != o.getClass()) {\n");
        bw.append("\t\t\treturn false;\n");
        bw.append("\t\t}\n");
        bw.newLine();
        bw.append("\t\t").append(dtoDef.name).append(" that = (").append(dtoDef.name).append(") o;\n");
        bw.newLine();

        for (Field field: genFields) {
            if (field.type.equals("float")) {
                bw.append("\t\tif (Float.compare(").append(field.fieldName).append(", that.").append(field.fieldName).append(") != 0) {\n");
            } else if (field.type.equals("double")) {
                bw.append("\t\tif (Double.compare(").append(field.fieldName).append(", that.").append(field.fieldName).append(") != 0) {\n");
            } else if (field.primitive) {
                bw.append("\t\tif (").append(field.fieldName).append(" != that.").append(field.fieldName).append(") {\n");
            } else if (field.type.endsWith("[]")) {
                bw.append("\t\tif (Arrays.equals(").append(field.fieldName).append(", that.").append(field.fieldName).append(")) {\n");
            } else {
                bw.append("\t\tif (").append(field.fieldName).append(" != null ? !").append(field.fieldName).append(".equals(that.").append(field.fieldName).append(") : that.").append(field.fieldName).append(" != null) {\n");
            }

            bw.append("\t\t\treturn false;\n");
            bw.append("\t\t}\n");
        }

        bw.newLine();
        bw.append("\t\treturn true;\n");
        bw.append("\t}\n");
        bw.newLine();

        bw.append("\tpublic int hashCode() {\n");
        bw.append("\t\tlong temp;\n\n");
        boolean first = true;
        for (Field field: genFields) {
            if (field.type.equals("double")) {
                bw.append("\t\ttemp = Double.doubleToLongBits(").append(field.fieldName).append(");\n");
            }
            if (first) {
                bw.append("\t\tint result = ");
                first = false;
            } else {
                bw.append("\t\tresult = 31 * result + ");
            }

            if (field.type.equals("long")) {
                bw.append("(int) (").append(field.fieldName).append(" ^ (").append(field.fieldName).append(" >>> 32));\n");
            } else if (field.type.equals("float")) {
                bw.append("(").append(field.fieldName).append(" != +0.0f ? Float.floatToIntBits(").append(field.fieldName).append(") : 0);");
            } else if (field.type.equals("double")) {
                bw.append("(int) (temp ^ (temp >>> 32));\n");
            } else if (field.type.equals("boolean")) {
                bw.append("(").append(field.fieldName).append(" ? 1 : 0);\n");
            } else if (field.primitive) {
                bw.append(field.fieldName).append(";\n");
            } else {
                bw.append("(").append(field.fieldName).append(" != null ? ").append(field.fieldName).append(".hashCode() : 0);\n");
            }
        }

        bw.append("\t\treturn result;\n");
        bw.append("\t}\n");
        bw.newLine();
    }

    private final TypeVisitor<Field, Element> getType =
            new SimpleTypeVisitor7<Field, Element>() {
                @Override public Field visitPrimitive(PrimitiveType t, Element element) {
                    Field field = new Field();
                    field.type = t.toString();
                    field.fieldName = element.toString();
                    field.primitive = true;
                    return field;
                }

                @Override public Field visitArray(ArrayType t, Element element) {
                    Field field = new Field();
                    field.type = t.toString();
                    field.fieldName = element.toString();
                    return field;
                }

                @Override public Field visitDeclared(DeclaredType t, Element element) {
                    Field field = new Field();
                    field.type = t.toString();
                    field.fieldName = element.toString();
                    return field;
                }

                @Override public Field visitExecutable(ExecutableType t, Element element) {
                    Field field = new Field();
                    if (t.getReturnType().getKind() != TypeKind.VOID) {
                        field = t.getReturnType().accept(getType, element);
                    } else if (t.getParameterTypes().size() == 1) {
                        field = t.getParameterTypes().get(0).accept(getType, element);
                    } else {
                        print("exParams:" + t.getParameterTypes().size());
                        field.type = "Error";
                    }

                    String name = element.toString();
                    int start = 0;
                    if (name.startsWith("set") || name.startsWith("get")) {
                        start = 3;
                    } else if (name.startsWith("is")) {
                        start = 2;
                    }
                    int end = name.indexOf('(');
                    if (end == -1) {
                        print("exName:" + name);
                        end = name.length();
                    }
                    field.fieldName = name.substring(start, end);

                    return field;
                }

            };

    public class DtoDef {
        public String packageString;
        public String name;
        public String qualifiedName;
        public Class[] sources;
        private LinkedHashMap<String, Field> fields = new LinkedHashMap<String, Field>();
    }

    public class Field {
        public String type;
        public String fieldName;
        public String enumName;
        public String accessorName;
        public String importString;
        public boolean primitive;
        public boolean readOnly;
        public boolean ignoreDirty;
        public boolean equalsHashCode;
        public Class[] jsonExclude;
        public Class[] jsonInclude;

        @Override public String toString() {
            return type + " " + fieldName;
        }
    }
}
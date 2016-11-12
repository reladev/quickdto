package com.github.quickdto.processor;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleTypeVisitor7;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;

import com.github.quickdto.shared.EqualsHashCode;
import com.github.quickdto.shared.QuickDto;
import com.github.quickdto.shared.ReadOnly;

@SupportedAnnotationTypes({"com.github.quickdto.shared.QuickDto"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class QuickDtoProcessor extends AbstractProcessor {
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
        processingEnv.getMessager().printMessage(Kind.NOTE, "QuickDto: " + defElement.getSimpleName());
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
                if (field == null) {
                    throw new IllegalStateException(mirror + " type can't be found.  If a DtoDef references another DtoDef, don't use the create Dto, use the DtoDef");
                }
                createNames(field);

                if (subelement.getAnnotation(EqualsHashCode.class) != null) {
                    field.equalsHashCode = true;
                }
                ReadOnly roAnnotation = subelement.getAnnotation(ReadOnly.class);
                if (roAnnotation != null) {
                    field.readOnly = true;
	                if (!roAnnotation.setter()) {
		                field.excludeSetter = true;
	                }
                }

	            addAnnotations(subelement, field);

	            Field existing = dtoDef.fields.get(field.accessorName);
                if (existing == null) {
                    dtoDef.fields.put(field.accessorName, field);
                } else {
                    //todo write merge
                }
            }
        }
        addSources(defElement, dtoDef);

        writeDto(dtoDef);
        return dtoDef;
    }

	private boolean isQuickDtoAnntoation(AnnotationMirror an) {
		return an.toString().startsWith("@com.github.quickdto");
	}

    private void addSources(Element element, DtoDef dtoDef) {
        final String annotationName = QuickDto.class.getName();
        element.getAnnotationMirrors();
        for(AnnotationMirror am : element.getAnnotationMirrors() ) {
            AnnotationValue action;
            if(annotationName.equals(am.getAnnotationType().toString()) ) {
                for(Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet() ) {
                    if("source".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        List sources = (List) action.getValue();
                        for (Object source: sources) {
	                        addSource(dtoDef, source);
                        }
                    } else if("implement".equals(entry.getKey().getSimpleName().toString())) {
	                    action = entry.getValue();
	                    List implementList = (List) action.getValue();
	                    for (Object implement: implementList) {
		                    String className = implement.toString();
		                    className = className.substring(0, className.length() - 6);
		                    dtoDef.implementList.add(className);
	                    }
                    } else if("extend".equals(entry.getKey().getSimpleName().toString())) {
	                    action = entry.getValue();
	                    List implementList = (List) action.getValue();
	                    if (!implementList.isEmpty()) {
		                    Object extend = implementList.get(0);
		                    String className = extend.toString();
		                    className = className.substring(0, className.length() - 6);
		                    dtoDef.extend = className;
	                    }
                    }
                }
            }
        }
    }

	private void addSource(DtoDef dtoDef, Object source) {Source sourceDef = new Source();
		dtoDef.sources.add(sourceDef);
		Elements elementUtils = processingEnv.getElementUtils();
		String className = source.toString();
		className = className.substring(0, className.length() - 6);
		sourceDef.type = className;
		TypeElement sourceType = elementUtils.getTypeElement(className);
		while (sourceType != null) {
		    for (Element sourceSubEl: sourceType.getEnclosedElements()) {
		        if (sourceSubEl instanceof ExecutableElement) {
		            String name = sourceSubEl.getSimpleName().toString();
		            if (name.startsWith("set")) {
		                String accessorName = name.substring(3);
		                if (dtoDef.fields.get(accessorName) != null) {
		                    sourceDef.setters.add(accessorName);
		                }
		            }
		            if (name.startsWith("get")) {
		                String accessorName = name.substring(3);
		                if (dtoDef.fields.get(accessorName) != null) {
		                    sourceDef.getters.add(accessorName);
		                }
		            }
		            if (name.startsWith("is")) {
		                String accessorName = name.substring(2);
		                if (dtoDef.fields.get(accessorName) != null) {
		                    sourceDef.getters.add(accessorName);
		                }
		            }

		        }
		    }

		    if (sourceType.getSuperclass() instanceof NoType) {
		        sourceType = null;
		    } else {
		        sourceType = (TypeElement)((DeclaredType)sourceType.getSuperclass()).asElement();
		    }
		}
	}

	private void addAnnotations(Element subelement, Field field) {
		List<? extends AnnotationMirror> annotationMirrors = subelement.getAnnotationMirrors();
		for (AnnotationMirror am: annotationMirrors) {
			if (!isQuickDtoAnntoation(am)) {
				if (subelement.getKind() == ElementKind.FIELD) {
					field.fieldAnnotations.add(am.toString());

				} else if (subelement.getKind() == ElementKind.METHOD) {
					if (subelement.getSimpleName().toString().startsWith("get")) {
						field.getterAnnotations.add(am.toString());

					} else if (subelement.getSimpleName().toString().startsWith("set")) {
						field.setterAnnotations.add(am.toString());
					}
				}

			}
		}
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
            bw.append("import java.util.HashSet;\n");
            bw.append("import java.util.List;\n");
            bw.append("import java.util.Map;\n");
            bw.append("import java.util.Set;\n");
            bw.append("import com.github.quickdto.shared.DtoUtil;\n");
            bw.append("import com.github.quickdto.shared.GwtIncompatible;\n");
            bw.newLine();
            bw.append("public class ").append(dtoDef.name);
	        if (dtoDef.extend != null) {
		        bw.append(" extends ").append(dtoDef.extend);
	        }
	        if (!dtoDef.implementList.isEmpty()) {
		        bw.append(" implements ");
		        for (String implement : dtoDef.implementList) {
			        bw.append(implement);
		        }
	        }
	        bw.append(" {\n");
            bw.newLine();
            writeFieldsEnum(dtoDef, bw);
            bw.newLine();
            writeFields(dtoDef, bw);
            bw.newLine();
            writeDirty(bw);
            writeGettersSetters(dtoDef, bw);
            writeEqualsHash(dtoDef, bw);
            writeCopyMethods(dtoDef, bw);
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
            bw.append("\t\t").append(field.enumName).append("(\"").append(field.fieldName).append("\")");
        }
        bw.append(";\n");
        bw.newLine();
        bw.append("\t\tprivate String name;\n");
        bw.newLine();
        bw.append("\t\tFields(String name) {\n");
        bw.append("\t\t\tthis.name = name;\n");
        bw.append("\t\t}\n");
        bw.newLine();
        bw.append("\t\tpublic String toString() {\n");
        bw.append("\t\t\treturn name;\n");
        bw.append("\t\t}\n");
        bw.newLine();
        bw.append("\t\tpublic static Fields get(String fieldName) {\n");
        bw.append("\t\t\tfor (Fields field: Fields.values()) {\n");
        bw.append("\t\t\t\tif (field.name.equals(fieldName)) {\n");
        bw.append("\t\t\t\t\treturn field;\n");
        bw.append("\t\t\t\t}\n");
        bw.append("\t\t\t}\n");
        bw.append("\t\t\treturn null;\n");
        bw.append("\t\t}\n");
        bw.append("\t}\n");
    }

    private void writeFields(DtoDef dtoDef, BufferedWriter bw) throws IOException {
        for (Field field: dtoDef.fields.values()) {
	        for (String annotation: field.fieldAnnotations) {
		        bw.append("\t").append(annotation).append("\n");
	        }
            bw.append("\tprivate ").append(field.type).append(" ").append(field.fieldName).append(";\n");
        }
    }

    private void writeDirty(BufferedWriter bw) throws IOException {
        bw.append("\tprotected Set<Fields> dirtyFields = new HashSet<Fields>();\n");
        bw.newLine();
        bw.append("\tpublic void setDirty(Fields field, boolean dirty) {\n");
        bw.append("\t\tif (dirty) {\n");
        bw.append("\t\t\tdirtyFields.add(field);\n");
        bw.append("\t\t} else {\n");
        bw.append("\t\t\tdirtyFields.remove(field);\n");
        bw.append("\t\t}\n");
        // Todo handle dirty for nested
        //bw.append("\t\tif (parent != null) {\n");
        //bw.append("\t\t\tparent.setDirty(fieldInParent, isDirty());\n");
        //bw.append("\t\t}\n");
        bw.append("\t}\n");
        bw.newLine();
        bw.append("\tpublic boolean isDirty() {\n");
        bw.append("\t\treturn !dirtyFields.isEmpty();\n");
        bw.append("\t}\n");
        bw.newLine();
        bw.append("\tpublic boolean isDirty(Fields field) {\n");
        bw.append("\t\treturn dirtyFields.contains(field);\n");
        bw.append("\t}\n");
        bw.newLine();
        bw.append("\tpublic void setDirtyFields(Set<Fields> dirtyFields) { \n");
        bw.append("\t\tthis.dirtyFields = dirtyFields;\n");
        bw.append("\t}\n");
        bw.newLine();
        bw.append("\tpublic Set<Fields> getDirtyFields() {\n");
        bw.append("\t\treturn dirtyFields;\n");
        bw.append("\t}\n");
        bw.newLine();
        bw.append("\tpublic void resetDirty() {\n");
        bw.append("\t\tdirtyFields.clear();\n");
        bw.append("\t}\n");
        bw.newLine();
    }

    private void writeGettersSetters(DtoDef dtoDef, BufferedWriter bw) throws IOException {
        for (Field field: dtoDef.fields.values()) {
	        for (String annotation: field.getterAnnotations) {
		        bw.append("\t").append(annotation).append("\n");
	        }
            bw.append("\tpublic ").append(field.type);
            if ("boolean".equals(field.type) || "java.lang.Boolean".equals(field.type)) {
                bw.append(" is");
            } else {
                bw.append(" get");
            }
            bw.append(field.accessorName).append("() {\n");
            bw.append("\t\treturn ").append(field.fieldName).append(";\n");
            bw.append("\t}\n");
            bw.newLine();
            if (!field.excludeSetter) {
	            for (String annotation: field.setterAnnotations) {
	   		        bw.append("\t").append(annotation).append("\n");
	   	        }
                bw.append("\tpublic void set").append(field.accessorName).append("(").append(field.type).append(" ").append(field.fieldName).append(") {\n");
                if (field.primitive) {
                    bw.append("\t\tif (this.").append(field.fieldName).append(" != ").append(field.fieldName).append(") {\n");
                } else {
                    bw.append("\t\tif (!DtoUtil.safeEquals(this.").append(field.fieldName).append(", ").append(field.fieldName).append(")) {\n");
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
        List<Field> equalsFields = new LinkedList<>();
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

    private void writeCopyMethods(DtoDef dtoDef, BufferedWriter bw) throws IOException {
        for (Source source: dtoDef.sources) {
            writeCopyTo(source, dtoDef, bw);
            writeCopyFrom(source, dtoDef, bw);
        }
    }

    private void writeCopyTo(Source source, DtoDef dtoDef, BufferedWriter bw) throws IOException {
        bw.append("\t@GwtIncompatible\n");
        bw.append("\tpublic void copyTo(").append(source.type).append(" dest, Fields... fields) {\n");
	    bw.append("\t\tif (fields.length > 0) {\n");
	    bw.append("\t\t\tcopyTo(dest, Arrays.asList(fields));\n");
	    bw.append("\t\t} else {\n");
	    bw.append("\t\t\tcopyTo(dest, getDirtyFields());\n");
	    bw.append("\t\t}\n");
	    bw.append("\t}\n");
        bw.newLine();
        bw.append("\t@GwtIncompatible\n");
        bw.append("\tpublic void copyTo(").append(source.type).append(" dest, Iterable<Fields> fields) {\n");
        bw.append("\t\tfor (Fields field: fields) {\n");
        bw.append("\t\t\tswitch (field) {\n");
        for (String setter: source.setters) {
            Field field = dtoDef.fields.get(setter);
	        if (!field.readOnly) {
		        bw.append("\t\t\t\tcase ").append(field.enumName).append(":\n");
		        bw.append("\t\t\t\t\tdest.set").append(field.accessorName).append("(").append(field.fieldName).append(");\n");
		        bw.append("\t\t\t\t\tbreak;\n");
	        }
        }
        bw.append("\t\t\t}\n");
        bw.append("\t\t}\n");
        bw.append("\t}\n");
        bw.newLine();
    }

    private void writeCopyFrom(Source source, DtoDef dtoDef, BufferedWriter bw) throws IOException {
        bw.append("\t@GwtIncompatible\n");
        bw.append("\tpublic void copyFrom(").append(source.type).append(" source) {\n");
        for (String getter: source.getters) {
            Field field = dtoDef.fields.get(getter);
            bw.append("\t\t").append(field.fieldName).append(" = source.");
            if ("boolean".equals(field.type) || "java.lang.Boolean".equals(field.type)) {
                bw.append("is");
            } else {
                bw.append("get");
            }
            bw.append(field.accessorName).append("();\n");
        }

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
                    if (field.type.endsWith("DtoDef")) {
                        field.type = field.type.substring(0, field.type.length() - 3);
                    }
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
                        end = name.length();
                    }
                    field.fieldName = name.substring(start, end);

                    return field;
                }

            };

    private class DtoDef {
        String packageString;
        String name;
        String qualifiedName;
        LinkedList<Source> sources = new LinkedList<Source>();
        LinkedList<String> implementList = new LinkedList<>();
        String extend;
        LinkedHashMap<String, Field> fields = new LinkedHashMap<String, Field>();
    }

    private class Field {
        String type;
        String fieldName;
        String enumName;
        String accessorName;
        String importString;
        boolean primitive;
        boolean readOnly;
	    boolean excludeSetter;
        boolean ignoreDirty;
        boolean equalsHashCode;
        Class[] jsonExclude;
        Class[] jsonInclude;
	    List<String> fieldAnnotations = new LinkedList<>();
	    List<String> setterAnnotations = new LinkedList<>();
	    List<String> getterAnnotations = new LinkedList<>();


        public String toString() {
            return type + " " + fieldName;
        }
    }

    private class Source {
        String type;
        LinkedList<String> getters = new LinkedList<>();
        LinkedList<String> setters = new LinkedList<>();
    }
}
package org.reladev.quickdto.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.UnionType;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.SimpleTypeVisitor7;
import javax.tools.Diagnostic.Kind;

import com.sun.source.tree.MethodTree;
import com.sun.source.util.Trees;


public class FieldVisitor extends SimpleTypeVisitor7<Component, Element> {

    private ProcessingEnvironment processingEnv;
    private Trees trees;

    public FieldVisitor(ProcessingEnvironment processingEnv, Trees trees) {
        this.processingEnv = processingEnv;
        this.trees = trees;
    }

    @Override
    protected Component defaultAction(TypeMirror e, Element element) {
        if (element.toString().endsWith("Dto")) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process Dto:" + element + ".  Use DtoDef instead.");
        } else {
            processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process Action:" + element);
        }
        return super.defaultAction(e, element);
    }

    @Override
    public Component visitPrimitive(PrimitiveType t, Element element) {
        DtoField field = new DtoField();
        field.setType(t);
        field.setFieldName(element.toString());
        field.setPrimitive();
        return field;
    }

    @Override
    public Component visitArray(ArrayType t, Element element) {
        DtoField field = new DtoField();
        field.setType(t);
        field.setFieldName(element.toString());
        return field;
    }

    @Override
    public Component visitDeclared(DeclaredType t, Element element) {
        DtoField field = new DtoField();
        field.setType(t);
        field.setFieldName(element.toString());
        return field;
    }

    @Override
    public Component visitExecutable(ExecutableType t, Element element) {
        Component component = null;

        String name = element.toString();
        if ((name.startsWith("get") || name.startsWith("is")) && t.getReturnType().getKind() != TypeKind.VOID && t.getParameterTypes().size() == 0) {
            component = t.getReturnType().accept(this, element);

        } else if (name.startsWith("set") && t.getReturnType().getKind() == TypeKind.VOID && t.getParameterTypes().size() == 1) {
            component = t.getParameterTypes().get(0).accept(this, element);
        }

        if (component instanceof DtoField) {
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
            ((DtoField) component).setFieldName(name.substring(start, end));

        } else if (element.toString().startsWith("convert(") && t.getParameterTypes().size() == 1) {
            Method method = new Method();
            method.converter = true;
            method.toType = t.getReturnType().toString();
            method.fromType = t.getParameterTypes().get(0).toString();

            if (trees != null) {
                MethodScanner methodScanner = new MethodScanner();
                MethodTree methodTree = methodScanner.scan((ExecutableElement) element, trees);
                method.body = "\t" + methodTree.toString().replace("\n", "\n\t");
                method.isStatic = element.getModifiers().contains(Modifier.STATIC);

            } else if (element.getModifiers().contains(Modifier.STATIC)) {
                method.isStatic = true;

            } else {
                processingEnv.getMessager().printMessage(Kind.WARNING, "IGNORING (" + element + ") it must be 'static' or copyMethod true");
                method = null;
            }
            component = method;

        } else if (trees != null) {
            Method method = new Method();
            MethodScanner methodScanner = new MethodScanner();
            MethodTree methodTree = methodScanner.scan((ExecutableElement) element, trees);
            method.body = "\t" + methodTree.toString().replace("\n", "\n\t");

            component = method;

        } else {
            processingEnv.getMessager().printMessage(Kind.WARNING, "--IGNORING Method:" + element + " - If method should be copied, set \"copyMethod\" in @QuickDto");
        }

        return component;
    }

    @Override
    public Component visitTypeVariable(TypeVariable t, Element element) {
        processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process Type:" + element);
        return super.visitTypeVariable(t, element);
    }

    @Override
    public Component visitUnknown(TypeMirror t, Element element) {
        processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process Unknown:" + element);
        return super.visitUnknown(t, element);
    }

    @Override
    public Component visitUnion(UnionType t, Element element) {
        processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process Union:" + element);
        return super.visitUnion(t, element);
    }

    @Override
    public Component visitNull(NullType t, Element element) {
        processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process Null:" + element);
        return super.visitNull(t, element);
    }

    @Override
    public Component visitWildcard(WildcardType t, Element element) {
        processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process Wildcard:" + element);
        return super.visitWildcard(t, element);
    }

    @Override
    public Component visitNoType(NoType t, Element element) {
        processingEnv.getMessager().printMessage(Kind.ERROR, "Couldn't process NoType:" + element);
        return super.visitNoType(t, element);
    }
}

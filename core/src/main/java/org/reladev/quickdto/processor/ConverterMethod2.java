package org.reladev.quickdto.processor;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

public class ConverterMethod2 implements Component {
    private Type toType;
    private Type fromType;
    private Type classType;
    private boolean existingParam;

    public static ConverterMethod2 build(ExecutableElement execElement, Type classType) {
        if (!execElement.toString().startsWith("convert(")) {
            return null;
        }

        ExecutableType t = (ExecutableType) execElement.asType();
        boolean isValid = true;

        ConverterMethod2 converter = new ConverterMethod2();
        converter.classType = classType;
        converter.toType = new Type(t.getReturnType());

        List<? extends TypeMirror> parameterTypes = t.getParameterTypes();
        int numParams = parameterTypes.size();
        if (parameterTypes.isEmpty() || numParams > 2) {
            QuickDtoProcessor.processingEnv.getMessager().printMessage(Kind.WARNING, "IGNORING (" + execElement + ") invalid parameters to be converter.");
            isValid = false;

        } else {
            converter.fromType = new Type(parameterTypes.get(0));
            if (numParams > 1) {
                if (converter.toType.equals(new Type(parameterTypes.get(1)))) {
                    converter.existingParam = true;

                } else {
                    QuickDtoProcessor.processingEnv.getMessager().printMessage(Kind.WARNING, "IGNORING (" + execElement + ") invalid 2nd parameter to be converter.");
                    isValid = false;
                }
            }
        }

        if (!execElement.getModifiers().contains(Modifier.STATIC)) {
            QuickDtoProcessor.processingEnv.getMessager().printMessage(Kind.WARNING, "IGNORING (" + execElement + ") to be converter, must be static.");
            isValid = false;
        }

        return isValid ? converter : null;
    }

    public Type getToType() {
        return toType;
    }

    public Type getFromType() {
        return fromType;
    }

    public Type getClassType() {
        return classType;
    }

    public boolean isExistingParam() {
        return existingParam;
    }

    public String toString() {
        return fromType + "->" + toType;
    }
}

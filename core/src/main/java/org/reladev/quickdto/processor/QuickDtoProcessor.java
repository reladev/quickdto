package org.reladev.quickdto.processor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.shared.QuickDtoHelper;

@SupportedAnnotationTypes({"org.reladev.quickdto.shared.QuickDto", "org.reladev.quickdto.shared.QuickDtoHelper"})
public class QuickDtoProcessor extends AbstractProcessor {
    public static final String DefSuffix = "Def";
    public static final String HelperSuffix = "Helper";

    public static ProcessingEnvironment processingEnv;
    public static Messager messager;
    public static Type processingType;

    Set<Element> dtoElements;
    Set<Element> helperElements;
    int round = 0;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        processingEnv = env;
        messager = processingEnv.getMessager();
        super.init(processingEnv);
    }

    public static boolean isQuickDtoAnntoation(AnnotationMirror an) {
        return an.toString().startsWith("@org.reladev.quickdto");
    }


    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        messager.printMessage(Kind.NOTE, "Starting round:" + round);
        CodeGenerator generator = new CodeGenerator();

        if (dtoElements == null) {
            dtoElements = new HashSet<>();
            dtoElements.addAll(env.getElementsAnnotatedWith(QuickDto.class));
        } else {
            Set<Element> temp = dtoElements;
            dtoElements = new HashSet<>();
            for (Element e: temp) {
                TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(e.toString());
                dtoElements.add(typeElement);
            }
        }

        Iterator<? extends Element> dtoIt = dtoElements.iterator();
        while (dtoIt.hasNext()) {
            Element element = dtoIt.next();
            processingType = new Type(element.asType());
            try {
                ParsedDtoDef parsedDtoDef = new ParsedDtoDef((TypeElement) element);
                generator.writeDto(parsedDtoDef);
                dtoIt.remove();
            } catch (DelayParseException e) {
                messager.printMessage(Kind.NOTE, "*****Delaying(" + element + "):" + e.getMessage());
            }
        }

        if (helperElements == null) {
            helperElements = new HashSet<>();
            helperElements.addAll(env.getElementsAnnotatedWith(QuickDtoHelper.class));
        } else {
            Set<Element> temp = helperElements;
            helperElements = new HashSet<>();
            for (Element e: temp) {
                TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(e.toString());
                helperElements.add(typeElement);
            }
        }

        Iterator<? extends Element> helperIt = helperElements.iterator();
        while (helperIt.hasNext()) {
            Element element = helperIt.next();
            processingType = new Type(element.asType());
            try {
                ParsedHelperDef parsedHelperDef = new ParsedHelperDef((TypeElement) element);
                generator.writeHelper(parsedHelperDef);
                helperIt.remove();
            } catch (DelayParseException e) {
                messager.printMessage(Kind.NOTE, "*****Delaying(" + element + "):" + e.getMessage());
            }
        }

        round++;

        return true;
    }
}
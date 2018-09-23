package org.reladev.quickdto.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import org.reladev.quickdto.shared.QuickCopy;
import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.shared.QuickDtoConfiguration;

import static org.reladev.quickdto.processor.AnnotationUtil.parseClassNameList;

@SupportedAnnotationTypes({"org.reladev.quickdto.shared.QuickDto", "org.reladev.quickdto.shared.QuickCopy"})
public class QuickDtoProcessor extends AbstractProcessor {
    public static String DefSuffix = "Def";
    public static String CopySuffix = "CopyUtil";
    public static List<String> GlobalFeatureClassNames = new ArrayList<>();

    public static ProcessingEnvironment processingEnv;
    public static Messager messager;
    public static Type processingType;

    Set<Element> dtoElements;
    Set<Element> copyElements;
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
        //messager.printMessage(Kind.NOTE, "Starting round:" + round);
        CodeGenerator generator = new CodeGenerator();

        Set<? extends Element> configs = env.getElementsAnnotatedWith(QuickDtoConfiguration.class);
        if (configs.size() > 1) {
            messager.printMessage(Kind.ERROR, "More then one @QuickDtoConfiguration was found.  Only one is allowed.");

        } else if (configs.size() == 1) {
            Element config = configs.iterator().next();
            parseQuickDtoConfiguration((TypeElement) config);
        }


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
                if (!element.toString().endsWith(DefSuffix)) {
                    messager.printMessage(Kind.ERROR, "QuickDto (" + element + ") definition must end with '" + DefSuffix +
                          "'.  This can be changed with @QuickDtoConfiguration.");

                } else {
                    messager.printMessage(Kind.NOTE, "QuickDto:" + element);
                    ParsedDtoDef parsedDtoDef = new ParsedDtoDef((TypeElement) element);
                    generator.writeDto(parsedDtoDef);
                    dtoIt.remove();
                }
            } catch (DelayParseException ignore) {
                //messager.printMessage(Kind.NOTE, "*****Delaying(" + element + "):" + e.getMessage());
            }
        }

        if (copyElements == null) {
            copyElements = new HashSet<>();
            copyElements.addAll(env.getElementsAnnotatedWith(QuickCopy.class));
        } else {
            Set<Element> temp = copyElements;
            copyElements = new HashSet<>();
            for (Element e: temp) {
                TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(e.toString());
                copyElements.add(typeElement);
            }
        }

        Iterator<? extends Element> copyIt = copyElements.iterator();
        while (copyIt.hasNext()) {
            Element element = copyIt.next();
            processingType = new Type(element.asType());
            try {
                messager.printMessage(Kind.NOTE, "QuickCopy:" + element);
                ParsedCopyDef parsedCopyDef = new ParsedCopyDef((TypeElement) element);
                generator.writeCopyUtil(parsedCopyDef);
                copyIt.remove();
            } catch (DelayParseException ignore) {
                //messager.printMessage(Kind.NOTE, "*****Delaying(" + element + "):" + e.getMessage());
            }
        }

        round++;

        return true;
    }

    private void parseQuickDtoConfiguration(TypeElement element) {
        final String annotationName = QuickDtoConfiguration.class.getName();
        element.getAnnotationMirrors();
        for (AnnotationMirror am: element.getAnnotationMirrors()) {
            AnnotationValue action;
            if (annotationName.equals(am.getAnnotationType().toString())) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry: am.getElementValues().entrySet()) {
                    if ("quickDtoDefSuffix".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        DefSuffix = (String) action.getValue();

                    } else if ("quickCopySuffix".equals(entry.getKey().getSimpleName().toString())) {
                        action = entry.getValue();
                        DefSuffix = (String) action.getValue();

                    } else if ("globalFeatures".equals(entry.getKey().getSimpleName().toString())) {
                        GlobalFeatureClassNames = parseClassNameList(entry.getValue());
                    }
                }
            }
        }
    }

}
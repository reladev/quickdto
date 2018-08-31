package org.reladev.quickdto.processor;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.reladev.quickdto.shared.QuickDto;
import org.reladev.quickdto.shared.QuickDtoHelper;

@SupportedAnnotationTypes({"org.reladev.quickdto.shared.QuickDto", "org.reladev.quickdto.shared.QuickDtoHelper"})
public class QuickDtoProcessor extends AbstractProcessor {
    public static final String DefSuffix = "Def";
    public static final String HelperSuffix = "Helper";

    public static ProcessingEnvironment processingEnv;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        //noinspection AccessStaticViaInstance
        this.processingEnv = processingEnv;
        super.init(processingEnv);
    }

    public static boolean isQuickDtoAnntoation(AnnotationMirror an) {
        return an.toString().startsWith("@org.reladev.quickdto");
    }


    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        CodeGenerator generator = new CodeGenerator();
        for (Element element : env.getElementsAnnotatedWith(QuickDto.class)) {
            ParsedDtoDef parsedDtoDef = new ParsedDtoDef((TypeElement) element);
            generator.writeDto(parsedDtoDef);
        }

        for (Element element : env.getElementsAnnotatedWith(QuickDtoHelper.class)) {
            ParsedHelperDef parsedHelperDef = new ParsedHelperDef((TypeElement) element);
            generator.writeHelper(parsedHelperDef);
        }

        return true;
    }
}
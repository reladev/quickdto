package org.reladev.quickdto;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.matthewtamlin.avatar.rules.AvatarRule;
import org.junit.Before;
import org.junit.Rule;
import org.reladev.quickdto.processor.QuickDtoProcessor;

public class QuickDtoTest {

    public String[] testClasses = {"src/test/java/org/reladev/quickdto/test_dto/AnnotationDtoDef.java",
          "src/test/java/org/reladev/quickdto/test_classes/AnnotationImpl.java",
          "src/test/java/org/reladev/quickdto/test_dto/BasicConvertDtoDef.java",
          "src/test/java/org/reladev/quickdto/test_classes/BasicConvertImpl.java",
          "src/test/java/org/reladev/quickdto/test_dto/BasicTypesDtoDef.java",
          "src/test/java/org/reladev/quickdto/test_classes/BasicTypesImpl.java",
          "src/test/java/org/reladev/quickdto/test_classes/GenericsBaseImpl.java",
          "src/test/java/org/reladev/quickdto/test_classes/GenericsImpl.java", "src/test/java/org/reladev/quickdto/test_dto/MappingErrorsDtoDef.java",
          "src/test/java/org/reladev/quickdto/test_classes/MappingErrorsImpl.java", "src/test/java/org/reladev/quickdto/test_dto/SameNameCopy.java",
          "src/test/java/org/reladev/quickdto/test_classes/SameNameCopy.java",
          "src/test/java/org/reladev/quickdto/test_dto/TestClassDtoDef.java",
          "src/test/java/org/reladev/quickdto/test_classes/TestClassImpl.java",};

    @Rule
    public final AvatarRule rule = AvatarRule.builder().withSourcesAt(testClasses).build();

    public TypeElement elementAnnotationDtoDef;
    public TypeElement elementAnnotationImpl;
    public TypeElement elementBasicConvertDtoDef;
    public TypeElement elementBasicConvertImpl;
    public TypeElement elementBasicTypesDtoDef;
    public TypeElement elementBasicTypesImpl;
    public TypeElement elementGenericsBaseImpl;
    public TypeElement elementGenericsImpl;
    public TypeElement elementMappingErrorsDtoDef;
    public TypeElement elementMappingErrorsImpl;
    public TypeElement elementSameNameDto;
    public TypeElement elementSameNameTest;
    public TypeElement elementTestClassDtoDef;
    public TypeElement elementTestClassImpl;

    @Before
    public void setupUtils() {
        QuickDtoProcessor.processingEnv = rule.getProcessingEnvironment();
        QuickDtoProcessor.messager = rule.getMessager();

        Set<Element> elements = rule.getRootElements();
        for (Element element : elements) {
            switch (element.toString()) {
                case "org.reladev.quickdto.test_dto.AnnotationDtoDef":
                    elementAnnotationDtoDef = (TypeElement) element;
                    break;
                case "org.reladev.quickdto.test_classes.AnnotationImpl":
                    elementAnnotationImpl = (TypeElement) element;
                    break;
                case "org.reladev.quickdto.test_dto.BasicConvertDtoDef":
                    elementBasicConvertDtoDef = (TypeElement) element;
                    break;
                case "org.reladev.quickdto.test_classes.BasicConvertImpl":
                    elementBasicConvertImpl = (TypeElement) element;
                    break;
                case "org.reladev.quickdto.test_dto.BasicTypesDtoDef":
                    elementBasicTypesDtoDef = (TypeElement) element;
                    break;
                case "org.reladev.quickdto.test_classes.BasicTypesImpl":
                    elementBasicTypesImpl = (TypeElement) element;
                    break;
                case "org.reladev.quickdto.test_classes.GenericsBaseImpl":
                    elementGenericsBaseImpl = (TypeElement) element;
                    break;
                case "org.reladev.quickdto.test_classes.GenericsImpl":
                    elementGenericsImpl = (TypeElement) element;
                    break;
                case "org.reladev.quickdto.test_dto.MappingErrorsDtoDef":
                    elementMappingErrorsDtoDef = (TypeElement) element;
                    break;
                case "org.reladev.quickdto.test_classes.MappingErrorsImpl":
                    elementMappingErrorsImpl = (TypeElement) element;
                    break;
                case "org.reladev.quickdto.test_dto.SameNameCopy":
                    elementSameNameDto = (TypeElement) element;
                    break;
                case "org.reladev.quickdto.test_classes.SameNameCopy":
                    elementSameNameTest = (TypeElement) element;
                    break;
                case "org.reladev.quickdto.test_dto.TestClassDtoDef":
                    elementTestClassDtoDef = (TypeElement) element;
                    break;
                case "org.reladev.quickdto.test_classes.TestClassImpl":
                    elementTestClassImpl = (TypeElement) element;
                    break;
            }
        }
    }
}
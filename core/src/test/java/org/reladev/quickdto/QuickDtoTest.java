package org.reladev.quickdto;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.matthewtamlin.avatar.rules.AvatarRule;
import org.junit.Before;
import org.junit.Rule;
import org.reladev.quickdto.processor.QuickDtoProcessor;

public class QuickDtoTest {
    @Rule
    public final AvatarRule rule = AvatarRule.builder().withSourcesAt("src/test/java/org/reladev/quickdto/TestClassDtoDef.java", "src/test/java/org/reladev/quickdto/TestClassImpl.java").build();

    public TypeElement elementTestClassDtoDef;
    public TypeElement elementTestClassImpl;

    @Before
    public void setupUtils() {
        QuickDtoProcessor.processingEnv = rule.getProcessingEnvironment();

        Set<Element> elements = rule.getRootElements();
        for (Element element : elements) {
            switch (element.toString()) {
                case "org.reladev.quickdto.TestClassDtoDef":
                    elementTestClassDtoDef = (TypeElement) element;
                    break;
                case "org.reladev.quickdto.TestClassImpl":
                    elementTestClassImpl = (TypeElement) element;
                    break;
            }
        }
    }

}
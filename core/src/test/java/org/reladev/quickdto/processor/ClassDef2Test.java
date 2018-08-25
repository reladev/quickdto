package org.reladev.quickdto.processor;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.matthewtamlin.avatar.rules.AvatarRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ClassDef2Test {
    @Rule
    public final AvatarRule rule = AvatarRule.builder().withSourcesAt("src/test/java/org/reladev/quickdto/processor/ClassTesterDtoDef.java").build();

    @Before
    public void setupUtils() {
        QuickDtoProcessor.processingEnv = rule.getProcessingEnvironment();
    }

    @Test
    public void test() {
        // Contains the 'TestData' type element
        Set<Element> elements = rule.getRootElements();
        Element element = elements.iterator().next();

        ClassDef2 classDef = new ClassDef2((TypeElement) element);


        System.out.println();
    }

    @Test
    public void verifyTha() {

    }

}
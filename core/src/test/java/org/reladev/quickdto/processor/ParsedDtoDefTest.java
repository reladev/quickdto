package org.reladev.quickdto.processor;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.matthewtamlin.avatar.rules.AvatarRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ParsedDtoDefTest {
    @Rule
    public final AvatarRule rule = AvatarRule.builder().withSourcesAt("src/test/java/org/reladev/quickdto/processor/ClassTesterDtoDef.java").build();

    @Before
    public void setupUtils() {
        QuickDtoProcessor.processingEnv = rule.getProcessingEnvironment();
    }

    @Test
    public void verifyAnnotationParsedCorrectly() {
        Set<Element> elements = rule.getRootElements();
        Element element = elements.iterator().next();

        ParsedDtoDef parsedDtoDef = new ParsedDtoDef((TypeElement) element);

        System.out.println();

    }

}
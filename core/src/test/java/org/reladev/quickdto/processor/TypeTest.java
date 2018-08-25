package org.reladev.quickdto.processor;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;

import com.matthewtamlin.avatar.rules.AvatarRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeTest {
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

        Type type = new Type(element.asType());
        assertEquals("ClassTesterDtoDef", type.getName());
        assertEquals("org.reladev.quickdto.processor", type.getPackageString());
        assertEquals("org.reladev.quickdto.processor.ClassTesterDtoDef", type.getQualifiedName());
        assertTrue(type.isQuickDto());
        assertFalse(type.isPrimitive());


        TypeMirror typeMirror = element.asType();
        System.out.println(element + ":" + element.getKind() + "       " + typeMirror + ":" + typeMirror.getKind());

        for (Element subElement : element.getEnclosedElements()) {
            if ("primitive".equals(subElement.toString())) {
                type = new Type(subElement.asType());
                assertEquals("int", type.getName());
                assertEquals("", type.getPackageString());
                assertEquals("int", type.getQualifiedName());
                assertFalse(type.isQuickDto());
                assertTrue(type.isPrimitive());
            } else if ("boxed".equals(subElement.toString())) {
                type = new Type(subElement.asType());
                assertEquals("Integer", type.getName());
                assertEquals("java.lang", type.getPackageString());
                assertEquals("java.lang.Integer", type.getQualifiedName());
                assertFalse(type.isQuickDto());
                assertFalse(type.isPrimitive());
            } else if ("string".equals(subElement.toString())) {
                type = new Type(subElement.asType());
                assertEquals("String", type.getName());
                assertEquals("java.lang", type.getPackageString());
                assertEquals("java.lang.String", type.getQualifiedName());
                assertFalse(type.isQuickDto());
                assertFalse(type.isPrimitive());
            } else if ("strings".equals(subElement.toString())) {
                type = new Type(subElement.asType());
                assertEquals("List", type.getName());
                assertEquals("java.util", type.getPackageString());
                assertEquals("java.util.List", type.getQualifiedName());
                assertFalse(type.isQuickDto());
                assertFalse(type.isPrimitive());
            }
        }


        System.out.println();
    }

    @Test
    public void verifyTha() {

    }

}
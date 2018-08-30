package org.reladev.quickdto.processor;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.junit.Test;
import org.reladev.quickdto.QuickDtoTest;

import static org.junit.Assert.*;

@SuppressWarnings("Duplicates")
public class TypeTest extends QuickDtoTest {
    @Test
    public void verifyBasicConvertType() {
        TypeElement element = elementBasicConvertDtoDef;

        Type type = new Type(element.asType());
        assertEquals("BasicConvertDto", type.getName());
        assertEquals("org.reladev.quickdto.test_classes", type.getPackageString());
        assertEquals("org.reladev.quickdto.test_classes.BasicConvertDto", type.getQualifiedName());
        assertTrue(type.isQuickDto());
        assertFalse(type.isPrimitive());

        for (Element subElement: element.getEnclosedElements()) {
            if ("basic".equals(subElement.toString())) {
                type = new Type(subElement.asType());
                assertEquals("BasicTypesDto", type.getName());
                assertEquals("org.reladev.quickdto.test_classes", type.getPackageString());
                assertEquals("org.reladev.quickdto.test_classes.BasicTypesDto", type.getQualifiedName());
                assertTrue(type.isQuickDto());
                assertFalse(type.isPrimitive());
                assertFalse(type.isQuickDtoList());

            } else if ("basicList".equals(subElement.toString())) {
                type = new Type(subElement.asType());
                assertEquals("List", type.getName());
                assertEquals("java.util", type.getPackageString());
                assertEquals("java.util.List", type.getQualifiedName());
                assertFalse(type.isQuickDto());
                assertFalse(type.isPrimitive());
                assertTrue(type.isQuickDtoList());
                Type listType = type.getListType();
                assertEquals("BasicTypesDto", listType.getName());
                assertEquals("org.reladev.quickdto.test_classes", listType.getPackageString());
                assertEquals("org.reladev.quickdto.test_classes.BasicTypesDto", listType.getQualifiedName());
                assertTrue(listType.isQuickDto());
                assertFalse(listType.isPrimitive());
                assertFalse(listType.isQuickDtoList());
            }
        }
    }

    @Test
    public void verifyTestClassDtoDefType() {
        TypeElement element = elementTestClassDtoDef;

        Type type = new Type(element.asType());
        assertEquals("TestClassDto", type.getName());
        assertEquals("org.reladev.quickdto.test_classes", type.getPackageString());
        assertEquals("org.reladev.quickdto.test_classes.TestClassDto", type.getQualifiedName());
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
    }

    @Test
    public void verifyClassTesterImplType() {
        TypeElement element = elementTestClassImpl;

        Type type = new Type(element.asType());
        assertEquals("TestClassImpl", type.getName());
        assertEquals("org.reladev.quickdto.test_classes", type.getPackageString());
        assertEquals("org.reladev.quickdto.test_classes.TestClassImpl", type.getQualifiedName());
        assertFalse(type.isQuickDto());
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

}
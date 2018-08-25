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
    public void verifyTestClassDtoDefType() {
        TypeElement element = elementTestClassDtoDef;

        Type type = new Type(element.asType());
        assertEquals("TestClassDto", type.getName());
        assertEquals("org.reladev.quickdto", type.getPackageString());
        assertEquals("org.reladev.quickdto.TestClassDto", type.getQualifiedName());
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
        assertEquals("org.reladev.quickdto", type.getPackageString());
        assertEquals("org.reladev.quickdto.TestClassImpl", type.getQualifiedName());
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
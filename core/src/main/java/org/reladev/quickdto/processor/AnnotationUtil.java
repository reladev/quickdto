package org.reladev.quickdto.processor;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationValue;

public class AnnotationUtil {
    static public String parseClassName(AnnotationValue action) {
        Object param = action.getValue();
        return annotationParamToClassName(param);
    }

    static public List<String> parseClassNameList(AnnotationValue action) {
        ArrayList<String> classNames = new ArrayList<>();
        List params = (List) action.getValue();
        for (Object param: params) {
            String className = annotationParamToClassName(param);
            classNames.add(className);
        }
        return classNames;
    }

    static public String annotationParamToClassName(Object source) {
        String className = source.toString();
        className = className.substring(0, className.length() - 6);
        return className;
    }

}

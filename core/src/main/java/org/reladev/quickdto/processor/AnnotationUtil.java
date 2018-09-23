package org.reladev.quickdto.processor;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationValue;
import javax.tools.Diagnostic.Kind;

import org.reladev.quickdto.feature.QuickDtoFeature;

import static org.reladev.quickdto.processor.QuickDtoProcessor.messager;

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

    static public List<QuickDtoFeature> createFeatures(List<String> featureClassNames) {
        List<QuickDtoFeature> features = new ArrayList<>();
        for (String className: QuickDtoProcessor.GlobalFeatureClassNames) {
            createFeature(features, className);
        }
        for (String className: featureClassNames) {
            createFeature(features, className);
        }
        return features;
    }

    static private void createFeature(List<QuickDtoFeature> features, String className) {
        try {
            Class featureClass = Class.forName(className);
            QuickDtoFeature feature = (QuickDtoFeature) featureClass.newInstance();
            if (!features.contains(feature)) {
                features.add(feature);
            }
        } catch (Exception e) {
            messager.printMessage(Kind.WARNING, "Can't create feature:" + className);
        }
    }


}

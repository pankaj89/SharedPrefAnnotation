package com.example;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes("com.example.PreferenceProcessor")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class PreferenceProcessor extends AbstractProcessor {

    private static final String METHOD_PREFIX = "start";
    private static final ClassName classIntent = ClassName.get("android.content", "Intent");
    private static final ClassName classContext = ClassName.get("android.content", "Context");
    private static final ClassName preferenceContext = ClassName.get("android.content", "SharedPreferences");

    private Filer filer;
    private Messager messager;
    private Elements elements;
    private Map<String, String> activitiesWithPackage;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        elements = processingEnvironment.getElementUtils();
        activitiesWithPackage = new HashMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        try {
            /**
             * 1- Find all annotated element
             */


            String packageName;
            for (Element element : roundEnvironment.getElementsAnnotatedWith(Preference.class)) {

                if (element.getKind() != ElementKind.CLASS) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Can be applied to class.");
                    return true;
                }

                Preference anotation = element.getAnnotation(Preference.class);
                String PREF_NAME = anotation.name();
                String activityName = element.getSimpleName().toString();
                packageName = elements.getPackageOf(element).getQualifiedName().toString();
                String generatedActivityName = activityName + "Pref";

                TypeSpec.Builder navigatorClass = TypeSpec
                        .classBuilder(generatedActivityName)
                        .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

                for (Element fields : roundEnvironment.getElementsAnnotatedWith(Item.class)) {

                    if (fields.getKind() != ElementKind.FIELD) {
                        messager.printMessage(Diagnostic.Kind.ERROR, "Can be applied to class.");
                        return true;
                    }
                    if (!isFieldSupported(fields)) {
                        messager.printMessage(Diagnostic.Kind.ERROR, "@Item only be applied to String, int, boolean, long, float, so skipping item " + fields);
                        continue;
                    }

                    String returnTypeSimple;
                    String returnTypeString = fields.asType().toString();
                    String type[] = returnTypeString.split("\\.");
                    returnTypeSimple = type[type.length - 1];

                    TypeName returnType = guessClassName(fields);

//                    ClassName activityClass = ClassName.get(packageName, activityName);

//                    preference.getString(key, value)

                    MethodSpec getMethodSpec = MethodSpec
                            .methodBuilder("get" + firstCaps(fields.toString()))
                            .addModifiers(Modifier.PUBLIC)
                            .returns(returnType)
                            .addStatement("return " + "preference.get" + firstCaps(returnTypeSimple) + "(\"" + fields.toString() + "\", " + getDefaultValue(fields) + ")")
                            .build();

                    MethodSpec setMethodSpec = MethodSpec
                            .methodBuilder("set" + firstCaps(fields.toString()))
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(returnType, fields.toString())
                            .returns(TypeName.VOID)
                            .addStatement("this." + fields.toString() + "=" + fields.toString())
                            .addStatement("preference.edit().put" + firstCaps(returnTypeSimple) + "(\"" + fields.toString() + "\", " + fields.toString() + ").commit()")
                            .build();

                    navigatorClass
                            .addField(returnType, fields.toString(), Modifier.PRIVATE)
                            .addMethod(getMethodSpec)
                            .addMethod(setMethodSpec);

                }


                MethodSpec constructorMethodSpec = MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .addParameter(classContext, "context")
                        .addStatement("preference = context.getSharedPreferences(\"" + PREF_NAME + "\", Context.MODE_PRIVATE)")
                        .build();

                MethodSpec getInstanceMethodSpec = MethodSpec
                        .methodBuilder("getInstance")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .returns(ClassName.get(packageName, generatedActivityName))
                        .addParameter(classContext, "context")
                        .addCode("if(instance==null)")
                        .addCode("\n")
                        .addStatement("\tinstance=new " + generatedActivityName + "(context)")
                        .addStatement("return instance")
                        .build();


                navigatorClass
                        .addField(preferenceContext, "preference", Modifier.PRIVATE)
                        .addField(ClassName.get(packageName, generatedActivityName), "instance", Modifier.PRIVATE, Modifier.STATIC)
                        .addMethod(constructorMethodSpec)
                        .addMethod(getInstanceMethodSpec);

                JavaFile.builder(packageName, navigatorClass.build()).build().writeTo(filer);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean isFieldSupported(Element field) {
        if (!field.asType().getKind().isPrimitive()) {
            return field.asType().toString().endsWith("String");
        }
        switch (field.asType().getKind()) {
            case INT:

            case LONG:

            case FLOAT:

            case BOOLEAN:
                return true;
        }
        return false;
    }

    private TypeName guessClassName(Element field) {
        if (!field.asType().getKind().isPrimitive()) {
            return ClassName.bestGuess(field.asType().toString());
        }
        switch (field.asType().getKind()) {
            case INT:
                return TypeName.get(int.class);
            case LONG:
                return TypeName.get(long.class);
            case FLOAT:
                return TypeName.get(float.class);
            case BOOLEAN:
                return TypeName.get(boolean.class);
        }
        return ClassName.bestGuess(field.asType().toString());
    }

    private Object getDefaultValue(Element field) {
        if (!field.asType().getKind().isPrimitive()) {
            return "\"\"";
        }
        switch (field.asType().getKind()) {
            case INT:
                return -1;
            case LONG:
                return -1;
            case FLOAT:
                return -1;
            case BOOLEAN:
                return false;
        }
        return "\"\"";
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Preference.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public String firstCaps(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
    }
}
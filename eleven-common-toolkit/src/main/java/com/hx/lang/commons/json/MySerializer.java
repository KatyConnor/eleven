//package com.hx.lang.commons.json;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public abstract class MySerializer extends JsonSerializer<Object> {
//
//    @Override
//    public void serialize(Object obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
//        boolean switchState = CurrentThreadConfusedSwitch.getSwitchState();
//        jsonGenerator.writeStartObject();
//        try {
//            Class<?> aClass = obj.getClass();
//            List<Field> declaredFields = getDeclaredFields(aClass);
//            for (Field declaredField : declaredFields) {
//                declaredField.setAccessible(true);
//                Object oldValue = declaredField.get(obj);
//                if (oldValue != null && declaredField.isAnnotationPresent(Confusion.class)) {//this thread turn on the switch and with the @interface of Confusion
//                    Confusion confusion = declaredField.getDeclaredAnnotation(Confusion.class);
//                    Object result = null;
//                    ConfusedFieldTypeEnum type = confusion.type();
//                    if (type != null && type != ConfusedFieldTypeEnum.NOT_CONFUSED) {//type is not NOT_CONFUSED,then set the wrapped value to obj
//                        if (switchState) {//turning on, then set the opening-wrapped value to it.
//                            result = wrapperValueOnSwitchOpen(type, oldValue);
//                        } else {//turning off, then set the offing-wrapped value to it.
//                            result = wrapperValueOnSwitchOff(type, oldValue);
//                        }
////                        if (result != null) {
//                        jsonGenerator.writeObjectField(declaredField.getName(), result);
////                        } else {
////                            jsonGenerator.writeObjectField(declaredField.getName(), oldValue);
////                        }
//                    } else {
//                        jsonGenerator.writeObjectField(declaredField.getName(), result);
//                    }
//                } else {//without being marked by @interface Confusion, then set the source value to it.
//                    jsonGenerator.writeObjectField(declaredField.getName(), oldValue);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        jsonGenerator.writeEndObject();
//
//    }
//
//    private List<Field> getDeclaredFields(Class<?> aClass) {
//        List<Field> fieldList = new ArrayList<>();
//        Field[] declaredFields1 = aClass.getDeclaredFields();
//        fieldList.addAll(Arrays.asList(declaredFields1));
//        List<Class<?>> classes = getAllSuperclass(aClass);
//        if (classes != null) {
//            for (Class<?> clazz: classes) {
//                Field[] declaredFields = clazz.getDeclaredFields();
//                fieldList.addAll(Arrays.asList(declaredFields));
//            }
//        }
//
//        return fieldList;
//    }
//
//    private List<Class<?>> getAllSuperclass(Class<?> aClass) {
//        List<Class<?>> allClasses = new ArrayList<>();
//        Class<?> superclass;
//        while ((superclass = aClass.getSuperclass()) != null) {
//            allClasses.add(superclass);
//            aClass = superclass;
//        }
//        return allClasses;
//    }
//
//    private Field[] getDeclaredFieldsJustThisClass(Class<?> aClass) {
//        return aClass.getDeclaredFields();
//    }
//
//    public abstract Object wrapperValueOnSwitchOff(ConfusedFieldTypeEnum type, Object value);
//
//    public abstract Object wrapperValueOnSwitchOpen(ConfusedFieldTypeEnum type, Object value);
//}

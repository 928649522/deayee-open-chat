package com.superhao.app.entity.seri;

/**
 * @Auther: super
 * @Date: 2019/11/6 23:19
 * @email:
 */
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;

public class PayloadDecoder {

    public static <T extends Codecable> T resolve(byte[] src, Class<T> clazz) {
        T instance = null;
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("实例化类失败", e);
        }

        List<FieldWrapper> fieldWrapperList = instance.getFieldWrapperList();
        ByteBuf buffer = Unpooled.buffer().writeBytes(src);
        for (FieldWrapper fieldWrapper : fieldWrapperList) {
            fillData(fieldWrapper, instance, buffer);
        }

        return instance;
    }

    private static void fillData(FieldWrapper fieldWrapper, Object instance, ByteBuf buffer) {
        Field field = fieldWrapper.getField();
        field.setAccessible(true);
        String typeName = field.getType().getName();
        try {
            switch (typeName) {
                case "java.lang.Boolean":
                case "boolean":
                    boolean b = buffer.readBoolean();
                    field.set(instance, b);
                    break;

                case "java.lang.Character":
                case "char":
                    CharSequence charSequence = buffer.readCharSequence(fieldWrapper.getCodecProprety().length(), Charset.forName("UTF-8"));
                    field.set(instance, charSequence);
                    break;
                case "java.lang.Byte":
                case "byte":
                    byte b1 = buffer.readByte();
                    field.set(instance, b1);
                    break;
                case "java.lang.Short":
                case "short":
                    short readShort = buffer.readShort();
                    field.set(instance, readShort);
                    break;
                case "java.lang.Integer":
                case "int":
                    int readInt = buffer.readInt();
                    field.set(instance, readInt);
                    break;
                case "java.lang.Long":
                case "long":
                    long l = buffer.readLong();
                    field.set(instance, l);
                    break;
                case "java.lang.Float":
                case "float":
                    float readFloat = buffer.readFloat();
                    field.set(instance, readFloat);
                    break;
                case "java.lang.Double":
                case "double":
                    double readDouble = buffer.readDouble();
                    field.set(instance, readDouble);
                    break;
                case "java.lang.String":
                    String readString = buffer.readCharSequence(fieldWrapper.getCodecProprety().length(), Charset.forName("UTF-8")).toString();
                    field.set(instance, readString);
                    break;
                default:
                    throw new RuntimeException(typeName + "不支持，bug");
            }
        } catch (Exception e) {
            throw new RuntimeException(typeName + "读取失败，field:" + field.getName(), e);
        }
    }


}
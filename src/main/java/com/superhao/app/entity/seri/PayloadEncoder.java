package com.superhao.app.entity.seri;

/**
 * @Auther: super
 * @Date: 2019/11/6 23:16
 * @email:
 */
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;

public class PayloadEncoder {

    public static <T extends Codecable> byte[] getPayload(T command) {
        List<FieldWrapper> fieldWrapperList = command.getFieldWrapperList();
        ByteBuf buffer = Unpooled.buffer();
        fieldWrapperList.forEach(fieldWrapper -> write2ByteBuf(fieldWrapper, command, buffer));
        return buffer.array();
    }

    /**
     * 数据写入到ByteBuf
     *
     * @param fieldWrapper
     * @param instance
     * @param buffer
     */
    private static void write2ByteBuf(FieldWrapper fieldWrapper, Object instance, ByteBuf buffer) {
        Field field = fieldWrapper.getField();
        String typeName = field.getType().getName();
        field.setAccessible(true);
        Object value = null;
        try {
            value = field.get(instance);
        } catch (IllegalAccessException e) {
            new RuntimeException("反射获取值失败，filed:" + field.getName(), e);
        }
        switch (typeName) {
            case "java.lang.Boolean":
            case "boolean":
                buffer.writeBoolean((Boolean) value);
                break;
            case "java.lang.Character":
            case "char":
                buffer.writeCharSequence((CharSequence) value, Charset.forName("UTF-8"));
                break;
            case "java.lang.Byte":
            case "byte":
                buffer.writeByte((byte) value);
                break;
            case "java.lang.Short":
            case "short":
                buffer.writeShort((short) value);
                break;
            case "java.lang.Integer":
            case "int":
                buffer.writeInt((int) value);
                break;
            case "java.lang.Long":
            case "long":
                buffer.writeLong((long) value);
                break;
            case "java.lang.Float":
            case "float":
                buffer.writeFloat((float) value);
                break;
            case "java.lang.Double":
            case "double":
                buffer.writeDouble((double) value);
                break;
            case "java.lang.String":
                buffer.writeCharSequence((CharSequence) value, Charset.forName("UTF-8"));
                break;
            default:
                throw new RuntimeException(typeName + "不支持，bug");
        }
    }
}
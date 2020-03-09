package com.transsnet.utils;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * @author yinqi
 * @date 2019/9/19
 */
public class MongoUtils {
    private static final Log logger = LogFactory.getLog(MongoUtils.class);
    /**
     *
     */
    public static <T> DBObject beanToDBObject(T t) {
        Gson gson = new Gson();
        String json =gson.toJson(t);
        return (DBObject) JSON.parse(json);
    }

    /**
     * 将实体Bean对象转换成DBObject
     *
     */
    public static <T> DBObject bean2DBObject(T bean)
            throws IllegalArgumentException, IllegalAccessException {
        if (bean == null)
            return null;
        DBObject dbObject = new BasicDBObject();
        // 获取对象类的属性域
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 获取变量的属性名
            String varName = field.getName();
            // 修改访问控制权限
            boolean accessFlag = field.isAccessible();
            if (!accessFlag) {
                field.setAccessible(true);
            }
            Object param = field.get(bean);
            if (param == null) {
                continue;
            } else if (param instanceof Integer) {
                // 判断变量的类型
                int value = ((Integer) param).intValue();
                dbObject.put(varName, value);
            } else if (param instanceof String) {
                String value = (String) param;
                dbObject.put(varName, value);
            } else if (param instanceof Double) {
                double value = ((Double) param).doubleValue();
                dbObject.put(varName, value);
            } else if (param instanceof Float) {
                float value = ((Float) param).floatValue();
                dbObject.put(varName, value);
            } else if (param instanceof Long) {
                long value = ((Long) param).longValue();
                dbObject.put(varName, value);
            } else if (param instanceof Boolean) {
                boolean value = ((Boolean) param).booleanValue();
                dbObject.put(varName, value);
            } else if (param instanceof Date) {
                Date value = (Date) param;
                dbObject.put(varName, value);
            }
            // 恢复访问控制权限
            field.setAccessible(accessFlag);
        }
        return dbObject;
    }


}

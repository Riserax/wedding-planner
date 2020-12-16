package pl.com.weddingPlanner.view.util;

import java.lang.reflect.Field;

import pl.com.weddingPlanner.util.Logger;

public class ResourceUtil {

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            Logger.logToDevice(ResourceUtil.class.getName(), e);
            return -1;
        }
    }
}

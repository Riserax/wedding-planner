package pl.com.weddingPlanner.view.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

import pl.com.weddingPlanner.util.Logger;

public class ResourceUtil {

    public static final String IC_ADD_BOX = "ic_add_box";

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField;

            if (StringUtils.isNotBlank(resName)) {
                idField = c.getDeclaredField(resName);
            } else {
                idField = c.getDeclaredField(IC_ADD_BOX);
            }

            return idField.getInt(idField);
        } catch (Exception e) {
            Logger.logToDevice(ResourceUtil.class.getName(), e);
            return -1;
        }
    }
}

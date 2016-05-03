package api.helpers;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Created by Mindaugas on 22/04/2016.
 */
public class Parser {

    public static<T> T fromQueryString(String qs, Class<T> entityClass){
        Object entity = null;

        try {
            Map<String, Object> map = queryStringToMap(qs);
            entity = entityClass.newInstance();

            for(Field field : entityClass.getFields()){
                if(map.containsKey(field.getName().toLowerCase())){
                    Object value = map.get(field.getName().toLowerCase());
                    switch (field.getType().getTypeName()){
                        case "int" :
                            value = Integer.parseInt((String) value);
                            break;
                    }

                    field.set(entity, value);
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return (T)entity;
    }

    public static Map<String,Object> queryStringToMap(String qs) {
        String qsParsed = "";
        try {
            if(qs != null){
                qsParsed = java.net.URLDecoder.decode(qs, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String,Object> map = new HashMap<>();

        for (String param : qsParsed.split("&")){
            String[] paramData= param.split("=");

            if(paramData.length == 2){
                map.put(paramData[0].toLowerCase(),paramData[1]);
            }
        }

        return map;
    }
}

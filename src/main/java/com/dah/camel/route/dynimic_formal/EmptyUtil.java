package com.dah.camel.route.dynimic_formal;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmptyUtil {
    private EmptyUtil() { }

    public static boolean isEmptyList(List<?> list) {
        return (null == list || list.size() == 0);
    }


    public static boolean isNotEmptyList(List<?> list)
    {
        return !isEmptyList(list);
    }


    public static boolean isEmptyMap(Map<?, ?> map) {
        return (null == map || map.size() == 0);
    }


    public static boolean isNotEmptyMap(Map<?, ?> map) {
        return !isEmptyMap(map);
    }


    public static boolean isEmptySet(Set<?> set)
    {
        return (null == set || set.size() == 0);
    }


    public static boolean isNotEmptySet(Set<?> set) {
        return !isEmptySet(set);
    }


    public static boolean isEmptyArray(Object[] objs) {
        return (null == objs || objs.length == 0);
    }


    public static boolean isNotEmptyArray(Object[] objs) {
        return !isEmptyArray(objs);
    }
}

package oms.pc_protector.util;


import java.util.HashMap;

public class CamelListMap extends HashMap { // Mybatis에서 반환값을 hashMap으로 반환 할 경우 camelCase 형태로 전환해준다.

    private String toProperCase(String s, boolean isCapital) {

        String rtnValue = "";

        if (isCapital) {
            rtnValue = s.substring(0, 1).toUpperCase() +
                    s.substring(1).toLowerCase();
        } else {
            rtnValue = s.toLowerCase();
        }
        return rtnValue;
    }

    private String toCamelCase(String s) {
        String[] parts = s.split("_");
        StringBuilder camelCaseString = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            camelCaseString.append(toProperCase(part, (i != 0 ? true : false)));
        }

        return camelCaseString.toString();
    }

    @Override
    public Object put(Object key, Object value) {
        return super.put(toCamelCase((String) key), value.toString());

    }

}

package com.geetion.puputuan.supervene.recommend.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 16/4/27.
 */
public class AnalyseMatch {
    public static Map<String, String> constellatoryMap = new HashMap<>();
    public static Map<String, Float> numberMap = new HashMap<>();

    static {
        init();
    }

    public static void init() {
        constellatoryMap.put("双鱼座", "巨蟹座天蝎座双鱼座");
        constellatoryMap.put("水瓶座", "白羊座双子座天秤座");
        constellatoryMap.put("魔蝎座", "金牛座处女座魔蝎座");
        constellatoryMap.put("射手座", "白羊座狮子座射手座");
        constellatoryMap.put("天蝎座", "巨蟹座天秤座双鱼座");
        constellatoryMap.put("天秤座", "双子座天秤座水瓶座");
        constellatoryMap.put("处女座", "金牛座处女座魔蝎座");
        constellatoryMap.put("狮子座", "白羊座天秤座射手座");
        constellatoryMap.put("巨蟹座", "巨蟹座天蝎座双鱼座");
        constellatoryMap.put("双子座", "双子座天秤座水瓶座");
        constellatoryMap.put("金牛座", "巨蟹座处女座魔蝎座");
        constellatoryMap.put("白羊座", "白羊座狮子座射手座");

        //初始化人数map
        numberMap.put("22", 1.0f);
        numberMap.put("23", 0.44f);
        numberMap.put("24", 0.20f);
        numberMap.put("25", 0.10f);

        numberMap.put("32", 2.25f);
        numberMap.put("33", 1.0f);
        numberMap.put("34", 0.56f);
        numberMap.put("35", 0.30f);

        numberMap.put("42", 3.5f);
        numberMap.put("43", 1.78f);
        numberMap.put("44", 1.0f);
        numberMap.put("45", 0.64f);

        numberMap.put("52", 4.0f);
        numberMap.put("53", 2.2f);
        numberMap.put("54", 1.56f);
        numberMap.put("55", 1.0f);
    }

    public static boolean getConstellatoryMatch(String constellatory, String matchConstellatory) {
        if (constellatory == null || matchConstellatory == null)
            return false;
        if (constellatoryMap == null)
            init();
        String result = constellatoryMap.get(constellatory);

        if (result == null)
            return false;
        return constellatoryMap.get(constellatory).contains(matchConstellatory);
    }

    public static float getNumberMatch(String tag) {
        if (numberMap == null)
            init();
        Float number = numberMap.get(tag);
        if (number == null)
            number = 0.0f;
        return number;
    }
}

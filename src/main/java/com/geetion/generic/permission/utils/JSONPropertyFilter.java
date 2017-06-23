package com.geetion.generic.permission.utils;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by virgilyan on 12/24/14.
 */
public class JSONPropertyFilter implements PropertyPreFilter {

    private final Class<?> clazz;
    private final Set<String> includes;
    private final Set<String> excludes;

    public JSONPropertyFilter(String... properties) {
        this(null, properties);
    }

    public JSONPropertyFilter(List<String> properties) {
        this(null, properties);
    }

    public JSONPropertyFilter(Class<?> clazz, String... properties) {
        this.includes = new HashSet();
        this.excludes = new HashSet();
        this.clazz = clazz;
        String[] str = properties;
        int length = properties.length;

        for(int i = 0; i < length; ++i) {
            String item = str[i];
            if(item != null) {
                this.excludes.add(item);
            }
        }

    }

    public JSONPropertyFilter(Class<?> clazz, List<String> properties) {
        this.includes = new HashSet();
        this.excludes = new HashSet();
        this.clazz = clazz;
        int length = properties.size();

        for(int i = 0; i < length; ++i) {
            String item = properties.get(i);
            if(item != null) {
                this.excludes.add(item);
            }
        }

    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public Set<String> getIncludes() {
        return this.includes;
    }

    public Set<String> getExcludes() {
        return this.excludes;
    }

    public boolean apply(JSONSerializer serializer, Object source, String name) {
        return source == null?true:(this.clazz != null && !this.clazz.isInstance(source)?true:(this.excludes.contains(name)?false:this.includes.size() == 0 || this.includes.contains(name)));
    }
}

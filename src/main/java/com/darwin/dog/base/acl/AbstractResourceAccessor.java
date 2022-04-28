package com.darwin.dog.base.acl;


import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;


import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

public abstract class AbstractResourceAccessor implements ResourceAccessor {

    private boolean isArray(Serializable resource){
        return resource.getClass().isArray();
    }

    protected boolean isMultiple(Serializable resource){
        return  isArray(resource)
                || ClassUtils.isAssignableValue(Collection.class,resource)
                || ClassUtils.isAssignableValue(Map.class,resource);
    }

    private <T> Set<T> arrayToSet(Serializable resource,Class<T> tClass){
        Set<T> set = new HashSet<>();
        int length = Array.getLength(resource);
        for (int i = 0; i < length; i++) {
            Object o = Array.get(resource, i);
            Assert.isAssignable(tClass,o.getClass(),
                    String.format("resource %s 的 %s 不能转化为 %s",resource,o,tClass));
            set.add((T)o);
        }
        return set;
    }

    private <T> Set<T> collectionToSet(Serializable resource,Class<T> tClass){
        Assert.isAssignable(tClass,CollectionUtils.findCommonElementType((Collection<?>) resource)
                ,String.format("resource %s componentType 不能完全转化为 %s",resource,tClass));
        return new HashSet<>((Collection<? extends T>) resource);
    }



    protected <T> Set<T> multipleToSet(Serializable resource,Class<T> tClass){
        if (isArray(resource)){
            return arrayToSet(resource,tClass);
        }
        return collectionToSet(resource, tClass);
    }

}

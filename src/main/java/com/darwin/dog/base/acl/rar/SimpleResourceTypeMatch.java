package com.darwin.dog.base.acl.rar;

import com.google.common.collect.Sets;

import java.util.Set;


public class SimpleResourceTypeMatch implements ResourceTypeMatch {
    Set<String> resourceTypeNames;

    public SimpleResourceTypeMatch(String...resourceTypeNames){
        this.resourceTypeNames = Sets.newHashSet(resourceTypeNames);
    }

    @Override
    public boolean match(String resourceType) {
        return resourceTypeNames.contains(resourceType);
    }
}

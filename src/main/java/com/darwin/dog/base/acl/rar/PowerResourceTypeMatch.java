package com.darwin.dog.base.acl.rar;



import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PowerResourceTypeMatch implements ResourceTypeMatch {
    List<Pattern> patterns ;
    public PowerResourceTypeMatch(String...patterns){
        this.patterns = Arrays.stream(patterns)
                .map(Pattern::compile)
                .collect(Collectors.toList());
    }
    @Override
    public boolean match(String resourceType) {
        return patterns.stream()
                .map(p->p.matcher(resourceType))
                .anyMatch(Matcher::matches);
    }
}

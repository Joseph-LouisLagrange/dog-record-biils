package com.darwin.dog.util;

import com.google.gson.Gson;
import org.apache.tika.Tika;

public class GlobalStaticBean {
    public static final Gson GSON = new Gson();
    public static final Tika TIKA = new Tika();
}

package com.darwin.dog.util;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.nio.charset.Charset;

public class ResolveUtil {
    public static MimeType parseMimeTypeFromContentType(String contentType) throws MimeTypeParseException {
        if (StringUtils.isEmpty(contentType)){
            return null;
        }
        return new MimeType(StringUtils.substringBefore(contentType,";"));
    }

    public static Charset parseCharsetFromContentType(String contentType){
        if (StringUtils.isEmpty(contentType)){
            return null;
        }
        String charsetName = StringUtils.substringAfter(contentType,"charset=");
        if (StringUtils.isEmpty(charsetName)){
            return null;
        }
        return Charset.forName(charsetName);
    }
}

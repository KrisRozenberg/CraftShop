package org.rozenberg.craftshop.util;

import org.apache.commons.codec.digest.DigestUtils;

public class PasswordEncoder {

    public static String encode(String password){
        return DigestUtils.md5Hex(password);
    }
}

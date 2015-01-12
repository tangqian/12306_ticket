package com.free.app.ticket.util;

import java.io.UnsupportedEncodingException;

public class DynamicJsUtil {
    
    private static String keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    
    static class Base32 {
        
        private static int delta = 0x9E3779B8;
        
        public static String longArrayToString(int[] data, boolean includeLength) {
            int length = data.length;
            int n = (length - 1) << 2;
            if (includeLength) {
                int m = data[length - 1];
                if ((m < n - 3) || (m > n))
                    return null;
                n = m;
            }
            //System.out.println(ArrayUtils.toString(data));
            
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < length; i++) {
                int i0 = data[i] & 0xff;
                int i8 = data[i] >>> 8 & 0xff;
                int i16 = data[i] >>> 16 & 0xff;
                int i24 = data[i] >>> 24 & 0xff;
                if (i0 != 0)
                    sb.append((char)i0);
                if (i8 != 0)
                    sb.append((char)i8);
                if (i16 != 0)
                    sb.append((char)i16);
                if (i24 != 0)
                    sb.append((char)i24);
            }
            
            String result;
            if (includeLength) {
                result = sb.substring(0, n);
            }
            else
                result = sb.toString();
            return result;
        }
        
        public static int[] stringToLongArray(String str, boolean includeLength) {
            int length = str.length();
            int arrsize = length % 4 == 0 ? length / 4 : length / 4 + 1;
            int[] result = new int[arrsize];
            for (int i = 0; i < length; i += 4) {
                if (i + 4 > length) {
                    int char8 = i + 1 >= length ? 0 : str.charAt(i + 1) << 8;
                    int char16 = i + 2 >= length ? 0 : str.charAt(i + 1) << 16;
                    int char24 = i + 3 >= length ? 0 : str.charAt(i + 1) << 24;
                    result[i >> 2] = str.charAt(i) | char8 | char16 | char24;
                }
                else
                    result[i >> 2] =
                        str.charAt(i) | str.charAt(i + 1) << 8 | str.charAt(i + 2) << 16 | str.charAt(i + 3) << 24;
            }
            if (includeLength) {
                int[] newArr = new int[arrsize + 1];
                System.arraycopy(result, 0, newArr, 0, arrsize);
                newArr[arrsize] = length;
                result = newArr;
            }
            return result;
        }
        
        public static String encrypt(String str, String key) {
            if (str == "") {
                return "";
            }
            int[] v = stringToLongArray(str, true);
            int[] k = stringToLongArray(key, false);
            if (k.length < 4) {
                int[] newArr = new int[4];
                System.arraycopy(k, 0, newArr, 0, k.length);
                k = newArr;
            }
            int n = v.length - 1;
            int z = v[n], y = v[0];
            int mx, e, p, sum = 0;
            int q = (int)Math.floor( 6 + 52 / (n + 1) );
            while (0 < q--) {
                sum = sum + delta & 0xffffffff;
                e = sum >>> 2 & 3;
                for (p = 0; p < n; p++) {
                    y = v[p + 1];
                    mx = (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
                    z = v[p] = v[p] + mx & 0xffffffff;
                }
                y = v[0];
                mx = (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
                z = v[n] = v[n] + mx & 0xffffffff;
            }
            return longArrayToString(v, false);
        };
    }
    
    public static String encode32(String input) {
        input = escape(input);
        StringBuilder output = new StringBuilder();
        int length = input.length();
        int chr1, chr2, chr3;
        int enc1, enc2, enc3, enc4;
        int i = 0;
        do {
            chr1 = input.charAt(i++);
            enc1 = chr1 >> 2;
            output.append(keyStr.charAt(enc1));
            
            if (i < length) {
                chr2 = input.charAt(i++);
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                output.append(keyStr.charAt(enc2));
                
                if (i < length) {
                    chr3 = input.charAt(i++);
                    enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                    enc4 = chr3 & 63;
                    output.append(keyStr.charAt(enc3)).append(keyStr.charAt(enc4));
                }
                else {
                    enc3 = ((chr2 & 15) << 2) | (0 >> 6);
                    output.append(keyStr.charAt(enc3)).append(keyStr.charAt(64));
                }
            }
            else {
                enc2 = ((chr1 & 3) << 4) | (0 >> 4);
                output.append(keyStr.charAt(enc2)).append(keyStr.charAt(64)).append(keyStr.charAt(64));
            }
        } while (i < length);
        return output.toString();
    }
    
    static String bin216(String s) {
        s += "";
        String output = "";
        int l = s.length();
        for (int i = 0; i < l; i++) {
            char c = s.charAt(i);
            String temp = Integer.toString(c, 16);
            output += temp.length() < 2 ? "0" + temp : temp;
        }
        return output;
    }
    
    public static String escape(String src) {
        char j;
        StringBuffer tmp = new StringBuffer(src.length() * 2);
        for (int i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                if (j == '*' || j == '@' || j == '-' || j == '_' || j == '+' || j == '.' || j == '/') {
                    tmp.append(j);
                }
                else {
                    tmp.append("%");
                    if (j < 16)
                        tmp.append("0");
                    tmp.append(Integer.toString(j, 16).toUpperCase());
                }
            }
            else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }
    
    /**
     * 获取动态参数
     * @param key
     * @return
     */
    public static String getRandomParamValue(String key) {
    	return encode32(DynamicJsUtil
				.bin216(Base32.encrypt("1111", key)));
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(encode32(bin216(Base32.encrypt("1111", "ODQwODk2"))));
    }
}

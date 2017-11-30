package com.lxinet.fenxiao.pay.weixinpay.util;

/**
 * 异或加密
 * Created by yanyuan on 15/10/21.
 */
public class XorUtil {

    /**
     * 简单异或加密
     *
     * @param source
     * @param key
     * @return
     */
    public static String simpleXor(String source, String key) {
        StringBuilder result = new StringBuilder();
        int j = 0;
        for (char c : source.toCharArray()) {
            result.append((char)(c ^ key.charAt(j)));
            j++;
            j = j%key.length();
        }
        return result.toString();
    }

    /**
     * 简单异或加密解密算法
     * @param source 要加密的字符串
     * @return
     */
    public static String simpleXor2(String source, String key) {
        char[] charArray = source.toCharArray();
        int j = 0;
        for(int i = 0; i < charArray.length; i++){
            charArray[i] = (char) (charArray[i] ^ key.charAt(j));
            j++;
            j = j%key.length();
        }
        return new String(charArray);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        String str = "muid=0f074dc8e1f0547310e729032ac0730b&conv_time=1422263664&client_ip=10.11.12.13&sign=8a4d7f5323fd91b37430d639e6f7371b";
        String key = "7d2506506db29c11";
        System.out.println(simpleXor(str, key));

        String result = simpleXor(str, key);
        System.out.println(result);
    }

}

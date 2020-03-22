package com.robot_brain.nlu.flow.kit;


import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DataEncryption {
    // 秘钥必须24位
    public static final String TOKEN = "VFNSMzQ1Njc4OTAxMjM0NTY3ODkwVFNS";
    /**
     *
     *描述：加密
     *
     * @author: qianlei
     *@date： 日期：2019-6-19 时间：上午09:54:29
     *@param sText
     *@param base64Key
     *@return
     */
    public static String encryptStringBase64(String sText, String base64Key) {
        if (StringUtils.isBlank(sText) || StringUtils.isBlank(base64Key)) {
            return null;
        }
        try {
            byte[] kb = Base64.decode(base64Key.getBytes("utf-8"));

            SecretKeySpec k = new SecretKeySpec(kb, "DESede");

            Cipher cp = Cipher.getInstance("DESede");
            cp.init(Cipher.ENCRYPT_MODE, k);

            byte[] b = sText.getBytes("utf-8");
            byte[] b2 = cp.doFinal(b);

            return new String(Base64.encode(b2), "utf-8");
        } catch (Exception e) {
            GenericUntil.myLog.debug("加密失败，原文{" + sText + "}");
        }
        return null;
    }

    /**
     *
     *描述：解密
     *
     * @author: qianlei
     *@date： 日期：2019-6-19 时间：上午09:54:37
     *@param base64Text
     *@param base64Key
     *@return 解密成功，返回结果；解密失败，返回密文
     */
    public static String decryptStringBase64(String base64Text, String base64Key) {
        if (StringUtils.isBlank(base64Text) || StringUtils.isBlank(base64Key)) {
            return null;
        }
        try {
            byte[] kb = Base64.decode(base64Key.getBytes("utf-8"));

            SecretKeySpec k = new SecretKeySpec(kb, "DESede");

            Cipher cp = Cipher.getInstance("DESede");
            cp.init(Cipher.DECRYPT_MODE, k);

            byte[] c = Base64.decode(base64Text.getBytes("utf-8"));
            byte[] b = cp.doFinal(c);
            return new String(b, "utf-8");
        } catch (Exception e) {
        }
        return base64Text;// 解密失败，返回密文
    }

    //64位加密
    public static String decryptStringBase64(String password) {
        if (password.length() == 0) {
            return "";
        } else {
            return decryptStringBase64(password, TOKEN);
        }
    }



}

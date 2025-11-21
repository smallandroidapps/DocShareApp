package com.docshare.docshare.util;

import android.util.Base64;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {

    public static String generateKey() {
        byte[] key = new byte[32]; // 256-bit
        new SecureRandom().nextBytes(key);
        return Base64.encodeToString(key, Base64.NO_WRAP);
    }

    public static CryptoResult encrypt(byte[] plaintext, String base64Key) throws Exception {
        byte[] keyBytes = Base64.decode(base64Key, Base64.NO_WRAP);
        SecretKey key = new SecretKeySpec(keyBytes, "AES");

        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] cipherText = cipher.doFinal(plaintext);

        return new CryptoResult(
                Base64.encodeToString(cipherText, Base64.NO_WRAP),
                Base64.encodeToString(iv, Base64.NO_WRAP)
        );
    }

    public static byte[] decrypt(String base64Ciphertext, String base64Key, String base64Iv) throws Exception {
        byte[] keyBytes = Base64.decode(base64Key, Base64.NO_WRAP);
        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        byte[] iv = Base64.decode(base64Iv, Base64.NO_WRAP);
        byte[] cipherBytes = Base64.decode(base64Ciphertext, Base64.NO_WRAP);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        return cipher.doFinal(cipherBytes);
    }

    public static class CryptoResult {
        public final String ciphertextBase64;
        public final String ivBase64;

        public CryptoResult(String ciphertextBase64, String ivBase64) {
            this.ciphertextBase64 = ciphertextBase64;
            this.ivBase64 = ivBase64;
        }
    }
}


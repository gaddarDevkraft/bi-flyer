package com.example.bi.utils;

import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.example.bi.utils.AppConstant.TRANSFORMATION;


@Component
public class EncryptionUtils {

    public String encrypt(String data) throws Exception {
        if (data != null) {

            IvParameterSpec iv = new IvParameterSpec(AppConstant.INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(AppConstant.SECRET_KEY.getBytes(StandardCharsets.UTF_8), AppConstant.ALGORITHM);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        }
        return null;

    }

    public String decrypt(String encryptedData) throws Exception {
        if (encryptedData != null) {

            IvParameterSpec iv = new IvParameterSpec(AppConstant.INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(AppConstant.SECRET_KEY.getBytes(StandardCharsets.UTF_8), AppConstant.ALGORITHM);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(original);
        }
        return null;
    }

}
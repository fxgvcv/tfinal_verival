package com.ages.informativoparaimigrantes;

import com.ages.informativoparaimigrantes.exceptions.EncryptionException;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Setter
public class HashUtils implements PasswordEncoder {

    //TODO: Use a real signing key - fetch from environment variables
    //@Value("${signingkey:}")
    String signingKey = "ad5aa0c08c53f9e0";

    private String encrypt(String strToEncrypt) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(signingKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e);
            throw new EncryptionException("Error while encrypting: " + e);
        }
    }

    private String decrypt(String strToDecrypt) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(signingKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e);
            throw new EncryptionException("Error while decrypting: " + e);
        }
    }

    @Override
    public String encode(CharSequence strToEncrypt) {
        return this.encrypt(strToEncrypt.toString());
    }

    public String decode(String strToDecrypt) {
        return this.decrypt(strToDecrypt);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return this.decrypt(encodedPassword).equals(rawPassword.toString());
    }
}

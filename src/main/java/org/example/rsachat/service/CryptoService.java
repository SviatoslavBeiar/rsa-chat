package org.example.rsachat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class CryptoService {
    private static final int RSA_KEY_SIZE = 2048;
    private static final String RSA_ALGO = "RSA";
    private static final String AES_ALGO = "AES/GCM/NoPadding";

    private final SecretKey aesKey;

    public CryptoService(@Value("${app.security.aesSecret}") String secret) {
        byte[] keyBytes = secret.getBytes();
        this.aesKey = new SecretKeySpec(keyBytes, "AES");
    }

    public KeyPair generateRsaKeyPair() throws GeneralSecurityException {
        KeyPairGenerator gen = KeyPairGenerator.getInstance(RSA_ALGO);
        gen.initialize(RSA_KEY_SIZE);
        return gen.generateKeyPair();
    }

    public String encryptPrivateKey(PrivateKey priv) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(AES_ALGO);
        byte[] iv = new byte[12];
        SecureRandom rnd = new SecureRandom();
        rnd.nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, spec);
        byte[] enc = cipher.doFinal(priv.getEncoded());
        String ivB = Base64.getEncoder().encodeToString(iv);
        String datB = Base64.getEncoder().encodeToString(enc);
        return ivB + ":" + datB;
    }

    public PrivateKey decryptPrivateKey(String ivAndCipher) throws GeneralSecurityException {
        String[] parts = ivAndCipher.split(":");
        byte[] iv = Base64.getDecoder().decode(parts[0]);
        byte[] data = Base64.getDecoder().decode(parts[1]);
        Cipher cipher = Cipher.getInstance(AES_ALGO);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(128, iv));
        byte[] pk = cipher.doFinal(data);
        KeyFactory kf = KeyFactory.getInstance(RSA_ALGO);
        return kf.generatePrivate(new PKCS8EncodedKeySpec(pk));
    }

    public String encryptWithPublicKey(String plain, String pubKeyB64) throws GeneralSecurityException {
        byte[] pubBytes = Base64.getDecoder().decode(pubKeyB64);
        KeyFactory kf = KeyFactory.getInstance(RSA_ALGO);
        PublicKey pub = kf.generatePublic(new X509EncodedKeySpec(pubBytes));
        Cipher cipher = Cipher.getInstance(RSA_ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, pub);
        byte[] enc = cipher.doFinal(plain.getBytes());
        return Base64.getEncoder().encodeToString(enc);
    }

    public String decryptWithPrivateKey(String cipherB64, PrivateKey priv) throws GeneralSecurityException {
        byte[] data = Base64.getDecoder().decode(cipherB64);
        Cipher cipher = Cipher.getInstance(RSA_ALGO);
        cipher.init(Cipher.DECRYPT_MODE, priv);
        byte[] dec = cipher.doFinal(data);
        return new String(dec);
    }
}

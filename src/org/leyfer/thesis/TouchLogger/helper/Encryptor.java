package org.leyfer.thesis.TouchLogger.helper;

import android.util.Base64;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Encryptor {
    private static final String publicKeyPem =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3RTlanDGDOcGuDp/SQBc" +
            "e4Qi3IipasyS7gk0JLMDYWB9Qql6/By7d2enhErMAGTnPcA2mIaJdINAFO+rXcw/" +
            "ANQ158XhqFRn+zKXdpw2nw8SV9s1iZEY33Wg8NNXKA2g6bwPXfywVEaQVM2lePW7" +
            "MY9Sdus7w9cdtOUv+DYAZouZt1u3F0sKkvxFaGxVQYYvV6CbosAM8lnZzzYIaid/" +
            "z6lhviBxN+q+nq2aDDxwkOJvaO+oWN/WI/aq66pVV3Xvp4+l86P4B3BNbFIci/U5" +
            "fuQfxKF1QCSB1R/yj/BEhojAAFQuOEPpTNAwRyBeyS0yEjIzShdwmDlraCexrpcH" +
            "oQIDAQAB";

    public static PublicKey getPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte [] encoded = Base64.decode(publicKeyPem, Base64.NO_WRAP);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(keySpec);
    }

    public static byte[] encryptData(PublicKey publicKey, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public static SecretKey generateSessionKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        return keyGen.generateKey();
    }

    public static SymmetricEncryptionResult encryptWithSessionKey(SecretKey sessionKey, byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, sessionKey);
        return new SymmetricEncryptionResult(cipher.doFinal(data), cipher.getIV());
    }
}

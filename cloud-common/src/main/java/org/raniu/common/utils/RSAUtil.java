package org.raniu.common.utils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @decs: OAuth2主文件
 *
 * @projectName: cloudDisk
 * @package: org.raniu.utils
 * @className: RSAUtil
 * @author: Raniu
 * @description: TODO
 * @date: 2023/9/28 1:02
 * @version: 1.0
 */


public class RSAUtil {
    private static final String RSA_KEY_ALGORITHM = "RSA";
    private static final String RSA_SIGNATURE_ALGORITHM = "SHA1withRSA"; // RSA:sha1模式
    private static final String RSA2_SIGNATURE_ALGORITHM = "SHA256withRSA"; // RSA2:sha256模式
    private static final int KEY_SIZE = 2048;
    private static final byte[] RANDOM_SEED = SecureRandom.getSeed(16);

    /**
     * RSA生成密钥对
     *
     * @return 密钥对
     */
    public static Map<String, String> generateKey() {
        KeyPairGenerator keygen;
        try {
            keygen = KeyPairGenerator.getInstance(RSA_KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("RSA初始化密钥出现错误,算法异常");
        }

        SecureRandom random = new SecureRandom();
        random.setSeed(RANDOM_SEED);
        keygen.initialize(KEY_SIZE, random);
        KeyPair keyPair = keygen.genKeyPair();
        byte[] publicKey = keyPair.getPublic().getEncoded();
        String publicKeyStr = Base64.getEncoder().encodeToString(publicKey);
        byte[] privateKey = keyPair.getPrivate().getEncoded();
        String privateKeyStr = Base64.getEncoder().encodeToString(privateKey);
        Map<String, String> keyPairMap = new HashMap<>();
        keyPairMap.put("publicKeyStr", publicKeyStr);
        keyPairMap.put("privateKeyStr", privateKeyStr);
        return keyPairMap;
    }

    /**
     *公钥加密
     *
     * @param data 待加密字符串
     * @param publicKeyStr 公钥
     * @return 加密后字符串
     */

    public static String encryptByPublicKey(String data, String publicKeyStr) throws Exception {
        byte[] pubKey = Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec); // x509密钥公约
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypt = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypt);
    }

    /**
     * 私钥解密
     *
     * @param data 加密字符串
     * @param privateKeyStr 私钥
     * @return 解密后字符串
     */
    public static String decryptByPrivateKey(String data, String privateKeyStr) throws Exception {
        byte[] priKey = Base64.getDecoder().decode(privateKeyStr);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec); // pkcs8模式
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        data = data.replaceAll(" ","+");
        byte[] decrypt = cipher.doFinal(Base64.getDecoder().decode(data)); // pkcs1 sha1 sha1
        return new String(decrypt);
    }

    /**
     * 私钥加密
     * @param data 待加密字符串
     * @param privateKeyStr 私钥
     * @return 加密字符串
     */

    public static String encryptByPrivateKey(String data, String privateKeyStr) throws Exception {
        byte[] priKey = Base64.getDecoder().decode(privateKeyStr);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encrypt = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypt);
    }

    /**
     * 数字签名校验
     *
     * @param data 待校验数据
     * @param sign 数字签名
     * @param pubKey 公钥
     * @param signType RSA OR RSA2
     * @return boolean 校验是否成功
     */
    public static boolean verify(byte[] data, byte[] sign, byte[] pubKey, String signType) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        String algorithm = RSA_KEY_ALGORITHM.equals(signType) ? RSA_SIGNATURE_ALGORITHM : RSA2_SIGNATURE_ALGORITHM;
        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(sign);
    }
}

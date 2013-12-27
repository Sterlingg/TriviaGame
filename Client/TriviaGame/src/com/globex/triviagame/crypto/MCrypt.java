package com.globex.triviagame.crypto;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * MCrypt
 * 
 * MAJOR OVERKILL!!! :D
 * Used for decrypting the JSON data received from the server.
 * http://www.androidsnippets.com/encrypt-decrypt-between-android-and-php
 * @author sterling
 *
 */
public class MCrypt {
        private SecretKeySpec keyspec;
        private IvParameterSpec ivspec;
        private Cipher cipher;
        
        // ... Hope someone doesn't want to disassemble the program.
        private static final String KEY = "768EE18AB6480D53CC8FFCD23D117D57";
        private static final String IV = "C111510372A7A003";
        
        public MCrypt()
        {
	        ivspec = new IvParameterSpec(IV.getBytes());
                keyspec = new SecretKeySpec(KEY.getBytes(), "AES");
                
                try {
                        cipher = Cipher.getInstance("AES/CBC/NoPadding");
                } catch (NoSuchAlgorithmException e) {
                    // Shouldn't happen
                        e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                	// Shouldn't happen
                        e.printStackTrace();
                }
        }
              
        /**
         * decrypt: Encrypts the given string using AES-CBC 128 bit encryption with no IV.
         * @param code
         * @return
         * @throws Exception
         */
        public byte[] decrypt(String code) throws Exception
        {
                if(code == null || code.length() == 0)
                        throw new Exception("Empty string");
                
                byte[] decrypted = null;

                try {
                        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
                        
                        decrypted = cipher.doFinal(hexToBytes(code));
                } catch (Exception e)
                {
                        throw new Exception("[decrypt] " + e.getMessage());
                }
                return decrypted;
        }
            
        /**
         * hexToBytes: 
         * @param str
         * @return
         */
        public static byte[] hexToBytes(String str) {
                if (str==null) {
                        return null;
                } else if (str.length() < 2) {
                        return null;
                } else {
                        int len = str.length() / 2;
                        byte[] buffer = new byte[len];
                        for (int i=0; i<len; i++) {
                                buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
                        }
                        return buffer;
                }
        }
        
        public static String bytesToHex(byte[] data)
        {
                if (data==null)
                {
                        return null;
                }
                
                int len = data.length;
                String str = "";
                for (int i=0; i<len; i++) {
                        if ((data[i]&0xFF)<16)
                                str = str + "0" + java.lang.Integer.toHexString(data[i]&0xFF);
                        else
                                str = str + java.lang.Integer.toHexString(data[i]&0xFF);
                }
                return str;
        }
}

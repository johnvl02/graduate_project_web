package com.john.graduate_project.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
public class EncryptionUtil {


    public static List<String> encruptPasswoed(String password){
        // chose a Character random from this String
        List<String> passwordList = new ArrayList<>();
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        // create StringBuffer size of AlphaNumericString
        StringBuilder s = new StringBuilder(20);
        for (int i = 0; i < 20; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int)(AlphaNumericString.length() * Math.random());
            // add Character one by one in end of sb
            s.append(AlphaNumericString.charAt(index));
        }
        passwordList.add(getHash(password , s.toString()));
        passwordList.add(s.toString());
        return passwordList ;
    }
    public static String encruptPasswoed(String password, String salt){
        return getHash(password , salt) ;
    }

    private static String getHash(String unhashed, String salt) {
        // Hash the password.
        String toHash = unhashed + salt;
        MessageDigest messageDigest = null;
        try
        {
            messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] encodedhash = messageDigest.digest(toHash.getBytes(StandardCharsets.UTF_8));
            toHash=bytesToHex(encodedhash);
        }
        catch (NoSuchAlgorithmException ex)
        {
            return "00000000000000000000000000000000";
        }
        return toHash;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}

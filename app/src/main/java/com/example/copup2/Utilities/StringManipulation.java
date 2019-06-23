package com.example.copup2.Utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringManipulation {
    /**
     * #TODO CREATE ENUM OF COUNTRY CODES
     */
    /**
     * This class will handle Strings
     */

    /**
     * This method of validating is very Bad
     * Fix
     */
    public static String getNumber(String number) {
        String num = "+27";
        String newNumber = num.concat(number.substring(1));
        return newNumber;
    }

    public static String reConvertNumber(String num) {
        String zero = "0";
        String s = zero.concat(num.substring(3));
        return s;

    }

    public static boolean isValidProductTitle(String title) {
        boolean operator = false;
        int counter = 0;
        String regex = "^[a-zA-Z0-9 -]+$";

        for (int i = 0; i < title.length(); i++) {
            if (Character.isDigit(title.charAt(i))) {
                counter++;
            }else if(!Character.isLetter(title.charAt(i))){
                counter++;
            }
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(title);

        if (!title.isEmpty() && counter != title.length()) {
            if (matcher.matches()) {
                operator = true;
            } else {
                operator = false;
            }
        } else {
            operator = false;
        }
        return operator;

    }

    public static boolean isValidProductPrice(String price) {
        boolean operator = false;
        int counter = 0;
        for (int i = 0; i < price.length(); i++) {
            if (Character.isDigit(price.charAt(i))) {
                counter++;
            }
        }
        if (counter == price.length()) {
            operator = true;
        } else {
            operator = false;
        }
        return operator;
    }

    public static boolean isNumberValid(String input) {
        int a = 0;
        if (input.length() == 10) {
            if (input.charAt(0) == '0') {
                for (int i = 0; i < input.length(); i++) {
                    if (Character.isDigit(input.charAt(i))) {
                        a++;
                    }
                }
            } else {
                a = -1;
            }
        } else {
            a = -2;
        }
        if (a == 10) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * I do not claim ownership of this code
     */
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * Again Bad Version Code of Regex
     * Fix Later
     */
    public static boolean isValidPassword(String password) {
        boolean operator = true;
        if (password.length() > 3) {
            if (password.contains("\\") || password.contains("/")) {
                operator = false;
            }
        } else {
            operator = false;
        }
        return operator;
    }

    public static boolean isValidUsername(String username) {
        String regex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        int falseCounter = 0;
        if (matcher.matches()) {
            int counter = 0;
            for (int i = 0; i < username.length(); i++) {
                if (Character.isDigit(username.charAt(i))) {
                    counter++;
                }
            }
            if (counter == username.length()) {
                falseCounter = -1;
            } else {
                if (username.length() <= 1) {
                    falseCounter = -1;
                }
                if (username.length() > 13) {
                    falseCounter = -1;
                }
            }
        } else {
            falseCounter = -1;
        }
        if (falseCounter == 0) {
            return true;
        } else {
            return false;
        }

    }

    public static String getFileExtension(Uri uri, Context context) {
        ContentResolver resolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));

    }

}

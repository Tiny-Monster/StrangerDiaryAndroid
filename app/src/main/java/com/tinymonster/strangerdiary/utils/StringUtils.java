package com.tinymonster.strangerdiary.utils;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TinyMonster on 10/01/2019.
 */

public class StringUtils {
    public static boolean isEmpty(String src) {
        return src == null || src.length() == 0;
    }

    public static boolean isTrimEmpty(String src) {
        return src == null || src.trim().length() == 0;
    }

    public static int length(final String s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 是否纯数组
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isLetter(String str) {
        Pattern pattern = Pattern.compile("[a-zA-Z] + ");
        Matcher isLetter = pattern.matcher(str);
        if (!isLetter.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isMobilePhone(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static String getSafePhone(String phone) {
        if (phone == null || phone.length() == 0) return "";
        String phoneNumber = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return phoneNumber;
    }

    public static String getSafeCardNum(String bankCard) {
        if (bankCard == null || bankCard.length() == 0) return "";
        int hideLength = 8;//替换位数
        int sIndex = bankCard.length() / 2 - hideLength / 2;
        String replaceSymbol = "*";
        StringBuilder sBuilder = new StringBuilder();
        for (int i = 0; i < bankCard.length(); i++) {
            char number = bankCard.charAt(i);
            if (i >= sIndex - 1 && i < sIndex + hideLength) {
                sBuilder.append(replaceSymbol);
            } else {
                sBuilder.append(number);
            }
        }
        return sBuilder.toString();
    }

    public static InputFilter passwordFilter(final Context context)
    {
        InputFilter inputFilter=new InputFilter() {


            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\ud800\udc00-\udbff\udfff\ud800-\udfff]",
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                Matcher matcher=  emoji.matcher(charSequence);
                if(!matcher.find()){
                    return null;
                }else{
                    ToastUtils.showToast(context,  "非法字符");
                    return "";
                }
            }
        };

        return inputFilter;
    }
}

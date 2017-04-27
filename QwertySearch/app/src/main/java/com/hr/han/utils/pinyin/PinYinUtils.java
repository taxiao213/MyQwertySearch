package com.hr.han.utils.pinyin;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;

/**
 * 将汉字转换为字母
 * Created by aaa on 2017/3/24.
 */

public class PinYinUtils {

    /**
     * 将汉字转换为全拼 大写
     *
     * @param str
     * @return
     */
    public static String getPinYin(String str) {

        //将汉字转换为字符
        char[] chars = str.toCharArray();

        //创建与字符等长的字符串数组
        String[] strings = new String[chars.length];

        // 设置汉字拼音输出的格式 决定输出的拼音格式，比如大小写，是否带有音标
        HanyuPinyinOutputFormat hyformat = new HanyuPinyinOutputFormat();
        hyformat.setCaseType(HanyuPinyinCaseType.UPPERCASE);//大写字母
        hyformat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//去掉声调
        hyformat.setVCharType(HanyuPinyinVCharType.WITH_V);

        String divide = "";
        int length1 = chars.length;
        try {
            for (int i = 0; i < length1; i++) {
                // 判断能否为汉字字符
                if (Character.toString(chars[i]).matches("[\\u4E00-\\u9FA5]+")) {

                    strings = PinyinHelper.toHanyuPinyinStringArray(chars[i], hyformat);// 将汉字的几种全拼都存到strings数组中

                    divide += strings[0];// 取出该汉字全拼的第一种读音并连接到字符串divide后
                } else {
                    // 如果不是汉字字符，间接取出字符并连接到字符串divide后
                    divide += Character.toString(chars[i]);
                }
            }

        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }

        return divide;

    }


    /**
     * 将汉字转换为全拼 小写
     *
     * @param str
     * @return
     */
    public static String getLowPinYin(String str) {

        //将汉字转换为字符
        char[] chars = str.toCharArray();

        //创建与字符等长的字符串数组
        String[] strings = new String[chars.length];

        // 设置汉字拼音输出的格式 决定输出的拼音格式，比如大小写，是否带有音标
        HanyuPinyinOutputFormat hyformat = new HanyuPinyinOutputFormat();
        hyformat.setCaseType(HanyuPinyinCaseType.LOWERCASE);//小写字母
        hyformat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//去掉声调
        hyformat.setVCharType(HanyuPinyinVCharType.WITH_V);

        String divide = "";
        int length1 = chars.length;
        try {
            for (int i = 0; i < length1; i++) {
                // 判断能否为汉字字符
                if (Character.toString(chars[i]).matches("[\\u4E00-\\u9FA5]+")) {

                    strings = PinyinHelper.toHanyuPinyinStringArray(chars[i], hyformat);// 将汉字的几种全拼都存到strings数组中

                    divide += strings[0];// 取出该汉字全拼的第一种读音并连接到字符串divide后
                } else {
                    // 如果不是汉字字符，间接取出字符并连接到字符串divide后
                    divide += Character.toString(chars[i]);
                }
            }

        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }

        return divide;

    }


    /**
     * 将每个汉字单独转换为全拼 小写,用;隔开
     *
     * @param str
     * @return
     */
    public static String getSingleLowPinYin(String str) {

        //将汉字转换为字符
        char[] chars = str.toCharArray();

        //创建与字符等长的字符串数组
        String[] strings = new String[chars.length];

        // 设置汉字拼音输出的格式 决定输出的拼音格式，比如大小写，是否带有音标
        HanyuPinyinOutputFormat hyformat = new HanyuPinyinOutputFormat();
        hyformat.setCaseType(HanyuPinyinCaseType.LOWERCASE);//小写字母
        hyformat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//去掉声调
        hyformat.setVCharType(HanyuPinyinVCharType.WITH_V);

        String divide = "";
        int length1 = chars.length;
        try {
            for (int i = 0; i < length1; i++) {
                // 判断能否为汉字字符
                if (Character.toString(chars[i]).matches("[\\u4E00-\\u9FA5]+")) {

                    strings = PinyinHelper.toHanyuPinyinStringArray(chars[i], hyformat);// 将汉字的几种全拼都存到strings数组中

                    divide += strings[0] + ";";// 取出该汉字全拼的第一种读音并连接到字符串divide后
                } else {
                    // 如果不是汉字字符，间接取出字符并连接到字符串divide后
                    divide += Character.toString(chars[i]) + ";";
                }
            }

        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }

        return divide;

    }


    public static ArrayList<String> getAllPinYin(String str) {
        ArrayList<String> list = new ArrayList<>();

        String singleLowPinYin = getSingleLowPinYin(str);
        //所有汉字的全拼
        String[] splitString = singleLowPinYin.split(";");
        //所有汉字的首字母
        String headChar = getPinYinHeadChar(str);

        StringBuffer sb;
        for (int i = 0; i < splitString.length; i++) {
            sb = new StringBuffer();
            sb.append(splitString[i]+"");
            sb.append(headChar.charAt(i));
        }


        //        for (int j = 0; j < splitString.length; j++) {
        //            sb = new StringBuffer();
        //
        //            String splitStr = splitString[j];
        //            sb.append(splitStr);
        //
        //            for (int i = j+1; i < headChar.length(); i++) {
        //               // String substring = headChar.substring(i, i + 1);
        //              //  if (!TextUtils.equals(splitStr.substring(0, 1), substring)) {
        //                    sb.append(headChar.substring(i, i + 1));
        //              //  }
        //
        //            }
        //
        //            list.add(sb.toString());
        //
        //        }
        return list;
    }


    /**
     * 提取每个汉字的首字母 小写
     *
     * @param str
     * @return String
     */
    public static String getPinYinHeadChar(String str) {

        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            // 提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }

    /**
     * 提取每个汉字的首字母 大写
     *
     * @param str
     * @return String
     */
    public static String getUppercasePinYinHeadChar(String str) {
        String pinYin = getPinYin(str);
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = pinYin.charAt(j);
            // 提取汉字的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert;
    }


    /**
     * 将字符串转换成ASCII码
     *
     * @param cnStr
     * @return String
     */
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();
        // 将字符串转换成字节序列
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            // 将每个字符转换成ASCII码
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return strBuf.toString();
    }

    /**
     * 测试用
     *
     * @param cnStr
     */
    public static void main(String cnStr) {
        //String cnStr = "陈";
        System.out.println(getPinYin(cnStr));
        System.out.println(getPinYinHeadChar(cnStr));
        System.out.println(getCnASCII(cnStr));
    }
}

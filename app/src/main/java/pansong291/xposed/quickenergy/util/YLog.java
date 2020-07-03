package pansong291.xposed.quickenergy.util;

import android.util.Log;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * LOG显示类，解决AndroidStudio的logcat显示超长字符串的问题
 *
 * @author yujing 2020年4月28日14:59:59
 */

@SuppressWarnings({"unused", "FieldCanBeLocal", "WeakerAccess"})
public class YLog {
    //规定每段显示的长度
    private static int LOG_MAX_LENGTH = 4000;
    private static String TAG = "■YLog";
    private static final int VERBOSE = 2;
    private static final int DEBUG = 3;
    private static final int INFO = 4;
    private static final int WARN = 5;
    private static final int ERROR = 6;

    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void v(String TAG, String msg) {
        v(TAG, msg, null);
    }

    public static void v(String TAG, String msg, Throwable tr) {
        println(TAG, msg, tr, VERBOSE);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void d(String TAG, String msg) {
        d(TAG, msg, null);
    }

    public static void d(String TAG, String msg, Throwable tr) {
        println(TAG, msg, tr, DEBUG);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String TAG, String msg) {
        i(TAG, msg, null);
    }

    public static void i(String TAG, String msg, Throwable tr) {
        println(TAG, msg, tr, INFO);
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void w(String TAG, String msg) {
        w(TAG, msg, null);
    }

    public static void w(String TAG, String msg, Throwable tr) {
        println(TAG, msg, tr, WARN);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(String TAG, String msg) {
        e(TAG, msg, null);
    }

    public static void e(String TAG, Throwable tr) {
        e(TAG, "ERROR", tr);
    }

    public static void e(Throwable tr) {
        e(TAG, "ERROR", tr);
    }

    public static void e(String TAG, String msg, Throwable tr) {
        println(TAG, msg, tr, ERROR);
    }

    /**
     * 打印日志
     *
     * @param TAG  tag
     * @param msg  内容
     * @param tr   异常
     * @param type 类型
     */
    private static void println(String TAG, String msg, Throwable tr, int type) {
        List<StringBuilder> lines = groupActual(msg, LOG_MAX_LENGTH);
        int i = 1;
        for (StringBuilder item : lines) {
            String tag = lines.size() == 1 ? TAG : TAG + i;
            if (type == VERBOSE)
                Log.v(tag, item.toString(), tr);
            else if (type == DEBUG)
                Log.d(tag, item.toString(), tr);
            else if (type == INFO)
                Log.i(tag, item.toString(), tr);
            else if (type == WARN)
                Log.w(tag, item.toString(), tr);
            else if (type == ERROR)
                Log.e(tag, item.toString(), tr);
            i++;
        }
    }

    /**
     * 字符串分组，真实长度，每digit位字符拆分一次字符串，英文算一个字符，中文算两个或者3字符
     *
     * @param str   字符串
     * @param digit 位
     * @return 拆分后的字符串
     */
    public static List<StringBuilder> groupActual(String str, int digit) {
        return groupActual(str, digit, null);
    }

    /**
     * 字符串分组，指定字符串编码
     *
     * @param str     字符串
     * @param digit   位
     * @param charset 编码
     * @return 拆分后的字符串
     */
    public static List<StringBuilder> groupActual(String str, int digit, Charset charset) {
        if (digit < 3) return group(str, digit);
        List<StringBuilder> strings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int index = 0;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);//获取每一个字
            if (charset != null)
                index += String.valueOf(c).getBytes(charset).length;
            else
                index += String.valueOf(c).getBytes().length;

            //如果大于就换行
            if (index > digit) {
                index = 0;
                strings.add(sb);
                sb = new StringBuilder();
                i--;
                continue;
            }
            //连接字符串
            sb.append(c);
            if (index == digit) {//如果大于就换行
                index = 0;
                strings.add(sb);
                sb = new StringBuilder();
            }
        }
        if (sb.length() > 0)
            strings.add(sb);
        return strings;
    }

    /**
     * 字符串分组，每digit位字符拆分一次字符串，中文英文都算一个字符
     *
     * @param str   字符串
     * @param digit 位
     * @return 拆分后的字符串
     */
    public static List<StringBuilder> group(String str, int digit) {
        List<StringBuilder> strings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);//获取每一个字
            sb.append(c);
            if (i % digit == digit - 1) {//如果是digit的倍数就换行
                strings.add(sb);
                sb = new StringBuilder();
            }
        }
        if (sb.length() > 0)
            strings.add(sb);
        return strings;
    }


    /**
     * 字符串分组，每digit位字符拆分一次字符串，英文算一个字符，中文算两个字符
     *
     * @param str   字符串
     * @param digit 位
     * @return 拆分后的字符串
     */
    public static List<StringBuilder> groupDouble(String str, int digit) {
        List<StringBuilder> strings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);//获取每一个字
            index = (c >= 1 && c <= 127) ? index + 1 : index + 2;
            if (index > digit) {//如果大于就换行
                index = 0;
                strings.add(sb);
                sb = new StringBuilder();
                i--;
                continue;
            }
            sb.append(c);
            if (index >= digit) {//如果大于2倍就换行
                index = 0;
                strings.add(sb);
                sb = new StringBuilder();
            }
        }
        if (sb.length() > 0)
            strings.add(sb);
        return strings;
    }
}

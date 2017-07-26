package github.com.kotlin_news;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import github.com.kotlin_news.util.TimeUtil;

/**
 * Created by guoshuaijie on 2017/7/24.
 */

public class javaCodeTest {

    public static void main(String... ss) {
//        System.out.println("Java："+switchTimeStrToLong("2017-07-25 09:17:39"));

//        System.out.println("kotlin："+ExtensionUtilsKt.switchTimeStrToLong("2017-07-24 16:01:26","yyyy-MM-dd HH:mm:ss"));

        System.out.println(TimeUtil.switchTimeStrToLong("2017-07-25 09:41:15"));


        System.out.println(System.currentTimeMillis());
    }

    public static long switchTimeStrToLong(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}





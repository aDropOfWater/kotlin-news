package github.com.kotlin_news.util;

import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import github.com.kotlin_news.data.newListItem;

/**
 * Created by guoshuaijie on 2017/7/24.
 */

public class TimeUtil {
    public static long switchTimeStrToLong(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        long ct = 0;
        try {
            ct = sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return ct;
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static void switchTimeBean(@NotNull newListItem item) {
        item.setTimeLong(switchTimeStrToLong(item.getPtime()));
        String fromeNew = ExtensionUtilsKt.getTimeFromeNew(new Date(), item.getTimeLong());
        if(TextUtils.isEmpty(fromeNew))fromeNew = item.getPtime().substring(5,16);
        item.setPublishtime(fromeNew);

        //            it.timeLong = TimeUtil.switchTimeStrToLong(it.ptime)
//            var timeFromeNew = Date().getTimeFromeNew(it.timeLong)
//            if (timeFromeNew.isNullOrEmpty()) timeFromeNew = it.ptime.substring(5, 16)
//            it.publishtime = timeFromeNew
    }
}

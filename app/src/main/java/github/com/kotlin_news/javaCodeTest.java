package github.com.kotlin_news;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import github.com.kotlin_news.util.JavaUtil;

/**
 * Created by guoshuaijie on 2017/7/24.
 */

public class javaCodeTest {

    public static void main(String... ss) {
//        System.out.println("Java："+switchTimeStrToLong("2017-07-25 09:17:39"));

//        System.out.println("kotlin："+ExtensionUtilsKt.switchTimeStrToLong("2017-07-24 16:01:26","yyyy-MM-dd HH:mm:ss"));

//        System.out.println(JavaUtil.switchTimeStrToLong("2017-07-25 09:41:15"));


//        System.out.println(System.currentTimeMillis());

        int[] t = new int[2];

       int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;


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

    class ad extends PagerAdapter{
        int i;
        public ad(int i) {
            this.i=i;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }
    }


    public class textHelper extends ItemTouchHelper.SimpleCallback{
        public textHelper(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }
    }

}





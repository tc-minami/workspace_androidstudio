package tablecloth.com.bookshelf.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * 汎用的な処理を格納する
 * Created by shnomura on 2014/08/17.
 */
public class Util {
    public static boolean isDebuggable(Context context) {
        return ( 0 != ( context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
    }

    public static boolean isEmpty(String str) {
        if(str == null || str.length() <= 0) return true;
        return false;
    }
    public static boolean isEmpty(byte[] byteData) {
        if(byteData == null || byteData.length <= 0) return true;
        return false;
    }
    public static boolean isEmpty(String[] str) {
        if(str == null || str.length <= 0) return true;
        return false;
    }
}

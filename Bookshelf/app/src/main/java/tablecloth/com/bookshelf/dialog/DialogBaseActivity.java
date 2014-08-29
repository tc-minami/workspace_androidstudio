package tablecloth.com.bookshelf.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by shnomura on 2014/08/17.
 */
public abstract class DialogBaseActivity extends Activity {

    final protected static String KEY_TITLE = "title";
    final protected static String KEY_MESSAGE = "message";
    final protected static String KEY_BTN_POSITIVE = "btn_positive";
    final protected static String KEY_BTN_NEGATIVE = "btn_negative";
    final protected static String KEY_DATA_TYPE = "data_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());    }

    // レイアウトIDを取得する
    abstract protected int getLayoutId();
}

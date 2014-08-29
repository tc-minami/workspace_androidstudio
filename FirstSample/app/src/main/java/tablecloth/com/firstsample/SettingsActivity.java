package tablecloth.com.firstsample;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.prefs.PreferenceChangeEvent;

public class SettingsActivity extends Activity {

    private TextView mTextView;
    ImageView iv;
    final public static String PREF_KEY = "watchee_pref";
    // theme_idをとりあえずpreferenceに保持しておく。
    // watchFace(MainActivity)でこれを参照し、背景画像を切り替える
    final public static String PREF_KEY_NAME_THEME_ID = "theme_id";
    boolean onCreate = false;
//    WatchViewStub mStub;

    SharedPreferences mPref;

    final public static int[] BG_IDS = {
            R.drawable.skin1, R.drawable.skin2, R.drawable.skin3,
    };

    final public static int[] TEXT_COLOR_IDS =  {
            R.color.white, R.color.dark_blue, R.color.grey,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_2);

        mPref = getApplicationContext().getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
        // 丸時計、四角時計両方に専用レイアウトを用意していた場合はstubを使う
//        mStub = (WatchViewStub) findViewById(R.id.watch_view_stub);
//        mStub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
//            @Override
//            public void onLayoutInflated(WatchViewStub stub) {
//
                iv = (ImageView)findViewById(R.id.bg_image);

                int themeId = mPref.getInt(PREF_KEY_NAME_THEME_ID, 0);
                iv.setImageResource(BG_IDS[themeId]);

                // 画面下部のテーマ選択（３択）を用意
                ImageView btn = (ImageView)findViewById(R.id.theme_1);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = mPref.edit();
                        editor.putInt(PREF_KEY_NAME_THEME_ID, 0);
                        editor.commit();
                        iv.setImageResource(R.drawable.skin1);
                    }
                });

                btn = (ImageView)findViewById(R.id.theme_2);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = mPref.edit();
                        editor.putInt(PREF_KEY_NAME_THEME_ID, 1);
                        editor.commit();
                        iv.setImageResource(R.drawable.skin2);
                    }
                });

                btn = (ImageView)findViewById(R.id.theme_3);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = mPref.edit();
                        editor.putInt(PREF_KEY_NAME_THEME_ID, 2);
                        editor.commit();
                        iv.setImageResource(R.drawable.skin3);
                    }
                });

//                iv = (ImageView)stub.findViewById(R.id.bg_image);
//                iv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(MainActivity.this, "onClick", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                iv.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        Toast.makeText(MainActivity.this, "onLongClick", Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//                });
//            }
//        });

        // バックグラウンドでのイベント取得ができる「らしい」が未使用・未テストの状態なのでまだ不明
//        IntentFilter localIntentFilter = new IntentFilter("com.google.android.clock.home.action.BACKGROUND_ACTION");
//        registerReceiver(mActionReceiver, localIntentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

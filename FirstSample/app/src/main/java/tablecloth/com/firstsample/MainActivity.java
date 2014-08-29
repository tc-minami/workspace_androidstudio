package tablecloth.com.firstsample;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;

public class MainActivity extends Activity {

    private TextView mTextView;
    ImageView iv;

    SharedPreferences mPref;
//    WatchViewStub stub;
    TextClock mTextClockLarge;
    TextClock mTextClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_2);

        mPref = getApplicationContext().getSharedPreferences(SettingsActivity.PREF_KEY, Activity.MODE_PRIVATE);

        // レイアウト設定
        initLayout();


    // バックグラウンドでのイベント取得ができる「らしい」が未使用・未テストの状態なのでまだ不明
//        IntentFilter localIntentFilter = new IntentFilter("com.google.android.clock.home.action.BACKGROUND_ACTION");
//        registerReceiver(mActionReceiver, localIntentFilter);
    }

    private void initLayout() {
        mTextClockLarge = (TextClock)findViewById(R.id.text_clock_large);
        mTextClockLarge.setFormat24Hour("HH:mm:ss");

        mTextClock = (TextClock)findViewById(R.id.text_clock);
        mTextClock.setFormat24Hour("MM/dd[E]");



        // 丸時計、四角時計それぞれの専用レイアウトを用意していた場合はstubを使う必要がある。
        // 今回はテストということで、未使用
//        stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
//        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
//            @Override
//            public void onLayoutInflated(WatchViewStub stub) {
                iv = (ImageView)findViewById(R.id.bg_image);
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

//                setBackground();
//            }
//        });
    }


    @Override
    public void onResume() {
        super.onResume();

        setBackground();
    }

    // 時計スキン（背景）の設定
    private void setBackground() {
        if(iv != null) {
            // テーマ番号取得
            int theme = mPref.getInt(SettingsActivity.PREF_KEY_NAME_THEME_ID, 0);
            mTextClockLarge.setTextColor(getResources().getColor(SettingsActivity.TEXT_COLOR_IDS[theme]));
            mTextClock.setTextColor(getResources().getColor(SettingsActivity.TEXT_COLOR_IDS[theme]));
            iv.setImageResource(SettingsActivity.BG_IDS[theme]);
            iv.invalidate();
        }
    }
}

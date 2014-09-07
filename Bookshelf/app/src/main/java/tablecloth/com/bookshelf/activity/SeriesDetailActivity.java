package tablecloth.com.bookshelf.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import tablecloth.com.bookshelf.R;
import tablecloth.com.bookshelf.db.DB;
import tablecloth.com.bookshelf.db.FilterDao;
import tablecloth.com.bookshelf.db.SeriesData;
import tablecloth.com.bookshelf.dialog.SimpleDialogActivity;
import tablecloth.com.bookshelf.util.G;
import tablecloth.com.bookshelf.util.ToastUtil;

/**
 * Created by minami on 14/09/07.
 * １作品の詳細表示画面
 */
public class SeriesDetailActivity extends Activity {

    SeriesData mSeriesData = null;
    int mSeriesId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list);

        mSeriesId = getIntent().getIntExtra(G.INTENT_SERIES_ID, -1);
        if(mSeriesId == -1) {
            ToastUtil.show(SeriesDetailActivity.this, "本の情報の取得に失敗しました");
            SeriesDetailActivity.this.finish();
        }


        // DBから情報取得
        refreshData();
        // 作品の情報を元に、レイアウトを反映
        initLayout();


        setClickListener();
    }

    private void setClickListener() {
    }

    /**
     * DBから最新情報を取得
     */
    private void refreshData() {
        mSeriesData = FilterDao.loadSeries(SeriesDetailActivity.this, mSeriesId);

    }

    /**
     * 本の情報をレイアウトに反映させる
     */
    private void initLayout() {
        ((TextView)findViewById(R.id.title)).setText(mSeriesData.mTitle);
        ((TextView)findViewById(R.id.author)).setText(mSeriesData.mAuthor);
        ((TextView)findViewById(R.id.company)).setText(mSeriesData.mCompany);
        ((TextView)findViewById(R.id.magazine)).setText(mSeriesData.mMagazine);

    }
}

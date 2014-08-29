package tablecloth.com.bookshelf.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import tablecloth.com.bookshelf.db.FilterDao;
import tablecloth.com.bookshelf.dialog.NewSeriesDialogActivity;
import tablecloth.com.bookshelf.util.G;
import tablecloth.com.bookshelf.R;
import tablecloth.com.bookshelf.db.SeriesData;
import tablecloth.com.bookshelf.dialog.SimpleDialogActivity;

/**
 * Created by shnomura on 2014/08/16.
 */
public class ListActivity extends Activity{

    private ListView mListView;
    ArrayList<SeriesData> mDataArrayList = new ArrayList<SeriesData>();
    ListAdapter mListAdapter;

//    String filterInfo = null;

    // データ編集時用のID情報一時保管庫
    // 使い終わったらnullにする
    private int[] mSelectSeriesIds = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_list);


        // リスト部分の生成・設定処理
        mListView = (ListView)findViewById(R.id.list_view);
        mListAdapter = new ListAdapter();
        mListView.setAdapter(mListAdapter);

        // DBから情報取得
        refreshData();

        setClickListener();
    }

    private void refreshData() {

        mDataArrayList.clear();

        SeriesData[] series = FilterDao.loadSeries(ListActivity.this);
        if(series != null) {
            for(int i = 0 ; i < series.length ; i ++) {
                SeriesData data = series[i];
                if(data != null) {
                    mDataArrayList.add(data);
                }
            }
        }
//        if(filterInfo == null) {
//            // とりあえず全ての情報を取得する
//
//        } else {
//            // 指定のデータのみ読み込む
//        }

        mListAdapter.notifyDataSetChanged();
    }

    private class ListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mDataArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView title;
            TextView author;
            TextView volume;
            ImageView image;
            View v = convertView;

            if(v==null){
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.book_list_row, null);
            }
            if(mDataArrayList != null) {
                final SeriesData series = (SeriesData) mDataArrayList.get(position);
                if (series != null) {
                    // データの設定
                    title = (TextView) v.findViewById(R.id.title);
                    author = (TextView) v.findViewById(R.id.author);
                    volume = (TextView) v.findViewById(R.id.volume);
                    image = (ImageView) v.findViewById(R.id.image);

                    title.setText(series.mTitle);
                    author.setText(series.mAuthor);
                    volume.setText(series.getVolumeString());
                    image.setImageBitmap(series.mImage);

                    // リストの各要素のタッチイベント
                    v.findViewById(R.id.delete_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSelectSeriesIds = new int[]{series.mSeriesId};
                            Intent intent = SimpleDialogActivity.getIntent(ListActivity.this, "削除しますか？", "登録された作品の情報を削除しますか？\n\n復元は出来ませんのでご注意ください", "はい", "いいえ");
                            startActivityForResult(intent, G.REQUEST_CODE_LIST_ROW_DELETE_SERIES);
                        }
                    });
                }
            }
            return v;
        }
    }

    /**
     * タッチイベントを設定
     */
    private void setClickListener() {
        // 新規追加ボタン
        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = NewSeriesDialogActivity.getIntent(ListActivity.this, "作品情報を追加", "追加", "キャンセル");
                startActivityForResult(intent, G.REQUEST_CODE_LIST_ADD_SERIES);
//                long unix = Calendar.getInstance().getTimeInMillis() / 1000L;
//                SeriesData data = new SeriesData("タイトル"+unix);
//                data.mAuthor = "作者" + unix;
//                FilterDao.saveSeries(data);
//                refreshData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // リストのアイテム内の削除ボタン押下
            case G.REQUEST_CODE_LIST_ROW_DELETE_SERIES:
                if(resultCode == G.RESULT_POSITIVE) {
                    if (mSelectSeriesIds != null) {
                        for (int i = 0; i < mSelectSeriesIds.length; i++) {
                            FilterDao.deleteSeries(mSelectSeriesIds[i]);
                        }
                    }
                    refreshData();
                }
                break;
            // 作品情報追加画面から戻ったとき
            // 基本的な保存処理は「作品情報追加画面」に任せる
            case G.REQUEST_CODE_LIST_ADD_SERIES:
                refreshData();
                break;
        }
    }
}

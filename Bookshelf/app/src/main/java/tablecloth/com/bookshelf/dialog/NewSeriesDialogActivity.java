package tablecloth.com.bookshelf.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tablecloth.com.bookshelf.db.FilterDao;
import tablecloth.com.bookshelf.db.SeriesData;
import tablecloth.com.bookshelf.R;
import tablecloth.com.bookshelf.util.G;
import tablecloth.com.bookshelf.util.ToastUtil;
import tablecloth.com.bookshelf.util.Util;

/**
 * タイトル・メッセージ・YES/NOボタンの要素を持ったダイアログ拡張クラス
 * Created by shnomura on 2014/08/17.
 */
public class NewSeriesDialogActivity extends DialogBaseActivity {

    public static SeriesData sSeriesData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        sSeriesData = new SeriesData();

        // テキスト設定
        ((TextView)findViewById(R.id.title)).setText(intent.getStringExtra(KEY_TITLE));
        ((TextView)findViewById(R.id.btn_positive)).setText(intent.getStringExtra(KEY_BTN_POSITIVE));
        ((TextView)findViewById(R.id.btn_negative)).setText(intent.getStringExtra(KEY_BTN_NEGATIVE));

        // ヒント設定
        View view = findViewById(R.id.data_detail_row_title);
        ((EditText)view.findViewById(R.id.data_content)).setHint("※必須");

        // 保存ボタン
        ((TextView)findViewById(R.id.btn_positive)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = findViewById(R.id.data_detail_row_title);
                String title = ((EditText) view.findViewById(R.id.data_content)).getText().toString();
                // タイトルは必須情報なので、無い場合は登録させない
                if (title == null || title.length() <= 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(NewSeriesDialogActivity.this, "タイトルが未入力です");
                        }
                    });
                    return;
                }
                sSeriesData.mTitle = title;
                FilterDao.saveSeries(sSeriesData);
                NewSeriesDialogActivity.this.finish();
            }
        });
        ((TextView)findViewById(R.id.btn_negative)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewSeriesDialogActivity.this.finish();
            }
        });

        // 追加ボタン
        findViewById(R.id.btn_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 要素を追加するためのダイアログを更に開く
                Intent intent = DataAddDialogActivity.getIntent(NewSeriesDialogActivity.this, "追加する情報を設定", "設定", "キャンセル");
                startActivityForResult(intent, G.REQUEST_CODE_NEW_SERIES_DETAIL);

                View view = findViewById(R.id.data_detail_row_title);
                try {
                    sSeriesData.mTitle = ((EditText) view.findViewById(R.id.data_content)).getText().toString();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_series_dialog;
    }

    public static Intent getIntent(Context context, String title, String btnPositive, String btnNegative) {
        Intent intent = new Intent(context, NewSeriesDialogActivity.class);

        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(KEY_BTN_POSITIVE, btnPositive);
        intent.putExtra(KEY_BTN_NEGATIVE, btnNegative);

        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // リストのアイテム内の削除ボタン押下
            case G.REQUEST_CODE_NEW_SERIES_DETAIL:
//                if(resultCode == G.RESULT_POSITIVE) {
//                    switch (resultCode) {
//                        case G.SELECT_DATA_TITLE:
//                            getLayoutInflater().inflate(R.layout.data_detail_row, null);
//                            break;
//                        case G.SELECT_DATA_TITLE_PRONUNCIATION:
//                            break;
//                        case G.SELECT_DATA_AUTHOR:
//                            break;
//                        case G.SELECT_DATA_AUTHOR_PRONUNCIATION:
//                            break;
//                        case G.SELECT_DATA_COMPANY:
//                            break;
//                        case G.SELECT_DATA_COMPANY_PRONUNCIATION:
//                            break;
//                        case G.SELECT_DATA_IMAGE:
//                            ///TODO イメージ保存
//                            break;
//                        case G.SELECT_DATA_VOLUME:
//                            break;
//                        case G.SELECT_DATA_TAGS:
//                            break;
//                    }
                    refreshData();
//                }
                break;
        }
    }

    private void refreshData() {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.data_container);
        linearLayout.removeAllViews();

        LayoutInflater inflater = getLayoutInflater();

        if(!Util.isEmpty(sSeriesData.mTitle)) {
            RelativeLayout relativeLayout = (RelativeLayout)inflater.inflate(R.layout.data_detail_row, null);
            ((TextView)relativeLayout.findViewById(R.id.data_name)).setText("タイトル");
            ((EditText)relativeLayout.findViewById(R.id.data_content)).setText(sSeriesData.mTitle);
            linearLayout.addView(relativeLayout);
        }
        if(!Util.isEmpty(sSeriesData.mTitlePronunciation)) {
            RelativeLayout relativeLayout = (RelativeLayout)inflater.inflate(R.layout.data_detail_row, null);
            ((TextView)relativeLayout.findViewById(R.id.data_name)).setText("タイトル（読み）");
            ((EditText)relativeLayout.findViewById(R.id.data_content)).setText(sSeriesData.mTitlePronunciation);
            linearLayout.addView(relativeLayout);
        }
        if(!Util.isEmpty(sSeriesData.mAuthor)) {
            RelativeLayout relativeLayout = (RelativeLayout)inflater.inflate(R.layout.data_detail_row, null);
            ((TextView)relativeLayout.findViewById(R.id.data_name)).setText("作者");
            ((EditText)relativeLayout.findViewById(R.id.data_content)).setText(sSeriesData.mAuthor);
            linearLayout.addView(relativeLayout);
        }
        if(!Util.isEmpty(sSeriesData.mTitlePronunciation)) {
            RelativeLayout relativeLayout = (RelativeLayout)inflater.inflate(R.layout.data_detail_row, null);
            ((TextView)relativeLayout.findViewById(R.id.data_name)).setText("作者（読み）");
            ((EditText)relativeLayout.findViewById(R.id.data_content)).setText(sSeriesData.mAuthorPronunciation);
            linearLayout.addView(relativeLayout);
        }
        if(!Util.isEmpty(sSeriesData.mCompany)) {
            RelativeLayout relativeLayout = (RelativeLayout)inflater.inflate(R.layout.data_detail_row, null);
            ((TextView)relativeLayout.findViewById(R.id.data_name)).setText("出版社");
            ((EditText)relativeLayout.findViewById(R.id.data_content)).setText(sSeriesData.mCompany);
            linearLayout.addView(relativeLayout);
        }
        if(!Util.isEmpty(sSeriesData.mCompanyPronunciation)) {
            RelativeLayout relativeLayout = (RelativeLayout)inflater.inflate(R.layout.data_detail_row, null);
            ((TextView)relativeLayout.findViewById(R.id.data_name)).setText("出版社（読み）");
            ((EditText)relativeLayout.findViewById(R.id.data_content)).setText(sSeriesData.mCompanyPronunciation);
            linearLayout.addView(relativeLayout);
        }
        if(sSeriesData.mVolumeList != null && sSeriesData.mVolumeList.size() > 0) {
            RelativeLayout relativeLayout = (RelativeLayout)inflater.inflate(R.layout.data_detail_row, null);
            ((TextView)relativeLayout.findViewById(R.id.data_name)).setText("所持巻数");
            ((EditText)relativeLayout.findViewById(R.id.data_content)).setText(sSeriesData.getVolumeString());
            linearLayout.addView(relativeLayout);
        }
        if(sSeriesData.mTagsList != null && sSeriesData.mTagsList.size() > 0) {
            for(int i = 0 ; i < sSeriesData.mTagsList.size() ; i ++) {
                RelativeLayout relativeLayout = (RelativeLayout)inflater.inflate(R.layout.data_detail_row, null);
                ((TextView)relativeLayout.findViewById(R.id.data_name)).setText("タグ");
                ((EditText)relativeLayout.findViewById(R.id.data_content)).setText(sSeriesData.mTagsList.get(i));
                linearLayout.addView(relativeLayout);
            }
        }
    }

}

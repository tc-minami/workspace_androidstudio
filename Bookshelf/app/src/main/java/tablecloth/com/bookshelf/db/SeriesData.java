package tablecloth.com.bookshelf.db;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by shnomura on 2014/08/16.
 */
public class SeriesData {
    public int mSeriesId;
    public String mTitle;
    public String mTitlePronunciation;
    public String mAuthor;
    public String mAuthorPronunciation;
    public String mCompany;
    public String mCompanyPronunciation;
    public Bitmap mImage;

    public ArrayList<String> mTagsList;
    public boolean mIsSeriesEnd = false;
    public ArrayList<Integer> mVolumeList;

    public long mInitUpdateUnix;
    public long mLastUpdateUnix;

    public SeriesData(String seriesName) {
        mTitle = seriesName;
        mVolumeList = new ArrayList<Integer>();
        mTagsList = new ArrayList<String>();
    }

    public SeriesData() {
        mVolumeList = new ArrayList<Integer>();
        mTagsList = new ArrayList<String>();
    }


    /**
     * 小さい巻数->最大巻数の順番入っている前提
     * @param volume
     */
    public void addVolume(int volume) {
        // 新しく所持巻情報を追加
        mVolumeList.add(volume);
    }

    public String getVolumeString() {
        String ret = "";
        int prevVolume = 0; // 最後に所持情報がある巻数
        int firstContinueVolume = 0; // 連番が始まった巻数
        int firstVolume = 0; // はじめに記録された巻数
        ///TODO ここでソート処理

        // 所持している巻数を小さいものから順番に見ていく
        for(int i = 0 ; i < mVolumeList.size() ; i ++ ) {
            int value = mVolumeList.get(i);

            // はじめの巻専用
            if(i == 0) {
                ret += value;
                firstVolume = value;
            }
            // 最終巻専用
            else if(i == mVolumeList.size() - 1) {
                if(value != firstVolume) {
                    ret += value;
                }
                ret += "巻";
            }
            // １つ前の巻が「数値的に１つ前の巻」出なかった場合、表記を変更
            // （１冊目以外）
            else if(value - 1 != prevVolume && prevVolume != 0) {
                // 「連番が始まった巻数」と「連番が終わった巻数」が同じの場合
                // 例）１巻、３巻、５巻〜１０巻を所持してい場合の１巻と３巻
                if(firstContinueVolume == prevVolume) {
                    ret += "、" + value;
                } else {
                    ret += "〜" + prevVolume + "、" + value;
                }
            }
            ret += String.valueOf(value);
        }
        if(mVolumeList.size() > 0) {
            ret += "巻";
        } else {
            ret += "所持巻なし";
        }
        return ret;
    }

}

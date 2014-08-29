package tablecloth.com.bookshelf.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;

import tablecloth.com.bookshelf.util.ImageUtil;

/**
 * Created by shnomura on 2014/08/16.
 * 指定のデータにて本情報の検索をかける用のクラス
 */
public class FilterDao {
    private static DB mDb = null; // DBクラス

    /**
     * シリーズ詳細の取得
     * @return
     */
//    public static SeriesData loadSeriesDetail(Context context, int series_id) {
//        instantiateDB(context);
//        return new SeriesData("sample");
//    }

    /**
     * シリーズ情報の保存
     * 最終更新日時を返すので、登録直後に本の情報を検索したい場合はそれを使う
     * @param data
     */
    public static long saveSeries(SeriesData data) {
        ContentValues contentValues = convertSeries2ContentValues(data);
        long nowUnix = Calendar.getInstance().getTimeInMillis() / 1000L;
        data.mLastUpdateUnix = nowUnix;
        if(isSeriesExist(data.mSeriesId)) {
            mDb.getSQLiteDatabase().update(DB.BookSeriesTable.TABLE_NAME, contentValues, DB.BookSeriesTable.SERIES_ID + " = ?", new String[]{Integer.toString(data.mSeriesId)});
        } else {
            data.mInitUpdateUnix = nowUnix;
            mDb.getSQLiteDatabase().insert(DB.BookSeriesTable.TABLE_NAME, null, contentValues);
        }
        return nowUnix;
    }

    /**
     * シリーズ情報の削除
     * 復元不可能なので要注意
     * @param seriesId
     * @return
     */
    public static void deleteSeries(int seriesId) {
        mDb.getSQLiteDatabase().delete(DB.BookSeriesTable.TABLE_NAME, DB.BookSeriesTable.SERIES_ID + " = ?", new String[]{Integer.toString(seriesId)});
    }

    /**
     * SeriesIDを取得、取得失敗した場合は-1を返す
     * @param lastUpdateUnix
     * @return
     */
    public static int getSeriesId(long lastUpdateUnix) {
        Cursor cursor = mDb.getSQLiteDatabase().rawQuery("SELECT " + DB.BookSeriesTable.SERIES_ID + " FROM " + DB.BookSeriesTable.TABLE_NAME + " WHERE " + DB.BookSeriesTable.LAST_UPDATE_UNIX + " = " + lastUpdateUnix, null);
        if(cursor.moveToFirst()) {
            // データが存在する
            return cursor.getInt(cursor.getColumnIndex(DB.BookSeriesTable.SERIES_ID));
        } else {
            // データが存在しない
            return -1;
        }

    }

    /**
     * そのシリーズが登録済みかを確認
     * @param seriesId
     * @return
     */
    private static boolean isSeriesExist(int seriesId) {
        // IDがそもそも設定されていない場合
        if(seriesId <= 0) {
            return false;
        }

        Cursor cursor = mDb.getSQLiteDatabase().rawQuery("SELECT * FROM " + DB.BookSeriesTable.TABLE_NAME + " WHERE " + DB.BookSeriesTable.SERIES_ID + " = " + seriesId, null);
        if(cursor.moveToFirst()) {
            // データが存在する
            return true;
        } else {
            // データが存在しない
            return false;
        }
    }

    /**
     * 全てのシリーズ情報の取得
     * @param context
     * @return
     */
    public static SeriesData[] loadSeries(Context context) {
        instantiateDB(context);

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(DB.BookSeriesTable.TABLE_NAME);

        Cursor cursor = mDb.getSQLiteDatabase().rawQuery(sql.toString(), null);
        SeriesData[] retData = null;

        // cursorからSeriesDataを生成
        if(cursor != null) {
            try {
                int max = cursor.getCount();
                retData = new SeriesData[max];
                for(int i = 0 ; i < max ; i ++) {
                    if(cursor.moveToNext()) {
                        String title = cursor.getString(cursor.getColumnIndex(DB.BookSeriesTable.TITLE_NAME));
                        retData[i] = new SeriesData(title);
                        retData[i].mSeriesId = cursor.getInt(cursor.getColumnIndex(DB.BookSeriesTable.SERIES_ID));
                        retData[i].mTitlePronunciation = cursor.getString(cursor.getColumnIndex(DB.BookSeriesTable.TITLE_PRONUNCIATION));
                        retData[i].mAuthor = cursor.getString(cursor.getColumnIndex(DB.BookSeriesTable.AUTHOR_NAME));
                        retData[i].mAuthorPronunciation = cursor.getString(cursor.getColumnIndex(DB.BookSeriesTable.AUTHOR_PRONUNCIATION));
                        retData[i].mCompany = cursor.getString(cursor.getColumnIndex(DB.BookSeriesTable.COMPANY_NAME));
                        retData[i].mCompanyPronunciation = cursor.getString(cursor.getColumnIndex(DB.BookSeriesTable.COMPANY_PRONUNCIATION));
                        retData[i].mImage = ImageUtil.convertByte2Bitmap(cursor.getBlob(cursor.getColumnIndex(DB.BookSeriesTable.IMAGE_DATA)));

                        String[] tagsStr = getTagsData(cursor.getString(cursor.getColumnIndex(DB.BookSeriesTable.TAGS)));
                        if(tagsStr != null) {
                            for(int k = 0 ; k < tagsStr.length ; k ++) {
                                retData[i].mTagsList.add(tagsStr[i]);
                            }
                        }
                        int isSeriesEnd = cursor.getInt(cursor.getColumnIndex(DB.BookSeriesTable.SERIES_IS_FINISH));
                        if(isSeriesEnd == 0) {
                            retData[i].mIsSeriesEnd = false;
                        } else {
                            retData[i].mIsSeriesEnd = true;
                        }
                        retData[i].mInitUpdateUnix = cursor.getLong(cursor.getColumnIndex(DB.BookSeriesTable.INIT_UPDATE_UNIX));
                        retData[i].mLastUpdateUnix = cursor.getLong(cursor.getColumnIndex(DB.BookSeriesTable.LAST_UPDATE_UNIX));

                        ///TODO 所持している巻数情報は後々
                        retData[i].addVolume(1);
                        retData[i].addVolume(2);
                        retData[i].addVolume(4);

                    // cursor情報取得失敗
                    } else {
                        break;
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return retData;
    }

    /**
     * 作品自体の詳細（配列）の取得
     * @param context
     * @param series_id
     * @return
     */
    public static BookData[] getBookData(Context context, int series_id) {
        instantiateDB(context);
        BookData[] data = new BookData[10];
        return data;
    }

    /**
     * DBの初期化処理
     * @param context
     */
    private static void instantiateDB(Context context) {
        if(mDb == null) {
            mDb = DB.getInstance(context);
        }
    }

    private static String[] getTagsData(String tagsStr) {
        String[] ret = null;
        if(tagsStr != null && tagsStr.length() > 0) {
            ret = tagsStr.split("|||");
        }
        return ret;
    }

    private static String getTagsStr(ArrayList<String> tagsStr) {
        String ret = null;
        if(tagsStr != null) {
            for(int i = 0 ; i < tagsStr.size() ; i ++) {
                if(i != 0) {
                    ret += "|||";
                }
                ret += tagsStr.get(i);
            }
        }
        return ret;
    }

    private static ContentValues convertSeries2ContentValues(SeriesData data) {
        ContentValues cv = new ContentValues();
        // SeriesIdが0以下（設定されていない場合）は登録しない
        if(data.mSeriesId > 0) {
            cv.put(DB.BookSeriesTable.SERIES_ID, data.mSeriesId);
        }
        cv.put(DB.BookSeriesTable.AUTHOR_NAME, data.mAuthor);
        cv.put(DB.BookSeriesTable.AUTHOR_PRONUNCIATION, data.mAuthorPronunciation);
        cv.put(DB.BookSeriesTable.TITLE_NAME, data.mTitle);
        cv.put(DB.BookSeriesTable.TITLE_PRONUNCIATION, data.mTitlePronunciation);
        cv.put(DB.BookSeriesTable.COMPANY_NAME, data.mCompany);
        cv.put(DB.BookSeriesTable.COMPANY_PRONUNCIATION, data.mCompanyPronunciation);
        cv.put(DB.BookSeriesTable.IMAGE_DATA, ImageUtil.convertBitmap2Byte(data.mImage));
        cv.put(DB.BookSeriesTable.TAGS, getTagsStr(data.mTagsList));
        cv.put(DB.BookSeriesTable.SERIES_IS_FINISH, data.mIsSeriesEnd);
        return cv;
    }

}

package tablecloth.com.bookshelf.util;

/**
 * あらゆるクラスで使用する用の定数を保格納する
 * Created by shnomura on 2014/08/17.
 */
public class G {
    final public static int RESULT_NONE = 0;
    final public static int RESULT_POSITIVE = 1;
    final public static int RESULT_NEGATIVE = 2;

    final public static int REQUEST_CODE_LIST_ROW_DELETE_SERIES = 1;
    final public static int REQUEST_CODE_LIST_ADD_SERIES = 2;
    final public static int REQUEST_CODE_NEW_SERIES_DETAIL = 3;

    final public static int DATA_TYPE_SERIES = 0;
    final public static int DATA_TYPE_BOOK = 1;

    final public static int SELECT_DATA_TITLE = 0;
    final public static int SELECT_DATA_TITLE_PRONUNCIATION = 1;
    final public static int SELECT_DATA_AUTHOR = 2;
    final public static int SELECT_DATA_AUTHOR_PRONUNCIATION = 3;
    final public static int SELECT_DATA_COMPANY = 4;
    final public static int SELECT_DATA_COMPANY_PRONUNCIATION = 5;
    final public static int SELECT_DATA_IMAGE = 6;
    final public static int SELECT_DATA_VOLUME = 7;
    final public static int SELECT_DATA_TAGS = 8;
}

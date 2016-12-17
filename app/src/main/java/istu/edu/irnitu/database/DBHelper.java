package istu.edu.irnitu.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = "LOG_TAG_DBH";

    private String id;
    private String theme;
    private String newsTimeDate;
    private String newsTitle;
    private String newsText;
    private String newsLink;
    private String imagesUrls;
    public static final String DATABASE_NAME = "irnitu_database.db";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME_RESOURCES = " resources_table ";
    public static final String TABLE_NAME_NEWS = " news_table ";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_UPDATE_TIME_DATE = "upd_time_date";
    public static final String COLUMN_UPDATE_TIME = "upd_time";
    public static final String COLUMN_RES_NAME = "res_name";
    public static final String COLUMN_IMG_URL = "img_url";
    public static final String COLUMN_RES_URL = "res_url";

    public static final String COLUMN_PUBLISH_DATE = "publish_date";
    public static final String COLUMN_NEWS_THEME = "news_theme";
    public static final String COLUMN_NEWS_TITLE = "news_title";
    public static final String COLUMN_NEWS_TEXT = "news_text";
    public static final String COLUMN_NEWS_LINK = "news_link";
    public static final String COLUMN_NEWS_IMAGES = "news_images";
    public static final String COLUMN_NEWS_IMAGE_HEADER = "news_image_header";

    public static final String SQL_CREATE_TABLE_NEWS =
            "CREATE TABLE IF NOT EXISTS" + TABLE_NAME_NEWS +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PUBLISH_DATE + " TEXT NOT NULL, " +
                    COLUMN_UPDATE_TIME + " TEXT NOT NULL, " +
                    COLUMN_NEWS_THEME + " TEXT NOT NULL, " +
                    COLUMN_NEWS_TITLE + " TEXT NOT NULL, " +
                    COLUMN_NEWS_TEXT + " TEXT, " +
                    COLUMN_NEWS_LINK + " TEXT NOT NULL, " +
                    COLUMN_NEWS_IMAGES + " TEXT, " +
                    COLUMN_NEWS_IMAGE_HEADER + " TEXT);";

    public static final String SQL_CREATE_TABLE_RESOURCES =
            "CREATE TABLE IF NOT EXISTS" + TABLE_NAME_RESOURCES +
                    "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_UPDATE_TIME_DATE + " TEXT NOT NULL, " +
                    COLUMN_UPDATE_TIME + " TEXT NOT NULL, " +
                    COLUMN_RES_NAME + " TEXT NOT NULL, " +
                    COLUMN_IMG_URL + " TEXT, " +
                    COLUMN_RES_URL + " TEXT);";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_RESOURCES);
        db.execSQL(SQL_CREATE_TABLE_NEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion <2){
            Log.d(LOG_TAG, "onUpgrade db " + DATABASE_VERSION);
            dropTable(db, TABLE_NAME_RESOURCES);
            dropTable(db, TABLE_NAME_NEWS);
            onCreate(db);
            db.setVersion(DATABASE_VERSION);
        }

    }

    public void dropTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public Cursor getAllEntries(String tableName) {
        Cursor cursor = null;
        SQLiteDatabase userDB = this.getReadableDatabase();
        try {
            cursor = userDB.query(tableName, null, null, null, null, null, null);
        } catch (SQLiteException ex) {
            ex.printStackTrace();
            Log.d(LOG_TAG, "SQLiteException " + tableName);
            Log.d(LOG_TAG, ex.toString());
        }
        return cursor;
    }
}

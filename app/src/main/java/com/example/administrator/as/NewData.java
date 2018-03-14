package com.example.administrator.as;

/**
 * Created by Administrator on 2018/2/6.
 */



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

    public class NewData extends SQLiteOpenHelper {
        public static final String CREATE_NEWS = "create table news ("
                + "id integer primary key autoincrement,"
                +"newsId integer unique,"
                + "date text,"
                + "title text,"
                + "image blob)";

        public static final String CREATE_DETAILED_NEWS = "create table detailedNews ("
                + "id integer primary key, "
                + "css text,"
                + "body text)";

        public static final String CREATE_TOP_NEWS = "create table topNews ("
                + "id integer primary key autoincrement,"
                +"newsId  integer unique,"//唯一
                + "date text,"
                + "title text,"
                + "image blob)";

        //private Context mContext;//如果要toast的话需要；

        public NewData(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
           // mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_NEWS);
            sqLiteDatabase.execSQL(CREATE_DETAILED_NEWS);
            sqLiteDatabase.execSQL(CREATE_TOP_NEWS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists news");
        sqLiteDatabase.execSQL("drop table if exists detailedNews ");
        sqLiteDatabase.execSQL("drop table if exists topNews");
        onCreate(sqLiteDatabase);
        //可写可不写
        }
    }


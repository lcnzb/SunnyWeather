package com.example.coolweather.db;// 替换为Android原生SQLite包（新手首选，无依赖问题）
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库帮助类：创建省/市/县三张表，使用原生SQLiteOpenHelper（避免AndroidX适配坑）
 * 包名：com.example.coolweather.db
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
    // 数据库名和版本号（抽离常量，便于维护）
    private static final String DB_NAME = "cool_weather.db";
    private static final int DB_VERSION = 1;

    // Province表建表语句（SQL规范：表名/字段名小写）
    public static final String CREATE_PROVINCE = "create table province (" +
            "id integer primary key autoincrement, " +
            "province_name text, " +
            "province_code text)";

    // City表建表语句
    public static final String CREATE_CITY = "create table city (" +
            "id integer primary key autoincrement, " +
            "city_name text, " +
            "city_code text, " +
            "province_id integer)";

    // County表建表语句
    public static final String CREATE_COUNTY = "create table county (" +
            "id integer primary key autoincrement, " +
            "county_name text, " +
            "county_code text, " +
            "city_id integer)";

    /**
     * 原生SQLiteOpenHelper标准构造方法（无任何适配问题）
     */
    public CoolWeatherOpenHelper(Context context) {
        // 参数说明：上下文、数据库名、游标工厂（null为默认）、版本号
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * 数据库首次创建时执行：创建三张表（重写方法参数为SQLiteDatabase，匹配父类）
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    /**
     * 数据库版本升级时执行（初期无需实现，后续可扩展）
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 示例：升级时删除旧表（实际开发需考虑数据迁移）
        db.execSQL("drop table if exists province");
        db.execSQL("drop table if exists city");
        db.execSQL("drop table if exists county");
        onCreate(db);
    }
}
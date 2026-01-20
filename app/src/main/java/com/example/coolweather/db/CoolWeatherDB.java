package com.example.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作工具类（单例模式）：处理省/市/县数据的增删改查
 * 解决问题：1.参数数量不匹配 2.getColumnIndex返回-1的异常处理
 */
public class CoolWeatherDB {
    // 单例实例
    private static CoolWeatherDB instance;
    // 数据库对象
    private SQLiteDatabase db;

    // 私有化构造方法（单例模式）
    private CoolWeatherDB(Context context) {
        // 获取ApplicationContext，避免内存泄漏
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context.getApplicationContext());
        // 获取可写数据库
        db = dbHelper.getWritableDatabase();
    }

    // 同步方法：获取单例实例（线程安全）
    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (instance == null) {
            instance = new CoolWeatherDB(context);
        }
        return instance;
    }

    // ------------------------ 省份数据操作 ------------------------
    /**
     * 保存省份数据到数据库
     */
    public void saveProvince(Province province) {
        if (province == null) return;

        ContentValues values = new ContentValues();
        // 错误修复：ContentValues.put仅需2个参数（列名+值），避免传4个参数
        values.put("province_name", province.getProvinceName());
        values.put("province_code", province.getProvinceCode());
        // 插入数据（参数：表名、nullColumnHack、ContentValues）
        db.insert("province", null, values);
    }

    /**
     * 查询所有省份数据
     */
    public List<Province> loadProvinces() {
        List<Province> provinceList = new ArrayList<>();
        // 查询语句：获取所有省份，按id升序
        Cursor cursor = db.query("province", null, null, null, null, null, "id asc");

        // 错误修复：Cursor操作必须try-catch-finally，避免内存泄漏；处理getColumnIndex=-1的情况
        try {
            if (cursor.moveToFirst()) {
                do {
                    Province province = new Province();
                    // 关键修复：先获取列索引，判断≥0后再使用（避免-1）
                    int idIndex = cursor.getColumnIndex("id");
                    int nameIndex = cursor.getColumnIndex("province_name");
                    int codeIndex = cursor.getColumnIndex("province_code");

                    // 仅当列索引有效时赋值（避免-1报错）
                    if (idIndex >= 0) {
                        province.setId(cursor.getInt(idIndex));
                    }
                    if (nameIndex >= 0) {
                        province.setProvinceName(cursor.getString(nameIndex));
                    }
                    if (codeIndex >= 0) {
                        province.setProvinceCode(cursor.getString(codeIndex));
                    }
                    provinceList.add(province);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 必须关闭Cursor，释放资源
            if (cursor != null) {
                cursor.close();
            }
        }
        return provinceList;
    }

    // ------------------------ 城市数据操作 ------------------------
    /**
     * 保存城市数据到数据库
     */
    public void saveCity(City city) {
        if (city == null) return;

        ContentValues values = new ContentValues();
        values.put("city_name", city.getCityName());
        values.put("city_code", city.getCityCode());
        values.put("province_id", city.getProvinceId());
        db.insert("city", null, values);
    }

    /**
     * 根据省份id查询城市数据
     */
    public List<City> loadCities(int provinceId) {
        List<City> cityList = new ArrayList<>();
        // 查询条件：province_id = 传入的省份id
        Cursor cursor = db.query("city", null, "province_id = ?",
                new String[]{String.valueOf(provinceId)}, null, null, "id asc");

        try {
            if (cursor.moveToFirst()) {
                do {
                    City city = new City();
                    int idIndex = cursor.getColumnIndex("id");
                    int nameIndex = cursor.getColumnIndex("city_name");
                    int codeIndex = cursor.getColumnIndex("city_code");
                    int provinceIdIndex = cursor.getColumnIndex("province_id");

                    if (idIndex >= 0) city.setId(cursor.getInt(idIndex));
                    if (nameIndex >= 0) city.setCityName(cursor.getString(nameIndex));
                    if (codeIndex >= 0) city.setCityCode(cursor.getString(codeIndex));
                    if (provinceIdIndex >= 0) city.setProvinceId(cursor.getInt(provinceIdIndex));

                    cityList.add(city);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return cityList;
    }

    // ------------------------ 区县数据操作 ------------------------
    /**
     * 保存区县数据到数据库
     */
    public void saveCounty(County county) {
        if (county == null) return;

        ContentValues values = new ContentValues();
        values.put("county_name", county.getCountyName());
        values.put("county_code", county.getCountyCode());
        values.put("city_id", county.getCityId());
        db.insert("county", null, values);
    }

    /**
     * 根据城市id查询区县数据
     */
    public List<County> loadCounties(int cityId) {
        List<County> countyList = new ArrayList<>();
        Cursor cursor = db.query("county", null, "city_id = ?",
                new String[]{String.valueOf(cityId)}, null, null, "id asc");

        try {
            if (cursor.moveToFirst()) {
                do {
                    County county = new County();
                    int idIndex = cursor.getColumnIndex("id");
                    int nameIndex = cursor.getColumnIndex("county_name");
                    int codeIndex = cursor.getColumnIndex("county_code");
                    int cityIdIndex = cursor.getColumnIndex("city_id");

                    if (idIndex >= 0) county.setId(cursor.getInt(idIndex));
                    if (nameIndex >= 0) county.setCountyName(cursor.getString(nameIndex));
                    if (codeIndex >= 0) county.setCountyCode(cursor.getString(codeIndex));
                    if (cityIdIndex >= 0) county.setCityId(cursor.getInt(cityIdIndex));

                    countyList.add(county);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return countyList;
    }
}
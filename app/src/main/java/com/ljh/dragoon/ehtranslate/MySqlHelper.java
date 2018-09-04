package com.ljh.dragoon.ehtranslate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dragoon on 2018/7/27.
 */

public class MySqlHelper extends SQLiteOpenHelper {
    private Context context;

    public MySqlHelper(Context context) {
        super(context, "ehtranslate.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        InputStream ins = null;
        BufferedReader in = null;
        Pattern patt = Pattern.compile("http.*?\\.jpg");
        db.beginTransaction();
        //创建表
        String creatTb1 = "CREATE TABLE 'type' (" +
                "	'name'	TEXT," +
                "	PRIMARY KEY('name')" +
                ");";
        String creatTb2 = "CREATE TABLE 'data' (" +
                "	'en'	TEXT," +
                "	'cn'	TEXT," +
                "	'url'	TEXT," +
                "	'type'	TEXT," +
                "	FOREIGN KEY('type') REFERENCES 'type'('name')" +
                ");";
        //插入数据到表
        try {
            db.execSQL(creatTb1);
            db.execSQL(creatTb2);
            String[] filess = context.getAssets().list("");
            List<String> files = new ArrayList<>();
            for (String i : filess) {
                if (i.indexOf('.') > 0)
                    files.add(i);
            }

            String[] types = new String[files.size()];
            for (int i = 0; i < files.size(); i++) {
                types[i] = files.get(i).substring(0, files.get(i).length() - 3);
                db.execSQL("insert into type values(?)", new String[]{types[i]});
            }
            for (int i = 0; i < files.size(); i++) {
                ins = context.getAssets().open(files.get(i));
                in = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
                int j = 1;
                String str, en, cn, url, mess;
                while (in.ready()) {
                    j++;
                    str = in.readLine();
                    if (str == null || str.length() < 1 || str.charAt(0) != '|') continue;
                    String[] strs = str.split("\\|");
                    if (strs.length < 4) continue;
                    strs[1] = strs[1].trim();
                    strs[2] = strs[2].trim();
                    Matcher matcher = patt.matcher(strs[3]);
                    if (matcher.find()) {
                        strs[3] = matcher.group();
                    } else strs[3] = "";
                    db.execSQL("insert into data (en,cn,url,type) values(?,?,?,?)",
                            new String[]{strs[1], strs[2], strs[3], types[i]});
                }
                Log.i("my", types[i] + "加载完成");
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

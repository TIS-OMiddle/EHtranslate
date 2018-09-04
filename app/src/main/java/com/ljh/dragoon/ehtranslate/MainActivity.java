package com.ljh.dragoon.ehtranslate;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText et;
    private ListView lv;
    private Button bt_search;
    private Button bt_set;
    private MyAdapter mAdapter;
    List<MyTranslateBean> list;
    private MySqlHelper sqlHelper;
    private boolean[] userChoose=new boolean[1];//目前仅一个选项...
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = findViewById(R.id.et);
        lv = findViewById(R.id.lv);
        bt_search = findViewById(R.id.bt_search);
        bt_set=findViewById(R.id.bt_set);
        list = new ArrayList<>();
        mAdapter = new MyAdapter(list, getApplicationContext());
        sqlHelper = new MySqlHelper(getApplicationContext());
        sp=getSharedPreferences("data",MODE_PRIVATE);
        init();
    }


    private void init() {
        userChoose[0] = sp.getBoolean("show", true);

        lv.setAdapter(mAdapter);

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    list.clear();
                    String text = et.getText().toString();
                    SQLiteDatabase db = sqlHelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("select * from data where cn like ?", new String[]{"%" + text + "%"});
                    if (cursor.moveToFirst()) {
                        String en = cursor.getString(0), cn = cursor.getString(1),
                                url = cursor.getString(2), type = cursor.getString(3);
                        MyTranslateBean item = new MyTranslateBean(en, cn, url, type);
                        list.add(item);
                        while (cursor.moveToNext()) {
                            en = cursor.getString(0);
                            cn = cursor.getString(1);
                            url = cursor.getString(2);
                            type = cursor.getString(3);
                            item = new MyTranslateBean(en, cn, url, type);
                            list.add(item);
                        }
                    }
                    cursor.close();
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.i("myerror", e.getMessage());
                }
            }
        });

        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    new AlertDialog.Builder(MainActivity.this).setTitle("设置")
                            .setIcon(R.mipmap.ic_launcher_round)
                            .setMultiChoiceItems(new String[]{"显示图片"}, userChoose, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    userChoose[which]=isChecked;
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAdapter.setVisiable(userChoose[0]);
                                    SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                                    editor.putBoolean("show",userChoose[0]);
                                    editor.commit();
                                }
                            })
                            .show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String en=list.get(position).getEn();
                ClipboardManager clipboardManager=(ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("ehtranslate",en);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(),en,Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}

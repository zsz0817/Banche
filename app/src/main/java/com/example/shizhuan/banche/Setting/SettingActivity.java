package com.example.shizhuan.banche.Setting;

/**
 * Created by ShiZhuan on 2018/1/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.shizhuan.banche.MainActivity;
import com.example.shizhuan.banche.R;
import com.example.shizhuan.banche.Search.SearchActivity;

public class SettingActivity extends Activity implements View.OnClickListener{

    ImageButton imageButton1,imageButton2,imageButton3;
    Intent intent;
    private LinearLayout choose_line,check_line,urge,notice,setting;


    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.setting);
        imageButton1 = (ImageButton)findViewById(R.id.ImageButton1);
        imageButton2 = (ImageButton)findViewById(R.id.ImageButton2);
        imageButton3 = (ImageButton)findViewById(R.id.ImageButton3);
        choose_line = (LinearLayout)findViewById(R.id.choose_line1);
        check_line = (LinearLayout)findViewById(R.id.check_line1);
        urge = (LinearLayout)findViewById(R.id.urge1);
        notice = (LinearLayout)findViewById(R.id.notice1);
        setting = (LinearLayout)findViewById(R.id.setting1);

        choose_line.setOnClickListener(this);
        check_line.setOnClickListener(this);
        urge.setOnClickListener(this);
        notice.setOnClickListener(this);
        setting.setOnClickListener(this);

        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ImageButton1:
                intent = new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.ImageButton2:
                intent = new Intent(SettingActivity.this,SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.ImageButton3:
//                intent = new Intent(SettingActivity.this,SettingActivity.class);
//                startActivity(intent);
                break;
            case R.id.choose_line1:
//                intent = new Intent(SettingActivity.this,ChooseLineActivity.class);
//                startActivity(intent);
                break;
            case R.id.check_line1:
//                intent = new Intent(SettingActivity.this,CheckLineActivity.class);
//                startActivity(intent);
                break;
            case R.id.urge1:
                intent = new Intent(SettingActivity.this,feedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.notice1:
                intent = new Intent(SettingActivity.this,MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.setting1:
//                intent = new Intent(SettingActivity.this,SearchActivity.class);
//                startActivity(intent);
                break;
        }
    }

}


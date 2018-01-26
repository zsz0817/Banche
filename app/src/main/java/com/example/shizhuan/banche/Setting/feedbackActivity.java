package com.example.shizhuan.banche.Setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.shizhuan.banche.R;

/**
 * Created by Administrator on 2015/7/16.
 */
public class feedbackActivity extends Activity {
    private EditText feedback;
    private RelativeLayout right_back;
    private Button send;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        feedback = (EditText) findViewById(R.id.feedback_edit);
        right_back = (RelativeLayout) findViewById(R.id.right_back_feedback);
        right_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(feedbackActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(feedbackActivity.this, SettingActivity.class);
                startActivity(intent);
                Toast.makeText(feedbackActivity.this, "感谢您的宝贵意见！！！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.wang.injectview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wang.injectview.annotation.ContentView;
import com.example.wang.injectview.annotation.OnClick;
import com.example.wang.injectview.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @ViewInject(R.id.btn_01)
    private Button mBtn1;
    @ViewInject(R.id.btn_02)
    private Button mBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjectUtils.injectContentView(this);

        mBtn1.setOnClickListener(this);
//        mBtn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_01) {
            Toast.makeText(this, "按钮1点击了", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.btn_02) {
            Toast.makeText(this, "按钮2点击了", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.btn_01, R.id.btn_02})
    public void clickBtnInvoked(View view) {
        if (view.getId() == R.id.btn_01) {
            Toast.makeText(this, "按钮1点击了", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.btn_02) {
            Toast.makeText(this, "按钮2点击了", Toast.LENGTH_SHORT).show();
        }
    }
}

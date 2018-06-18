package com.example.nguyenducke.lovenotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenducke.lovenotes.DatabaseHelper.DatabaseHelper;
import com.example.nguyenducke.lovenotes.Models.Login;

/**
 * Created by PHANTHELINH on 12/22/2017.
 */

public class LoginActivity extends AppCompatActivity {

    final int MAX_PASS_LENGTH = 6;
    Button btnone, btntwo, btnthree, btnfour, btnfive, btnsix, btnseven, btneight, btnnine, btnzero;
    ImageView btnclear, btnok;
    TextView  tvThongBao;
    EditText txtPass;
    String password, textInput;
    boolean isNew;
    DatabaseHelper db;
    Login login;
    AlertDialog dialog;

    private AlphaAnimation buttonClick = new AlphaAnimation(2.F,0.F);//Hieu ung click button
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Init();

        try{
            password = login.getPassword();

            if(password.isEmpty()||password.equals("")){
                isNew =true;
                tvThongBao.setText("Đặt mật khẩu");
            }
        }
        catch (Exception e){

        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(txtPass.getWindowToken(),0);
        //An keyboard khi cham vao thanh text
    }

    public void Init()
    {
        db = new DatabaseHelper(this);
        login = new Login(db);
        textInput = new String();
        isNew = false;

        btnone = (Button) findViewById(R.id.btnone);
        btntwo = (Button) findViewById(R.id.btntwo);
        btnthree = (Button) findViewById(R.id.btnthree);
        btnfour = (Button) findViewById(R.id.btnfour);
        btnfive = (Button) findViewById(R.id.btnfive);
        btnsix = (Button) findViewById(R.id.btnsix);
        btnseven = (Button) findViewById(R.id.btnseven);
        btneight = (Button) findViewById(R.id.btneight);
        btnnine = (Button) findViewById(R.id.btnnine);
        btnzero = (Button) findViewById(R.id.btnzero);
        btnclear = (ImageView) findViewById(R.id.btnclear);
        btnok = (ImageView) findViewById(R.id.btnok);
        tvThongBao = (TextView) findViewById(R.id.thongbao);
        txtPass = (EditText) findViewById(R.id.txtPass);
    }

    //region Cac phim so
    public void btnone_Click(View v){
        v.startAnimation(buttonClick);
        if(textInput.length()<MAX_PASS_LENGTH)
            textInput += "1";
        txtPass.setText(textInput);
    }

    public void btntwo_Click(View v){
        v.startAnimation(buttonClick);
        if(textInput.length()<MAX_PASS_LENGTH)
            textInput += "2";
        txtPass.setText(textInput);
    }

    public void btnthree_Click(View v){
        v.startAnimation(buttonClick);
        if(textInput.length()<MAX_PASS_LENGTH)
            textInput += "3";
        txtPass.setText(textInput);
    }

    public void btnfour_Click(View v){
        v.startAnimation(buttonClick);
        if(textInput.length()<MAX_PASS_LENGTH)
            textInput += "4";
        txtPass.setText(textInput);
    }

    public void btnfive_Click(View v){
        v.startAnimation(buttonClick);
        if(textInput.length()<MAX_PASS_LENGTH)
            textInput += "5";
        txtPass.setText(textInput);
    }

    public void btnsix_Click(View v){
        v.startAnimation(buttonClick);
        if(textInput.length()<MAX_PASS_LENGTH)
            textInput += "6";
        txtPass.setText(textInput);
    }

    public void btnseven_Click(View v){
        v.startAnimation(buttonClick);
        if(textInput.length()<MAX_PASS_LENGTH)
            textInput += "7";
        txtPass.setText(textInput);
    }

    public void btneight_Click(View v){
        v.startAnimation(buttonClick);
        if(textInput.length()<MAX_PASS_LENGTH)
            textInput += "8";
        txtPass.setText(textInput);
    }

    public void btnnine_Click(View v){
        v.startAnimation(buttonClick);
        if(textInput.length()<6)
            textInput += "9";
        txtPass.setText(textInput);
    }

    public void btnzero_Click(View v){
        v.startAnimation(buttonClick);
        if(textInput.length()<MAX_PASS_LENGTH)
            textInput += "0";
        txtPass.setText(textInput);
    }

    public void btnclear_Click(View v){
        v.startAnimation(buttonClick);
        if(textInput.length()>1)
            textInput = textInput.substring(0,textInput.length()-1);
        else {
            if(textInput.length()==1)
                textInput ="";
        }
        txtPass.setText(textInput);
    }

    public void btnok_Click(View v){
        v.startAnimation(buttonClick);
        //login.restoreDatabase();
        if(isNew){
            if(textInput.length()<MAX_PASS_LENGTH){
                Toast.makeText(this, "Mã bảo vệ phải gồm "+MAX_PASS_LENGTH+" ký số", Toast.LENGTH_SHORT).show();
            }
            else {
                login.setPassword(textInput);
                Toast.makeText(this,"Đã thiết lập thành công",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else{
            textInput = txtPass.getText().toString();
            if(password.equals(textInput)){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                tvThongBao.setText("Mã bảo vệ sai!");
            }
        }
    }
    //endregion

}



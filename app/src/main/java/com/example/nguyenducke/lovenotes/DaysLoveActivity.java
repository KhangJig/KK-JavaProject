package com.example.nguyenducke.lovenotes;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nguyenducke.lovenotes.DatabaseHelper.DatabaseHelper;
import com.example.nguyenducke.lovenotes.Models.CountDays;
import com.meg7.widget.SvgImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import javax.microedition.khronos.opengles.GL;

public class DaysLoveActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CAMERA = 111;
    private static final int REQUEST_CODE_FOLDER = 222;

    public static String LinkData = "";
    private static boolean checkPerson;
    private static int checkEditTextView = 0;

    private android.support.v7.widget.Toolbar toolbar;
    private FrameLayout frameLayout;
    private TextView txtTitle1;
    private TextView txtDays;
    private TextView txtTitle2;
    private LinearLayout linearLayoutPerson1;
    private SvgImageView imgPerson1;
    private TextView txtPerson1;
    private LinearLayout linearLayoutPerson2;
    private SvgImageView imgPerson2;
    private TextView txtPerson2;
    private TextView txtTuNgay;

    public Calendar now, calendar;
    private java.text.SimpleDateFormat simpleDateFormat;

    //use for execute SQL
    private DatabaseHelper databaseHelper;
    private CountDays countDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days_love);

        initDB(); //initialize database

        InitUI();
        ActionBar();

        //region Sự kiện click sửa hiển thị nền
        final String[] options = {"Đổi ngày", "Chỉnh sửa tiêu đề trên", "Chỉnh sửa tiêu đề dưới"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, options);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tuỳ chọn");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        DoiNgay();
                        break;
                    case 1:
                        checkEditTextView = 3;
                        doClickEditText();
                        //Toast.makeText(DaysLoveActivity.this, "Chỉnh sửa tiêu đề trên", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        checkEditTextView = 4;
                        doClickEditText();
                        //Toast.makeText(DaysLoveActivity.this, "Chỉnh sửa tiêu đề dưới", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

        final AlertDialog alertDialog = builder.create();
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });
        //endregion

        //region Sự kiện click sửa hiển thị người 1
        final String[] optionsPerson1 = {"Đổi ảnh đại diện", "Sửa tên hiển thị"};
        ArrayAdapter<String> adapterPerson1 = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, optionsPerson1);
        final AlertDialog.Builder builderPerson1 = new AlertDialog.Builder(this);

        builderPerson1.setAdapter(adapterPerson1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        checkPerson = true;
                        doClickDoiAnhDaiDien();
                        break;
                    case 1:
                        checkEditTextView = 1;
                        doClickEditText();
                        break;
                    default:
                        break;
                }
            }
        });
        final AlertDialog alertDialogPerson1 = builderPerson1.create();
        linearLayoutPerson1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogPerson1.show();
            }
        });
        //endregion

        //region Sự kiện click sửa hiển thị người 2
        final String[] optionsPerson2 = {"Đổi ảnh đại diện", "Sửa tên hiển thị"};
        ArrayAdapter<String> adapterPerson2 = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, optionsPerson2);
        final AlertDialog.Builder builderPerson2 = new AlertDialog.Builder(this);
        builderPerson2.setAdapter(adapterPerson2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        checkPerson = false;
                        doClickDoiAnhDaiDien();
                        break;
                    case 1:
                        checkEditTextView = 2;
                        doClickEditText();
                        break;
                    default:
                        break;
                }
            }
        });
        final AlertDialog alertDialogPerson2 = builderPerson2.create();
        linearLayoutPerson2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogPerson2.show();
            }
        });
        //endregion

        setViewFromDatabase();
    }


    //region Initialize Database
    private void initDB(){
        databaseHelper = new DatabaseHelper(this);
        countDays = new CountDays(databaseHelper);
    }

    //Load man hinh chinh tu csdl len
    private void setViewFromDatabase(){
        if(!countDays.getTitle1().equals(""))
            txtTitle1.setText(countDays.getTitle1());
        if(!countDays.getTitle2().equals(""))
            txtTitle2.setText(countDays.getTitle2());
        if(!countDays.getName1().equals(""))
            txtPerson1.setText(countDays.getName1());
        if(!countDays.getName2().equals(""))
            txtPerson2.setText(countDays.getName2());

            if(!countDays.getPic1().equals(""))
                Glide.with(this).load(countDays.getPic1()).into(imgPerson1);
            if(!countDays.getPic2().equals(""))
                Glide.with(this).load(countDays.getPic2()).into(imgPerson2);


        try {

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            if(!countDays.getStartDay().equals("")){
                Date startDay = df.parse(countDays.getStartDay());
                Date now = new Date();

                txtTuNgay.setText(df.format(startDay)+" - "+ df.format(now));

                long diff = Math.abs(now.getTime()- startDay.getTime());
                long days = diff/(24*60*60*1000);
                txtDays.setText(String.valueOf(days)); //Set so ngay da yeu
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    //endregion

    //region Ánh xạ view
    private void InitUI() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarDaysLove);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        imgPerson1 = (SvgImageView) findViewById(R.id.imgPerson1);
        imgPerson2 = (SvgImageView) findViewById(R.id.imgPerson2);

        txtDays = (TextView) findViewById(R.id.txtDays);
        txtPerson1 = (TextView) findViewById(R.id.txtPerson1);
        txtPerson2 = (TextView) findViewById(R.id.txtPerson2);
        txtTitle1 = (TextView) findViewById(R.id.txtTitle1);
        txtTitle2 = (TextView) findViewById(R.id.txtTitle2);
        txtTuNgay = (TextView) findViewById(R.id.txtTuNgay_dayslove);

        linearLayoutPerson1 = (LinearLayout) findViewById(R.id.linearLayoutPerson1);
        linearLayoutPerson2 = (LinearLayout) findViewById(R.id.linearLayoutPerson2);

        MainActivity.changeToolbarFont(toolbar, this);
    }
    //endregion

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //region Menu chính
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_days_love, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chupManHinh_menu_day_slove:
                Bitmap bitmap=takeScreenshot();
                saveBitMap(bitmap);
                break;
            case R.id.share_menu_days_love:
                Toast.makeText(this, "Chia sẻ", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    public void DoiNgay() {

        simpleDateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");

        now = Calendar.getInstance();
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                int daysLove = (int) ((now.getTimeInMillis() - calendar.getTimeInMillis()) / (1000 * 60 * 60 * 24));
                if (daysLove < 0) {
                    Toast.makeText(DaysLoveActivity.this, "Vui lòng chọn ngày trước ngày hiện tại!", Toast.LENGTH_SHORT).show();
                } else {
                    txtDays.setText(" " + daysLove);
                    String str = simpleDateFormat.format(calendar.getTime()) + " - " + simpleDateFormat.format(now.getTime());
                    txtTuNgay.setText(str);
                    //Luu ngay duoc chon vao csdl
                    countDays.setStartDay(simpleDateFormat.format(calendar.getTime()));
                    countDays.commit();
                }
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    //region Đổi ảnh từ camera or folder
    public void doClickDoiAnhDaiDien() {
        View mView = getLayoutInflater().inflate(R.layout.dialog_doianhdaidien, null);
        final ImageView imgCamera = (ImageView) mView.findViewById(R.id.imageCamera);
        final ImageView imgFolder = (ImageView) mView.findViewById(R.id.imageFolder);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(mView);
        alertDialog.setCancelable(false);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(DaysLoveActivity.this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                dialog.dismiss();
            }
        });

        imgFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_FOLDER);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        } else {
            Toast.makeText(DaysLoveActivity.this, "Bạn không cho phép mở camera", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Uri uri = data.getData();
            //luu csdl
            if (checkPerson) { //neu la nguoi 1
                countDays.setPic1(uri.toString());
                Glide.with(this).load(uri).into(imgPerson1);
                //imgPerson1.setImageBitmap(bitmap);
            } else {
                countDays.setPic2(uri.toString());
                Glide.with(this).load(uri).into(imgPerson2);
                //imgPerson2.setImageBitmap(bitmap);
            }
            countDays.commit();
        }
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();//đường đẫn trong android
            try {
                //InputStream inputStream = getContentResolver().openInputStream(uri);
                //Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (checkPerson) {
                    Glide.with(this).load(uri).into(imgPerson1);
                    countDays.setPic1(uri.toString());
                    //imgPerson1.setImageBitmap(bitmap);
                } else {
                    Glide.with(this).load(uri).into(imgPerson2);
                    countDays.setPic2(uri.toString());
                    //imgPerson2.setImageBitmap(bitmap);
                }
                countDays.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //endregion

    //region Chỉnh sửa text
    private void doClickEditText() {
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_text, null);
        final EditText edtNhapThongTin = (EditText) view.findViewById(R.id.edtNhap_dialog_edit_text);
        final Button btnHuy = (Button) view.findViewById(R.id.btnHuy_dialog_edit_text);
        final Button btnDongY = (Button) view.findViewById(R.id.btnDongY_dialog_edit_text);

        AlertDialog.Builder aler = new AlertDialog.Builder(this);
        aler.setView(view);
        aler.setCancelable(false);
        final AlertDialog dialog = aler.create();
        dialog.show();

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkEditTextView
                //1- tên hiển thị người 1
                //2- tên hiển thị người 2
                //3- chỉnh sửa tiêu đề trên
                //4- chỉnh sửa tiêu đề dưới
                switch (checkEditTextView) {
                    case 1:
                        txtPerson1.setText(edtNhapThongTin.getText().toString());
                        countDays.setName1(edtNhapThongTin.getText().toString());
                        dialog.dismiss();
                        break;
                    case 2:
                        txtPerson2.setText(edtNhapThongTin.getText().toString());
                        countDays.setName2(edtNhapThongTin.getText().toString());
                        dialog.dismiss();
                        break;
                    case 3:
                        txtTitle1.setText(edtNhapThongTin.getText().toString());
                        countDays.setTitle1(edtNhapThongTin.getText().toString());
                        dialog.dismiss();
                        break;
                    case 4:
                        txtTitle2.setText(edtNhapThongTin.getText().toString());
                        countDays.setTitle2(edtNhapThongTin.getText().toString());
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
                countDays.commit();
            }
        });
    }
    //endregion

    //region Chụp ảnh màn hình
    public Bitmap takeScreenshot(){
        View rootView=findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }
    public void saveBitMap(Bitmap bitmap){
        File pictureDirectory=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String pictureName=getPictureName();
        File imageFile=new File(pictureDirectory,pictureName);
        FileOutputStream fos;
        try{
            fos=new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            fos.close();
        }catch(FileNotFoundException e){
            Log.e("GREC",e.getMessage(),e);
        }catch(IOException e){
            Log.e("GREC",e.getMessage(),e);
        }
        Uri savedImageURI=Uri.parse(imageFile.getAbsolutePath());
        Toast.makeText(DaysLoveActivity.this, "Lưu hình ảnh thành công tại đường dẫn \n"+savedImageURI,Toast.LENGTH_LONG).show();
    }
    private String getPictureName(){
        SimpleDateFormat sdf=new SimpleDateFormat("ddMMyyyy_HHmmss");
        String time=sdf.format(new Date());
        return "Picture screenshot LoveNotes "+time+".png";
    }
    //endregion

}

package com.example.nguyenducke.lovenotes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nguyenducke.lovenotes.Adapter.Note;
import com.example.nguyenducke.lovenotes.DatabaseHelper.DatabaseHelper;

import java.util.Calendar;

import static com.example.nguyenducke.lovenotes.MainActivity.changeToolbarFont;

public class NotesActivity extends AppCompatActivity {

    private Context mcontext = NotesActivity.this;

    private static final String TAG = NotesActivity.class.getSimpleName();

    private static final int REQUEST_CODE_CAMERA = 111;
    private static final int REQUEST_CODE_FOLDER = 222;

    public static final int IMAGE_VIEW_VISIBLE = 1;
    public static final int IMAGE_VIEW_GONE = 2;
    public static int checkImageView;
    private Uri uriSaveImage;

    Note note;
    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private int mode;

    private boolean needRefresh;

    private Calendar calendarNgayTao;
    private String strNgayTao_edit;
    private java.text.SimpleDateFormat simpleDateFormat;

    private EditText edtTitle, edtContent;
    private ImageView imgContent;
    Button btnSave, btnCancle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        if (savedInstanceState == null) {
            Log.e(TAG, "NotesActivity is null");
        } else {
            Log.e(TAG, "NotesActivity is not null");
        }

        InitUI();
        ActionBar();

        Intent callerIntent = getIntent();
        note = (Note) callerIntent.getSerializableExtra("note");

        if (note == null) {
            mode = MODE_CREATE;
        } else {
            mode = MODE_EDIT;
            edtTitle.setText(note.getNoteTitle());
            edtContent.setText(note.getNoteContent());
            strNgayTao_edit = note.getNoteNgayTao();
            if (!note.getNotePicture().equals(" ")) {
                imgContent.setVisibility(View.VISIBLE);
                Glide.with(mcontext).load(Uri.parse(note.getNotePicture())).into(imgContent);
                //imgHinhAnh.setImageURI(Uri.parse(note.getNotePicture()));
            }
        }

        //region Save note
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(NotesActivity.this);
                String title = edtTitle.getText().toString();
                String content = edtContent.getText().toString();
                String Picture = " ";

                if (title.equals("") || content.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter title & content", Toast.LENGTH_LONG).show();
                    return;
                }

                //neu note khac null thi kiem tra xem co note da co anh chua
                if (note != null) {
                    if (checkImageView == IMAGE_VIEW_GONE && (!note.getNotePicture().equals(" "))) {
                        Picture = note.getNotePicture();
                    }
                }

                if (checkImageView == IMAGE_VIEW_VISIBLE) {
                    Picture = uriSaveImage.toString();
                }

                if (mode == MODE_CREATE) {

                    calendarNgayTao = Calendar.getInstance();
                    simpleDateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String strNgayTao_Create = "Ngày tạo: " + simpleDateFormat.format(calendarNgayTao.getTime());
                    note = new Note(title, content, Picture, strNgayTao_Create);
                    db.addNote(note);
                }
                if (mode == MODE_EDIT) {
                    note.setNoteTitle(title);
                    note.setNoteContent(content);
                    note.setNotePicture(Picture);
                    note.setNoteNgayTao(strNgayTao_edit);
                    db.updateNote(note);
                }
                needRefresh = true;
                // Trở lại MainActivity.
                onBackPressed();
            }
        });
        //endregion

        //region click Cancle note
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //endregion

//        Intent myIntent = new Intent(this , NotifyService.class);
//        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.HOUR, 0);
//        calendar.set(Calendar.AM_PM, Calendar.AM);
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*24 , pendingIntent);
    }

    //region finish NotesActivity trả về kết quả
    // Khi Activity này hoàn thành, có thể cần gửi phản hồi gì đó về cho Activity đã gọi nó.
    @Override
    public void finish() {
        // Chuan bi du lieu Intent.
        Intent data = new Intent();
        // yeu cau MainActivity refresh lai ListView or khong.
        data.putExtra("needRefresh", needRefresh);

        // Activity da hoan thanh OK, tra ve du lieu
        this.setResult(Activity.RESULT_OK, data);
        super.finish();
    }
    //endregion

    //region Menu chính
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.themHinhAnh:
                doClickThemHinhAnh();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

    private void InitUI() {
        edtTitle = (EditText) findViewById(R.id.edtTitle_view_note);
        edtContent = (EditText) findViewById(R.id.edtContent_view_note);
        toolbar = (Toolbar) findViewById(R.id.toolbar_view_note);

        imgContent = (ImageView) findViewById(R.id.img_view_note);
        imgContent.setVisibility(View.GONE);
        checkImageView = IMAGE_VIEW_GONE;

        btnCancle = (Button) findViewById(R.id.btnCancle_view_note);
        btnSave = (Button) findViewById(R.id.btnSave_view_note);

        changeToolbarFont(toolbar,this);

    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
    }

    //region them anh tu camera or folder
    public void doClickThemHinhAnh() {
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
                ActivityCompat.requestPermissions(NotesActivity.this,
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
            Toast.makeText(NotesActivity.this, "Bạn không cho phép mở camera", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        {
            if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                try {
                    uriSaveImage = uri;
                    imgContent.setVisibility(View.VISIBLE);
                    checkImageView = IMAGE_VIEW_VISIBLE;
                    Glide.with(mcontext).load(uri).into(imgContent);
                    //imgHinhAnh.setImageURI(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                uriSaveImage = uri;
                imgContent.setVisibility(View.VISIBLE);
                checkImageView = IMAGE_VIEW_VISIBLE;
                Glide.with(mcontext).load(uri).into(imgContent);
                //imgHinhAnh.setImageURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    //endregion


}

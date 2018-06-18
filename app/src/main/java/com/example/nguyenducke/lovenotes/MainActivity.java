package com.example.nguyenducke.lovenotes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyenducke.lovenotes.Adapter.Note;
import com.example.nguyenducke.lovenotes.Adapter.NoteArrayAdapter;
import com.example.nguyenducke.lovenotes.DatabaseHelper.DatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int MY_REQUEST_CODE = 100;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView nav_view;

    private RelativeLayout relativeLayout;

    private android.support.v7.widget.Toolbar toolbar;
    private ListView lvNotes;
    private FloatingActionButton btnThem;

    private final ArrayList<Note> noteList = new ArrayList<Note>();
    private NoteArrayAdapter adapter;

    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Log.e(TAG, "savedInstanceState is null");
        } else {
            Log.e(TAG, "savedInstanceState is not null");
        }

        InitUI();
        ActionBar();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //region Sự kiện click Navigation item
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.dayslove_menu_navi:
                        intent = new Intent(MainActivity.this, DaysLoveActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case R.id.remind_menu_navi:
                        //Toast.makeText(MainActivity.this, "Nhắc nhở", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.setting_menu_navi:
                        intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        //endregion


        //List notes
        DatabaseHelper db = new DatabaseHelper(this);
        //db.createDefaultNotesIfNeed();

        ArrayList<Note> list = db.getAllNotes();
        if (list.size()>0) {
            noteList.addAll(list);
//            adapter = new NoteArrayAdapter(this, R.layout.view_notes_item, noteList);
//
//            // Đăng ký Adapter cho ListView.
//            lvNotes.setAdapter(adapter);
        }

        adapter = new NoteArrayAdapter(this, R.layout.view_notes_item, noteList);

        // Đăng ký Adapter cho ListView.
        lvNotes.setAdapter(adapter);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                // Start AddEditNoteActivity, có phản hồi.
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
        });

        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Note selectedNote = (Note) adapter.getItem(i);
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);

                intent.putExtra("note", selectedNote);
                // Start AddEditNoteActivity, có phản hồi.
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
        });

        //region ListView Item long click
        final String[] options = {"Xoá"};
        ArrayAdapter<String> adapterDialog = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, options);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(adapterDialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        doClickDeleteNote();
                        break;
                    default:
                        break;
                }
            }
        });

        final AlertDialog alertDialog = builder.create();
        lvNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                alertDialog.show();
                position = i;
                return false;
            }
        });
        //endregion
    }
    private void doClickDeleteNote() {
        final Note selectedNote = (Note) adapter.getItem(position);
        new AlertDialog.Builder(this)
                .setMessage(selectedNote.getNoteTitle() + ". Bạn có chắc chắn muốn xoá ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteNote(selectedNote);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    // Nguoi dung dong y xoa mot note
    private void deleteNote(Note note) {
        DatabaseHelper db = new DatabaseHelper(this);
        db.deleteNote(note);
        this.noteList.remove(note);
        // Refresh ListView.
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE) {
            boolean needRefresh = data.getBooleanExtra("needRefresh", true);
            // Refresh ListView
            if (needRefresh) {
                this.noteList.clear();
                DatabaseHelper db = new DatabaseHelper(this);
                ArrayList<Note> list = db.getAllNotes();
                this.noteList.addAll(list);
                adapter.notifyDataSetChanged();
            }
        }
    }

    //region Sự kiện click menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_menu_detail:
                //Toast.makeText(MainActivity.this, "tim kiem", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //endregion

    //region Ánh xạ view
    public void InitUI() {
        drawerLayout = (DrawerLayout) findViewById(R.id.dl);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbarmanhinhchinh);
        lvNotes = (ListView) findViewById(R.id.listView_Note);
        btnThem = (FloatingActionButton) findViewById(R.id.faButtonAdd);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        changeToolbarFont(toolbar,this);
    }
    //endregion

    public void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    //region doi font toolbar
    public static void changeToolbarFont(Toolbar toolbar, Activity context) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(toolbar.getTitle())) {
                    applyFont(tv, context);
                    break;
                }
            }
        }
    }

    public static void applyFont(TextView tv, Activity context) {
        tv.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/fiolex_girl.ttf"));
    }
    //endregion
}

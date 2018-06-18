package com.example.nguyenducke.lovenotes.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nguyenducke.lovenotes.R;

import java.util.ArrayList;

public class NoteArrayAdapter extends ArrayAdapter<Note> {
    private Context context;
    private int layout;
    private ArrayList<Note> noteList;

    public NoteArrayAdapter(Context context, int resource, ArrayList<Note> objects) {
        super(context, resource, objects);
        this.context = context;
        this.noteList = objects;
        this.layout = resource;
    }

    private class ViewHolder {
        ImageView imgHinh;
        TextView txtTitle, txtContent, txtNgayTao;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            viewHolder = new ViewHolder();
            //initUI
            viewHolder.txtTitle = (TextView) view.findViewById(R.id.txtTitle_view_note_item);
            viewHolder.txtContent = (TextView) view.findViewById(R.id.txtContent_view_note_item);
            viewHolder.imgHinh = (ImageView) view.findViewById(R.id.image_view_note_item);
            viewHolder.imgHinh.setVisibility(View.GONE);
            viewHolder.txtNgayTao = (TextView) view.findViewById(R.id.txtNgayTao_view_note_item);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //gan gia tri
        Note note = noteList.get(position);

        if (note.getNotePicture().equals(" ")) {
            viewHolder.imgHinh.setVisibility(View.GONE);
            viewHolder.imgHinh.setImageResource(R.drawable.no_image);
        } else {
            viewHolder.imgHinh.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(note.getNotePicture());
            Glide.with(context).load(Uri.parse(note.getNotePicture())).into(viewHolder.imgHinh);
            //viewHolder.imgHinh.setImageURI(uri);
        }

        if (note.getNoteTitle().length() <= 10) {
            viewHolder.txtTitle.setText(note.getNoteTitle());
        } else {
            viewHolder.txtTitle.setText(note.getNoteTitle().substring(0, 10) + " ...");
        }

        if (note.getNoteContent().length() <= 20) {
            viewHolder.txtContent.setText(note.getNoteContent());
        } else {
            viewHolder.txtContent.setText(note.getNoteContent().substring(0, 20) + " ...");
        }

        //viewHolder.txtContent.setText(note.getNoteContent());
        viewHolder.txtNgayTao.setText(note.getNoteNgayTao());

        //an di nhung van giu vi tri cu
        //viewHolder.imgHinh.setVisibility(View.INVISIBLE);

        //gone- cac view khac se chiem vi tri cua no
        //viewHolder.imgHinh.setVisibility(View.GONE);

        //hien 1 view len man hinh
        //viewHolder.imgHinh.setVisibility(View.VISIBLE);

        //gan animation
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_list);
        view.startAnimation(animation);

        return view;
    }
}

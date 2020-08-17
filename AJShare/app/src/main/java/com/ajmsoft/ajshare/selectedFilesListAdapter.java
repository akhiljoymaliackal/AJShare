package com.ajmsoft.ajshare;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.ajmsoft.ajshare.R;

public class selectedFilesListAdapter extends ArrayAdapter {
    String[] fieldname;
    Bitmap[] iconimageid;
    Activity context;
    public selectedFilesListAdapter(Activity context, String[] fieldname, Bitmap[] iconimageid) {
        super(context, R.layout.myfileslistview,fieldname);
        this.context=context;
        this.fieldname=fieldname;
        this.iconimageid=iconimageid;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.selected_file_list, null, true);
        TextView textname = (TextView) row.findViewById(R.id.selected_file_list_text);
        ImageView imageicon = (ImageView) row.findViewById(R.id.selected_file_image_thumb);

        textname.setText(fieldname[position]);
        imageicon.setImageBitmap(iconimageid[position]);
        return  row;
    }

}

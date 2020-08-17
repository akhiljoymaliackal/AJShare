package com.ajmsoft.ajshare;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.ajmsoft.ajshare.R;

public class FileslistViewAdapter extends ArrayAdapter {
    String[] fieldname;
    Integer[] iconimageid;
    Activity context;
    public FileslistViewAdapter(Activity context, String[] fieldname, Integer[] iconimageid) {
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
            row = inflater.inflate(R.layout.myfileslistview, null, true);
        TextView textname = (TextView) row.findViewById(R.id.file_list_text);
        ImageView imageicon = (ImageView) row.findViewById(R.id.file_list_images);

        textname.setText(fieldname[position]);
        imageicon.setImageResource(iconimageid[position]);
        return  row;
    }

}

package com.ajmsoft.ajshare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class identifiedPeerListAdapter extends ArrayAdapter {
    String[] fieldname;
    Integer[] iconimageid;
    Activity context;
    public identifiedPeerListAdapter (Activity context, String[] firstletter, String[] devicename {
        super(context, R.layout.identified_peer_name_list);
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




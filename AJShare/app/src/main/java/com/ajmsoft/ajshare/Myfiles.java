package com.ajmsoft.ajshare;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Size;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Myfiles extends Fragment {

    private ListView listView;
    Intent intent;
    ArrayList<Uri> selectedFilelist= new ArrayList<Uri>();

    public  Activity selAct;


    public int selectedFilescount=0;

    public ArrayList<String> SelectedFilesName=new ArrayList<>();
    public ArrayList<Bitmap> createdBitmaps= new ArrayList<>();
    public int listcreationcount=1;
    private static View fragmentView=null;
    public Snackbar wifiSnackbar;

    Button clearSelection,sendButton;
    ProgressBar fileloadprogress;
    TextView progresstextview,nofilesSelected;

    public Myfiles(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(fragmentView!=null){
            return fragmentView;
        }
        else{

            fragmentView = inflater.inflate(R.layout.myfiles_fragment, container, false);
            clearSelection = fragmentView.findViewById(R.id.clearselection);
            nofilesSelected = fragmentView.findViewById(R.id.nofilesselected);
            sendButton=fragmentView.findViewById(R.id.sendfromfilelist);
            fileloadprogress=fragmentView.findViewById(R.id.progressBarfilelistloading);
            progresstextview = fragmentView.findViewById(R.id.showprogress);
            progresstextview.setVisibility(View.GONE);
            fileloadprogress.setVisibility(View.GONE);
            clearSelection.setVisibility(View.INVISIBLE);
            sendButton.setVisibility(View.INVISIBLE);

            String fieldNames[]={"Applications","Open Storage"};
            Integer imageiconid[]={R.mipmap.applicationicon,R.mipmap.storageicon};
            selAct = this.getActivity();
            listView=(ListView)fragmentView.findViewById(R.id.Catagorylist);
            FileslistViewAdapter  newfileadapter = new FileslistViewAdapter(this.getActivity(),fieldNames,imageiconid);
            listView.setAdapter(newfileadapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i){
                            case 0:

                                break;
                            case 1:
                                Intent openStoragIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                openStoragIntent.setType("*/*");
                                openStoragIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                                startActivityForResult(openStoragIntent,101);
                                break;
                            default:
                                return;
                        }
                }
            });

            clearSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearSelection();
                }
            });


        }

//        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
//        chooseFile.setType("*/*");
//        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
//        startActivityForResult(chooseFile, 1);
        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 101) {

            if(resultCode== Activity.RESULT_OK){
                nofilesSelected.setVisibility(View.GONE);
                final ArrayList<Bitmap> thumbnails = new ArrayList<>();
                final ArrayList<String> filenames = new ArrayList<>();
                    final int size;
                    if(data.getClipData()!=null){
                        size=data.getClipData().getItemCount();
                        for(int i=0;i<data.getClipData().getItemCount();i++){
                            selectedFilelist.add(data.getClipData().getItemAt(i).getUri());
                        }
                    }
                    else{
                        size=1;
                        selectedFilelist.add(data.getData());

                    }
                    selectedFilescount=selectedFilelist.size();
                    SelectedFilesName.clear();
                    createdBitmaps.clear();
                    fileloadprogress.setVisibility(View.VISIBLE);
                    fileloadprogress.setMax(selectedFilelist.size());

                    progresstextview.setVisibility(View.VISIBLE);

                    progresstextview.setText("0 / "+size+ " Loaded");
                    for (int i=0;i<selectedFilelist.size();i++){


                        final File filename= new File(selectedFilelist.get(i).getPath());
                            filenames.add(filename.getName());


                        final int finalI = i;
                        Glide.with(this).asBitmap().load(selectedFilelist.get(i).toString()).override(100,100).circleCrop().into(new CustomTarget<Bitmap>() {

                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                thumbnails.add(resource);


                                    fileloadprogress.setProgress(finalI+1);
                                progresstextview.setText(finalI+1+" / "+size+ " Loaded");
                                selectedListCreator(getFileName(selectedFilelist.get(finalI)),resource);


                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {}

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                Bitmap defaultimage = BitmapFactory.decodeResource(getResources(),R.mipmap.defaultdocuments);


                                fileloadprogress.setProgress(finalI+1);
                                progresstextview.setText(finalI+1+" / "+selectedFilelist.size()+ "Loaded");
                                selectedListCreator(getFileName(selectedFilelist.get(finalI)),defaultimage);
                            }
                        });}





                /*String fieldNames[]={"Applications","Open Storage"};
                Integer imageiconid[]={R.mipmap.applicationicon,R.mipmap.storageicon};
                listView=(ListView)fragmentView.findViewById(R.id.selectedfileslist);
                selectedFilesListAdapter  newfileadapter = new selectedFilesListAdapter(selAct,fieldNames,imageiconid);
                listView.setAdapter(newfileadapter);*/



                //selectedFilelist.clear();
            }
        }
    }

    public void selectedListCreator(String filenames, Bitmap thumbnails){


        SelectedFilesName.add(filenames);
        createdBitmaps.add(thumbnails);
        listcreationcount++;
        if(listcreationcount>selectedFilescount){
            String[] files=new String[SelectedFilesName.size()];
            Bitmap[] bitthumb = new Bitmap[createdBitmaps.size()];
            for (int i=0;i<SelectedFilesName.size();i++){
                files[i]=SelectedFilesName.get(i);
                bitthumb[i]=createdBitmaps.get(i);
            }

            listView=(ListView)fragmentView.findViewById(R.id.selectedfileslist);

            selectedFilesListAdapter  newfileadapter = new selectedFilesListAdapter(selAct,files,bitthumb);

            final float scale = getResources().getDisplayMetrics().density;

            LinearLayout.LayoutParams setpara = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)scale*SelectedFilesName.size()*66);
            listView.setLayoutParams(setpara);

            listView.setAdapter(newfileadapter);

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    return false;
                }



            });
            listView.setVisibility(View.VISIBLE);
            progresstextview.setVisibility(View.GONE);
            fileloadprogress.setVisibility(View.GONE);
            clearSelection.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.VISIBLE);
        }




    }

    public  void clearSelection(){


        selectedFilelist.clear();
        SelectedFilesName.clear();
        createdBitmaps.clear();
        String[] name={};
        Bitmap[] bitmap={};
        listView=(ListView)fragmentView.findViewById(R.id.selectedfileslist);
        selectedFilesListAdapter  newfileadapter = new selectedFilesListAdapter(selAct,name,bitmap);
        listView.setAdapter(newfileadapter);

        clearSelection.setVisibility(View.GONE);
        sendButton.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
        nofilesSelected.setVisibility(View.VISIBLE);



    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = MainActivity.fetchcontentreceiver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}

package com.tekno.localdatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.nio.file.Files;

public class DownloadActivity extends AppCompatActivity {
    Button mbtnDownload;
    TextView mTxtvShowText;
    String Path = Environment.getExternalStorageDirectory() + "/Android/";
    String folderName = "/filesWeb";
    private static int PERMISSION_REQUEST_CODE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        mbtnDownload = findViewById(R.id.btnDownloadFile);
        mTxtvShowText = findViewById(R.id.txtvShowText);
        mbtnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(DownloadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    createDirectory(folderName, Path);
                }else {
                    askPermission();
                }
               // val openNewWindows = Intent(this,MainActivity::class.java);
            }

        });
    }
    private void askPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] ==  PackageManager.PERMISSION_GRANTED){
                createDirectory(folderName, Path);
            }else{
                Toast.makeText(DownloadActivity.this,"Permiso denegado",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private  void createDirectory(String folderName,String Path){
        ///*****Queda Pendiente Que la ruta sea por el ExternaStorageDirectory
        File file = new File(Path+"/data/com.tekno.localdatabase/files/",folderName.trim());
       if (!file.exists()){
            if (file.mkdir()){
                Toast.makeText(DownloadActivity.this,"Carpeta Creada Correctamente!",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(DownloadActivity.this,"Carpeta No se ha creado!",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(DownloadActivity.this,"Folder Ya Existe!",Toast.LENGTH_SHORT).show();
        }
        initDownload(folderName, Path);
    }
    private void initDownload(String folderName,String Path) {
        String uri = "https://tekno-step-web.dyndns.org/Intranet/web/anexos/fileWeb/index.html&export=download";
        download(getApplicationContext(),"Index",".html",folderName, uri.trim());
    }

    private void download(Context context, String FileName, String FileExtension, String DesignationDirectory, String URL) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(URL);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,DesignationDirectory, FileName +FileExtension);
        assert downloadManager !=null;
        downloadManager.enqueue(request);
        setContentView(R.layout.activity_main);

        /*Snackbar snackbar = (Snackbar) Snackbar
                .make(findViewById(android.R.id.content),"Downloading..." Snackbar.LENGTH_LONG);
        snackbar.show();*/
    }
}
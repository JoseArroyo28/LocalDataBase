package com.tekno.localdatabase;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tekno.localdatabase.db.dbHelper;
import com.tekno.localdatabase.db.dbUsers;
import com.tekno.localdatabase.entities.Users;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button mBtnCreate;
    Context context;
    WebView mWVHTML;
    ProgressBar mProgresBarLoading;
    private static int PERMISSION_REQUEST_CODE = 7;
    String Path = Environment.getExternalStorageDirectory() + "/Android/";
    String rootFolder = "/filesWeb";
    String folderJS = "/jsFiles";
    String folderCSS = "/cssFiles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnCreate = findViewById(R.id.btnCreateDB);
        String Path = Environment.getExternalStorageDirectory() + "/Android/";
        mWVHTML = (WebView) findViewById(R.id.webViewHtml);
        mProgresBarLoading = (ProgressBar) findViewById(R.id.progressBarLoading);
        mBtnCreate.setOnClickListener(view -> {

        });
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            Log.d("TAG", "onCreate:  Entra " );
            createRootFolder(rootFolder, Path);
        }else {
            Log.d("TAG", "onCreate:  NO Entra " );
            askPermission();
        }
        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run() {
                        Log.e("PATH" ," " + Path);
                        loadWebView(Path);
                    }
                },
                9000);

        //testShowJSObject showJSObject = new testShowJSObject();

        /*
        //webView.loadUrl("File:///" + PathHtml);
        //webView.addJavascriptInterface(showJSObject,"ob");
        //
        Log.e("Ver Web view","----------------------hj");*/
    }
    private boolean verifyFile(String path){
        File indexHtml = new File(path);
        if (indexHtml.exists()){
            return true;
        }
        return false;
    }
    private void askPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] ==  PackageManager.PERMISSION_GRANTED){
            }else{
                Toast.makeText(MainActivity.this,"Permiso denegado",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public void loadWebView(String Path){
        mWVHTML.setWebChromeClient(new WebChromeClient() {});
        mWVHTML.getSettings().setJavaScriptEnabled(true);
       // mWVHTML.getSettings().setCacheMode(5 * 1024 * 1024);
        mWVHTML.getSettings().setDomStorageEnabled(true);
        mWVHTML.getSettings().setAllowFileAccess(true);
        mWVHTML.getSettings().setAllowContentAccess(true);
        mWVHTML.setWebViewClient(new WebViewClient());
        File folder, fileHTML;
        folder = new File(Path+"/data/com.tekno.localdatabase/files/","filesWeb");
        fileHTML = new File(Path+"/data/com.tekno.localdatabase/files/filesWeb/","Index.html");
        if (folder.exists() && fileHTML.exists()){
            mWVHTML.loadUrl("file://"+Path+ "data/com.tekno.localdatabase/files/filesWeb/Index.html");
        }else   {
            mWVHTML.loadUrl("file:///android_asset/index.html");
        }
       /*
        mWVHTML.loadUrl("file://"+Path+ "data/com.tekno.localdatabase/files/filesWeb/Index.html");*/
    }
    public  void createRootFolder(String folderName,String Path){
        ///*****Queda Pendiente Que la ruta sea por el ExternaStorageDirectory
        File file = new File(Path+"/data/com.tekno.localdatabase/files/",folderName);
        if (!file.exists()){
            if (file.mkdir()){
                //Toast.makeText(MainActivity.this,"Carpeta Creada Correctamente!",Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(MainActivity.this,"Carpeta No se ha creado!",Toast.LENGTH_SHORT).show();
                return;
            }
        }else{
            Toast.makeText(MainActivity.this,"Folder Ya Existe!",Toast.LENGTH_SHORT).show();
            validationExistIndex(folderName);
        }
        initDownload(folderName, Path);
        createCSSFoleder(folderCSS, Path);
        createFolderJS(folderJS, Path);
    }
    public void createCSSFoleder(String folderCSS,String Path){
        File file = new File(Path+"/data/com.tekno.localdatabase/files/"+rootFolder+"",folderCSS);
        if (!file.exists()){
            if (file.mkdir()){
                Toast.makeText(MainActivity.this,"Carpeta CSS Creada Correctamente!",Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(MainActivity.this,"Carpeta No se ha creado!",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(MainActivity.this,"Folder CSS Ya Existe!",Toast.LENGTH_SHORT).show();
            validationExistExternFiles(folderCSS, "style.css");
        }

    }
    public void createFolderJS(String folderJS,String Path){
        File file = new File(Path+"/data/com.tekno.localdatabase/files/"+rootFolder+"/",folderJS);
        if (!file.exists()){
            if (file.mkdir()){
                Toast.makeText(MainActivity.this,"Carpeta JS Creada Correctamente!",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this,"Carpeta No se ha creado!",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(MainActivity.this,"Folder JS Ya Existe!",Toast.LENGTH_SHORT).show();
            validationExistExternFiles(folderJS, "event.js");
        }

    }
    public void validationExistIndex(String Folder){
        File file = new File(Path+"/data/com.tekno.localdatabase/files/"+rootFolder+"/","Index.html");
        if (!file.exists()){

        }else{
            Toast.makeText(MainActivity.this,"Archivo HTML Ya Existe!",Toast.LENGTH_SHORT).show();
            file.delete();
        }
    }
    public void validationExistExternFiles(String nameFolder,String nameFile){
        File file = new File(Path+"/data/com.tekno.localdatabase/files/"+rootFolder+"/",nameFolder + "/"+ nameFile);
        if (file.exists()){
            Toast.makeText(MainActivity.this,"Archivo Ya Existe!",Toast.LENGTH_SHORT).show();
            file.delete();
        }
        if (nameFolder.equals(folderJS)){
            donwloadJavaScript(rootFolder+""+folderJS, Path);
        }else if(nameFolder.equals(folderCSS)){
            downloadCSS(rootFolder+""+folderCSS, Path);
        }
    }
    public void initDownload(String folderName,String Path) {
        String uri = "https://tekno-step-web.dyndns.org/Intranet/web/anexos/fileweb/index.html";

        download(getApplicationContext(),"Index",".html",folderName, uri.trim());
        Log.e("Datos:", "Foldername " + folderName + " |uri " + uri +"download  " );
    }
    public void donwloadJavaScript(String folderName,String Path) {
        String uri = "https://tekno-step-web.dyndns.org/Intranet/web/anexos/fileweb/event.js";
        download(getApplicationContext(),"event",".js",folderName, uri.trim());
        Log.e("Datos:", "Foldername " + folderName + " |uri " + uri  );
    }
    public void downloadCSS(String folderName,String Path) {
        String uri = "https://tekno-step-web.dyndns.org/Intranet/web/anexos/fileweb/style.css";
        download(getApplicationContext(),"style",".css",folderName, uri.trim());
        Log.e("Datos:", "Foldername " + folderName + " |uri " + uri  );

    }
    public void download(Context context, String FileName, String FileExtension, String DesignationDirectory, String URL) {

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(URL);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,DesignationDirectory, FileName +FileExtension);
        assert downloadManager !=null;
        downloadManager.enqueue(request);

        /*Snackbar snackbar = (Snackbar) Snackbar
                .make(findViewById(android.R.id.content),"Downloading..." Snackbar.LENGTH_LONG);
        snackbar.show();*/
    }

    class testShowJSObject{
        dbUsers user = new dbUsers(MainActivity.this);
        ArrayList<Users> usersArrayList = user.showUsers();
        @android.webkit.JavascriptInterface
        public String testMessage(String mensaje) {
           for (int i  = 0; i<usersArrayList.size();i++){
                Log.d("Mensaje que viene a la consola", usersArrayList.toString());
            }
            if (user.insertUser(mensaje)>0){
                Toast.makeText(MainActivity.this,"Registro se Agrego Correctamente",Toast.LENGTH_LONG).show();
            }
            return mensaje;
        };
        @android.webkit.JavascriptInterface
        public int getValueIndex (){
            return usersArrayList.size();
        }
        @android.webkit.JavascriptInterface
        public /*List<Users>*/ String getFromAndroid(int valueIndex) {
            String [] array = new String[usersArrayList.size()];
            for (int i = valueIndex; i < usersArrayList.size(); i++){
                array[valueIndex] = usersArrayList.get(i).toString();
                return array[i];
            }
            return null;
        }
    }

}
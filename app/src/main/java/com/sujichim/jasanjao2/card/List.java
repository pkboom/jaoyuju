package com.sujichim.jasanjao2.card;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.sujichim.jasanjao2.Globals;
import com.sujichim.jasanjao2.MainActivity;
import com.sujichim.jasanjao2.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 * Created by yedam on 15. 9. 28.
 * when adding a new column, fix
 * 1. Record
 * 2. DBHelper
 * 3. Form
 */

public class List extends AppCompatActivity {
    private static final String LOGTAG = "List";
    private Toolbar toolbar; // Declaring the Toolbar Object

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DBHelper db;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar); // Setting toolbar as the ActionBar with setSupportActionBar() call
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //open new form to input new record
                Intent intent = new Intent(List.this, Form.class);
                intent.putExtra("info", "");
                startActivity(intent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        assert mRecyclerView != null;
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        db = DBHelper.getInstance(this);
        // Reading all records
        java.util.List myDataset = db.getAllRecords();

        // specify an adapter
        mAdapter = new RecyclerViewAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        File folder = new File(getApplicationInfo().dataDir +
                File.separator + "medicalrecord");
        if (!folder.exists()) {
//            success = folder.mkdir();
            folder.mkdir();
            Log.d(LOGTAG, "folder created");
        }
/*
        if (success) {
            Log.d(LOGTAG, "medicalrecord folder exists");
        } else {
        }
*/
        Log.d(LOGTAG, "image folder path :" + folder.getPath());

        //get info from Form when MainActivity is called from Form
        Intent intent = getIntent();
        Record record = (Record) intent.getSerializableExtra("record");
        String mString = intent.getStringExtra("form");
        if (mString != null) {
            if (mString.equals("new")) {
                ((RecyclerViewAdapter) mAdapter).addItem(record);
            } else if (mString.equals("update")) {
                ((RecyclerViewAdapter) mAdapter).updateItem(record);
            }
        }

        MobileAds.initialize(getApplicationContext(), Globals.getMobileAdApi());

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    //open fragment showing selected item.
    public void switchContent(Record record) {
        Intent intent = new Intent(this, Form.class);
        intent.putExtra("record", record);
        startActivity(intent);
    }

/*
    //with back key pressed, return to RecordList
    @Override
    public void onBackPressed() {
        Log.d(LOGTAG, "BackPressed");
        startActivity(new Intent(this, MainActivity.class));
    }
*/

/*
    private static final int MENU_ITEM_DOWNANDUP = 10;
    private static final int MENU_ITEM_DOWNLOAD = 30;
    private static final int MENU_ITEM_UPLOAD = 50;
    private static final int MENU_ITEM_GOTOMAIN = 100;
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_menu, menu);
        // return true;
        return super.onCreateOptionsMenu(menu);

/*
        menu.add(Menu.NONE, MENU_ITEM_GOTOMAIN, Menu.NONE, "메인으로").setIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        menu.add(Menu.NONE, MENU_ITEM_DOWNANDUP, Menu.NONE, "다운로드/업로드설명");
        menu.add(Menu.NONE, MENU_ITEM_DOWNLOAD, Menu.NONE, "다운로드");
        menu.add(Menu.NONE, MENU_ITEM_UPLOAD, Menu.NONE, "업로드");
*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
            startActivity(new Intent(this, MainActivity.class));
            break;
            case R.id.explanation:
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("맥진카드 다운로드&업로드 안내")
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss(); // 닫기
                                    }
                                });
            String instructions = "1. '다운로드' 클릭\n"
                        + "2. '내파일->Download/medicalrecord.zip' 파일 생성\n"
                        + "3. 다운로드 완료\n"
                        + "4. 새 스마트폰의 '내파일->Download'폴더로 'medicalrecord.zip'이동\n"
                        + "5. 새 스마트폰에 '맥진카드' 설치\n"
                        + "6. '업로드' 클릭\n"
                        + "7. 업로드 완료";
            builder.setMessage(instructions);
            AlertDialog dialog = builder.create();    // 알림창 객체 생성
            dialog.show();    // 알림창 띄우기

            break;
            case R.id.upload:
                try {
                    uploadDB();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.download:
                exportDB();

        }

        return super.onOptionsItemSelected(item);
    }

    public void exportDB() {
        String dbFile = Environment.getDataDirectory().getPath()
                + "/data/com.yedam.medicalrecord/databases/MedicalRecord.db";
        String download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        File backupFileFolder = new File(download + "/medicalrecord");
        File backupImageFileFolder = new File(download + "/medicalrecord" + "/images");
        backupFileFolder.mkdir();
        backupImageFileFolder.mkdir();

        String backupFile = backupFileFolder.getPath() + File.separator + "MedicalRecord.db";
        Log.i("database path ", dbFile);
        Log.i("backup db path ", backupFile);

        try {
            FileInputStream in = new FileInputStream(dbFile);
            FileOutputStream out = new FileOutputStream(backupFile);
            int readcount = 0;
            byte[] buffer = new byte[1024];
            while ((readcount = in.read(buffer, 0, 1024)) != -1) {
                out.write(buffer, 0, readcount);
            }
            out.close();
            in.close();

            Toast.makeText(this, "다운로드가 되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File folder = new File(getApplicationInfo().dataDir +
                File.separator + "medicalrecord");

        //for images
        copyFolder(folder, backupImageFileFolder);

        // calling the zip function
        zipFileAtPath(backupFileFolder.getPath(), download + "/medicalrecord.zip");

        DeleteRecursive(backupFileFolder);
    }

/*
 *
 * Zips a file at a location and places the resulting zip file at the toLocation
 * Example: zipFileAtPath("downloads/myfolder", "downloads/myFolder.zip");
 */

    public boolean zipFileAtPath(String sourcePath, String toLocation) {
        // ArrayList<String> contentList = new ArrayList<String>();
        final int BUFFER = 2048;


        File sourceFile = new File(sourcePath);
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            if (sourceFile.isDirectory()) {
                zipSubFolder(out, sourceFile, sourceFile.getParent().length());
            } else {
                byte data[] = new byte[BUFFER];
                FileInputStream fi = new FileInputStream(sourcePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

/*
 *
 * Zips a subfolder
 *
 */

    private void zipSubFolder(ZipOutputStream out, File folder,
                              int basePathLength) throws IOException {

        final int BUFFER = 2048;

        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                zipSubFolder(out, file, basePathLength);
            } else {
                byte data[] = new byte[BUFFER];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath
                        .substring(basePathLength);
                Log.i("ZIP SUBFOLDER", "Relative Path : " + relativePath);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relativePath);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
        }
    }

    /*
     * gets the last path component
     *
     * Example: getLastPathComponent("downloads/example/fileToZip");
     * Result: "fileToZip"
     */
    public String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        String lastPathComponent = segments[segments.length - 1];
        return lastPathComponent;
    }

    void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();
    }

    public void uploadDB() throws IOException {
        String dbFile = Environment.getDataDirectory().getPath()
                + "/data/com.yedam.medicalrecord/databases/MedicalRecord.db";
        String download = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        String zipFile = download + "/medicalrecord.zip";
        unzip(zipFile, download);

        File backupFileFolder = new File(download + "/medicalrecord");
        File backupImageFileFolder = new File(download + "/medicalrecord" + "/images");

        String backupFile = backupFileFolder.getPath() + File.separator + "MedicalRecord.db";
        Log.i("database path ", dbFile);
        Log.i("backup db path ", backupFile);

        try {
            FileInputStream in = new FileInputStream(backupFile);
            FileOutputStream out = new FileOutputStream(dbFile);
            int readcount = 0;
            byte[] buffer = new byte[1024];
            while ((readcount = in.read(buffer, 0, 1024)) != -1) {
                out.write(buffer, 0, readcount);
            }
            out.close();
            in.close();
            Toast.makeText(this, "업로드가 되었습니다.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        db = DBHelper.getInstance(this);
        // Reading all records
        java.util.List myDataset = db.getAllRecords();

        // specify an adapter
        mAdapter = new RecyclerViewAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        File folder = new File(getApplicationInfo().dataDir +
                File.separator + "medicalrecord");

        //for images
        copyFolder(backupImageFileFolder, folder);

        DeleteRecursive(backupFileFolder);
        new File(zipFile).delete();

    }

    /**
     * Unzip a zip file.  Will overwrite existing files.
     *
     * @param zipFile Full path of the zip file you'd like to unzip.
     * @param location Full path of the directory you'd like to unzip to (will be created if it doesn't exist).
     * @throws IOException
     */
    public static void unzip(String zipFile, String location) throws IOException {
        final int BUFFER = 2048;
        int size;
        byte[] buffer = new byte[BUFFER];

        try {
            if ( !location.endsWith("/") ) {
                location += "/";
            }
            File f = new File(location);
            if(!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER));
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();
                    File unzipFile = new File(path);

                    if (ze.isDirectory()) {
                        if(!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {
                        // check for and create parent directories if they don't exist
                        File parentDir = unzipFile.getParentFile();
                        if ( null != parentDir ) {
                            if ( !parentDir.isDirectory() ) {
                                parentDir.mkdirs();
                            }
                        }

                        // unzip the file
                        FileOutputStream out = new FileOutputStream(unzipFile, false);
                        BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER);
                        try {
                            while ( (size = zin.read(buffer, 0, BUFFER)) != -1 ) {
                                fout.write(buffer, 0, size);
                            }

                            zin.closeEntry();
                        }
                        finally {
                            fout.flush();
                            fout.close();
                        }
                    }
                }
            }
            finally {
                zin.close();
            }
        }
        catch (Exception e) {
            Log.e(LOGTAG, "Unzip exception", e);
        }
    }

    public void copyFolder(File src, File dest) {
        if (src.isDirectory()) {
            //if directory not exists, create it
            if (!dest.exists()) {
                dest.mkdir();
                Log.i(LOGTAG, "Directory copied from " + src + "  to " + dest);
            }

            //list all the directory contents
            String files[] = src.list();

            for (String file : files) {
                //construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                //recursive copy
                copyFolder(srcFile, destFile);
            }
        } else {
            //if file, then copy it
            //Use bytes stream to support all file types
            try {
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest);
                byte[] buffer = new byte[1024];
                int length;
                //copy the file content in bytes
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                in.close();
                out.close();
                Log.i(LOGTAG, "File copied from " + src + " to " + dest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


}

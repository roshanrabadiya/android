package com.example.corepixmobileappp;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;
import com.example.corepixmobileappp.Database_Helper.DBHandler;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int count = 0;
    private long startMillis=0;

    private String FILE_NAME = "";

    private Button btn_show_all_file;
     DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(this);

        init();

        btn_show_all_file.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this,ViewFiles.class)));

    }

    private void init() {

        btn_show_all_file = findViewById(R.id.btn_view_File);
    }


//    public void viewAll() {
//        btnviewAll.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Cursor res = dbHandler.getAllData();
//                        if(res.getCount() == 0) {
//                            // show message
//                            showMessage("Error","Nothing found");
//                            return;
//                        }
//
//                        StringBuffer buffer = new StringBuffer();
//                        while (res.moveToNext()) {
//                            buffer.append("Id :"+ res.getString(0)+"\n");
//                            buffer.append("Name :"+ res.getString(1)+"\n");
//                            buffer.append("Surname :"+ res.getString(2)+"\n");
//                            buffer.append("Marks :"+ res.getString(3)+"\n\n");
//                        }
//
//                        // Show all data
//                        showMessage("Data",buffer.toString());
//                    }
//                }
//        );
//    }
//
//    public void showMessage(String title,String Message){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(true);
//        builder.setTitle(title);
//        builder.setMessage(Message);
//        builder.show();
//    }
















    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();
        if (eventAction == MotionEvent.ACTION_UP) {

            //get system current milliseconds
            long time= System.currentTimeMillis();


            //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
            if (startMillis==0 || (time-startMillis> 3000) ) {
                startMillis=time;
                count=1;
            }
            //it is not the first, and it has been  less than 3 seconds since the first
            else{ //  time-startMillis< 3000
                count++;
            }

            if (count==3) {
                CreateFile();
            }
            return true;
        }
        return false;

    }

    private void CreateFile() {
        try {
           // calling fileNameWithTimeStamp Method
            fileNameWithTimeStamp();

            // Create File and Save File
            FileOutputStream fOut = openFileOutput(FILE_NAME,MODE_PRIVATE);
            Toast.makeText(this, "Saved To"+getFilesDir()+"/"+FILE_NAME, Toast.LENGTH_LONG).show();

            // Calling insertData Method
            insertData();

            fOut.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This method insert data in SQLi Database
    public void insertData(){
        boolean isInserted = dbHandler.insertData(FILE_NAME);
        if(isInserted)
            Toast.makeText(MainActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(MainActivity.this,"Data not Inserted",Toast.LENGTH_LONG).show();
    }

    // Set the File name According To The Device Current Time
    public  void fileNameWithTimeStamp(){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
        Date now = new Date();
        FILE_NAME = formatter.format(now) + ".txt";

    }
}

package com.example.augmentedreality2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class File_chooser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_chooser);
    }



    public void ope_pdf(View view)
    {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // intent.setType("application/octet-stream"); //msword

        intent.setType("application/*");

        startActivityForResult(intent,1001);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == 1001 && resultCode==RESULT_OK)
        {
            if(data!=null)
            {


                Uri uri=data.getData();

                Toast.makeText(getApplicationContext(),uri.toString(),Toast.LENGTH_SHORT).show();



                //tv.setText( uri.toString());

                Intent intent = new Intent(getApplicationContext(),ARpage.class);
                //intent.setDataAndType(uri,"application/*");
                intent.putExtra("imageUri", uri.toString());
                startActivity(intent);


            }
        }





    }
}
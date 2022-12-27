package com.example.project_1;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class studentInfo extends Activity {
TextView stdData;
Button btnClose;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_info_activity);
        stdData= findViewById(R.id.stdData);
        btnClose = findViewById(R.id.btnClose);

        //get all the student info from the bundle
        Intent mylocalIntent= getIntent();
        Bundle myBundle = mylocalIntent.getExtras();
        String id = myBundle.getString("id");
        String fname = myBundle.getString("fname");
        String lname = myBundle.getString("lname");
        String bdate = myBundle.getString("bdate");
        String bplace = myBundle.getString("bcity");
        String gender = myBundle.getString("gender");
        String faculty = myBundle.getString("faculty");
        String department = myBundle.getString("department");
        String gpa = myBundle.getString("gpa");
        String scholarShip = myBundle.getString("scholarShip");
        String extInfo = myBundle.getString("extInfo");
//display the info in the new activity
        stdData.setText("           Student Info:\n\n\n"
                +"ID: "+id+"\n\n"
                +"First Name: "+fname+"\n\n"
                +"Last Name: "+lname+"\n\n"
                +"Birth Date: "+bdate+"\n\n"
                +"Birth Place: "+bplace+"\n\n"
                +"Gender : "+gender+"\n\n"
                +"Faculty : "+faculty+"\n\n"
                +"Department: "+department+"\n\n"
                +"GPA : "+gpa+"\n\n"
                +"Scholarship status : "+scholarShip+"\n\n"
                +"Extra Info: "+extInfo+"\n\n"
        );

        // attach updated bumble to invoking intent
        mylocalIntent.putExtras(myBundle);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}

package com.example.project_1;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //3ldzzaa
    //اقول لاتشدها
    private TextView txtCity; // city corresponding to its code
    //result  information
    private TextView reslbl,resId,resfname,reslname,resbirthdate,resbirthcity,resGender,resFaculty,resDept,resGpa,reschlrshp,resinfo;
    private EditText stdId,stdFname,stdLname,stdGpa,extraInfo;  //user inputs
    private CheckBox info;
    //The Submitted Information
    private LinearLayout result;
    //RadioButtons for the Gender and Scholarship
    private RadioButton rdMale, rdFemale, rdFull, rdHalf, rdNone;
    //3 buttons with 1 button for date-picker
    private Button btnSubmit, btnReset, btnExit, btnDate;
    //We have 3 Spinners , 1)For the  city code , 2)For the  Faculties , 3) For the Departments
    private DatePickerDialog datePickerDialog; //Date-Picker dialog that pops when user click on the btnDate button
    private Spinner BirthSpinner,FacultySpinner,DeptSpinner;
    //For City Codes
    Integer birthplace[] = {0,966,20,962,90,212,416,04,86};
    //For Faculties and its corresponding departments
    String faculties []= {"Select","ENGINEERING","BUSINESS","ARCHITECTURE","COMMUNICATION"};
    String EngDept [] = {"Select","CMPE","EEEN","MECA","CIVIL","MECH","IE"};
    String ArchDept [] = {"Select","ARCH","IND","INDS"};
    String BussDept [] = {"Select","BUSS","ECON"};
    String CommDept [] ={ "Select","FILM","DIGITAL GAME DESIGN","MEDIA","ARTS"};
    //default selection for the spinners
    String empty []={"Select"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //define all widgets
        btnDate = (Button) findViewById(R.id.btnDatePckr);
        stdId = findViewById(R.id.stdId);
        stdFname = findViewById(R.id.FirstName);
        stdLname = findViewById(R.id.LastName);
        rdMale = findViewById(R.id.rdMale);
        rdFemale = findViewById(R.id.rdFemale);
        rdFull = findViewById(R.id.rdFull);
        rdHalf = findViewById(R.id.rdHalf);
        rdNone = findViewById(R.id.rdNone);
        reslbl=findViewById(R.id.reslbl);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnReset = findViewById(R.id.btnReset);
        btnExit = findViewById(R.id.btnExit);
        extraInfo= findViewById(R.id.extInfo);
        stdGpa = findViewById(R.id.gpa);
        result = (LinearLayout) findViewById(R.id.result);
        BirthSpinner = findViewById(R.id.BirthSpinner);
        txtCity=findViewById(R.id.TxtBirthCity);
        FacultySpinner= findViewById(R.id.FacultySpinner);
        DeptSpinner= findViewById(R.id.DeptSpinner);
        info=findViewById(R.id.info);
        resId= findViewById(R.id.resId);
        resfname= findViewById(R.id.resFname);
        reslname= findViewById(R.id.resLname);
        resbirthdate= findViewById(R.id.resBirthDate);
        resbirthcity= findViewById(R.id.resBirthPlace);
        resFaculty= findViewById(R.id.resFaculty);
        resDept= findViewById(R.id.resDept);
        resGender= findViewById(R.id.resGender);
        resGpa= findViewById(R.id.resGpa);
        reschlrshp= findViewById(R.id.rescholar);
        resinfo= findViewById(R.id.resinfo);
        initDatePicker();// call the date picker method that prints the date after user select a date from the date picker dialog

        // ******   DEFINE ADAPTERS   **********
        //use my custom spinner style to make text white and background red
        //for the deafult selection
        ArrayAdapter<String> adapter0 = new ArrayAdapter<>(this, R.layout.custom, empty);
        //for city codes
        ArrayAdapter<Integer> adapter1 = new ArrayAdapter<>(this, R.layout.custom, birthplace);
        //for faculties spinner
        ArrayAdapter<String> Facadapter = new ArrayAdapter<>(this, R.layout.custom, faculties);
        //adapter for departments corresponding for each faculties
        // to set the dept spinner according to the selected faculty
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.custom, EngDept);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, R.layout.custom, ArchDept);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, R.layout.custom, BussDept);
        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(this, R.layout.custom, CommDept);


        //*************SET ADAPTERS***********************
        BirthSpinner.setAdapter(adapter1);
        FacultySpinner.setAdapter(Facadapter);

//*******************************SET LISTENERS************************************************
        //add listener for city code spinner to display the corresponding selected city
        BirthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //set the text to city  of the selected corresponding code
                txtCity.setText(matching_cities(birthplace[i]));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){/* required for interface*/}
        });//// end of setOnItemSelectedListener of city code spinner

        //add listener for faculty spinner to set department adapters according to the selected faculty
        FacultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               //default
                if(i == 0){
                    DeptSpinner.setAdapter(adapter0);
                }
                //if engineer faculty is selected, set ENG departments for the spinner
                else if(i == 1){
                    DeptSpinner.setAdapter(adapter2);
                }
                //if business faculty is selected, set BUS departments for the spinner
                else if(i == 2){
                    DeptSpinner.setAdapter(adapter4);
                }
                //if architecture faculty is selected, set ARCH departments for the spinner
                else if(i == 3){
                    DeptSpinner.setAdapter(adapter3);
                }
                //if Communication faculty is selected, set Comm departments for the spinner
                else if(i == 4){
                    DeptSpinner.setAdapter(adapter5);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){ /*required for the interface*/}
        }); // end of setOnItemSelectedListener of faculty spinner


        //set  click listeners for buttons and checkbox
        btnDate.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        info.setOnClickListener(this);


    }//end of OnCreate

    //******************************************************************************************
    //                                   ON CLICK METHOD
    //******************************************************************************************
    @Override
    public void onClick(View v){

        // when user click the submit button
        if(v.getId() == btnSubmit.getId()){
            //  CHECK VALID DATA FIRST BEFORE SUBMITTING INFO
            // 1) ID must be exactly 11 digits
            // 2) if all edit texts is not empty(length is not 0) means that the user wrote in it
            // 3) use || 'OR' operator for radio buttons to make sure if one of them is checked
            // 4) checking for non selected spinners if the first selection is selected 'default'
            // 5) checking if the gpa is 4 digits but the 2nd position is a dot '.' not a number so it can be only 3 digits gpa
            // 6) if the check box is checked but user wrote info in edit text
            // 7) CITY CODE IS SELECTED AND NOT LEFT IN DEFAULT POSITION
            if(stdId.getText().toString().length()==11 && stdFname.getText().length()!=0 && stdLname.getText().length()!=0&&
            btnDate.getText().length()!=0 &&txtCity.getText().length()!=0 && (rdMale.isChecked() ||
            rdFemale.isChecked()) &&  FacultySpinner.getSelectedItemPosition()!=0 && DeptSpinner.getSelectedItemPosition()!=0 &&
             (stdGpa.getText().length()==4 && stdGpa.getText().charAt(1)=='.') && (rdFull.isChecked() || rdNone.isChecked() || rdHalf.isChecked())&&
                    ( (info.isChecked() && extraInfo.getText().length()!=0) || (!info.isChecked()))){
                //IF DATA IS VALID SUBMIT THE INFO BY CALLING Submit method
            Submit();
        }
            //IF DATA IS NOT VALID DISPLAY TOAST MESSAGE TO ASK TO INPUT DATA CORRECTLY
        else{ Toast.makeText(this,"Invalid input Data\nplease try again",Toast.LENGTH_LONG).show();
            result.setVisibility(View.INVISIBLE);
            reslbl.setVisibility(View.INVISIBLE);}
        } //end of submit button

        //when user click on reset button
        if (v.getId() == btnReset.getId()) {
            reset(); // call reset method
        }
        //when user click on exit button
        if(v.getId() == btnExit.getId()){//To make the button Exit work when we click on it.
           //exit the app
            finish();
            System.exit(0);;
        }
        // if user click on birthdate button, display a date picker
        if (v.getId() == btnDate.getId()){
            datePickerDialog.show();
        }

        // display the textbox if user wants to add extra information
        if(info.isChecked()){
            extraInfo.setVisibility(View.VISIBLE);
        }
        // if user checked it but changed his mind and unchecked it, make it invisible again and reset the text
        else if(!info.isChecked()){
            extraInfo.setVisibility(View.INVISIBLE);
            extraInfo.setText("");
        }
    }//end of OnClick

    //*****************************************************************************************************

    //****************************************************************************************************
    //Method to Submit information once submit button is clicked
    public void Submit(){
        // display the submitted information section
        reslbl.setVisibility(View.VISIBLE);
        result.setVisibility(View.VISIBLE);
        // add all inputted information to its position
        resId.setText(stdId.getText().toString());
        resfname.setText(stdFname.getText().toString());
        reslname.setText(stdLname.getText().toString());
        resbirthdate.setText(btnDate.getText().toString());
        resbirthcity.setText(txtCity.getText().toString());
        resFaculty.setText(FacultySpinner.getSelectedItem().toString());
        resDept.setText(DeptSpinner.getSelectedItem().toString());
        resGpa.setText(stdGpa.getText().toString());
        //add selected radio button
        if(rdFull.isChecked()){reschlrshp.setText("Full");}
        if(rdHalf.isChecked()){reschlrshp.setText("Half");}
        if(rdNone.isChecked()){reschlrshp.setText("None");}
        if(rdMale.isChecked()){ resGender.setText("male");}
        if(rdFemale.isChecked()){ resGender.setText("female");}
        resinfo.setText(extraInfo.getText().toString());
    }//end of submit

    //Method to Reset all values to default
    public void reset(){
        stdId.setText("");
        stdFname.setText("");
        stdLname.setText("");
        btnDate.setText("");
        txtCity.setText("");
        rdMale.setChecked(false);
        rdFemale.setChecked(false);
        rdFull.setChecked(false);
        rdHalf.setChecked(false);
        rdNone.setChecked(false);
        resId.setText("");
        resfname.setText("");
        reslname.setText("");
        resbirthdate.setText("");
        resbirthcity.setText("");
        resGpa.setText("");
        stdGpa.setText("");
        resGender.setText("");
        resFaculty.setText("");
        resDept.setText("");
        BirthSpinner.setSelection(0);
        FacultySpinner.setSelection(0);
        DeptSpinner.setSelection(0);
        resinfo.setText("");
        reschlrshp.setText("");
        extraInfo.setText("");
        info.setChecked(false);
        result.setVisibility(View.INVISIBLE);
        reslbl.setVisibility(View.INVISIBLE);
        extraInfo.setVisibility(View.INVISIBLE);
    }// end of reset



    //Matching algorithm for the city codes
    public String matching_cities(Integer data){
        String city="";
        //USE SWITCH CASE TO CHECK EVERY CITY CODE AND SET THE CITY
        switch (data){
            case 966: city= "Riyadh"; break;
            case 20: city= "Cairo"; break;
            case 962: city= "Amman"; break;
            case 90:  city= "Istanbul"; break;
            case 212: city= "New York"; break;
            case 416: city= "Toronto"; break;
            case 04: city= "Dubai"; break;
            case 86: city="China"; break;
            case 0: city="";break;
        }
        return city;

    }//end of method

//*****************************************************************************************
//                               DATE PICKER SETUP
// ****************************************************************************************

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
            //get selected date and assign it to a string to set text the button
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day){
                month++;
                String date = day + "/" + month+ "/" + year;
                btnDate.setText(date);
            }
        };//end of OnDateSetListener

        //calender of current time zone
        Calendar cal = Calendar.getInstance();
        //set year,month and day
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.BUTTON_NEUTRAL;
        // create  the date picker
        datePickerDialog  = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        cal.set(2010,11,30);// set maximum birth year for a student is 2006 for a student "16 years"
        datePickerDialog .getDatePicker().setMaxDate(cal.getTimeInMillis());
        cal.set(1960,0,1); //minimum birth year a student can be is 1960 "82 years :)"
        datePickerDialog .getDatePicker().setMinDate(cal.getTimeInMillis());
    }//end of method

}//end of class
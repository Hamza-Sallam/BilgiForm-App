package com.example.project_1;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase db =null; // database instance
    String path; //database location
    Context activityContext;
    private TextView txtCity; // city corresponding to its code
    //result  information
    private TextView resscl1,resG; //hidden but used to retrieve info
    private EditText stdId,stdFname,stdLname,stdGpa,extraInfo;  //user inputs
    private CheckBox info;
    //RadioButtons for the Gender and Scholarship
    private RadioButton rdMale, rdFemale, rdFull, rdHalf, rdNone;
    //8 buttons with 1 button for date-picker
    private Button btnSubmit, btnReset, btnExit, btnDate,btnDisplay,btnSearch,btnUpdate,btnDelete;
    //We have 3 Spinners , 1)For the  city code , 2)For the  Faculties , 3) For the Departments
    private DatePickerDialog datePickerDialog; //Date-Picker dialog that pops when user click on the btnDate button
    private Spinner BirthSpinner,FacultySpinner,DeptSpinner;
    private ListView listView; //listview to hold records
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
        activityContext = this;
        //define all widgets
        listView = findViewById(R.id.listView);
        btnDate = (Button) findViewById(R.id.btnDatePckr);
        stdId = findViewById(R.id.stdId);
        stdFname = findViewById(R.id.FirstName);
        stdLname = findViewById(R.id.LastName);
        rdMale = findViewById(R.id.rdMale);
        rdFemale = findViewById(R.id.rdFemale);
        rdFull = findViewById(R.id.rdFull);
        rdHalf = findViewById(R.id.rdHalf);
        rdNone = findViewById(R.id.rdNone);
        resscl1= findViewById(R.id.rescholar1);
        resG = findViewById(R.id.resG);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnReset = findViewById(R.id.btnReset);
        btnExit = findViewById(R.id.btnExit);
        btnDisplay = findViewById(R.id.btnDisplay);
        btnSearch = findViewById(R.id.btnSearch);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        extraInfo= findViewById(R.id.extInfo);
        stdGpa = findViewById(R.id.gpa);
        BirthSpinner = findViewById(R.id.BirthSpinner);
        txtCity=findViewById(R.id.TxtBirthCity);
        FacultySpinner= findViewById(R.id.FacultySpinner);
        DeptSpinner= findViewById(R.id.DeptSpinner);
        info=findViewById(R.id.info);
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
                txtCity.setText(matching_cities(birthplace[i]));}
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){/* required for interface*/}
        });//// end of setOnItemSelectedListener of city code spinner

        //add listener for faculty spinner to set department adapters according to the selected faculty
        FacultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               //default
                if(i == 0){DeptSpinner.setAdapter(adapter0);}
                //if engineer faculty is selected, set ENG departments for the spinner
                else if(i == 1){DeptSpinner.setAdapter(adapter2);}
                //if business faculty is selected, set BUS departments for the spinner
                else if(i == 2){DeptSpinner.setAdapter(adapter4);}
                //if architecture faculty is selected, set ARCH departments for the spinner
                else if(i == 3){DeptSpinner.setAdapter(adapter3);}
                //if Communication faculty is selected, set Comm departments for the spinner
                else if(i == 4){DeptSpinner.setAdapter(adapter5);}
            } @Override
            public void onNothingSelected(AdapterView<?> adapterView){ /*required for the interface*/}
        }); // end of setOnItemSelectedListener of faculty spinner



        //set  click listeners for buttons and checkbox
        btnDate.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnDisplay.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        info.setOnClickListener(this);

        //***********************       DATABASE  CREATION SECTION    ******************************************
        File myDbPath = getApplication().getFilesDir();
        path = myDbPath +"/"+"project22";
        //create database
        try {
            if(!databaseExist()){
                // we dont have data base
                db =SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.CREATE_IF_NECESSARY);
                //CREATE A STUDENT TABLE
                String mytable ="create table student (id bigint primary key , firstname text,lastname text,"
                        +"birthdate date,birthplace text,gender text,faculty text,department text,gpa float,scholarship text,additionalInfo text);";
                //execute the sql statement
                db.execSQL(mytable);
                showCustomToast("table is created");
            }
        }
        catch(SQLException e){showCustomToast(e.getMessage());}
    }//end of OnCreate


    //******************************************************* OPTION MENU SETUP *************************************************************
    // set the option menu for the current activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        PopulateMenu(menu);
        return true;
    }
    private void PopulateMenu(Menu menu){
        int groupId = 0;
//arguments: groupId, optionId, order, title
        menu.add(groupId, 1, 1, "Submit");
        menu.add(groupId, 2, 2, "Display");
        menu.add(groupId, 3, 3, "Search");
        menu.add(groupId, 4, 4, "Update");
        menu.add(groupId, 5, 5, "Delete");
        menu.add(groupId, 6, 8, "Reset");
        menu.add(groupId, 7, 7, "Exit");
        menu.add(groupId, 8, 6, "Help");
    } //populateMyFirstMenu
    // called whenever an item in options menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { return applyMenuOption( item ); }
// apply functionality to menu options
    private boolean applyMenuOption(MenuItem item) {
        int menuItemId = item.getItemId(); //1, 2, 3, ...8
        if (menuItemId == 1) {SubmitData();}
        else if(menuItemId == 2){DisplayData();}
        else if(menuItemId == 3){SearchStdDialog();}
        else if(menuItemId == 4){UpdateStd();}
        else if(menuItemId == 5 ){DeleteStdDialog();}
        else if(menuItemId == 6 ){reset();}
        else if(menuItemId== 7){System.exit(0); finish();}
        return false;
    }
    //helper method to check if database exists
    private boolean databaseExist(){
        File dbFile = new File(path);
        return dbFile.exists();
    }
//    **************************************    END OF OPTION MENU SETUP *******************************************************


    //******************************************************************************************
    //                                   ON CLICK METHOD
    //******************************************************************************************
    @Override
    public void onClick(View v){
        // when user click the submit button
        if(v.getId() == btnSubmit.getId()){SubmitData();}//submit button
        if (v.getId() == btnReset.getId()) {reset();}// reset button
        if(v.getId() == btnDisplay.getId()){DisplayData();}//display button
        if(v.getId() == btnDelete.getId()){DeleteStdDialog();}//delete button
        if(v.getId() == btnSearch.getId()){SearchStdDialog();}//search button
        if(v.getId() == btnUpdate.getId()){UpdateStd();}//update button
        // if user click on birthdate button, display a date picker
        if (v.getId() == btnDate.getId()){datePickerDialog.show();}
        // display the textbox if user wants to add extra information
        if(info.isChecked()){extraInfo.setVisibility(View.VISIBLE);}
        //when user click on exit button
        if(v.getId() == btnExit.getId()){//To make the button Exit work when we click on it.
            //exit the app
            finish();
            System.exit(0);;
        }
        // if user checked it but changed his mind and unchecked it, make it invisible again and reset the text
        else if(!info.isChecked()){
            extraInfo.setVisibility(View.INVISIBLE);
            extraInfo.setText("");
        }
    }//end of OnClick

    //*****************************************************************************************************


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

 //************************************************************************************************************************
//*************************************   BUTTON HANDLING FUNCTIONS *********************************************************
//*****************************************************************************************************************************

    //*********************************     RESET STUDENT INFO    ****************************************************************
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
        stdGpa.setText("");
        BirthSpinner.setSelection(0);
        FacultySpinner.setSelection(0);
        DeptSpinner.setSelection(0);
        extraInfo.setText("");
        info.setChecked(false);
        extraInfo.setVisibility(View.INVISIBLE);
    }// end of reset
    //************************************      SUBMIT STUDENT INFO TO DATABASE     *************************************
public void SubmitData(){
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
        try {
            db=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.CREATE_IF_NECESSARY);
            //insert data
            //get all inputs first
            long id = Long.parseLong(stdId.getText().toString());
            String FName = stdFname.getText().toString();
            String Lname = stdLname.getText().toString();
            String BirthD = btnDate.getText().toString();
            String birthP = txtCity.getText().toString();
            //set the hidden textview widget's text according to user's selection
            if(rdMale.isChecked()){ resG.setText("male");}
            if(rdFemale.isChecked()){ resG.setText("female");}
            String Gend =resG.getText().toString();
            String Fac = FacultySpinner.getSelectedItem().toString();
            String Dep = DeptSpinner.getSelectedItem().toString();
            float gpa = Float.parseFloat(stdGpa.getText().toString());
            //set the hidden textview widget's text according to user's selection
            if(rdFull.isChecked()){resscl1.setText("Full");}
            if(rdHalf.isChecked()){resscl1.setText("Half");}
            if(rdNone.isChecked()){resscl1.setText("None");}
            String sclshp = resscl1.getText().toString();
            String addinfo = extraInfo.getText().toString();
            //insert the data in the table
            String insert = "insert into student values("+id+",'"+FName+"','"+Lname+"','"+BirthD+"','"+birthP+"','"
                    +Gend+"','"+Fac+"','"+Dep+"',"+gpa+",'"+sclshp+"','"+addinfo+"');";
            db.execSQL(insert);
            showCustomToast("data inserted");
            db.close();
            reset(); //reset inputs
        }
        catch(SQLException e){showCustomToast(e.getMessage());}}
    //IF DATA IS NOT VALID DISPLAY TOAST MESSAGE TO ASK TO INPUT DATA CORRECTLY
    else{showCustomToast("Invalid input Data\nplease try again");}
}

    //************************ DISPLAY ALL STUDENTS STORED IN THE DATABASE ****************************************************
 public void DisplayData(){
         try {
             db= SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.CREATE_IF_NECESSARY);
             String search = "select id,firstname,lastname,department from student";
             Cursor cursor = db.rawQuery(search,null);
             ArrayList<String> students =new ArrayList<>();
             ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom,students);
             while(cursor.moveToNext()){
                 String  id = cursor.getString(0);
                 String fname = cursor.getString(1);
                 String lname =cursor.getString(2);
                 String Dept = cursor.getString(3);
                 String result = id+" , "+fname+" , "+lname+" , "+Dept;
                 students.add(result);
             }
             listView.setAdapter(adapter);
             db.close();
         }
         catch(SQLException e){showCustomToast(e.getMessage());}
     }

     //**************************** DELETE THE STUDENT RECORD FROM DATABASE **************************************
     private void DeleteStd(String id,Dialog customDialog) {
         try {
             db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
             String query = "select * from student where id =" + id + ";";
             Cursor cursor = db.rawQuery(query, null);
             // if such a query exist, meaning the student exists, delete the student
             if (cursor.moveToFirst()) {
                 String delete = "delete from student where id =" + id + ";"; // execute the sql delete statment
                 db.execSQL(delete);
                 showCustomToast("student deleted  successfully");
                 db.close();
                 customDialog.dismiss(); //close the dialog
             } else {showCustomToast("student doesnt exist\ntry again");}
         } catch (SQLException e) {showCustomToast(e.getMessage());}}
     //************************************ SEARCH THE DATABASE FOR THE ASKED STUDENT AND PRINT HIS INFO *****************************************
    private void SearchStd(String id , Dialog customDialog){
        try {
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            //first lets check if the student exist by running a query and seeing if there is a result table
            String query = "select * from student where id =" + id + ";";
            Cursor c = db.rawQuery(query, null);
            // if such a query exist, meaning the student exists, print all his info
            if (c.moveToFirst()) {
                String select = "select * from student where id =" + id + ";";
                Cursor cursor = db.rawQuery(select, null);
                ArrayList<String> foundStd = new ArrayList<>();
                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.custom, foundStd);
                while (cursor.moveToNext()) {
                    for (int i = 0; i < 11; i++) { foundStd.add(cursor.getString(i)); }}
                //modify listview to hold the student info
                listView.setAdapter(adapter1);
                db.close();
                showCustomToast("student found");
                customDialog.dismiss();//close the dialog box
            }
            else{showCustomToast("student doesn't exist\ntry again");}
        }
        catch(SQLException e){showCustomToast(e.getMessage());}
    }
//***********************************           UPDATE THE STUDENT INFO     *****************************************************
    private void UpdateStdInfo(String id,String fname,String lname,String fac,String dep){
        try{
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            String update = "update student set firstname='"+fname+"',lastname='"+lname+"',faculty='"+fac+"',department='"+dep+"'"
            +"where id="+id+";";
            db.execSQL(update);
            showCustomToast("Student Updated Successfully");
        }
        catch(SQLException e){showCustomToast(e.getMessage());}
    }
    //**************************     END OF BUTTONS FUNCTION ******************************************************************


//*****************************************************************************************
//                               DATE PICKER SETUP
// ****************************************************************************************
    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
            //get selected date and assign it to a string to set text the button
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day){
                month++;
                String date = year + "-" + month+ "-" + day;
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

//**************************************************************************************************************

    //********************* CUSTOM TOAST **********************************************88
    public void showCustomToast(String msg){
// this fragment creates a custom Toast showing
//text + shaped_background
        // triggered by XML button's android:onClick=...
        Toast customToast = makeCustomToast(this,msg);
        customToast.show();
    }
    protected Toast makeCustomToast(Context context,String msg) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate( R.layout.custom_toast, null);
        TextView text =layout.findViewById(R.id.toast_text);
        text.setText(msg);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        return toast;
    }//makeCustomToast

    //************************************  END OF CUSTOM TOAST SETUP *******************************************8


//**********************************    DIALOG(s) ***************************************
    // ****************** SHOW DIALOG BOX ONCE USER CLICKS ON DELETE BUTTON ***********************************8
private void DeleteStdDialog() {
    final Dialog customDialog = new Dialog(activityContext);
    customDialog.setTitle("ID query");
// match customDialog with custom dialog layout
    customDialog.setContentView(R.layout.custom_dialog_layout);
    TextView text = customDialog.findViewById(R.id.sd_textView1);
    text.setText("Enter ID of student\n to delete");
    final EditText IdData = customDialog
            .findViewById(R.id.queryID);
// if user with id exists and user click submit, call DeletStd() that deletes  the student \
    (customDialog.findViewById(R.id.btnIdDialog))
            .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { DeleteStd(IdData.getText().toString(),customDialog);}});
    customDialog.show();
}
//*******************************     SHOW DIALOG BOX FOR ONCE THE USER CLICKS ON SEARCH BUTTON *******************************************
    private void SearchStdDialog() {
        final Dialog customDialog = new Dialog(activityContext);
        customDialog.setTitle("ID query");
// match customDialog with custom dialog layout
        customDialog.setContentView(R.layout.custom_dialog_layout);
        TextView text = customDialog.findViewById(R.id.sd_textView1);
        text.setText("Enter ID of student\n to search");
        final EditText IdData = customDialog
                .findViewById(R.id.queryID);
//calls SearchStd which prints all the student's info in a list view if exists
        (customDialog.findViewById(R.id.btnIdDialog))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){ SearchStd(IdData.getText().toString(),customDialog);}});
        customDialog.show();
    }

    //******************************************  UPDATE STUDENT SETUP **************************************
    //****************************************** SHOW DIALOG BOX ONCE THE USER CLICKS ON UPDATE BUTTON **********************************
    //first checks id by displaying a dialog box for id input
    private void UpdateStd() {
        final Dialog customDialog = new Dialog(activityContext);
        customDialog.setTitle("ID query");
// match customDialog with custom dialog layout
        customDialog.setContentView(R.layout.custom_dialog_layout);
        TextView text = customDialog.findViewById(R.id.sd_textView1);
        text.setText("Enter ID of student\n to update");
        final EditText IdData = customDialog
                .findViewById(R.id.queryID);

        (customDialog.findViewById(R.id.btnIdDialog))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UpdateStdDialog(IdData.getText().toString());
                        customDialog.dismiss();
                    }
                });
        customDialog.show();
    }
    //*************************** if student exist, show a dialog box to input new firstname,lastname,faculty and department**********************************
    private void UpdateStdDialog(String id) {
        //first check if student exists
        try {
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            String query = "select * from student where id=" + id + ";";
            Cursor c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                final Dialog customDialog = new Dialog(activityContext);
                customDialog.setTitle("Update Info");
// match customDialog with custom dialog layout
                customDialog.setContentView(R.layout.custom_update_dialog);
                Window window = customDialog.getWindow();
                window.setLayout(500, 500); //set size of dialog box

                final EditText fname = customDialog.findViewById(R.id.Updatefirstname);
                final EditText lname = customDialog.findViewById(R.id.Updatelastname);
                final Spinner facs = customDialog.findViewById(R.id.UpFacultySpinner);
                //**************************** SET THE SPINNER ADAPTER SAME AS BEFORE ****************************************
                ArrayAdapter<String> facadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, faculties);
                final Spinner Deps = customDialog.findViewById(R.id.UpDeptSpinner);
                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, empty);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, EngDept);
                ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, BussDept);
                ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ArchDept);
                ArrayAdapter<String> adapter5 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CommDept);
                facs.setAdapter(facadapter);
                facs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        //default
                        if (i == 0) {Deps.setAdapter(adapter1);}
                        //if engineer faculty is selected, set ENG departments for the spinner
                        else if (i == 1) {Deps.setAdapter(adapter2);}
                        //if business faculty is selected, set BUS departments for the spinner
                        else if (i == 2) {Deps.setAdapter(adapter3);}
                        //if architecture faculty is selected, set ARCH departments for the spinner
                        else if (i == 3) {Deps.setAdapter(adapter4);}
                        //if Communication faculty is selected, set Comm departments for the spinner
                        else if (i == 4) {Deps.setAdapter(adapter5);}}
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) { /*required for the interface*/}
                }); // end of setOnItemSelectedListener of faculty spinner
                (customDialog.findViewById(R.id.btnUpdateDialog))
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String Sfname = fname.getText().toString();
                                String Slname = lname.getText().toString();
                                String Sfac = facs.getSelectedItem().toString();
                                String Sdep = Deps.getSelectedItem().toString();
                                //if user inputs correctly update the student info by calling UpdateStdInfo() method that handles SQL statements
                                if (Sfname.isEmpty() || Sfac.isEmpty() || Sdep.equals("Select") || Sfac.equals("Select")) {
                                    showCustomToast("incorrect submitted information\ntry again");
                                } else {
                                    UpdateStdInfo(id, Sfname, Slname, Sfac, Sdep);
                                    customDialog.dismiss();//close the dialog
                                }
                            }
                        });
                customDialog.show();
                db.close();
            } else {showCustomToast("student doesnt exist\n try again");}
        }
        catch(SQLException e){showCustomToast(e.getMessage());}
    }
//************************************************* END OF UPDATE STUDENT SETUP ********************************************************************8

}//end of class
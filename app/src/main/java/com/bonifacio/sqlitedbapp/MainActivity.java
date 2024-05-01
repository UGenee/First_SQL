package com.bonifacio.sqlitedbapp;

import android.os.Bundle;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private android.widget.EditText EditText;
    private android.widget.Button Button;
    private android.database.sqlite.SQLiteDatabase SQLiteDatabase;
    EditText etStdntID, etStdntName, etStdntProg = (EditText);
    Button btAdd, btDelete, btSearch, btView = (Button);
    SQLiteDatabase db = (SQLiteDatabase);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Edit_texts
        etStdntID = findViewById(R.id.edit_id);
        etStdntName = findViewById(R.id.edit_nm);
        etStdntProg = findViewById(R.id.edit_prog);

        //Buttons & OnClickListener
        btAdd = findViewById(R.id.add);
            btAdd.setOnClickListener(this);
        btDelete = findViewById(R.id.delete);
            btDelete.setOnClickListener(this);
        btSearch = findViewById(R.id.search);
            btSearch.setOnClickListener(this);
        btView = findViewById(R.id.view_all);
            btView.setOnClickListener(this);

        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(stdnt_id VARCHAR, stdnt_name VARCHAR, stdnt_prog VARCHAR);");

    }

    @Override
    public void onClick(View view) {
        if (view == btAdd) { // <- Add Button
            db.execSQL("INSERT INTO student VALUES('" + etStdntID.getText() + "','" + etStdntName.getText() + "','" + etStdntProg.getText() + "');");
            showMessage("Success", "Record added.");
            clearText();
        } else
        if (view == btDelete) { // <- Delete Button
            Cursor c = db.rawQuery("SELECT * FROM student WHERE stdnt_id='" + etStdntID.getText() + "'", null);
            if (c.moveToFirst()) {
                db.execSQL("DELETE FROM student WHERE stdnt_id= '" + etStdntID.getText() + "'");
                showMessage("Success", "Record deleted.");
            }
            clearText();
        } else
        if (view == btSearch) { // <- Search Button
            Cursor c = db.rawQuery("SELECT * FROM student WHERE stdnt_id='" + etStdntID.getText() + "'", null);
            StringBuffer buffer = new StringBuffer();
            if (c.moveToFirst()) {
                buffer.append("Name: " + c.getString(1) + "\n");
                buffer.append("Program: " + c.getString(2) + "\n\n");
            }
            showMessage("Student Details", buffer.toString());
        } else
        if (view == btView) { // <- View Button
            Cursor c = db.rawQuery("SELECT * FROM student", null);
            if (c.getCount() == 0)
            {
                showMessage("Error", "No records found.");
                return;
            }
            StringBuilder buffer = new StringBuilder();
            while (c.moveToNext()) {
                buffer.append("ID: " + c.getString(0) + "\n");
                buffer.append("Name: " + c.getString(1) + "\n");
                buffer.append("Program: " + c.getString(2) + "\n\n");
            }
            showMessage("Student Details", buffer.toString());
        }

    }

    private void showMessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void clearText() {
        etStdntID.setText("");
        etStdntName.setText("");
        etStdntProg.setText("");

        etStdntID.requestFocus();
        etStdntName.requestFocus();
        etStdntProg.requestFocus();

    }

}
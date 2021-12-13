package com.example.mypdfapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    String data, date, time, locationData;
    TextView numberTV, nameTV;
    EditText numberET, nameET, amountET, narrationET;
    Button submitBTN;
    Dialog finalDialogBox;
    LinearLayout layout_view;
    PaymentInfo paymentInfo;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize UI Components
        numberTV = findViewById(R.id.numberTV);
        nameTV = findViewById(R.id.nameTV);

        submitBTN = findViewById(R.id.submitBTN);

        numberET = findViewById(R.id.numberET);
        nameET = findViewById(R.id.nameET);
        amountET = findViewById(R.id.amountET);
        narrationET = findViewById(R.id.narrationET);

        finalDialogBox = new Dialog(this);

        //layout_view = findViewById(R.id.pdfLayout);



        retriveDataFromBundle(); // This function is used to retrive data from bundle
        setTextView(); // This function is Dynamically set bkash/ nagad word in front of TextView
        OnClickSubmitButtonListener();// OnClickListener for submit button
    }

    private void retriveDataFromBundle() {
        data = "Bkash";
    }

    private void setTextView() {
        numberTV.setText(data + " number");
        nameTV.setText(data + " name");
    }

    public void OnClickSubmitButtonListener() {
        submitBTN.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paymentInfo = new PaymentInfo(numberET.getText().toString(), nameET.getText().toString(), amountET.getText().toString(), narrationET.getText().toString());
                        Intent i = new Intent(MainActivity.this, MainActivity2.class);
                        i.putExtra("myObject", new Gson().toJson(paymentInfo));
                        startActivity(i);
                    }
                }
        );
    }
}
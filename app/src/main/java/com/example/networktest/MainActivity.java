package com.example.networktest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editTextNumber);
        long matrNr = Long.parseLong(editText.getText().toString());
        Button btn = findViewById(R.id.sendBtn);
        TextView serverResponse = findViewById(R.id.serverResponse);
        try {
            Socket socket = new Socket("se2-isys.aau.at", 53212);

            //send to server
            OutputStream output = socket.getOutputStream();
            byte[] number = BigInteger.valueOf(matrNr).toByteArray();
            btn.setOnClickListener(view -> {

                    try {
                        output.write(number);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            });


            //receive response
            String response = socket.getInputStream().toString();
            serverResponse.setText(response);
            socket.close();
        }catch (Exception e){
            System.err.println("Error occurred!");
        }
    }
}
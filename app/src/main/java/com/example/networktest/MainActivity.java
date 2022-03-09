package com.example.networktest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button btn;
    TextView serverResponse;
    Context context; //because of android.content.Context.getApplicationInfo()' on a null object reference
    Socket socket;
    Button calculate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        editText = findViewById(R.id.editTextNumber);
        serverResponse = findViewById(R.id.serverResponse);
        btn = findViewById(R.id.sendBtn);
        calculate = findViewById(R.id.calculate);

        calculate.setOnClickListener(this::primeOnly);

    }

    public void send(View view) { //button has send-method as OnClick-Attribute
        Log.i("i", "send clicked");
        String matrNr = editText.getText().toString();
        ServerCommunication serverCommunication = new ServerCommunication();
        serverCommunication.execute(matrNr); //executes task; starts asyncTask ServerCommunication
    }

    @SuppressLint("StaticFieldLeak")
    public class ServerCommunication extends AsyncTask<String, Void, String> { //Async because can't run on main thread

        String response ="";


        @Override
        protected String doInBackground(String... strings) {
            try {
                socket = new Socket("se2-isys.aau.at", 53212);

                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                output.writeBytes(strings[0]+'\n'); //needs \n
                output.flush();

                BufferedReader serverInput = new BufferedReader(new InputStreamReader((socket.getInputStream())));
                response = serverInput.readLine();

                socket.close();

            }catch (IOException e){
                e.printStackTrace();
            }
            return response;

        }


        @Override
        protected void onPostExecute(String result) { //String result is automatically set as returned value of doInBackground
            super.onPostExecute(result);
            serverResponse.setText(result);

        }
    }
//2.2 MatrNr: 11808621 mod 7 = 6 => nur jene Ziffern ausgeben, die Primzahlen sind
    /*
    Algorithmus:
    Teile n der Reihe nach von 2 bis n-1 durch alle Zahlen und schaue jeweils, ob ein Rest (d.h. eine Zahl ungleich 0)
    bei der Division entsteht. Wenn das immer der Fall ist, dann handelt es sich um eine Primzahl.
     */
public void primeOnly(View view) {
    try {
        int length = editText.getText().toString().length();
        int matrNr = Integer.parseInt(editText.getText().toString());
        StringBuilder res = new StringBuilder();
        boolean isPrime = true;
        int k = 2;
        for (int i = 0; i < length; i++) {
            isPrime = true;
            int number = matrNr % 10; //go through numbers from last to first (right to left)

            while (k < number) {
                if (number % k == 0) {
                    isPrime = false;
                    break;
                }
                k++;

            }

            if (isPrime) {
                res.append(number);
            }

            matrNr /= 10; //cut last number
        }
        //reverse res to get right order for matrNr
        String reverseRes = res.reverse().toString();
        serverResponse.setText(reverseRes);
    }catch (NumberFormatException e){
        serverResponse.setText(R.string.ungueltigeMatrNr);
    }
    }

}




package com.example.androidir;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.ConsumerIrManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Object irdaService;
    Method irWrite;
    SparseArray<String> irData;
    TextView mFreqsText;
    ConsumerIrManager mCIR;
    Button btn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Be sure to call the super class.
        super.onCreate(savedInstanceState);
        setTitle("IR APP TD RA");
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("-------------------------------------------------------------------------");

                String data = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
                if (data != null) {
//                    String values[] = data.split(",");
//                    int[] pattern = new int[values.length-1];
//
//                    for (int i=0; i<pattern.length; i++){
//                        pattern[i] = Integer.parseInt(values[i+1]);
//                    }
//                    int pattern[] = new int[50] ;
//                    for(int i = 0 ; i < 50 ; i++)
//                        pattern[i] = i * i ;
                    int pattern[] = new int[2] ;
                    pattern[0] = 1000 ;
                    pattern[1] = 1000 ;


                    mCIR.transmit(700, pattern);
                }

            }
        });

//        setContentView(R.layout.activity_main);

//        irData = new SparseArray<String>();
//        irData.put(
//                R.id.button,
//                hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0040 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e"));
//        irData.put(
//                R.id.buttonChUp,
//                hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 003f 0015 003f 0015 0015 0015 0040 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e"));
//        irData.put(
//                R.id.buttonChDown,
//                hex2dec("0000 006d 0022 0003 00a9 00a8 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 0015 003f 0015 0015 0015 0015 0015 0015 0015 003f 0015 003f 0015 003f 0015 003f 0015 0015 0015 003f 0015 003f 0015 003f 0015 0702 00a9 00a8 0015 0015 0015 0e6e"));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

            irInit4KitKat();
        }else{
            irInit4JellyBean();
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void irInit4KitKat() {

        // Get a reference to the ConsumerIrManager
        System.out.println("zbeeeeeeeeeeeeeeeeeeeeeeeeeel");
        mCIR = null;
        mCIR = (ConsumerIrManager)getSystemService(Context.CONSUMER_IR_SERVICE);
        if(mCIR == null) System.out.println("KHRAAAAAAAAAAAAAAAA");

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void irInit4JellyBean() {
        irdaService = this.getSystemService(Context.CONSUMER_IR_SERVICE);
        Class c = irdaService.getClass();
        Class p[] = { String.class };
        try {
            irWrite = c.getMethod("write_irsend", p);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void irSend(View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

            irSend4Kitkat(view);
        }else{

            irSend4JellyBean(view);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void irSend4Kitkat(View view) {


        String data = irData.get(view.getId());
        if (data != null) {
            String values[] = data.split(",");
            int[] pattern = new int[values.length-1];

            for (int i=0; i<pattern.length; i++){
                pattern[i] = Integer.parseInt(values[i+1]);
            }

            mCIR.transmit(Integer.parseInt(values[0]), pattern);
        }
    }

    private void irSend4JellyBean(View view) {
        String data = irData.get(view.getId());
        if (data != null) {
            try {
                irWrite.invoke(irdaService, data);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    protected String hex2dec(String irData) {
        List<String> list = new ArrayList<String>(Arrays.asList(irData
                .split(" ")));
        list.remove(0); // dummy
        int frequency = Integer.parseInt(list.remove(0), 16); // frequency
        list.remove(0); // seq1
        list.remove(0); // seq2

        for (int i = 0; i < list.size(); i++) {
            list.set(i, Integer.toString(Integer.parseInt(list.get(i), 16)));
        }

        frequency = (int) (1000000 / (frequency * 0.241246));
        list.add(0, Integer.toString(frequency));

        irData = "";
        for (String s : list) {
            irData += s + ",";
        }
        return irData;
    }

}




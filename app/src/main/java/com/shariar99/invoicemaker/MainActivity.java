package com.shariar99.invoicemaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button generateBTN;
    EditText nameText,numberText,itemNumberOne,itemNumberTwo;
    Spinner spinnerOne,spinnerTwo;
    Bitmap bmp,scalebmp;
    int pageWith=1200;
    Date dateObj;
    DateFormat dateFormat;
    float[] prices = new float[]{0,200,500,450,325,499};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateBTN = findViewById(R.id.generateBTN);
        nameText = findViewById(R.id.nameText);
        numberText = findViewById(R.id.numberText);
        numberText = findViewById(R.id.numberText);
        itemNumberOne = findViewById(R.id.itemNumberOne);
        itemNumberTwo= findViewById(R.id.itemNumberTwo);
        spinnerOne = findViewById(R.id.spinnerOne);
        spinnerTwo = findViewById(R.id.spinnerTwo);
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.pizzaheads);
        scalebmp = Bitmap.createScaledBitmap(bmp,1200,518,false);

        //Permission for write external storage
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        createPDF();
    }

    public void createPDF(){
        generateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dateObj = new Date();


                if (nameText.getText().toString().length()==0 ||
                numberText.getText().toString().length()==0||
                itemNumberOne.getText().toString().length()==0||
                itemNumberTwo.getText().toString().length()==0){
                    Toast.makeText(MainActivity.this, "Some fields Empty", Toast.LENGTH_SHORT).show();
                }else {
                    PdfDocument mypdfDoc = new PdfDocument();
                    Paint myPaint = new Paint();
                    Paint titlepaint= new Paint();


                    PdfDocument.PageInfo mypageInfo1 = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                    PdfDocument.Page myPage1 = mypdfDoc.startPage(mypageInfo1);
                    Canvas canvas = myPage1.getCanvas();
                    //Title Picture
                    canvas.drawBitmap(scalebmp,0,0,myPaint);
                  //Title Item Name
                    titlepaint.setTextAlign(Paint.Align.CENTER);
                    titlepaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlepaint.setTextSize(70);
                    canvas.drawText("Dimonr Pizza",pageWith/2,270,titlepaint);
                    //side Phone Number
                    myPaint.setColor(Color.rgb(0,113,188));
                    myPaint.setTextSize(30f);
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("Call:019123456",1160,40,myPaint);
                    canvas.drawText("017324515",1160,80,myPaint);
                    //title Invoice
                    titlepaint.setTextAlign(Paint.Align.CENTER);
                    titlepaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.ITALIC));
                    titlepaint.setTextSize(70);
                    canvas.drawText("Invoice",pageWith/2,500,titlepaint);
                    //customer Name
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setTextSize(35f);
                    myPaint.setColor(Color.BLACK);
                    canvas.drawText("Customer Name :" + nameText.getText(),20,590,myPaint);
                    canvas.drawText("Customer No :" + numberText.getText(),20,640,myPaint);

                    //invoice no
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("Invoice No:" +"32415",pageWith-20,590,myPaint);

                    //current Date
                    dateFormat = new SimpleDateFormat("dd/MM/yy");
                    canvas.drawText("Date: "+ dateFormat.format(dateObj),pageWith-20,640,myPaint);

                    //current Time
                    dateFormat = new SimpleDateFormat("HH:mm:ss");
                    canvas.drawText("Time: "+ dateFormat.format(dateObj),pageWith-20,690,myPaint);

                    //create table line
                    myPaint.setStyle(Paint.Style.STROKE);
                    myPaint.setStrokeWidth(2);
                    canvas.drawRect(20,780,pageWith-20,860,myPaint);
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    myPaint.setStyle(Paint.Style.FILL);
                    canvas.drawText("Si. No.",40,830,myPaint);
                    canvas.drawText("Item Description.",200,830,myPaint);
                    canvas.drawText("Price.",700,830,myPaint);
                    canvas.drawText("Qty",900,830,myPaint);
                    canvas.drawText("Total",1050,830,myPaint);

                    canvas.drawLine(180,790,180,840,myPaint);
                    canvas.drawLine(680,790,680,840,myPaint);
                    canvas.drawLine(880,790,880,840,myPaint);
                    canvas.drawLine(1030,790,1030,840,myPaint);

                    //Item spinner selected
                    float total = 0,total2= 0;
                    if (spinnerOne.getSelectedItemPosition()!=0)
                    {
                        canvas.drawText("1.",40,950,myPaint);
                        canvas.drawText(spinnerOne.getSelectedItem().toString(),200,950,myPaint);
                        canvas.drawText(String.valueOf(prices[spinnerOne.getSelectedItemPosition()]),700,950,myPaint);
                        canvas.drawText(itemNumberOne.getText().toString(),900,950,myPaint);
                        total = Float.parseFloat(itemNumberOne.getText().toString())*prices[spinnerOne.getSelectedItemPosition()];
                        myPaint.setTextAlign(Paint.Align.RIGHT);
                        canvas.drawText(String.valueOf(total),pageWith-40,950,myPaint);
                        myPaint.setTextAlign(Paint.Align.LEFT);
                    }
                    if (spinnerTwo.getSelectedItemPosition()!=0)
                    {
                        canvas.drawText("2.",40,1050,myPaint);
                        canvas.drawText(spinnerTwo.getSelectedItem().toString(),200,1050,myPaint);
                        canvas.drawText(String.valueOf(prices[spinnerTwo.getSelectedItemPosition()]),700,1050,myPaint);
                        canvas.drawText(itemNumberTwo.getText().toString(),900,1050,myPaint);
                        total2 = Float.parseFloat(itemNumberTwo.getText().toString())*prices[spinnerTwo.getSelectedItemPosition()];
                        myPaint.setTextAlign(Paint.Align.RIGHT);
                        canvas.drawText(String.valueOf(total2),pageWith-40,1050,myPaint);
                        myPaint.setTextAlign(Paint.Align.LEFT);
                    }
                    float subTotal = total+total2;
                    canvas.drawLine(680,1200,pageWith-20,1200,myPaint);
                    canvas.drawText("sub total",700,1250,myPaint);
                    canvas.drawText(":",900,1250,myPaint);
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(String.valueOf(subTotal),pageWith-40,1250,myPaint);

                    myPaint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText("Tax (12%)", 700,1300,myPaint);
                    canvas.drawText(":",900,1300,myPaint);
                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(String.valueOf(subTotal*12/100),pageWith-40,1300,myPaint);

                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    myPaint.setColor(Color.rgb(247,147,30));
                    canvas.drawRect(680,1350,pageWith-20,1450,myPaint);

                    myPaint.setColor(Color.BLACK);
                    myPaint.setTextSize(50f);
                    myPaint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText("Total",700,1415,myPaint);

                    myPaint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText(String.valueOf(subTotal+(subTotal*12/100)),pageWith-40,1415,myPaint);




                    mypdfDoc.finishPage(myPage1);

// Generate a unique filename based on current date and time
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                    String currentDateAndTime = sdf.format(new Date());
                    String filename = "Invoice_" + currentDateAndTime + ".pdf";

                    File file = new File(Environment.getExternalStorageDirectory(), filename);

                    try {
                        mypdfDoc.writeTo(new FileOutputStream(file));
                        Toast.makeText(MainActivity.this, "PDF saved as: " + filename, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mypdfDoc.close();
                }
            }
        });
    }
}
package com.example.Szaman.Receipt_Writer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.os.Build;
import android.os.Environment;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.Szaman.MainActivity;
import com.example.Szaman.R;
import com.example.Szaman.model.CartItem;
import com.example.Szaman.model.User;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class PDFWriter {
    int pageHeight = 1120;
    int pagewidth = 792;
    String pdfName="";
    Context mcontext = null;

    public PDFWriter(Context mcontext) {
        this.mcontext = mcontext;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void generatePDF(User user, ArrayList<CartItem> dataBank) throws IOException {
        pdfName=""+ Calendar.getInstance().getTime()+"_"+user.getUserId()+".pdf";
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();
        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();
        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();
        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        //canvas.drawBitmap(bitmap, 56, 40, paint);
        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(24);
        paint.setTextSize(15);
        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(mcontext, R.color.black));
        paint.setColor(ContextCompat.getColor(mcontext, R.color.black));
        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        StringBuilder dishes = new StringBuilder();
        double price = 0.0;
        for (CartItem c : dataBank) {
            dishes.append("\n").append(c.getDish().getName()).append("\t").append(c.getDish().getPrice()).append("\t x").append(c.getCountOfDish()).append("\n");
            price += c.getDish().getPrice() * c.getCountOfDish();
        }

        QRCodeWriter qrCodeWriter=new QRCodeWriter(mcontext);
        Bitmap qrCode= qrCodeWriter.qrCodeCreate(""+user.getLogin()+","+user.getUserId()+","+user.getDebitCardNumber()+"\n"+ dishes);
        InputStream ims = mcontext.getAssets().open("logo_login.png");
        Bitmap logoBmp = BitmapFactory.decodeStream(ims);
        canvas.drawBitmap(logoBmp,281,20,null);
        canvas.drawText("Thank you " + user.getName() + " for Buying with Szama(n)", 150, 300, title);
        canvas.drawText("You have bought:", 100, 480, title);

        TextPaint mTextPaint = new TextPaint();
        StaticLayout mTextLayout = new StaticLayout( dishes ,mTextPaint, canvas.getWidth() - 100, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.save();
        canvas.translate(150, 520);
        mTextLayout.draw(canvas);
        canvas.restore();
        canvas.drawText("For:\t "+price+"zl", 100, 1000, title);
        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(mcontext, R.color.black));
        title.setTextSize(15);
        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);
        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);
        PdfDocument.PageInfo secondmypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 2).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page secondPage= pdfDocument.startPage(secondmypageInfo);
        Canvas secCanvas= secondPage.getCanvas();
        secCanvas.drawBitmap(qrCode,221,100,null);
        pdfDocument.finishPage(secondPage);
        // below line is used to set the name of
        // our PDF file and its path.
        File dir = new File(Environment.getExternalStorageDirectory(),pdfName);

        try {

            pdfDocument.writeTo(new FileOutputStream(dir));
        } catch (Exception e){
            e.printStackTrace();
        }

        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }

}

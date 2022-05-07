package com.example.Szaman.receiptWriter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.example.Szaman.R;
import com.example.Szaman.model.CartItem;
import com.example.Szaman.model.User;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PDFWriter {
    int pageHeight = 1120;
    int pagewidth = 792;
    Context mcontext=null;
    public PDFWriter(Context mcontext) {
        this.mcontext = mcontext;
    }

    public void generatePDF(User user, ArrayList<CartItem> dataBank) {
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
        paint.setColor(ContextCompat.getColor(mcontext,R.color.black));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.

        canvas.drawText("Thank you "+user.getName()+" for Buying with Szama(n)", 209, 100, title);
        String dishes="";
        double price=0.0;
        for (CartItem c:dataBank) {
            dishes+= c.getDish().getName()+"\t"+c.getDish().getPrice()+"\t"+c.getCountOfDish()+"\n";
            price+=c.getDish().getPrice()*c.getCountOfDish();
        }
        canvas.drawText("You have bought:", 209, 80, title);
        canvas.drawText(dishes, 209, 60, paint);
        canvas.drawText("For:\t price", 209, 200, title);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(mcontext, R.color.black));
        title.setTextSize(15);

        // below line is used for setting
        // our text to center of PDF.
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("This is sample document which we have created.", 396, 560, title);

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        File file = new File("/data/data/com.example.p1/PDF", "Receipt.pdf");
        Log.w("PDFPath",Environment.getExternalStorageDirectory().toString());
        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }

}
///////////////
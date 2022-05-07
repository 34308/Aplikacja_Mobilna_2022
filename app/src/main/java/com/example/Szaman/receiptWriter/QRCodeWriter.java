package com.example.Szaman.receiptWriter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import androidx.annotation.RequiresApi;
import com.google.zxing.WriterException;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRCodeWriter {
    Context mContext;
    public QRCodeWriter(Context mContext) {
        this.mContext = mContext;
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public Bitmap qrCodeCreate(String content){
        Bitmap bitmap = null;
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        int width = point.x;
        int height = point.y;
        Display display = manager.getDefaultDisplay();
        display.getSize(point);
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;
        QRGEncoder qrgEncoder = new QRGEncoder(content, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }
        return bitmap;
    }

}
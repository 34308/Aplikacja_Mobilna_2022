package com.example.Szaman.receiptWriter;

import static com.itextpdf.kernel.geom.PageSize.A4;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import com.example.Szaman.model.CartItem;
import com.example.Szaman.model.User;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class PDFWriter {
    int pageHeight = 1120;
    int pagewidth = 792;
    String pdfName="";
    Context mcontext = null;

    public PDFWriter(Context mcontext) {
        this.mcontext = mcontext;
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void generatePDF(User currentUser, ArrayList<CartItem> dataBank,String note,boolean delivery) throws FileNotFoundException {

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        pdfName=""+ Calendar.getInstance().getTime()+"_"+currentUser.getUserId()+".pdf";
        File file=new File(pdfPath,pdfName);
        PdfWriter pdfWriter=new PdfWriter(file);
        Random random=new Random();
        long nr= random.nextInt(123);

        com.itextpdf.layout.element.List orders=new com.itextpdf.layout.element.List();
        com.itextpdf.layout.element.List orders2=new com.itextpdf.layout.element.List();
        StringBuilder qrOrders= new StringBuilder();
        int it=0;
        double price=0;
        for (CartItem cartItem:dataBank) {
            if(it>=12){
                orders2.add(cartItem.getDish().getName()+" x"+cartItem.getCountOfDish());
            }
            else{
                orders.add(cartItem.getDish().getName()+" x"+cartItem.getCountOfDish());
            }
            price+=cartItem.getCountOfDish()*cartItem.getDish().getPrice();
            qrOrders.append(cartItem.getDish().getName()).append(" x").append(cartItem.getCountOfDish());
            it++;
        }
        com.itextpdf.kernel.pdf.PdfDocument pdfDocument=new com.itextpdf.kernel.pdf.PdfDocument(pdfWriter);
        pdfDocument.addNewPage();
        Document document=new Document(pdfDocument);
        pdfDocument.setDefaultPageSize(A4);
        float col=260f;
        float[] columnWidth={col,col};
        Table table=new Table(columnWidth);
        table.setBackgroundColor(new DeviceRgb(65,123,243));
        table.addCell(new Cell().add(new Paragraph( "Szama(n)"))
                .setMargin(40f)
                .setMarginBottom(40f)
                .setFontSize(40f).setBorder(Border.NO_BORDER));

        //tworzenie KODU QR
        String infoToCode=currentUser.getUserId()+" | "+currentUser.getLogin()+" | "+currentUser.getName()+" | "+currentUser.getSurname()+" | "+" ORDER: "+
                nr+"\n"+qrOrders;
        QRCodeWriter qrCodeWriter=new QRCodeWriter(mcontext);
        Bitmap qrCode= qrCodeWriter.qrCodeCreate(infoToCode);
        ByteArrayOutputStream stream=new com.itextpdf.io.source.ByteArrayOutputStream();
        qrCode.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] bitmapData=stream.toByteArray();
        ImageData imageData= ImageDataFactory.create(bitmapData);

        table.addCell(new Cell().add(new Image(imageData)).setBorder(Border.NO_BORDER).setHorizontalAlignment(HorizontalAlignment.RIGHT));

        Table customerinfo=new Table(columnWidth);
        customerinfo.addCell(new Cell(0,4)
                .setFontColor(ColorConstants.WHITE)
                .add(new Paragraph("Customer INFO") )
                .setBold().setBackgroundColor(new DeviceRgb(50,150,250)));
        customerinfo.addCell(new Cell().add(new Paragraph( "Name & Surname")));
        customerinfo.addCell(new Cell().add(new Paragraph(currentUser.getName()+" "+currentUser.getSurname())) );
        customerinfo.addCell(new Cell().add(new Paragraph("login")));
        customerinfo.addCell(new Cell().add(new Paragraph(currentUser.getLogin())));
        customerinfo.addCell(new Cell().add(new Paragraph("ID")));
        customerinfo.addCell(new Cell().add(new Paragraph(""+currentUser.getUserId()) ));

        Table header=new Table(columnWidth);

        header.setBackgroundColor(new DeviceRgb(65,123,243));
        header.addCell(new Cell()
                .add(new Paragraph("Order"))
                .setMargin(20f)
                .setMarginBottom(20f)
                .setFontSize(20f).setBorder(Border.NO_BORDER)
                .setFontColor(ColorConstants.WHITE));


        Table customerOrder=new Table(columnWidth);
        customerOrder.setBorder(Border.NO_BORDER).setFontSize(12);


        Table summary=new Table(columnWidth);
        summary.setBackgroundColor(new DeviceRgb(65,123,243));
        summary.addCell(new Cell()
                .setFontColor(ColorConstants.WHITE)
                .add(new Paragraph("Price: "+price+"zl"))
                .setMargin(20f)
                .setMarginBottom(20f)
                .setFontSize(20f).setBorder(Border.NO_BORDER));

        if(delivery) summary.addCell("Delivery: YES") .setFontColor(ColorConstants.WHITE);else summary.addCell("Delivery: NO").setFontColor(ColorConstants.WHITE);
        if(!note.isEmpty())summary.addCell(note).setFontColor(ColorConstants.WHITE);
        summary.addCell(new Cell().add(new Paragraph("Order Number: "+nr) ).setFontColor(ColorConstants.WHITE));
        Cell listcell1=new Cell();
        Cell listcell2=new Cell();
        listcell1.add(orders);
        listcell2.add(orders2);
        customerOrder.addCell(listcell1);
        customerOrder.addCell(listcell2);

        document.add(table);
        document.add(new Paragraph("\n"));
        document.add(customerinfo);
        document.add(new Paragraph("\n"));
        document.add(header);
        document.add(customerOrder);
        document.add(new Paragraph("\n"));
        document.add(summary);
        document.close();
    }

}

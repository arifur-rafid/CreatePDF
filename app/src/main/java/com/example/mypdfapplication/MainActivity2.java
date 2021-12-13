package com.example.mypdfapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity2 extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    PaymentInfo paymentInfo;
    private String jsonMyObject;
    String data, date, time, locationData;
    TextView receiptTV, nameTV, amountTV, narrationTV, numberTV, locationTV, TotalTV, dateTV;
    private LinearLayout linear;
    private Bitmap bitmap;
    private Button downloadPDFBTN;
    private String perms[] = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("myObject");
        }
        paymentInfo = new Gson().fromJson(jsonMyObject, PaymentInfo.class);

        ImageButton cancelButton = findViewById(R.id.cancelButton);
        Button shareBTN = findViewById(R.id.shareBTN);
        receiptTV = findViewById(R.id.receiptTV);
        nameTV = findViewById(R.id.dialogBoxNameTV);
        amountTV = findViewById(R.id.dialogBoxAmountTV);
        narrationTV = findViewById(R.id.dialogBoxNarrationTV);
        numberTV = findViewById(R.id.dialogBoxNumberTV);
        locationTV = findViewById(R.id.dialogBoxLocationTV);
        TotalTV = findViewById(R.id.dialogBoxTotalTV);
        dateTV = findViewById(R.id.dateTV);

        receiptTV.setText("Bkash " + "Receipt");
        nameTV.setText(paymentInfo.getUserName());
        amountTV.setText("BDT " + paymentInfo.getAmount());
        narrationTV.setText(paymentInfo.getNarration());
        locationTV.setText("Dhaka");
        numberTV.setText(paymentInfo.getPhoneNumber());
        TotalTV.setText("BDT " + paymentInfo.getAmount());

        dateTV.setText(getDate() + " " + getTime());
        linear = findViewById(R.id.pdfLayout);
        downloadPDFBTN = findViewById(R.id.downloadPDFBTN);

        downloadOnClickListener();

        /* This code handle this error android.os.FileUriExposedException: file:///storage/emulated/0/page.pdf exposed beyond app through Intent.getData()*/
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

    }

    private void downloadOnClickListener() {
        downloadPDFBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
    }

    private void checkPermission() {
        if (EasyPermissions.hasPermissions(MainActivity2.this, perms)) {
            // when permission already granted
            bitmap = LoadBitmap(linear, linear.getWidth(), linear.getHeight());
            createPdf();
        } else {
            // Once user press the deny button it will notify user why this permission is needed through a dialogbox
            EasyPermissions.requestPermissions(MainActivity2.this, "This Application need this permission to make pdf", GALLERY, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        switch (requestCode) {
            case GALLERY:
                bitmap = LoadBitmap(linear, linear.getWidth(), linear.getHeight());
                createPdf();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            {
                // if user select do not show button. app will take him to app permission page
                new AppSettingsDialog.Builder(this).build().show();
            }
        } else {
            // if user select deny button
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
        }
    }


    private Bitmap LoadBitmap(View v, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private void createPdf() {
        // You will find this content in this URL https://www.techypid.com/how-to-convert-android-linearlayout-to-pdf-in-android/
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();

        int A4PageWeidth = 2480;
        int A4PageHeight = 3508;

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(A4PageWeidth, A4PageHeight, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet / 2, true);

        paint.setColor(Color.BLUE);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, (A4PageWeidth / 4) - 60, 100, null);
        document.finishPage(page);

        // write the document content
        String targetPdf = "/sdcard/page.pdf";
        File filePath;
        //File file = new File(Environment.getExternalStorageDirectory() + "/FolderName/" + "yourFile.apk");
        //filePath = new File(Environment.getExternalStorageDirectory() + "/sdcard/" + "page.pdf");
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(this, "successfully pdf created", Toast.LENGTH_SHORT).show();

        openPdf();

    }

    private void openPdf() {
        //File file = new File(Environment.getExternalStorageDirectory() + "" + "page.pdf");
        //startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
        //File file = new File("file:///storage/page.pdf");
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/page.pdf");
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No Application for pdf view", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* Get current date*/
    private String getDate() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        date = currentDate;
        return "" + currentDate;
    }

    /* Get current time*/
    private String getTime() {
        String currentDate = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date());
        time = currentDate;
        return "" + currentDate;
    }

    public void openFile(final String fileName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(android.os.Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "" + fileName));
        intent.setDataAndType(uri, "application/pdf");
        startActivity(intent);
    }
}
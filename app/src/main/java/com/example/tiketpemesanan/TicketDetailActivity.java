package com.example.tiketpemesanan;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

public class TicketDetailActivity extends AppCompatActivity {
    private ImageView ivQrCode;
    private TextView tvJenis, tvMaskapai, tvRute, tvTanggal, tvHarga;

    // simpan detail tiket + qr biar bisa dishare
    private String bookingId, jenis, maskapai, rute, tanggal, qrContent;
    private double harga;
    private Bitmap qrBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        initViews();
        loadData();
        generateQrCode();
    }

    private void initViews() {
        ivQrCode = findViewById(R.id.ivQrCode);
        tvJenis = findViewById(R.id.tvDetailJenis);
        tvMaskapai = findViewById(R.id.tvDetailMaskapai);
        tvRute = findViewById(R.id.tvDetailRute);
        tvTanggal = findViewById(R.id.tvDetailTanggal);
        tvHarga = findViewById(R.id.tvDetailHarga);

        // tombol back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // tombol share
        findViewById(R.id.btnShare).setOnClickListener(v -> shareTicket());
    }

    private void loadData() {
        jenis = getIntent().getStringExtra("jenis");
        maskapai = getIntent().getStringExtra("maskapai");
        rute = getIntent().getStringExtra("rute");
        tanggal = getIntent().getStringExtra("tanggal");
        harga = getIntent().getDoubleExtra("harga", 0);

        tvJenis.setText("Jenis: " + jenis);
        tvMaskapai.setText("Maskapai: " + maskapai);
        tvRute.setText("Rute: " + rute);
        tvTanggal.setText("Tanggal: " + tanggal);
        tvHarga.setText("Harga: Rp " + String.format("%,.0f", harga));
    }

    private void generateQrCode() {
        bookingId = "TKT-" + System.currentTimeMillis();
        qrContent = "TIKET TRANSPORTASI\n" +
                "ID: " + bookingId + "\n" +
                "Jenis: " + jenis + "\n" +
                "Maskapai: " + maskapai + "\n" +
                "Rute: " + rute + "\n" +
                "Tanggal: " + tanggal + "\n" +
                "Harga: Rp " + String.format("%,.0f", harga);

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            qrBitmap = barcodeEncoder.encodeBitmap(qrContent, BarcodeFormat.QR_CODE, 400, 400);
            ivQrCode.setImageBitmap(qrBitmap);
        } catch (Exception e) {
            e.printStackTrace();
            ivQrCode.setImageResource(android.R.drawable.ic_dialog_info);
        }
    }

    private void shareTicket() {
        if (qrBitmap == null) {
            Toast.makeText(this, "QR Code belum tersedia", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // simpan bitmap ke cache & buat Uri
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), qrBitmap, "TicketQR", null);
            Uri qrUri = Uri.parse(path);

            // isi pesan
            String shareText = "🎫 DETAIL TIKET ANDA 🎫\n\n" +
                    "ID: " + bookingId + "\n" +
                    "Jenis: " + jenis + "\n" +
                    "Maskapai: " + maskapai + "\n" +
                    "Rute: " + rute + "\n" +
                    "Tanggal: " + tanggal + "\n" +
                    "Harga: Rp " + String.format("%,.0f", harga) + "\n\n" +
                    "Scan QR Code untuk validasi tiket.";

            // intent share
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, qrUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

            startActivity(Intent.createChooser(shareIntent, "Bagikan tiket via"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal membagikan tiket", Toast.LENGTH_SHORT).show();
        }
    }
}

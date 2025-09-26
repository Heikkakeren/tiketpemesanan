package com.example.tiketpemesanan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {
    private TextView tvRute, tvTanggal, tvHarga;
    private RadioGroup rgPayment;
    private Button btnBayar, btnBatal;
    private int transportId;
    private String rute, tanggal;
    private double harga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initViews();
        loadData();
        setupClickListeners();
    }

    private void initViews() {
        tvRute = findViewById(R.id.tvPaymentRute);
        tvTanggal = findViewById(R.id.tvPaymentTanggal);
        tvHarga = findViewById(R.id.tvPaymentHarga);
        rgPayment = findViewById(R.id.rgPayment);
        btnBayar = findViewById(R.id.btnBayar);
        btnBatal = findViewById(R.id.btnBatal);
    }

    private void loadData() {
        transportId = getIntent().getIntExtra("transportId", -1);
        rute = getIntent().getStringExtra("rute");
        tanggal = getIntent().getStringExtra("tanggal");
        harga = getIntent().getDoubleExtra("harga", 0);

        tvRute.setText(rute);
        tvTanggal.setText(tanggal);
        tvHarga.setText("Total: Rp " + String.format("%.0f", harga));
    }

    private void setupClickListeners() {
        btnBayar.setOnClickListener(v -> processPayment());
        btnBatal.setOnClickListener(v -> finish());
    }

    private void processPayment() {
        // Ambil metode pembayaran
        int selectedId = rgPayment.getCheckedRadioButtonId();
        RadioButton selected = findViewById(selectedId);
        String metode = selected != null ? selected.getText().toString() : "Gopay";

        // Simulasi: langsung sukses (dummy)
        Toast.makeText(this, "Pembayaran berhasil via " + metode + "!", Toast.LENGTH_LONG).show();

        // Simpan ke histori (opsional, bisa pakai tabel baru atau flag di transportasi)
        saveToHistory();

        // Kembali ke daftar
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void saveToHistory() {
        // Untuk versi sederhana, kita simpan timestamp pemesanan di SharedPreferences
        // Atau buat tabel baru "history" di database (lebih baik)
        // Di sini kita pakai SharedPreferences sebagai contoh cepat

        String bookingId = "BOOK-" + System.currentTimeMillis();
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        // Simpan ke histori (nanti bisa ditampilkan di menu "Riwayat")
        getSharedPreferences("HISTORY", MODE_PRIVATE)
                .edit()
                .putString(bookingId, rute + "|" + tanggal + "|" + harga + "|" + currentTime)
                .apply();
    }
}
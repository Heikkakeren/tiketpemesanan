package com.example.tiketpemesanan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class AddTransportasiActivity extends AppCompatActivity {
    private AutoCompleteTextView actJenis;
    private TextInputEditText etMaskapai, etRute, etTanggal, etHarga;
    private Button btnSimpan, btnBatal;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transportasi);

        db = new DatabaseHelper(this);
        initViews();
        setupJenisDropdown();
        setupClickListeners();
    }

    private void initViews() {
        actJenis = findViewById(R.id.actJenis);
        etMaskapai = findViewById(R.id.etMaskapai);
        etRute = findViewById(R.id.etRute);
        etTanggal = findViewById(R.id.etTanggal);
        etHarga = findViewById(R.id.etHarga);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnBatal = findViewById(R.id.btnBatal);
    }

    private void setupJenisDropdown() {
        String[] jenis = {"Pesawat", "Kereta"};

        // ✅ PERBAIKAN: Gunakan layout yang tersedia di Android SDK
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line, // Layout standar Android
                jenis
        );
        actJenis.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnSimpan.setOnClickListener(v -> simpanTransportasi());
        btnBatal.setOnClickListener(v -> finish());
    }

    private void simpanTransportasi() {
        String jenis = actJenis.getText().toString().trim();
        String maskapai = etMaskapai.getText().toString().trim();
        String rute = etRute.getText().toString().trim();
        String tanggal = etTanggal.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();

        if (jenis.isEmpty() || maskapai.isEmpty() || rute.isEmpty() || tanggal.isEmpty() || hargaStr.isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        double harga;
        try {
            harga = Double.parseDouble(hargaStr);
            if (harga <= 0) {
                Toast.makeText(this, "Harga harus lebih dari 0!", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Harga harus berupa angka!", Toast.LENGTH_SHORT).show();
            return;
        }

        Transportasi t = new Transportasi(jenis, maskapai, rute, tanggal, harga);
        long id = db.addTransportasi(t);

        if (id != -1) {
            Toast.makeText(this, "Tiket berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Gagal menyimpan data!", Toast.LENGTH_SHORT).show();
        }
    }
}
package com.example.tiketpemesanan;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class EditTransportasiActivity extends AppCompatActivity {
    private AutoCompleteTextView actJenis;
    private TextInputEditText etMaskapai, etRute, etTanggal, etHarga;
    private Button btnSimpan, btnBatal;
    private DatabaseHelper db;
    private int transportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transportasi);

        db = new DatabaseHelper(this);
        transportId = getIntent().getIntExtra("transport_id", -1);

        if (transportId == -1) {
            Toast.makeText(this, "Data tidak ditemukan!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupJenisDropdown();
        loadTransportasiData();
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                jenis
        );
        actJenis.setAdapter(adapter);
    }

    private void loadTransportasiData() {
        // ✅ PERBAIKAN: Gunakan getAllTransportasi() dan cari berdasarkan ID
        List<Transportasi> allTransportasi = db.getAllTransportasi();
        Transportasi t = null;

        for (Transportasi transport : allTransportasi) {
            if (transport.getId() == transportId) {
                t = transport;
                break;
            }
        }

        if (t != null) {
            actJenis.setText(t.getJenis());
            etMaskapai.setText(t.getMaskapai());
            etRute.setText(t.getRute());
            etTanggal.setText(t.getTanggal());
            etHarga.setText(String.valueOf(t.getHarga()));
        } else {
            Toast.makeText(this, "Data transportasi tidak ditemukan!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupClickListeners() {
        btnSimpan.setOnClickListener(v -> updateTransportasi());
        btnBatal.setOnClickListener(v -> finish());
    }

    private void updateTransportasi() {
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

        // ✅ PERBAIKAN: Buat Transportasi dengan constructor yang sesuai
        Transportasi t = new Transportasi();
        t.setId(transportId);
        t.setJenis(jenis);
        t.setMaskapai(maskapai);
        t.setRute(rute);
        t.setTanggal(tanggal);
        t.setHarga(harga);

        int rows = db.updateTransportasi(t);
        if (rows > 0) {
            Toast.makeText(this, "Tiket berhasil diperbarui!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Gagal memperbarui data!", Toast.LENGTH_SHORT).show();
        }
    }
}
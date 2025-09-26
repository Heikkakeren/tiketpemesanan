package com.example.tiketpemesanan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class TransportasiListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TransportasiAdapter adapter;
    private DatabaseHelper db;
    private TextInputEditText etSearchRute, etSearchTanggal;
    private Button btnSearch;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportasi_list);

        db = new DatabaseHelper(this);
        initViews();
        setupRecyclerView();
        loadAllTransportasi();
        setupClickListeners();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        etSearchRute = findViewById(R.id.etSearchRute);
        etSearchTanggal = findViewById(R.id.etSearchTanggal);
        btnSearch = findViewById(R.id.btnSearch);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void setupRecyclerView() {
        adapter = new TransportasiAdapter(db.getAllTransportasi());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 🔹 Klik item → buka detail tiket
        adapter.setOnItemClickListener(transportasi -> {
            Intent intent = new Intent(TransportasiListActivity.this, TicketDetailActivity.class);
            intent.putExtra("jenis", transportasi.getJenis());
            intent.putExtra("maskapai", transportasi.getMaskapai());
            intent.putExtra("rute", transportasi.getRute());
            intent.putExtra("tanggal", transportasi.getTanggal());
            intent.putExtra("harga", transportasi.getHarga());
            startActivity(intent);
        });

        // 🔹 Klik Edit, Delete, Detail & Bayar
        adapter.setOnActionClickListener(new TransportasiAdapter.OnActionClickListener() {
            @Override
            public void onEditClick(Transportasi transportasi) {
                Intent intent = new Intent(TransportasiListActivity.this, EditTransportasiActivity.class);
                intent.putExtra("id", transportasi.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Transportasi transportasi) {
                db.deleteTransportasi(transportasi.getId());
                loadAllTransportasi();
                Toast.makeText(TransportasiListActivity.this, "Tiket dihapus", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDetailClick(Transportasi transportasi) {
                Intent intent = new Intent(TransportasiListActivity.this, TicketDetailActivity.class);
                intent.putExtra("jenis", transportasi.getJenis());
                intent.putExtra("maskapai", transportasi.getMaskapai());
                intent.putExtra("rute", transportasi.getRute());
                intent.putExtra("tanggal", transportasi.getTanggal());
                intent.putExtra("harga", transportasi.getHarga());
                startActivity(intent);
            }

            @Override
            public void onBayarClick(Transportasi transportasi) {
                // 👇 Aksi ketika tombol Bayar diklik → buka PaymentActivity
                Intent intent = new Intent(TransportasiListActivity.this, PaymentActivity.class);
                intent.putExtra("jenis", transportasi.getJenis());
                intent.putExtra("maskapai", transportasi.getMaskapai());
                intent.putExtra("rute", transportasi.getRute());
                intent.putExtra("tanggal", transportasi.getTanggal());
                intent.putExtra("harga", transportasi.getHarga());
                startActivity(intent);
            }
        });
    }

    private void loadAllTransportasi() {
        List<Transportasi> list = db.getAllTransportasi();
        adapter.updateList(list);
    }

    private void setupClickListeners() {
        fabAdd.setOnClickListener(v -> {
            startActivity(new Intent(TransportasiListActivity.this, AddTransportasiActivity.class));
        });

        btnSearch.setOnClickListener(v -> {
            String rute = etSearchRute.getText().toString().trim();
            String tanggal = etSearchTanggal.getText().toString().trim();

            if (rute.isEmpty() && tanggal.isEmpty()) {
                loadAllTransportasi();
                return;
            }

            List<Transportasi> results = db.searchTransportasi(rute, tanggal);
            adapter.updateList(results);

            if (results.isEmpty()) {
                Toast.makeText(this, "Tidak ada tiket ditemukan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ditemukan " + results.size() + " tiket", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllTransportasi();
    }
}

package com.example.tiketpemesanan;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TransportasiAdapter adapter;
    private DatabaseHelper db;
    private TextInputEditText etSearchRute, etSearchTanggal;
    private Button btnSearch;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cek session
        SessionManager session = new SessionManager(this);
        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

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
        List<Transportasi> transportasiList = db.getAllTransportasi();
        adapter = new TransportasiAdapter(transportasiList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 🔹 Tambahkan listener tombol aksi dari adapter
        adapter.setOnActionClickListener(new TransportasiAdapter.OnActionClickListener() {
            @Override
            public void onEditClick(Transportasi transportasi) {
                Intent intent = new Intent(MainActivity.this, AddTransportasiActivity.class);
                intent.putExtra("transportasi", transportasi);
                startActivityForResult(intent, 100);
            }

            @Override
            public void onDeleteClick(Transportasi transportasi) {
                db.deleteTransportasi(transportasi.getId());
                loadAllTransportasi();
                Toast.makeText(MainActivity.this, "Tiket dihapus", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDetailClick(Transportasi transportasi) {
                Intent intent = new Intent(MainActivity.this, TicketDetailActivity.class);
                intent.putExtra("transportasi", transportasi);
                startActivity(intent);
            }

            @Override
            public void onBayarClick(Transportasi transportasi) {
                Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
                intent.putExtra("transportasi", transportasi);
                startActivity(intent);
            }
        });
    }

    private void loadAllTransportasi() {
        List<Transportasi> list = db.getAllTransportasi();
        if (adapter != null) {
            adapter.updateList(list);
        }
    }

    private void setupClickListeners() {
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTransportasiActivity.class);
            startActivityForResult(intent, 100);
        });

        btnSearch.setOnClickListener(v -> {
            String rute = etSearchRute.getText() != null ? etSearchRute.getText().toString().trim() : "";
            String tanggal = etSearchTanggal.getText() != null ? etSearchTanggal.getText().toString().trim() : "";

            List<Transportasi> results = db.searchTransportasi(rute, tanggal);
            if (adapter != null) {
                adapter.updateList(results);
                if (results.isEmpty()) {
                    Toast.makeText(this, "Tidak ada tiket ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadAllTransportasi();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            SessionManager session = new SessionManager(this);
            session.logout();
            Toast.makeText(this, "Anda telah logout", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_history) {
            Toast.makeText(this, "Fitur riwayat belum diimplementasi", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllTransportasi();
    }
}

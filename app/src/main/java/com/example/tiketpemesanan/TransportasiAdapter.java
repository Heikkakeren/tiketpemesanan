package com.example.tiketpemesanan;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransportasiAdapter extends RecyclerView.Adapter<TransportasiAdapter.ViewHolder> {
    private List<Transportasi> transportasiList;
    private OnItemClickListener onItemClickListener;
    private OnActionClickListener onActionClickListener;

    // 🔹 Listener untuk klik item
    public interface OnItemClickListener {
        void onItemClick(Transportasi transportasi);
    }

    // 🔹 Listener untuk tombol aksi
    public interface OnActionClickListener {
        void onEditClick(Transportasi transportasi);
        void onDeleteClick(Transportasi transportasi);
        void onDetailClick(Transportasi transportasi);
        void onBayarClick(Transportasi transportasi);
    }

    public TransportasiAdapter(List<Transportasi> transportasiList) {
        this.transportasiList = transportasiList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnActionClickListener(OnActionClickListener listener) {
        this.onActionClickListener = listener;
    }

    public void updateList(List<Transportasi> newList) {
        this.transportasiList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transportasi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transportasi transportasi = transportasiList.get(position);
        holder.bind(transportasi);

        // Klik item → detail tiket
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(transportasi);
            }
        });

        // Klik tombol Edit
        holder.btnEdit.setOnClickListener(v -> {
            if (onActionClickListener != null) {
                onActionClickListener.onEditClick(transportasi);
            }
        });

        // Klik tombol Delete
        holder.btnDelete.setOnClickListener(v -> {
            if (onActionClickListener != null) {
                onActionClickListener.onDeleteClick(transportasi);
            }
        });

        // Klik tombol Detail → ke TicketDetailActivity
        holder.btnDetail.setOnClickListener(v -> {
            if (onActionClickListener != null) {
                onActionClickListener.onDetailClick(transportasi);
            } else {
                Intent intent = new Intent(v.getContext(), TicketDetailActivity.class);
                intent.putExtra("transportasi", transportasi); // ✅ sudah Serializable
                v.getContext().startActivity(intent);
            }
        });

        // Klik tombol Bayar → ke PaymentActivity
        holder.btnBayar.setOnClickListener(v -> {
            if (onActionClickListener != null) {
                onActionClickListener.onBayarClick(transportasi);
            } else {
                Intent intent = new Intent(v.getContext(), PaymentActivity.class);
                intent.putExtra("transportasi", transportasi); // ✅ sudah Serializable
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transportasiList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvJenis, tvMaskapai, tvRute, tvTanggal, tvHarga;
        private ImageView iconTransport;
        private ImageButton btnEdit, btnDelete, btnDetail, btnBayar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Ambil komponen dari item_transportasi.xml
            iconTransport = itemView.findViewById(R.id.iconTransport);
            tvJenis = itemView.findViewById(R.id.tvJenis);
            tvMaskapai = itemView.findViewById(R.id.tvMaskapai);
            tvRute = itemView.findViewById(R.id.tvRute);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnDetail = itemView.findViewById(R.id.btnDetail);
            btnBayar = itemView.findViewById(R.id.btnBayar);
        }

        public void bind(Transportasi transportasi) {
            tvJenis.setText(transportasi.getJenis());
            tvMaskapai.setText(transportasi.getMaskapai());
            tvRute.setText(transportasi.getRute());
            tvTanggal.setText(transportasi.getTanggal());
            tvHarga.setText("Rp " + String.format("%,.0f", transportasi.getHarga()));

            // 🔹 Ganti ikon sesuai jenis transportasi
            if (transportasi.getJenis().equalsIgnoreCase("Pesawat")) {
                iconTransport.setImageResource(R.drawable.ic_airplane);
            } else if (transportasi.getJenis().equalsIgnoreCase("Kereta")) {
                iconTransport.setImageResource(R.drawable.ic_train);
            } else if (transportasi.getJenis().equalsIgnoreCase("Bus")) {
                iconTransport.setImageResource(R.drawable.ic_bus);
            } else {
                iconTransport.setImageResource(R.drawable.ic_transport); // default
            }
        }
    }
}

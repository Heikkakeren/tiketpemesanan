package com.example.tiketpemesanan;

import java.io.Serializable;

public class Transportasi implements Serializable {
    private int id;
    private String jenis;
    private String maskapai;
    private String rute;
    private String tanggal;
    private double harga;

    // Constructor default
    public Transportasi() {}

    // Constructor dengan parameter
    public Transportasi(String jenis, String maskapai, String rute, String tanggal, double harga) {
        this.jenis = jenis;
        this.maskapai = maskapai;
        this.rute = rute;
        this.tanggal = tanggal;
        this.harga = harga;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }

    public String getMaskapai() { return maskapai; }
    public void setMaskapai(String maskapai) { this.maskapai = maskapai; }

    public String getRute() { return rute; }
    public void setRute(String rute) { this.rute = rute; }

    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }
}

package com.example.tiketpemesanan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tiketpemesanan.db";
    private static final int DATABASE_VERSION = 1;

    // Tabel User
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";

    // Tabel Transportasi
    private static final String TABLE_TRANSPORTASI = "transportasi";
    private static final String COL_TRANSPORT_ID = "id";
    private static final String COL_JENIS = "jenis";
    private static final String COL_MASKAPAI = "maskapai";
    private static final String COL_RUTE = "rute";
    private static final String COL_TANGGAL = "tanggal";
    private static final String COL_HARGA = "harga";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Buat tabel users
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_USERNAME + " TEXT NOT NULL UNIQUE,"
                + COL_EMAIL + " TEXT NOT NULL UNIQUE,"
                + COL_PASSWORD + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // Buat tabel transportasi
        String CREATE_TRANSPORTASI_TABLE = "CREATE TABLE " + TABLE_TRANSPORTASI + "("
                + COL_TRANSPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_JENIS + " TEXT NOT NULL,"
                + COL_MASKAPAI + " TEXT NOT NULL,"
                + COL_RUTE + " TEXT NOT NULL,"
                + COL_TANGGAL + " TEXT NOT NULL,"
                + COL_HARGA + " REAL NOT NULL" + ")";
        db.execSQL(CREATE_TRANSPORTASI_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSPORTASI);
        onCreate(db);
    }

    // ──────────────────────── USER METHODS ────────────────────────

    public boolean registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, user.getUsername());
        values.put(COL_EMAIL, user.getEmail());
        values.put(COL_PASSWORD, user.getPassword());

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public User loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(
                    TABLE_USERS,
                    new String[]{COL_USER_ID, COL_USERNAME, COL_EMAIL, COL_PASSWORD},
                    COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                    new String[]{username, password},
                    null, null, null
            );

            if (cursor != null && cursor.moveToFirst()) {
                User user = new User();
                user.setId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setPassword(cursor.getString(3));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return null;
    }

    // ──────────────────────── TRANSPORTASI METHODS ────────────────────────

    public long addTransportasi(Transportasi t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_JENIS, t.getJenis());
        values.put(COL_MASKAPAI, t.getMaskapai());
        values.put(COL_RUTE, t.getRute());
        values.put(COL_TANGGAL, t.getTanggal());
        values.put(COL_HARGA, t.getHarga());

        long id = db.insert(TABLE_TRANSPORTASI, null, values);
        db.close();
        return id;
    }

    public List<Transportasi> getAllTransportasi() {
        List<Transportasi> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_TRANSPORTASI, null);
            if (cursor.moveToFirst()) {
                do {
                    Transportasi t = new Transportasi();
                    t.setId(cursor.getInt(0));
                    t.setJenis(cursor.getString(1));
                    t.setMaskapai(cursor.getString(2));
                    t.setRute(cursor.getString(3));
                    t.setTanggal(cursor.getString(4));
                    t.setHarga(cursor.getDouble(5));
                    list.add(t);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return list;
    }

    public List<Transportasi> searchTransportasi(String rute, String tanggal) {
        List<Transportasi> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM " + TABLE_TRANSPORTASI + " WHERE rute LIKE ? AND tanggal LIKE ?";
            cursor = db.rawQuery(query, new String[]{"%" + rute + "%", "%" + tanggal + "%"});
            if (cursor.moveToFirst()) {
                do {
                    Transportasi t = new Transportasi();
                    t.setId(cursor.getInt(0));
                    t.setJenis(cursor.getString(1));
                    t.setMaskapai(cursor.getString(2));
                    t.setRute(cursor.getString(3));
                    t.setTanggal(cursor.getString(4));
                    t.setHarga(cursor.getDouble(5));
                    list.add(t);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return list;
    }

    public int updateTransportasi(Transportasi t) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_JENIS, t.getJenis());
        values.put(COL_MASKAPAI, t.getMaskapai());
        values.put(COL_RUTE, t.getRute());
        values.put(COL_TANGGAL, t.getTanggal());
        values.put(COL_HARGA, t.getHarga());

        int rows = db.update(TABLE_TRANSPORTASI, values, COL_TRANSPORT_ID + " = ?",
                new String[]{String.valueOf(t.getId())});
        db.close();
        return rows;
    }

    public void deleteTransportasi(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSPORTASI, COL_TRANSPORT_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
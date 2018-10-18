package com.fatiha.ichsannuur.budgetalarm.Object;

public class Alarm {
    public int id_pengeluaran;
    public String deskripsi;
    public int jumlah_pengeluaran;
    public String tanggal;
    public String waktu;
    public String ulang;
    public String type;
    public String status;
    public String uri;


    public int getId_pengeluaran() {
        return id_pengeluaran;
    }

    public void setId_pengeluaran(int id_pengeluaran) {
        this.id_pengeluaran = id_pengeluaran;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getJumlah_pengeluaran() {
        return jumlah_pengeluaran;
    }

    public void setJumlah_pengeluaran(int jumlah_pengeluaran) {
        this.jumlah_pengeluaran = jumlah_pengeluaran;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getUlang() {
        return ulang;
    }

    public void setUlang(String ulang) {
        this.ulang = ulang;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}

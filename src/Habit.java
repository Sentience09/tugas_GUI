public class Habit {
    private String namaKebiasaan;
    private String kategori;
    private boolean selesai;

    public Habit(String namaKebiasaan, String kategori) {
        this.namaKebiasaan = namaKebiasaan;
        this.kategori = kategori;
        this.selesai = false;
    }

    public String getNamaKebiasaan() {
        return namaKebiasaan;
    }

    public void setNamaKebiasaan(String namaKebiasaan) {
        this.namaKebiasaan = namaKebiasaan;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public boolean isSelesai() {
        return selesai;
    }

    public void setSelesai(boolean selesai) {
        this.selesai = selesai;
    }

    // Method untuk menghasilkan teks tampilan pada checkbox
    public String getFormatTampilan() {
        return "[" + kategori + "] " + namaKebiasaan;
    }
}
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Habit_Frame extends JFrame implements ActionListener, ItemListener {

    private JTextField txtNamaHabit;
    private JComboBox<String> cbKategori;
    private JButton btnTambah, btnReset;
    private JPanel panelDaftarHabit;
    private JProgressBar progressBar;
    private JLabel lblStatus;

    private Habit_Manager habit_Manager;
    private List<JCheckBox> listCheckBox;

    public Habit_Frame() {
        habit_Manager = new Habit_Manager();
        listCheckBox = new ArrayList<>();

        initComponents();
        muatDataAwal();
    }

    private void initComponents() {
        // 1. TOP-LEVEL CONTAINER (JFrame)
        setTitle("Habit Tracker - Pelacak Kebiasaan Harian (5C)");
        setSize(520, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // LAYOUT MANAGER 1: BorderLayout untuk JFrame utama
        setLayout(new BorderLayout(10, 10));

        // 2. INTERMEDIATE COMPONENT 1: JPanel untuk Form Input (Bagian NORTH)
        // LAYOUT MANAGER 2: GridLayout (3 baris, 2 kolom)
        JPanel panelInput = new JPanel(new GridLayout(3, 2, 8, 8));
        panelInput.setBorder(new EmptyBorder(15, 15, 5, 15));

        // 3. ATOMIC COMPONENTS pada Form Input
        panelInput.add(new JLabel("Kebiasaan Baru:"));
        txtNamaHabit = new JTextField();
        panelInput.add(txtNamaHabit);

        panelInput.add(new JLabel("Kategori:"));
        String[] kategori = {"Kesehatan", "Belajar", "Ibadah", "Produktivitas"};
        cbKategori = new JComboBox<>(kategori);
        panelInput.add(cbKategori);

        btnTambah = new JButton("Tambah Kebiasaan");
        btnReset = new JButton("Reset Centang Harian");
        panelInput.add(btnTambah);
        panelInput.add(btnReset);

        add(panelInput, BorderLayout.NORTH);

        // INTERMEDIATE COMPONENT 2: JPanel & JScrollPane untuk Daftar Checkbox (Bagian CENTER)
        panelDaftarHabit = new JPanel(new GridLayout(0, 1, 5, 5));
        panelDaftarHabit.setBorder(new EmptyBorder(10, 15, 10, 15));

        JScrollPane scrollPane = new JScrollPane(panelDaftarHabit);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Checklist Kebiasaan Hari Ini"));
        add(scrollPane, BorderLayout.CENTER);

        // INTERMEDIATE COMPONENT 3: JPanel untuk Progress Bar & Status (Bagian SOUTH)
        JPanel panelBawah = new JPanel(new BorderLayout(5, 5));
        panelBawah.setBorder(new EmptyBorder(5, 15, 15, 15));

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        // LAYOUT MANAGER 3: FlowLayout untuk meratakan teks status ke kiri
        JPanel panelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        lblStatus = new JLabel("Status: Belum ada kebiasaan yang diselesaikan hari ini.");
        panelStatus.add(lblStatus);

        panelBawah.add(progressBar, BorderLayout.NORTH);
        panelBawah.add(panelStatus, BorderLayout.SOUTH);

        add(panelBawah, BorderLayout.SOUTH);

        // 4. REGISTRASI EVENT HANDLING (ActionListener pada tombol)
        btnTambah.addActionListener(this);
        btnReset.addActionListener(this);
    }

    private void muatDataAwal() {
        tambahHabitBaru("Minum air putih 2 liter", "Kesehatan");
        tambahHabitBaru("Latihan koding PBO Java 30 menit", "Belajar");
        tambahHabitBaru("Merapikan tempat tidur", "Produktivitas");
    }

    private void tambahHabitBaru(String nama, String kategori) {
        // Membuat objek dari class Habit
        Habit habitBaru = new Habit(nama, kategori);
        habit_Manager.tambahHabit(habitBaru);

        // Membuat Atomic Component JCheckBox berdasarkan objek Habit
        JCheckBox checkBox = new JCheckBox(habitBaru.getFormatTampilan());
        checkBox.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // Mendaftarkan INTERFACE 2: ItemListener ke JCheckBox
        checkBox.addItemListener(this);

        listCheckBox.add(checkBox);
        panelDaftarHabit.add(checkBox);

        panelDaftarHabit.revalidate();
        panelDaftarHabit.repaint();
        perbaruiTampilanProgres();
    }

    private void perbaruiTampilanProgres() {
        int total = habit_Manager.getTotalHabit();
        int selesai = habit_Manager.getJumlahSelesai();
        int persentase = habit_Manager.hitungPersentase();

        progressBar.setValue(persentase);

        if (total == 0) {
            lblStatus.setText("Status: Daftar kebiasaan masih kosong.");
        } else if (selesai == total) {
            lblStatus.setText("Status: Luar biasa! Semua kebiasaan selesai (" + selesai + "/" + total + ")!");
        } else {
            lblStatus.setText("Status: " + selesai + " dari " + total + " kebiasaan telah dikerjakan.");
        }
    }

    //ActionListener (Menangani aksi klik tombol)
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnTambah) {
            String namaHabit = txtNamaHabit.getText().trim();
            String kategori = (String) cbKategori.getSelectedItem();

            if (namaHabit.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Masukkan nama kebiasaan terlebih dahulu!",
                        "Input Kosong",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                tambahHabitBaru(namaHabit, kategori);
                txtNamaHabit.setText("");
                txtNamaHabit.requestFocus();
            }
        } else if (e.getSource() == btnReset) {
            habit_Manager.resetSemuaStatus();
            for (JCheckBox cb : listCheckBox) {
                cb.setSelected(false);
            }
            lblStatus.setText("Status: Checklist harian berhasil di-reset.");
        }
    }

    // IMPLEMENTASI INTERFACE 2: ItemListener (Menangani perubahan centang)
    @Override
    public void itemStateChanged(ItemEvent e) {
        JCheckBox source = (JCheckBox) e.getItemSelectable();
        int index = listCheckBox.indexOf(source);

        if (index != -1) {
            boolean dicentang = (e.getStateChange() == ItemEvent.SELECTED);
            
            // Mengubah status selesai pada objek Habit di dalam Habit_Manager
            habit_Manager.getDaftarHabit().get(index).setSelesai(dicentang);

            // Mengubah warna teks checkbox sebagai umpan balik visual
            if (dicentang) {
                source.setForeground(new Color(34, 139, 34)); // Hijau
            } else {
                source.setForeground(Color.BLACK); // Hitam saat belum selesai
            }

            perbaruiTampilanProgres();
        }
    }
}
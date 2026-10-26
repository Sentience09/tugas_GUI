import java.util.ArrayList;
import java.util.List;

public class Habit_Manager {
    private List<Habit> daftarHabit;

    public Habit_Manager() {
        this.daftarHabit = new ArrayList<>();
    }

    public void tambahHabit(Habit habit) {
        daftarHabit.add(habit);
    }

    public List<Habit> getDaftarHabit() {
        return daftarHabit;
    }

    public void resetSemuaStatus() {
        for (Habit h : daftarHabit) {
            h.setSelesai(false);
        }
    }

    public int getJumlahSelesai() {
        int jumlah = 0;
        for (Habit h : daftarHabit) {
            if (h.isSelesai()) {
                jumlah++;
            }
        }
        return jumlah;
    }

    public int getTotalHabit() {
        return daftarHabit.size();
    }

    public int hitungPersentase() {
        if (daftarHabit.isEmpty()) {
            return 0;
        }
        return (getJumlahSelesai() * 100) / getTotalHabit();
    }
}
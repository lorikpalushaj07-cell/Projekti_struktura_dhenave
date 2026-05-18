import java.util.ArrayList;

public class User {
    int id;
    String emri, email;
    ArrayList<Integer> blerjet = new ArrayList<>();
    ArrayList<Integer> vleresimet = new ArrayList<>();

    public User(int id, String emri, String email) {
        this.id = id; this.emri = emri; this.email = email;
    }

    public void ble(Product p, int v) {
        if (kaBlere(p.id)) return;
        blerjet.add(p.id); vleresimet.add(v);
        p.blerjet++;
        if (v > 0) p.shtoVleresim(v);
    }

    public boolean kaBlere(int id) { return blerjet.contains(id); }
}

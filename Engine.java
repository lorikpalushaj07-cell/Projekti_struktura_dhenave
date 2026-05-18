import java.util.*;

public class Engine {
    Product[] prod;
    User[] users;
    double[] skorreteFundit; // Ruan pikët e llogaritura për t'i shfaqur në grafikë

    public Engine(Product[] prod, User[] users) { this.prod = prod; this.users = users; }

    public Product[] gjejRekomandimet(User u, int strategji) {
        double[] skor = new double[prod.length];
        for (int i = 0; i < prod.length; i++) {
            if (u.kaBlere(prod[i].id)) { skor[i] = -1; continue; }
            switch (strategji) {
                case 0: skor[i] = skorPopullariteti(prod[i]); break;
                case 1: skor[i] = skorKategoria(u, prod[i]); break;
                case 2: skor[i] = skorNgjashmeri(u, prod[i]); break;
                case 3: skor[i] = skorKolaborim(u, i); break;
                default:
                    skor[i] = 0.20 * skorPopullariteti(prod[i]) + 0.30 * skorKategoria(u, prod[i])
                            + 0.25 * skorNgjashmeri(u, prod[i]) + 0.25 * skorKolaborim(u, i);
            }
        }
        this.skorreteFundit = skor;
        return top5(skor);
    }

    private double skorPopullariteti(Product p) { return p.blerjet * 0.6 + p.vleresimi * p.blerjet * 0.4; }

    private double skorKategoria(User u, Product p) {
        int count = 0;
        for (int pid : u.blerjet)
            for (Product pr : prod)
                if (pr.id == pid && pr.kategoria.equals(p.kategoria)) count++;
        return count * p.vleresimi;
    }

    private double skorNgjashmeri(User u, Product p) {
        double s = 0;
        for (int i = 0; i < u.blerjet.size(); i++) {
            if (u.vleresimet.get(i) < 4) continue;
            for (Product pr : prod)
                if (pr.id == u.blerjet.get(i)) { s += p.etiketatEperbashketa(pr) * u.vleresimet.get(i); break; }
        }
        return s;
    }

    private double skorKolaborim(User u, int pi) {
        double s = 0;
        for (User tjeter : users) {
            if (tjeter.id == u.id) continue;
            int ngjashmeria = 0;
            for (int pid : u.blerjet) if (tjeter.kaBlere(pid)) ngjashmeria++;
            if (tjeter.kaBlere(prod[pi].id)) s += ngjashmeria;
        }
        return s;
    }

    private Product[] top5(double[] skor) {
        ArrayList<Product> rezultat = new ArrayList<>();
        boolean[] perdorur = new boolean[prod.length];
        for (int k = 0; k < 5; k++) {
            int bi = -1; double bv = -1;
            for (int i = 0; i < prod.length; i++)
                if (!perdorur[i] && skor[i] > bv) { bv = skor[i]; bi = i; }
            if (bi == -1 || bv <= 0) break;
            rezultat.add(prod[bi]); perdorur[bi] = true;
        }
        return rezultat.toArray(new Product[0]);
    }
}

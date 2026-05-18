public class Product {
    int id, blerjet;
    String emri, kategoria, pershkrim;
    double cmimi, vleresimi;
    String[] etiketat;
    private int nrVleresimeve;

    public Product(int id, String emri, String kategoria, double cmimi, String pershkrim, String[] etiketat) {
        this.id = id; this.emri = emri; this.kategoria = kategoria;
        this.cmimi = cmimi; this.pershkrim = pershkrim; this.etiketat = etiketat;
    }

    public void shtoVleresim(int v) {
        if (v < 1 || v > 5) return;
        vleresimi = (vleresimi * nrVleresimeve + v) / ++nrVleresimeve;
    }

    public int etiketatEperbashketa(Product tjeter) {
        int sasia = 0;
        for (String a : etiketat)
            for (String b : tjeter.etiketat)
                if (a.equalsIgnoreCase(b)) sasia++;
        return sasia;
    }
}

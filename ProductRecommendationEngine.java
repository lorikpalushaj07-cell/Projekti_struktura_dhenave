import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ProductRecommendationEngine extends JFrame {
    Product[] prod; User[] users; Engine eng;
    JPanel zonaEKartave; JComboBox<String> cmbUser, cmbStrateg;
    JLabel statusiPoshte;

    public ProductRecommendationEngine() {
        prod = krijoProdukte(); users = krijoUsers(); inicializo(prod, users); eng = new Engine(prod, users);

        setTitle("Sistemi i Rekomandimit te Produkteve");
        setSize(950, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panelKryesor = new JPanel(new BorderLayout(15, 15));
        panelKryesor.setBackground(new Color(240, 244, 248)); 
        panelKryesor.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel panelKontroll = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        panelKontroll.setBackground(Color.WHITE);
        panelKontroll.setBorder(BorderFactory.createLineBorder(new Color(218, 226, 234), 1));

        JLabel lUser = new JLabel("Klienti:");
        lUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cmbUser = new JComboBox<>();
        for (User u : users) cmbUser.addItem(u.emri);

        JLabel lStrat = new JLabel("  Metoda e Filtrimit:");
        lStrat.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cmbStrateg = new JComboBox<>(new String[]{
            "Sipas Popullaritetit", "Sipas Kategorisë", "Sipas Etiketave të Përbashkëta", "Sipas Blerësve të Ngjashëm", "Metoda e Kombinuar"
        });

        JButton btn = new JButton("Gjej Produktet");
        btn.setBackground(new Color(43, 108, 176)); 
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelKontroll.add(lUser); panelKontroll.add(cmbUser);
        panelKontroll.add(lStrat); panelKontroll.add(cmbStrateg);
        panelKontroll.add(btn);

        zonaEKartave = new JPanel();
        zonaEKartave.setLayout(new BoxLayout(zonaEKartave, BoxLayout.Y_AXIS));
        zonaEKartave.setBackground(new Color(240, 244, 248));
        
        JScrollPane scroll = new JScrollPane(zonaEKartave);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        statusiPoshte = new JLabel(" Gati.");
        statusiPoshte.setOpaque(true);
        statusiPoshte.setBackground(new Color(45, 55, 72));
        statusiPoshte.setForeground(Color.WHITE);
        statusiPoshte.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusiPoshte.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        btn.addActionListener(e -> rifreskoKartatGrafike());

        rifreskoKartatGrafike();

        panelKryesor.add(panelKontroll, BorderLayout.NORTH);
        panelKryesor.add(scroll, BorderLayout.CENTER);
        panelKryesor.add(statusiPoshte, BorderLayout.SOUTH);
        add(panelKryesor);
    }

    private void rifreskoKartatGrafike() {
        zonaEKartave.removeAll();
        User u = users[cmbUser.getSelectedIndex()];
        Product[] listaRekomanduar = eng.gjejRekomandimet(u, cmbStrateg.getSelectedIndex());

        for (int i = 0; i < listaRekomanduar.length; i++) {
            Product p = listaRekomanduar[i];
            double piket = eng.skorreteFundit[p.id - 1]; 

            JPanel karta = new JPanel(new BorderLayout(15, 5));
            karta.setBackground(Color.WHITE);
            karta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(221, 228, 237), 1),
                    BorderFactory.createEmptyBorder(12, 15, 12, 15)
            ));
            karta.setMaximumSize(new Dimension(Short.MAX_VALUE, 90));

            JPanel panelMajtas = new JPanel(new GridLayout(2, 1));
            panelMajtas.setOpaque(false);
            
            JLabel lblEmri = new JLabel((i + 1) + ". " + p.emri + " (" + p.kategoria + ")");
            lblEmri.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblEmri.setForeground(new Color(29, 33, 41));
            
            JLabel lblPershkrim = new JLabel(p.pershkrim);
            lblPershkrim.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblPershkrim.setForeground(new Color(113, 128, 150));
            
            panelMajtas.add(lblEmri);
            panelMajtas.add(lblPershkrim);

            JPanel panelDjathtas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 8));
            panelDjathtas.setOpaque(false);

            JLabel lblPiket = new JLabel(String.format("Pikët e Përshtatjes: %.1f", piket));
            lblPiket.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lblPiket.setForeground(new Color(43, 108, 176));
            lblPiket.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(43, 108, 176), 1),
                    BorderFactory.createEmptyBorder(2, 6, 2, 6)
            ));

            JLabel lblYjet = new JLabel(String.format("⭐ %.1f (%d blerje)", p.vleresimi, p.blerjet));
            lblYjet.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblYjet.setForeground(new Color(217, 119, 6));

            JLabel lblCmimi = new JLabel(String.format("%.2f €", p.cmimi));
            lblCmimi.setFont(new Font("Segoe UI", Font.BOLD, 15));
            lblCmimi.setForeground(new Color(39, 121, 105));

            panelDjathtas.add(lblPiket);
            panelDjathtas.add(lblYjet);
            panelDjathtas.add(lblCmimi);

            karta.add(panelMajtas, BorderLayout.WEST);
            karta.add(panelDjathtas, BorderLayout.EAST);

            zonaEKartave.add(karta);
            zonaEKartave.add(Box.createRigidArea(new Dimension(0, 10))); 
        }

        statusiPoshte.setText(" U gjeneruan " + listaRekomanduar.length + " produkte të sugjeruara për klientin " + u.emri);
        zonaEKartave.revalidate();
        zonaEKartave.repaint();
    }

    Product[] krijoProdukte() {
        return new Product[]{
            new Product(1,  "Xiaomi Redmi Note 13",    "Elektronike", 219.99, "Ekran AMOLED 6.67 inc",  new String[]{"celular","ekran"}),
            new Product(2,  "Kufje JBL Tune 510BT",    "Elektronike",  49.99, "Bluetooth 5.3, 40 ore",  new String[]{"kufje","muzike"}),
            new Product(3,  "Karikues Baseus 65W",      "Elektronike",  22.99, "Mbush 0-100 ne 35min",   new String[]{"karikues","usb"}),
            new Product(4,  "Atleta Nike Revolution 7", "Veshje",       74.99, "Te lehta per sport",     new String[]{"atleta","sport"}),
            new Product(5,  "Cante JanSport 25L",       "Veshje",       52.99, "Xhep laptopi 15 inc",    new String[]{"cante","shkolla"}),
            new Product(6,  "Nespresso Essenza Mini",   "Kuzhine",      89.99, "Ben kafe ne 25 sekonda", new String[]{"kafe","kuzhine"}),
            new Product(7,  "Kusi Uji Tefal 1.7L",      "Kuzhine",      29.99, "Zien nje litër ne 3min", new String[]{"kusi","uje"}),
            new Product(8,  "Top Futbolli Adidas",       "Sport",        22.99, "Madhesi 5, qendrueshëm", new String[]{"futboll","sport"}),
            new Product(9,  "Jastëk Yoga Decathlon",    "Sport",        19.99, "10mm i trashe",          new String[]{"sport","yoga"}),
            new Product(10, "Gramatike e Shqipes",       "Libra",        16.99, "Rregullat e gjuhes",     new String[]{"libër","shqip"}),
        };
    }

    User[] krijoUsers() {
        return new User[]{ new User(1, "Agron", "agron@email.com"), new User(2, "Ela", "ela@email.com") };
    }

    void inicializo(Product[] k, User[] u) {
        u[0].ble(k[0], 5); u[0].ble(k[1], 5); u[0].ble(k[2], 4); u[0].ble(k[3], 4); u[0].ble(k[7], 3);
        u[1].ble(k[3], 5); u[1].ble(k[4], 5); u[1].ble(k[9], 4); u[1].ble(k[1], 3); u[1].ble(k[6], 4);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductRecommendationEngine().setVisible(true));
    }
}

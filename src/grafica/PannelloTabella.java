package grafica;
import bilancio.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * gestione della tabella che mostra il bilancio e dei filtri applicabili su di essa
 */
public class PannelloTabella extends JPanel implements ActionListener, ListSelectionListener {
    /**
     * modelli dei data picker
     */
    private final UtilDateModel modelInizio,modelFine;
    /**
     * booleana per sapere se sulla tabella è applicato un filtro
     */
    private boolean filtraggio;
    /**
     * radio button per filtrare rispettivamente per giorno, settimana, mese e anno
     * settimana: indicare il primo giorno della settimana (esempio input 18: 18-24)
     */
    private final JRadioButton gg,set,mese,anno;
    /**
     * button group che racchiude i radio button
     */
    private final ButtonGroup filtri;
    /**
     * campo di testo nel quale inserire il numero
     */
    private final JTextField numero;
    /**
     * tabella voci
     */
    private final JTable t;
    /**
     * riferimento al modello della tabella
     * @see grafica.MyTableModel
     */
    private final MyTableModel dataModel;
    /**
     * riferimento al pannello delle voci
     * @see grafica.PannelloVoci
     */
    private PannelloVoci pv;
    /**
     * vettore prima del filtraggio e vettore durante il filtraggio
     */
    private Vector<Voce> vettoreOriginale,vFiltro;
    /**
     * bilancio
     * @see bilancio.Bilancio
     */
    private final Bilancio b;

    /**
     * costruttore della classe
     * inserimento degli elementi nel pannello e aggiunta degli ascoltatori
     * settaggio della tabella con il DefaultRenderer per dare il colore alle celle dell'ammontare in base al loro valore
     * @param b bilancio
     */
    public PannelloTabella(Bilancio b) {
        JLabel etichettaInizio = new JLabel("Data inizio");
        etichettaInizio.setPreferredSize(new Dimension(50,10));
        JLabel etichettaFine = new JLabel("Data fine");
        etichettaFine.setPreferredSize(new Dimension(50,10));

        Properties p = new Properties();
        p.put("text.today", "oggi");
        p.put("text.month", "mese");
        p.put("text.year", "anno");
        modelInizio = new UtilDateModel();
        JDatePanelImpl datePanelInizio = new JDatePanelImpl(modelInizio, p);
        JDatePickerImpl dataInizio = new JDatePickerImpl(datePanelInizio, new DateLabelFormatter());
        modelInizio.setValue(new Date());
        modelInizio.setSelected(true);
        modelFine = new UtilDateModel();
        JDatePanelImpl datePanelFine = new JDatePanelImpl(modelFine, p);
        JDatePickerImpl dataFine = new JDatePickerImpl(datePanelFine, new DateLabelFormatter());
        modelFine.setValue(new Date());
        modelFine.setSelected(true);

        JPanel filtroData = new JPanel();
        filtroData.add(etichettaInizio);
        filtroData.add(dataInizio);
        filtroData.add(etichettaFine);
        filtroData.add(dataFine);


        filtraggio=false;
        gg=new JRadioButton("Giorno");
        set=new JRadioButton("Primo giorno settimana");
        mese=new JRadioButton("Mese");
        anno=new JRadioButton("Anno");
        numero=new JTextField(4);
        JButton filtra = new JButton("Filtra");
        JButton reset = new JButton("Rimuovi filtro");
        filtra.setFocusable(false);
        reset.setFocusable(false);
        JPanel filtro = new JPanel();
        this.b=b;
        b.add(new Voce(LocalDate.now(),"Saldo",b.calcoloSaldo()));
        dataModel=new MyTableModel(b.getVoci());
        t=new JTable(dataModel);
        t.setFocusable(false);
        t.setDefaultRenderer(Double.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
                c.setForeground(((Double) value)>0 ? Color.BLUE : Color.RED);
                return c;
            }
        });
        t.getSelectionModel().addListSelectionListener(this);
        t.setSelectionBackground(new Color(184,207,229));
        t.setSelectionForeground(Color.BLACK);
        JScrollPane sp=new JScrollPane(t);
        sp.setPreferredSize(new Dimension(600,350));
        setBorder(BorderFactory.createTitledBorder("Tabella voci"));

        filtri=new ButtonGroup();
        filtri.add(gg);
        filtri.add(set);
        filtri.add(mese);
        filtri.add(anno);
        filtro.add(gg);
        filtro.add(set);
        filtro.add(mese);
        filtro.add(anno);
        filtro.add(numero);
        filtro.add(filtra);
        filtro.add(reset);
        filtra.addActionListener(this);
        reset.addActionListener(this);
        setLayout(new BorderLayout(10,10));
        add(filtro,BorderLayout.NORTH);
        add(filtroData,BorderLayout.CENTER);
        add(sp,BorderLayout.SOUTH);
    }

    /**
     * settaggio riferimento pannello voci
     * @param pv pannello voci
     */
    public void setPannelloVoci(PannelloVoci pv){this.pv=pv;}

    /**
     * restituisce il modello della tabella
     * @return modello tabella
     */
    public MyTableModel getDataModel(){return dataModel;}

    /**
     * effettua l'aggiornamento della tabella
     */
    public void aggiornaTabella(){dataModel.fireTableDataChanged();}

    /**
     * seleziona una riga della tabella
     * utile per la ricerca
     * @param row riga
     */
    public void selezionaRiga(int row){
        t.setRowSelectionInterval(row,row);
    }

    /**
     * deseleziona ogni riga della tabella
     */
    public void eliminaSelezionati(){
        t.clearSelection();
    }

    /**
     * restituisce la tabella
     * @return tabella
     */
    public JTable getTable(){return t;}

    /**
     * calcola il saldo delle voci della tabella filtrata
     * @param vFiltro vettore delle voci filtrato
     * @return saldo
     */
    public double calcoloSaldo(Vector<Voce> vFiltro){
        double saldo=0;
        for(Voce v:vFiltro)
            saldo+=v.getAmmontare();
        saldo=Math.round(saldo*100.0)/100.0;
        return saldo;
    }

    /**
     * indica se è applicato un filtro sulla tabella
     * @return valore booleano
     */
    public boolean isFiltraggio(){return filtraggio;}

    /**
     * settaggio del vettore prima del filtraggio
     * @param v vettore d'input
     */
    public void setVettoreOriginale(Vector<Voce> v){vettoreOriginale=v;}

    /**
     * restituisce il vettore prima del filtraggio
     * @return vettore originale
     */
    public Vector<Voce> getVettoreOriginale(){return vettoreOriginale;}

    /**
     * filtra la tabella in base al tipo di filtro e all'input immesso dall'utente
     * le voci che rispecchiano il filtro vengono aggiunte in un vettore che sostituirà quello originale
     * infine viene aggiornata la tabella e viene memorizzato il vettore originale
     * @param num numero immesso
     * @param filtro tipo di filtro
     */
    public void filtraTabella(int num,int filtro){
        filtraggio=true;
        vettoreOriginale=b.getVoci();
        vFiltro= new Vector<>();
        switch (filtro){
            case 0: //giorno
                for(Voce v:vettoreOriginale){
                    if(!(v.getDescrizione().equals("Saldo")) && (v.getGiorno()==num)){
                        vFiltro.add(v);
                    }
                }
                break;
            case 1: //settimana
                for(Voce v:vettoreOriginale){
                    if(!(v.getDescrizione().equals("Saldo")) && (v.getGiorno()>=num && v.getGiorno()<=num+6)){
                        vFiltro.add(v);
                    }
                }
                break;
            case 2: //mese
                for(Voce v:vettoreOriginale){
                    if(!(v.getDescrizione().equals("Saldo")) && (v.getMese()==num)){
                        vFiltro.add(v);
                    }
                }
                break;
            case 3: //anno
                for(Voce v:vettoreOriginale){
                    if(!(v.getDescrizione().equals("Saldo")) && (v.getAnno()==num)){
                        vFiltro.add(v);
                    }
                }
                break;
            case 4: //date
                try{
                    LocalDate startDate=modelInizio.getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate endDate=modelFine.getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    for(Voce v:vettoreOriginale){
                        if(!(v.getDescrizione().equals("Saldo"))){
                            if((v.getDataFormato().compareTo(startDate)>=0)&&(v.getDataFormato().compareTo(endDate)<=0)){
                                vFiltro.add(v);
                            }
                        }
                    }
                }catch (Exception err){
                    err.printStackTrace();
                }
                break;
        }
        vFiltro.add(new Voce(LocalDate.now(),"Saldo",calcoloSaldo(vFiltro)));
        b.setVoci(vFiltro);
        dataModel.setTableModel(vFiltro);
        dataModel.fireTableDataChanged();
        pv.setDate(new Date());
        b.setVoci(vettoreOriginale);
    }

    /**
     * gestisce la tabella controllando le righe selezionate
     * la riga selezionata viene mostrata nel pannello voci per poterla modificare in modo più agevole
     * la riga di saldo non può essere selezionata
     * viene usato il getValueIsAdjusting per catturare un solo evento tra mouseUp e mouseDown quando si clicca sulla riga
     * getSelectedRow, siccome ritorna due valori, deve essere diverso da -1 (de selezionamento della riga)
     * @param e l'evento da elaborare
     */
    public void valueChanged(ListSelectionEvent e) {
        if(!e.getValueIsAdjusting() && t.getSelectedRow()!=-1){
            int row = t.getSelectedRow();
            if (t.getValueAt(row, 1).equals("Saldo")) {
                t.clearSelection();
                pv.setDate(new Date());
                pv.setDescrizione("");
                pv.setAmmontare(0);
                pv.disabilitaBottoneModifica();
                pv.disabilitaBottoneRimuovi();
            } else {
                try {
                    pv.setDate(new SimpleDateFormat("dd/MM/yyyy").parse((String) t.getValueAt(row, 0)));
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                pv.setDescrizione((String) t.getValueAt(row, 1));
                pv.setAmmontare((Double) t.getValueAt(row, 2));
                if(filtraggio){
                    pv.setVoceOriginale(vFiltro.get(row));
                    b.setVoci(vettoreOriginale);
                    pv.setRow(b.search(pv.getVoceOriginale()));
                    b.setVoci(vFiltro);
                }
                else
                    pv.setRow(row);
                pv.abilitaBottoneModifica();
                pv.abilitaBottoneRimuovi();
            }
        }
    }

    /**
     * controllo sulla correttezza dei dati immessi per filtrare
     * se una voce per il filtro è selezionata e il campo di teso è riempito
     * il filtraggio avverrà in base a questi ultimi dati
     * altrimenti si prende in considerazione l'intervallo di date
     * in caso di problemi con l'intervallo di date il filtro è applicato sulla data odierna
     * con la rimozione del filtro si resettano tutti i filtri e si ritorna alla tabella originale
     * @param e l'evento da elaborare
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Filtra")) {
            if ((!numero.getText().isBlank())) {
                try{
                    int num = Integer.parseInt(numero.getText().trim());
                    if (gg.isSelected() || set.isSelected()) {
                        if (num > 0 && num < 32) {
                            if(gg.isSelected())
                                filtraTabella(num,0);
                            else
                                filtraTabella(num,1);
                        } else {
                            JOptionPane.showMessageDialog(this, "Giorno non valido!", "Errore", JOptionPane.ERROR_MESSAGE);
                            numero.setText("");
                            filtri.clearSelection();
                        }
                    }
                    if (mese.isSelected()) {
                        if (num > 0 && num < 13) {
                            filtraTabella(num,2);
                        } else {
                            JOptionPane.showMessageDialog(this, "Mese non valido!", "Errore", JOptionPane.ERROR_MESSAGE);
                            numero.setText("");
                            filtri.clearSelection();
                        }
                    }
                    if (anno.isSelected()) {
                        if (num >= (LocalDate.now().getYear()-100) && num <= LocalDate.now().getYear()) {
                            filtraTabella(num,3);
                        } else {
                            JOptionPane.showMessageDialog(this, "Anno non valido!", "Errore", JOptionPane.ERROR_MESSAGE);
                            numero.setText("");
                            filtri.clearSelection();
                        }
                    }
                }
                catch (java.lang.NumberFormatException err){
                    JOptionPane.showMessageDialog(this, "Inserire un numero!", "Errore", JOptionPane.ERROR_MESSAGE);
                    numero.setText("");
                    filtri.clearSelection();
                }
                modelInizio.setValue(new Date());
                modelFine.setValue(new Date());
            }
            else{
                filtraTabella(0,4);
            }
        }
        if (e.getActionCommand().equals("Rimuovi filtro")) {
            if(filtraggio){
                filtraggio=false;
                numero.setText("");
                gg.setSelected(false);
                set.setSelected(false);
                mese.setSelected(false);
                anno.setSelected(false);
                pv.setDate(new Date());
                filtri.clearSelection();
                b.setVoci(vettoreOriginale);
                dataModel.setTableModel(vettoreOriginale);
                dataModel.fireTableDataChanged();
                modelInizio.setValue(new Date());
                modelFine.setValue(new Date());
            }
        }
    }
}

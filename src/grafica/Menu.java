package grafica;
import bilancio.*;
import file.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.*;
import java.time.LocalDate;

/**
 * menù dal quale selezionare una delle seguenti funzionalità
 * @see file.FileSerializzazione salvataggio e caricamento file
 * @see file.FileCsv esportazione su file csv
 * @see file.FileTesto esportazione su file di testo e su file Excel
 * svuotamento della tabella
 * stampa della tabella
 */
public class Menu extends JPanel implements ActionListener {
    /**
     * panello contenente la tabella del bilancio
     * @see grafica.PannelloTabella
     */
    private PannelloTabella pt;
    /**
     * bilancio
     * @see bilancio.Bilancio
     */
    private final Bilancio b;

    /**
     * costruttore della classe
     * creazione delle varie voci del menù e assegnamento dell'ascoltatore
     * @param f finestra in cui appare il menù
     * @param b bilancio
     */
    public Menu(JFrame f,Bilancio b) {
        JMenuItem[] mi = new JMenuItem[7];
        mi[0] = new JMenuItem("Salva");
        mi[1] = new JMenuItem("Carica");
        mi[2] = new JMenuItem("Svuota tabella");
        mi[3] = new JMenuItem("Stampa");

        JMenu esporta=new JMenu("Esporta");
        mi[4] = new JMenuItem("File CSV");
        mi[5] = new JMenuItem("File di testo");
        mi[6] = new JMenuItem("File Excel");
        esporta.add(mi[4]);
        esporta.addSeparator();
        esporta.add(mi[5]);
        esporta.addSeparator();
        esporta.add(mi[6]);

        JMenuBar mb = new JMenuBar();
        mb.add(esporta); mb.add(mi[0]); mb.add(mi[1]); mb.add(mi[2]); mb.add(mi[3]);
        for (JMenuItem m:mi) {
            m.addActionListener(this);
        }
        f.setJMenuBar(mb);
        this.b=b;
    }

    /**
     * imposta la tabella di riferimento
     * @param pt pannello
     */
    public void setPannelloTabella(PannelloTabella pt){this.pt=pt;}

    /**
     * elabora il percorso file selezionato dall'utente ed effettua una delle
     * funzionalità sopra citate in base alla voce del menù selezionata dall'utente
     * è presente il controllo sulla sovrascrittura di un file con la scelta finale che spetta all'utente
     * @param e l'evento da elaborare
     */
    public void actionPerformed(ActionEvent e) {
        FileTesto esportazione;
        FileSerializzazione f;
        File fileToSave;
        FileNameExtensionFilter filter;
        int userSelection;
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        if(e.getActionCommand().equals("Salva")){
            fileChooser.setDialogTitle("Salvataggio file");
            filter = new FileNameExtensionFilter("DAT FILES", "dat", "dat");
            fileChooser.setFileFilter(filter);
            userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                fileToSave = fileChooser.getSelectedFile();
                if(!(fileToSave.getName().endsWith(".dat"))) {
                    fileToSave= new File(fileToSave + ".dat");
                }
                if(fileToSave.exists()){
                    int result = JOptionPane.showConfirmDialog(this,"File già esistente. Sovrascrivere?", "File esistente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(result == JOptionPane.YES_OPTION){
                        f = new FileSerializzazione(fileToSave.getAbsolutePath(), this, b, pt);
                        f.salva(false);
                    }
                } else {
                    f = new FileSerializzazione(fileToSave.getAbsolutePath(), this, b, pt);
                    f.salva(false);
                }
            }
        }
        if(e.getActionCommand().equals("Carica")){
            fileChooser.setDialogTitle("Caricamento file");
            filter = new FileNameExtensionFilter("DAT FILES", "dat", "dat");
            fileChooser.setFileFilter(filter);
            userSelection = fileChooser.showOpenDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                fileToSave = fileChooser.getSelectedFile();
                pt.actionPerformed(new ActionEvent(pt,ActionEvent.ACTION_PERFORMED,"Rimuovi filtro"));
                f = new FileSerializzazione(fileToSave.getAbsolutePath(), this, b, pt);
                f.carica();
            }
        }
        if(e.getActionCommand().equals("File CSV")){
            fileChooser.setDialogTitle("Esportazione in file CSV");
            filter = new FileNameExtensionFilter("CSV FILES", "csv", "csv");
            fileChooser.setFileFilter(filter);
            userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                fileToSave = fileChooser.getSelectedFile();
                if(!(fileToSave.getName().endsWith(".csv"))) {
                    fileToSave= new File(fileToSave + ".csv");
                }
                if(fileToSave.exists()){
                    int result = JOptionPane.showConfirmDialog(this,"File già esistente. Sovrascrivere?", "File esistente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(result == JOptionPane.YES_OPTION){
                        esportazione=new FileCsv(fileToSave.getAbsolutePath(),this,pt.getDataModel());
                        esportazione.esporta();
                    }
                }
                else{
                    esportazione=new FileCsv(fileToSave.getAbsolutePath(),this,pt.getDataModel());
                    esportazione.esporta();
                }
            }
        }
        if(e.getActionCommand().equals("File di testo")){
            fileChooser.setDialogTitle("Esportazione in file di testo");
            filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
            fileChooser.setFileFilter(filter);
            userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                fileToSave = fileChooser.getSelectedFile();
                if(!(fileToSave.getName().endsWith(".txt"))) {
                    fileToSave= new File(fileToSave + ".txt");
                }
                if(fileToSave.exists()){
                    int result = JOptionPane.showConfirmDialog(this,"File già esistente. Sovrascrivere?", "File esistente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(result == JOptionPane.YES_OPTION){
                        esportazione=new FileTesto(fileToSave.getAbsolutePath(),this,pt.getDataModel());
                        esportazione.esporta();
                    }
                }
                else{
                    esportazione=new FileTesto(fileToSave.getAbsolutePath(),this,pt.getDataModel());
                    esportazione.esporta();
                }
            }
        }
        if(e.getActionCommand().equals("File Excel")){
            fileChooser.setDialogTitle("Esportazione in file Excel");
            filter = new FileNameExtensionFilter("EXCEL FILES", "xls", "xls");
            fileChooser.setFileFilter(filter);
            userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                fileToSave = fileChooser.getSelectedFile();
                if(!(fileToSave.getName().endsWith(".xls"))) {
                    fileToSave= new File(fileToSave + ".xls");
                }
                if(fileToSave.exists()){
                    int result = JOptionPane.showConfirmDialog(this,"File già esistente. Sovrascrivere?", "File esistente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(result == JOptionPane.YES_OPTION){
                        esportazione=new FileTesto(fileToSave.getAbsolutePath(),this,pt.getDataModel());
                        esportazione.esporta();
                    }
                }
                else{
                    esportazione=new FileTesto(fileToSave.getAbsolutePath(),this,pt.getDataModel());
                    esportazione.esporta();
                }
            }
        }
        if(e.getActionCommand().equals("Svuota tabella")){
            b.clear();
            pt.aggiornaTabella();
            b.add(new Voce(LocalDate.now(), "Saldo", b.calcoloSaldo()));
        }
        if(e.getActionCommand().equals("Stampa")) {
            try {
                pt.getTable().print(JTable.PrintMode.FIT_WIDTH);
            } catch (PrinterException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}

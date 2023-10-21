package file;
import grafica.*;
import javax.swing.*;
import java.io.*;
/**
 * esportazione della tabella su un file di testo o Excel
 */
public class FileTesto{
    /**
     * percorso file
     */
    private final String path;
    /**
     * modello della tabella da cui prendere le voci
     * @see grafica.MyTableModel
     */
    private final MyTableModel tm;
    /**
     * menù dal quale viene selezionata questa funzionalità
     * @see grafica.Menu
     */
    private final Menu m;

    /**
     * costruttore della classe
     * @param path percorso file
     * @param m menù
     * @param tm modello della tabella
     */
    public FileTesto(String path,Menu m,MyTableModel tm){
        this.path=path;
        this.m=m;
        this.tm=tm;
    }

    /**
     * ritorna il menù
     * @return menù
     */
    public Menu getMenu(){return m;}

    /**
     * ritorna il modello della tabella delle voci
     * @return modello tabella
     */
    public MyTableModel getModello(){return tm;}

    /**
     * ritorna il percorso del file
     * @return percorso file
     */
    public String getPath(){return path;}

    /**
     * carica i dati della tabella in un file di testo o Excel
     */
    public void esporta() {
        try {
            FileWriter fout = new FileWriter(path);
            for(int i=0; i<tm.getColumnCount(); i++)
                fout.write(tm.getColumnName(i)+"\t");
            fout.write("\n");
            for(int i=0; i<tm.getRowCount(); i++) {
                for (int j = 0; j < tm.getColumnCount(); j++){
                    fout.write(tm.getValueAt(i,j).toString()+"\t");
                }
                fout.write("\n");
            }
            fout.close();
            JOptionPane.showMessageDialog(m,"Dati esportati correttamente su file!");
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(m,"Errore durante l'esportazione!","Errore",JOptionPane.ERROR_MESSAGE);
        }
    }
}

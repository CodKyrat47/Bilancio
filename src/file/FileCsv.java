package file;
import grafica.*;
import java.io.*;
import java.util.*;
import com.opencsv.CSVWriter;
import javax.swing.*;

/**
 * esportazione della tabella su un file csv ereditando dalla classe FileTesto
 * @see file.FileTesto
 */
public class FileCsv extends FileTesto{
    /**
     * costruttore della classe richiamando il super costruttore
     * @param path percorso file
     * @param m menù
     * @param tm modello tabella
     */
    public FileCsv(String path,Menu m,MyTableModel tm){
        super(path,m,tm);
    }
    /**
     * Override del metodo esporta per caricare i dati della tabella in un file csv
     */
    @Override

    public void esporta() {
        File file = new File(super.getPath());
        try {
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            List<String[]> dati= new ArrayList<>();
            dati.add(super.getModello().getCampi());
            for(int i=0; i<super.getModello().getRowCount(); i++) {
                String[] riga=new String[super.getModello().getColumnCount()];
                for (int j = 0; j < super.getModello().getColumnCount(); j++){
                    riga[j]=super.getModello().getValueAt(i,j).toString();
                }
                dati.add(riga);
            }
            writer.writeAll(dati);
            writer.close();
            JOptionPane.showMessageDialog(super.getMenu(),"Dati esportati correttamente su file!");
        }catch (IOException e) {
            JOptionPane.showMessageDialog(super.getMenu(),"Errore durante l'esportazione!","Errore",JOptionPane.ERROR_MESSAGE);
        }
    }
}

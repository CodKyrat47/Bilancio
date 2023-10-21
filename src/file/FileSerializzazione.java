package file;
import java.io.*;
import java.time.*;
import java.util.*;
import bilancio.*;
import grafica.*;
import javax.swing.*;

/**
 * salvataggio e caricamento del bilancio su file
 */
public class FileSerializzazione {
    /**
     * percorso file
     */
    private final String path;
    /**
     * menù dal quale viene selezionata questa funzionalità
     * @see grafica.Menu
     */
    private final Menu menu;
    /**
     * bilancio
     * @see bilancio.Bilancio
     */
    private final Bilancio b;
    /**
     * pannello contenente la tabella di bilancio
     * @see grafica.PannelloTabella
     */
    private final PannelloTabella pt;

    /**
     * costruttore della classe
     * @param path percorso file
     * @param m menù
     * @param b bilancio
     * @param pt pannello tabella
     */
    public FileSerializzazione(String path, Menu m,Bilancio b,PannelloTabella pt){
        this.path=path; menu=m; this.b=b; this.pt=pt;
    }

    /**
     * salvataggio del bilancio su file
     * se si tratta di file temporanei, vengono salvati su una cartella predefinita al momento dell'avvio del programma
     * è prevista l'eliminazione dei file temporanei alla chiusura del programma
     * @param temp booleana per sapere se si sta salvando in un file temporaneo
     */
    public void salva(boolean temp){
        try {
            File file;
            if(!temp)
                file=new File(path);
            else{
                File f=new File(path+File.separator+"Temp");
                if(!f.exists() || !f.isDirectory())
                    f.mkdir();
                file=File.createTempFile("Bilancio-",".tmp",f);
            }
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(f);
            b.removeLast();
            Vector <Voce> v=b.getVoci();
            for(Voce x:v)
                os.writeObject(x);
            os.flush();
            os.close();
            b.add(new Voce(LocalDate.now(), "Saldo", b.calcoloSaldo()));
            if(!temp)
                JOptionPane.showMessageDialog(menu, "File salvato correttamente!");
            else
                file.deleteOnExit();
        }
        catch (IOException e){
            JOptionPane.showMessageDialog(menu, "Problema salvataggio file!","Errore",JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * caricamento di un file sulla tabella
     */
    public void carica(){
        boolean caricamento=false;
        FileInputStream fin;
        ObjectInputStream is;
        Vector<Voce> v = new Vector<>(b.getVoci());
        try {
            fin = new FileInputStream(path);
            is = new ObjectInputStream(fin);
            b.clear();
            for(;;)
                b.add((Voce) is.readObject());
        } catch (EOFException e){
            JOptionPane.showMessageDialog(menu, "File caricato correttamente!");
            caricamento=true;
        } catch(IOException e){
            JOptionPane.showMessageDialog(menu, "Problema apertura file!","Errore",JOptionPane.ERROR_MESSAGE);
            b.clear();
            for(Voce x:v){
                b.add(x);
            }
            pt.aggiornaTabella();
        }
        catch (ClassNotFoundException e){
            JOptionPane.showMessageDialog(menu, "Errore","Errore lettura da file!",JOptionPane.ERROR_MESSAGE);
        }
        if(caricamento){
            b.sort(b.getVoci());
            b.add(new Voce(LocalDate.now(), "Saldo", b.calcoloSaldo()));
            pt.aggiornaTabella();
        }
    }
}

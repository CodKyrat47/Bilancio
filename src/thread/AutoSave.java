package thread;
import bilancio.Bilancio;
import file.*;
import grafica.Menu;
import grafica.*;
import javax.swing.*;
/**
 * Gestione del thread per l'auto salvataggio del bilancio in un file temporaneo
 */
public class AutoSave extends Thread{
    /**
     * intervallo di tempo per ogni salvataggio
     */
    private static final int SECONDI = 60*1000;
    /**
     * percorso file
     */
    private final String path;
    /**
     * menù della finestra principale
     * @see grafica.Menu
     */
    private final Menu menu;
    /**
     * bilancio voci
     * @see bilancio.Bilancio
     */
    private final Bilancio b;
    /**
     * finestra principale
     * @see grafica.Finestra
     */
    private final Finestra f;
    /**
     * pannello contenente la tabella
     * @see grafica.PannelloTabella
     */
    private final PannelloTabella pt;
    /**
     * Costruttore della classe
     * @param path percorso file
     * @param m menù finestra
     * @param b bilancio
     * @param pt pannello con la tabella
     * @param f finestra
     */
    public AutoSave(String path, Menu m,Bilancio b,PannelloTabella pt,Finestra f){
        this.path=path; menu=m; this.b=b; this.pt=pt; this.f=f;
    }
    /**
     * metodo per far eseguire il thread
     * prende il bilancio nella tabella e lo carica su un file temporaneo
     * ogni {@link thread.AutoSave#SECONDI} secondi
     * @see file.FileSerializzazione
     */
    public void run() {
        while (true) {
            try {
                f.getBilancio();
                FileSerializzazione fs=new FileSerializzazione(path,menu,b,pt);
                fs.salva(true);
                sleep(SECONDI);
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(f,"Problema salvataggio con thread!","Errore",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

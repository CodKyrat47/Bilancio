package grafica;
import bilancio.*;
import thread.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * classe per l'unica finestra nel programma
 * da qui avviene l'inserimento dei vari pannelli
 * c'è l'ascoltatore per interrompere il thread nel momento in cui la finestra viene chiusa
 */
public class Finestra extends JFrame implements WindowListener {
    /**
     * gestione del vettore delle voci
     * @see bilancio.Bilancio
     */
    private final Bilancio b;
    /**
     * gestione dei file temporanei
     * @see thread.AutoSave
     */
    private final AutoSave as;

    /**
     * costruttore della classe
     * inserimento degli elementi nella finestra e avvio del thread
     * @param titolo finestra
     */
    public Finestra(String titolo){
        super(titolo);
        try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
        catch(Exception e){e.printStackTrace();}
        int width=800,height=800;
        setLayout(new FlowLayout());
        b=new Bilancio();
        Menu m = new Menu(this, b);
        PannelloVoci pv = new PannelloVoci(b);
        PannelloRicerca pr = new PannelloRicerca(b);
        PannelloTabella pt = new PannelloTabella(b);
        m.setPannelloTabella(pt);
        pv.setPannelloTabella(pt);
        pr.setPannelloTabella(pt);
        pt.setPannelloVoci(pv);
        add(m);
        add(pv);
        add(pr);
        add(pt);

        String path = System.getProperty("user.dir");
        as=new AutoSave(path, m,b, pt,this);
        as.start();

        setSize(width,height);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * ritorna il bilancio
     * @return bilancio
     */
    public Bilancio getBilancio(){return b;}
    @Override
    public void windowOpened(WindowEvent e) { }

    /**
     * interruzione del thread al momento della chiusura della finestra
     * @param e l'evento da elaborare
     */
    @Override
    public void windowClosing(WindowEvent e) {
        as.interrupt();
    }
    @Override
    public void windowClosed(WindowEvent e) { }

    @Override
    public void windowIconified(WindowEvent e) { }

    @Override
    public void windowDeiconified(WindowEvent e) { }

    @Override
    public void windowActivated(WindowEvent e) { }

    @Override
    public void windowDeactivated(WindowEvent e) { }
}

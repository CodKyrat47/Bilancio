package grafica;
import bilancio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * pannello per effettuare la ricerca nella tabella
 */
public class PannelloRicerca extends JPanel implements ActionListener {
    /**
     * testo per scrivere cosa cercare
     */
    private final JTextField t_ricerca;
    /**
     * bottone per passare all'eventuale corrispondenza successiva
     */
    private final JButton successivo;
    /**
     * vettore che memorizza le righe della tabella in cui c'è stata la corrispondenza
     */
    private final Vector <Integer> indici;
    /**
     * riferimento alla tabella con il bilancio
     * @see grafica.PannelloTabella
     */
    private PannelloTabella pt;
    /**
     * bilancio
     * @see bilancio.Bilancio
     */
    private final Bilancio b;

    /**
     * costruttore della classe tramite il quale vengono inseriti gli elementi nel pannello
     * @param b bilancio
     */
    public PannelloRicerca(Bilancio b){
        indici= new Vector<>();
        JLabel l_ricerca = new JLabel("Inserisci informazioni voce");
        t_ricerca=new JTextField(25);
        JButton cerca = new JButton("Cerca");
        successivo=new JButton("Successivo");
        cerca.setFocusable(false);
        successivo.setFocusable(false);
        setBorder(BorderFactory.createTitledBorder("Ricerca voci"));

        setLayout(new GridLayout(2,2,10,10));
        add(l_ricerca); add(t_ricerca);
        add(cerca); add(successivo);
        successivo.setEnabled(false);
        cerca.addActionListener(this);
        successivo.addActionListener(this);

        this.b=b;
    }

    /**
     * prende il riferimento del pannello contenente la tabella
     * @param pt pannello tabella
     */
    public void setPannelloTabella(PannelloTabella pt){this.pt=pt;}

    /**
     * gestione della ricerca con l'ausilio del vettore  {@link grafica.PannelloRicerca#indici}
     * viene selezionata la riga della tabella memorizzata alla prima posizione del vettore
     * dopodiché il primo elemento del vettore viene rimosso
     * la ricerca continua fino a quando il vettore non contiene più elementi
     * @param e l'evento da elaborare
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Cerca")){
            indici.clear();
            String testo=t_ricerca.getText();
            if(!(testo.isBlank())){
                for(int i=0; i<b.size()-1; i++){
                    if((b.getElement(i).getData().contains(testo)) || (b.getElement(i).getDescrizione().contains(testo)) || (Double.toString(b.getElement(i).getAmmontare()).contains(testo))){
                        indici.add(i);
                    }
                }
                if(indici.size()>0) {
                    pt.selezionaRiga(indici.get(0));
                    indici.remove(0);
                    successivo.setEnabled(true);
                }
                else
                {
                    t_ricerca.setText("");
                    JOptionPane.showMessageDialog(this, "Nessun riscontro!");
                }
            }
        }
        if(e.getActionCommand().equals("Successivo")){
            if(indici.size()>0){
                pt.eliminaSelezionati();
                pt.selezionaRiga(indici.get(0));
                indici.remove(0);
            }
            else{
                successivo.setEnabled(false);
                t_ricerca.setText("");
                JOptionPane.showMessageDialog(this, "Fine ricerca!");
            }
        }
    }
}

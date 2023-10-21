package grafica;
import javax.swing.*;
import org.jdatepicker.impl.*;
import bilancio.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.time.*;
import java.util.*;

/**
 * pannello per la gestione delle voci: aggiunta, modifica e rimozione
 */
public class PannelloVoci extends JPanel implements ActionListener {
    /**
     * bottoni per effettuare la modifica e la rimozione della voce
     */
    private final JButton b_mod,b_rem;
    /**
     * campo di testo formattato per l'ammontare
     */
    private final JSpinner t_importo;
    /**
     * campo di testo per la descrizione
     */
    private final JTextField t_desc;
    /**
     * modello per il data picker
     */
    private final UtilDateModel model;
    /**
     * pannello con la tabella
     * @see grafica.PannelloTabella
     */
    private PannelloTabella pt;
    /**
     * bilancio
     * @see bilancio.Bilancio
     */
    private final Bilancio b;
    /**
     * voce prima di essere modificata
     * utile per tenere traccia della voce durante il filtraggio
     * in modo da recuperare la riga nella tabella originale
     */
    private Voce voceOriginale;
    /**
     * riga occupata dalla voce richiesta nella tabella
     */
    private int row;

    /**
     * costruttore della classe
     * inserimento degli elementi nel panello con i dovuti ascoltatori
     * @param b bilancio
     */
    public PannelloVoci(Bilancio b) {
        JLabel l_date = new JLabel("Inserisci data");
        JLabel l_desc = new JLabel("Inserisci descrizione");
        JLabel l_importo = new JLabel("Inserisci importo");

        JButton b_add = new JButton("Aggiungi");
        b_rem=new JButton("Rimuovi");
        b_mod=new JButton("Modifica");
        b_add.setFocusable(false);
        b_mod.setFocusable(false);
        b_rem.setFocusable(false);

        t_importo = new JSpinner(new SpinnerNumberModel(0,-1000000,1000000,0.01));

        t_desc=new JTextField(20);

        model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "oggi");
        p.put("text.month", "mese");
        p.put("text.year", "anno");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        model.setValue(new Date());
        model.setSelected(true);

        b_add.addActionListener(this);
        b_mod.addActionListener(this);
        b_rem.addActionListener(this);
        b_mod.setEnabled(false);
        b_rem.setEnabled(false);

        setBorder(BorderFactory.createTitledBorder("Gestione voci"));
        setLayout(new GridLayout(3,3,10,10));

        add(l_date); add(l_desc); add(l_importo);
        add(datePicker); add(t_desc); add(t_importo);
        add(b_add); add(b_mod); add(b_rem);

        this.b=b;
    }

    /**
     * riferimento al pannello con la tabella
     * @param pt pannello tabella
     */
    public void setPannelloTabella(PannelloTabella pt){this.pt=pt;}

    /**
     * settaggio della data della voce
     * @param d data in input
     */
    public void setDate(Date d){model.setValue(d);}

    /**
     * settaggio della descrizione della voce
     * @param s stringa relativa alla descrizione
     */
    public void setDescrizione(String s){t_desc.setText(s);}

    /**
     * settaggio dell'ammontare della voce
     * @param val valore in input
     */
    public void setAmmontare(double val){t_importo.setValue(val);}

    /**
     * il bottone della modifica viene disattivato
     */
    public void disabilitaBottoneModifica(){b_mod.setEnabled(false);}

    /**
     * il bottone della rimozione viene disattivato
     */
    public void disabilitaBottoneRimuovi(){b_rem.setEnabled(false);}

    /**
     * il bottone della modifica viene attivato
     */
    public void abilitaBottoneModifica(){b_mod.setEnabled(true);}

    /**
     * il bottone della rimozione viene attivato
     */
    public void abilitaBottoneRimuovi(){b_rem.setEnabled(true);}

    /**
     * viene memorizzata la voce per essere ricercata nella tabella originale
     * @param v voce
     */
    public void setVoceOriginale(Voce v){voceOriginale=v;}

    /**
     * viene restituita la voce originale
     * @return voce
     */
    public Voce getVoceOriginale(){return voceOriginale;}

    /**
     * viene settata la riga (della tabella non filtrata) nella quale si trova la voce
     * @param r riga
     */
    public void setRow(int r){row=r;}

    /**
     * aggiunta della voce nel bilancio
     * in caso di aggiunta sulla tabella filtrata si recupera il bilancio prima del filtro
     * @param voce voce da aggiungere
     */
    public void aggiuntaVoce(Voce voce){
        if(pt.isFiltraggio()){
            b.setVoci(pt.getVettoreOriginale());
        }
        if(!(b.aggiungiVoce(voce)))
            JOptionPane.showMessageDialog(this, "Errore: voce già presente!","Errore",JOptionPane.ERROR_MESSAGE);
    }
    /**
     * modifica della voce nel bilancio
     * in caso di modifica sulla tabella filtrata si recupera il bilancio prima del filtro
     * @param voce voce da modificare
     */
    public void modificaVoce(Voce voce){
        if(b_mod.isEnabled())
        {
            if(pt.isFiltraggio()){
                b.setVoci(pt.getVettoreOriginale());
            }
            if (!(b.modificaVoce(voce,row)))
                JOptionPane.showMessageDialog(this, "Errore: voce già presente. Modifica non effettuata!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * rimozione della voce nel bilancio
     * in caso di rimozione sulla tabella filtrata si recupera il bilancio prima del filtro
     */
    public void rimozioneVoce(){
        if(pt.isFiltraggio()){
            b.setVoci(pt.getVettoreOriginale());
        }
        if(b_rem.isEnabled()) {
            b.rimuoviVoce(row);
        }
    }

    /**
     * dopo un controllo sulla data immessa, viene effettuata l'operazione selezionata dall'utente con i bottoni
     * se l'operazione avviene durante un filtraggio si recupera la tabella prima del filtro e poi
     * si effettua l'operazione desiderata
     * infine si resettano i valori d'input
     * @param e l'evento da elaborare
     */
    public void actionPerformed(ActionEvent e) {
        LocalDate data=model.getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if(data.isAfter(LocalDate.now())) {
            String oggi=LocalDate.now().getDayOfMonth()+"-"+LocalDate.now().getMonthValue()+"-"+LocalDate.now().getYear();
            JOptionPane.showMessageDialog(this, "Data non valida! Inserimento automatico della data: "+oggi,"Errore",JOptionPane.ERROR_MESSAGE);
            data = LocalDate.now();
        }

        try {
            t_importo.commitEdit();
        }
        catch (ParseException pe) {
            JComponent editor = t_importo.getEditor();
            if (editor instanceof JSpinner.DefaultEditor) {
                ((JSpinner.DefaultEditor)editor).getTextField().setValue(t_importo.getValue());
            }
        }

        double val = Math.round((((Number) t_importo.getValue()).doubleValue())*100.0)/100.0;
        Voce voce=new Voce(data,t_desc.getText(),val);
        if(e.getActionCommand().equals("Aggiungi")) {
            aggiuntaVoce(voce);
        }
        if(e.getActionCommand().equals("Modifica")) {
            modificaVoce(voce);
        }
        if(e.getActionCommand().equals("Rimuovi")){
            rimozioneVoce();
        }
        if(pt.isFiltraggio()){
            pt.setVettoreOriginale(b.getVoci());
            pt.actionPerformed(new ActionEvent(pt,ActionEvent.ACTION_PERFORMED,"Rimuovi filtro"));
        }
        pt.aggiornaTabella();
        pt.eliminaSelezionati();
        model.setValue(new Date());
        t_desc.setText("");
        t_importo.setValue(0);
        b_mod.setEnabled(false);
        b_rem.setEnabled(false);
    }
}

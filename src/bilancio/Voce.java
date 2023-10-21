package bilancio;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * gestione di una singola voce del bilancio
 */
public class Voce implements Serializable {
    /**
     * data dell'operazione
     */
    private final LocalDate data;
    /**
     * descrizione dell'operazione
     */
    private final String descrizione;
    /**
     * ammontare dell'operazione
     */
    private final double ammontare;

    /**
     * costruttore della classe
     * @param data data dell'operazione
     * @param descrizione descrizione dell'operazione
     * @param ammontare ammontare dell'operazione
     */
    public Voce(LocalDate data, String descrizione, double ammontare) {
        this.data = data;
        this.descrizione = descrizione;
        this.ammontare = ammontare;
    }

    /**
     * restituisce il giorno del mese in cui è avvenuta l'operazione
     * @return numero giorno
     */
    public int getGiorno() {
        return data.getDayOfMonth();
    }
    /**
     * restituisce il mese in cui è avvenuta l'operazione
     * @return numero mese
     */
    public int getMese() {
        return data.getMonthValue();
    }
    /**
     * restituisce l'anno in cui è avvenuta l'operazione
     * @return numero anno
     */
    public int getAnno() {
        return data.getYear();
    }
    /**
     * restituisce la data in cui è avvenuta l'operazione sotto forma di stringa
     * @return data
     */
    public String getData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.ITALY);
        return data.format(formatter);
    }

    /**
     * restituisce la data in formato LocalDate
     * @return data
     */
    public LocalDate getDataFormato(){return data;}
    /**
     * restituisce la descrizione dell'operazione
     * @return descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }
    /**
     * restituisce l'ammontare dell'operazione
     * @return ammontare
     */
    public double getAmmontare() {
        return ammontare;
    }

    /**
     * confronta una voce con un'altra data in input per vedere se sono uguali
     * @param v voce
     * @return esito dell'operazione
     */
    public boolean equals(Voce v) {
        if(!(this.getData().equals(v.getData())))
            return false;
        if(!(this.descrizione.equals(v.getDescrizione())))
            return false;
        return this.ammontare == v.getAmmontare();
    }
}

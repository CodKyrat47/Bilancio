package bilancio;
import java.text.*;
import java.time.*;
import java.util.*;

/**
 * Gestione del bilancio
 */
public class Bilancio {
    /**
     * vettore delle voci
     */
    private Vector<Voce> voci;
    /**
     * costruttore della classe che crea un vettore
     */
    public Bilancio(){
        voci= new Vector<>();
    }
    /**
     * restituisce il riferimento al vettore delle voci
     * @return voci
     */
    public Vector<Voce> getVoci(){return voci;}
    /**
     * aggiunta di una voce al vettore
     * @param v voce
     */
    public void add(Voce v){voci.add(v);}
    /**
     * rimozione dell'ultimo elemento dal vettore
     * che è sempre la voce Saldo
     */
    public void removeLast(){
        voci.remove(voci.size()-1);
    }
    /**
     * rimozione di un elemento dal vettore
     * @param index indice del vettore a cui si trova l'elemento
     */
    public void remove(int index){
        voci.remove(index);
    }
    /**
     * restituisce la lunghezza del vettore
     * e quindi il numero di voci nel bilancio (compreso il Saldo)
     * @return lunghezza
     */
    public int size(){return voci.size();}
    /**
     * elimina ogni voce del bilancio
     */
    public void clear(){voci.clear();}
    /**
     * calcola il saldo delle voci
     * @return saldo
     */
    public double calcoloSaldo(){
        double saldo=0;
        for(Voce v:voci)
            saldo+=v.getAmmontare();
        saldo=Math.round(saldo*100.0)/100.0;
        return saldo;
    }
    /**
     * controlla se la voce è gia presente nel bilancio
     * @param vIn voce in input
     * @return l'esito dell'operazione
     */
    public boolean isPresent(Voce vIn){
        for(Voce v:voci)
            if(v.equals(vIn))
                return true;
        return false;
    }
    /**
     * aggiunge la voce nel bilancio e ordina quest ultimo per data crescente
     * @param voce voce da aggiungere
     * @return l'esito dell'operazione
     */
    public boolean aggiungiVoce(Voce voce){
        if(!(isPresent(voce))){
            removeLast();
            add(voce);
            sort(getVoci());
            add(new Voce(LocalDate.now(), "Saldo",calcoloSaldo()));
            return true;
        }
        return false;
    }
    /**
     * modifica una voce presente nel bilancio e ordina quest ultimo per data crescente
     * @param voce voce da modificare
     * @param row riga nella tabella
     * @return l'esito dell'operazione
     */
    public boolean modificaVoce(Voce voce,int row){
        if (!(isPresent(voce))) {
            remove(row);
            removeLast();
            add(voce);
            sort(getVoci());
            add(new Voce(LocalDate.now(), "Saldo",calcoloSaldo()));
            return true;
        }
        return false;
    }
    /**
     * rimuove una voce dal bilancio
     * @param row indice della voce nel vettore
     */
    public void rimuoviVoce(int row){
        remove(row);
        removeLast();
        add(new Voce(LocalDate.now(), "Saldo",calcoloSaldo()));
    }

    /**
     * assegnamento del vettore delle voci
     * @param v vettore
     */
    public void setVoci(Vector<Voce>v){voci=v;}

    /**
     * ritorna l'elemento d'indice i-esimo nel vettore
     * @param i indice
     * @return voce
     */
    public Voce getElement(int i){return voci.get(i);}

    /**
     * ritorna l'indice a cui si trova l'elemento
     * @param vIn voce
     * @return indice del vettore o -1 se non è presente
     */
    public int search(Voce vIn){
        for(int i=0; i<size()-1;i++)
            if(getElement(i).equals(vIn))
                return i;
        return -1;
    }

    /**
     * ordina il vettore delle voci per data crescente
     * @param v vettore
     */
    public void sort(Vector<Voce> v) {
        v.sort((v1, v2) -> {
            try {
                return new SimpleDateFormat("dd/MM/yyyy").parse(v1.getData()).compareTo(new SimpleDateFormat("dd/MM/yyyy").parse(v2.getData()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

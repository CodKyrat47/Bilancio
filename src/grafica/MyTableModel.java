package grafica;
import bilancio.*;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 * classe per definire la struttura della tabella
 */
public class MyTableModel extends AbstractTableModel{
    /**
     * vettore da inserire nella tabella
     */
    private Vector v;
    /**
     * campi della tabella
     */
    private final String[] colName={"Data","Descrizione","Ammontare"};

    /**
     * costruttore della classe
     * @param v vettore
     */
    public MyTableModel(Vector v){
        this.v=v;
    }

    /**
     * ritorna il numero di colonne della tabella
     * @return numero colonne
     */
    public int getColumnCount(){return colName.length;}

    /**
     * ritorna il numero di righe della tabella
     * @return numero righe
     */
    public int getRowCount(){return v.size();}

    /**
     * ritorna il contenuto di una cella
     * @param row la riga il cui valore deve essere interrogato
     * @param col la colonna il cui valore deve essere interrogato
     * @return valore
     */
    public Object getValueAt(int row,int col){
        Voce voce=(Voce)v.elementAt(row);
        return switch (col) {
            case 0 -> voce.getData();
            case 1 -> voce.getDescrizione();
            case 2 -> voce.getAmmontare();
            default -> "";
        };
    }

    /**
     * ritorna il nome del campo di una determinata colonna
     * @param col  la colonna il cui valore deve essere interrogato
     * @return nome campo
     */
    public String getColumnName(int col){
        return colName[col];
    }

    /**
     * ritorna il nome di tutti i campi della tabella
     * @return stringa di nomi
     */
    public String[] getCampi(){return colName;}

    /**
     * ritorna il tipo dei valori della colonna
     * @param col  la colonna il cui valore deve essere interrogato
     * @return tipo
     */
    public Class getColumnClass(int col){
        return getValueAt(0,col).getClass();
    }

    /**
     * dice se la cella è modificabile o meno
     * @param row  la riga il cui valore deve essere interrogato
     * @param col la colonna il cui valore deve essere interrogato
     * @return esito controllo
     */
    public boolean isCellEditable(int row,int col){
        return false;
    }
    /**
     * imposta il vettore da mostrare in tabella
     * @param v vettore
     */
    public void setTableModel(Vector v){this.v=v;}
}

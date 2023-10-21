import grafica.Finestra;
/**
 * classe principale da cui parte il programma aprendo la finestra per la gestione del bilancio
 * @see grafica.Finestra
 * comandi per compilare ed eseguire dalla directory Progetto (da adattare in base alla directory in cui ci si trova)
 * javac -d ".\out" -cp ".\src;.\libs\jdatepicker-1.3.4.jar;.\libs\opencsv-5.7.1.jar" src\bilancio\*.java src\grafica\*.java src\file\*.java src\thread\*.java src\Main.java
 * java -cp ".\out;.\libs\jdatepicker-1.3.4.jar;.\libs\opencsv-5.7.1.jar" Main
 */
public class Main {
    /**
     * metodo da cui viene avviato il programma
     * @param args argomenti passati da terminale (non utilizzati)
     */
    public static void main(String[] args) {
        new Finestra("Gestione Bilancio");
    }
}
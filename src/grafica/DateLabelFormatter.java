package grafica;
import javax.swing.*;
import java.text.*;
import java.util.Calendar;

/**
 * classe per la formattazione del datepicker
 */
public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
    /**
     * stringa di formattazione della data
     */
    private final String datePattern = "dd-MM-yyyy";
    /**
     * creazione dell'oggetto
     */
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    /**
     * parsing della stringa in una data
     * @param text stringa da convertire
     * @return data
     * @throws ParseException eccezione di parsing
     */
    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }

    /**
     * parsing della data in una stringa
     * @param value valore da convertire
     * @return stringa
     */
    @Override
    public String valueToString(Object value) {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
        return "";
    }

}
package principal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wesley
 */
public class Calendario {
    static Calendar cal = Calendar.getInstance();
    
    public static String getDataAtual() {
        Date d = new Date();
        cal.setTime(d);
        cal.get(Calendar.SHORT);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");                
        return df.format(cal.getTime());
    }
    
    public static String getDataAtualDerbyDB() {
        Date d = new Date();
        cal.setTime(d);
        cal.get(Calendar.SHORT);        
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");                
        return df.format(cal.getTime());        
    }
    
    public static Date getDataDerbyDB(String sData) {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Date data = null;
        try {
            data = df.parse(sData);
        } catch (ParseException ex) {
            Logger.getLogger(Calendario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }
    
    public Date determinarDataFinal(Date dataInicial, int dias) {
        Calendar data2 = Calendar.getInstance(); 
        Calendar data3 = Calendar.getInstance(); 
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        data2.setTime(dataInicial);
        data3.setTime(dataInicial);      

        int i = 1;
        do {                         
            data3.add(Calendar.DAY_OF_MONTH, -1);
            if((data3.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) || (data3.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {

            } else {                                
                i++;
            }                                    
        } while(i <= dias);       
        //System.out.println("Dias: " + dias + ", Data Inicial:" + Calendario.dateToString(data2.getTime()) + " Data final" + Calendario.dateToString(data3.getTime()));
        return data3.getTime();
    }
    
    public static Date addDia(Date data, int qtd) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.add(Calendar.DAY_OF_MONTH, qtd);
        return cal.getTime();
    }
    
    public static Date getData() {
        return cal.getTime();        
    }
    
    public static String getHoraAtual() {
        Date d = new Date();
        cal.setTime(d);
        cal.get(Calendar.SHORT);
        SimpleDateFormat df = new SimpleDateFormat("k:mm");                
        return df.format(cal.getTime());
    }    

    public static String nomeMes(int i) {
        String MES[] = {"JANEIRO", "FEVEREIRO", "MARÇO", "ABRIL", "MAIO", "JUNHO", "JULHO", "AGOSTO", "SETEMBRO", "OUTUBRO", "NOVEMBRO", "DEZEMBRO"};
        return MES[i];
    }
    
    public static String nomeSemana(int i) {
        String[] SEMANA = {"DOMINGO", "SEGUNDA", "TERÇA", "QUARTA", "QUINTA", "SEXTA", "SÁBADO"};
        return SEMANA[i];
    }
    
    public static void setData(String dia) {
        cal.setTime(stringToDate(dia));
        cal.setFirstDayOfWeek(Calendar.SUNDAY);    
    }
    
    public static void setData(Date dia) {
        cal.setTime(dia);
        cal.setFirstDayOfWeek(Calendar.SUNDAY);    
    }
    
    public static String getDataString() {    
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        return df.format(cal.getTime());
    }
    
    public static String getHoraString() {
        DateFormat hf = DateFormat.getTimeInstance(DateFormat.SHORT);        
        return hf.format(cal.getTime());
    }
    
    public static Date stringToDate(String sData) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date data = null;
        try {
            data = df.parse(sData);
        } catch (ParseException ex) {
            Logger.getLogger(Calendario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }
    
    public static String dateToString(Date dDate) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(dDate);
    }
    
    public static Date stringToTime(String sData) {
        SimpleDateFormat df = new SimpleDateFormat("k:mm");
        Date data = null;
        try {
            data = df.parse(sData);
        } catch (ParseException ex) {
            Logger.getLogger(Calendario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return data;
    }
    
    public static String timeToString(Date dDate) {
        SimpleDateFormat df = new SimpleDateFormat("k:mm");
        return df.format(dDate);
    }
    
    public static int semana(String dia) {
        cal.setTime(stringToDate(dia));
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        return cal.get(Calendar.WEEK_OF_MONTH);        
    }
    
    public static int semana(Date dia) {
        cal.setTime(dia);
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        return cal.get(Calendar.WEEK_OF_MONTH);        
    }    
    
    public static int diaDaSemana() {
        return cal.get(Calendar.DAY_OF_WEEK);
    }
    
    public static int dia(Date data) {
        setData(data);
        return dia();
    }
    
    public static int dia() {
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    
    public static int mes() {
        return cal.get(Calendar.MONTH);
    }
    
    public static int mes(Date data) {
        setData(data);
        return mes();
    }    
    
    public static int ano() {
        return cal.get(Calendar.YEAR);
    }
    
    public static int ano(Date data) {
        setData(data);
        return ano();
    }
    
    public static boolean dataIgual(Date data1, Date data2) {
        return data1.equals(data2);
    }
        
}

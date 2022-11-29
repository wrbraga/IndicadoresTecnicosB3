package DB;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class mudarCorPreco implements TableCellRenderer {
    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = DEFAULT_RENDERER.getTableCellRendererComponent(table,value, isSelected, hasFocus, row, column);
        
        String v = table.getModel().getValueAt(row, Constantes.COLUNA_VARIACAO_COTACAO).toString();        
        Double variacao = Double.parseDouble(v.substring(0, v.length()-1));
        
        int ifr = Integer.parseInt(table.getModel().getValueAt(row, Constantes.COLUNA_IFR_COTACAO).toString());     
        
        String mm17s = table.getModel().getValueAt(row, Constantes.COLUNA_MM17_COTACAO).toString();
        Double mm17 = Double.parseDouble(mm17s.substring(0, mm17s.length()-1));        
        
        String mm74s = table.getModel().getValueAt(row, Constantes.COLUNA_MM74_COTACAO).toString();
        Double mm74 = Double.parseDouble(mm74s.substring(0, mm74s.length()-1));        

//        /*
//            Cor da linha quando selecionada
//        */
        if(table.getSelectedRow() == row) {
            c.setBackground(Color.LIGHT_GRAY);
        } else {
            c.setBackground(Color.WHITE);
        }
        
        if (column == Constantes.COLUNA_CODIGO_COTACAO) {
            c.setForeground(Color.BLACK);
        }
        
        if (column == Constantes.COLUNA_IFR_COTACAO) {

            if (ifr >= 40 && ifr <= 60) {                
                c.setForeground(Color.MAGENTA);
            } 
            
            if(ifr < 40) {
                c.setForeground(Color.green);
            }          
        
            if(ifr > 60) {
                c.setForeground(Color.red);
            }          

        } 
        
        if (column == Constantes.COLUNA_MM17_COTACAO) {
            if ( mm17 == 0.01) {
                c.setForeground(Color.BLUE);
            } else if ( mm17 < 0) {
                c.setForeground(Color.red);
            } else if (mm17 >= 1 && mm17 <= 5 ) {
                c.setForeground(Color.GREEN);
            } else {
                c.setForeground(Color.black);
            }
        }
        
        if (column == Constantes.COLUNA_MM74_COTACAO) {
            if ( mm74 >= 0 && mm74 < 1) {
                c.setForeground(Color.BLUE);
            } else if ( mm74 < 0) {
                c.setForeground(Color.red);
            } else if (mm74 >= 1 && mm74 <= 5 ) {
                c.setForeground(Color.GREEN);
            } else {
                c.setForeground(Color.black);
            }
        }
        
        switch(column) {
            case Constantes.COLUNA_ABERTURA_COTACAO:
            case Constantes.COLUNA_DATA_COTACAO:
            case Constantes.COLUNA_HORA_COTACAO:
                c.setForeground(Color.BLACK);
        }        
        
        if (column == Constantes.COLUNA_VALOR_COTACAO) {
            if (variacao >= 0.0) {
                c.setForeground(Color.green);
            } else {
                c.setForeground(Color.red);
            }                            
        } 
        
        if (column == Constantes.COLUNA_VARIACAO_COTACAO) {
            if (variacao >= 0) {
                c.setForeground(Color.green);
            } else {
                c.setForeground(Color.red);
            }                            
        } 
        
        return c;
    }
    
}

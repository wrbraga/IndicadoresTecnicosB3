package DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

public class codigoTableModel extends AbstractTableModel{
    private static final String[] COLUNAS = {"ID","CODIGO","DESCRICAO"};
    private DataBase db;
    private ResultSet resultSet;
    private int numeroDeLinhas;
    
    public codigoTableModel(DataBase db, String sql) {
         this.db = db;
                 
        resultSet = db.executeSQL(sql);
        try {
            resultSet.last();
            numeroDeLinhas = resultSet.getRow();            
        } catch (SQLException ex) {
            Logger.getLogger(codigoTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public int getRowCount() {
        return numeroDeLinhas;
    }

    @Override
    public int getColumnCount() {
        return COLUNAS.length;
    }
    
    @Override
    public String getColumnName(int col) {
        return COLUNAS[col];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            resultSet.absolute(rowIndex + 1);
            return resultSet.getObject(columnIndex + 1);
        } catch (SQLException | NullPointerException ex) { 
        }
        fireTableStructureChanged();
        return "";
    }
    
}

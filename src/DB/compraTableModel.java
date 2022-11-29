/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import principal.Main;
import principal.atualizaPreco;
import static principal.util.arredonda;

/**
 *
 * @author WRBraga
 */
public class compraTableModel extends AbstractTableModel implements Runnable{
    private final List<CompraDB> linha = new ArrayList<>();
    private static final String[] COLUNAS = {"CODIGO", "QUANT.", "VALOR COMPR.", "VALOR ATUAL", "VARIAÇÃO", "STOP LOSS", "STOP GAIN", "INVESTIDO", "DATA"};
    private CompraDB compra;
    private DataBase dataBase;
    private final SimpleDateFormat sdf;
    private Thread T;
    private final int tempo = 60;
    
    public compraTableModel(List resultado, DataBase db) {
        this.dataBase = db;
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        T = new Thread(this);
        //T.start();
        addCompra(resultado);
    }
    
    public void setDataBase(DataBase db) {
        dataBase = db;
    }
    
    @Override
    public int getRowCount() {
        return linha.size();
    }

    @Override
    public int getColumnCount() {
        return COLUNAS.length;
    }
    
    @Override
    public String getColumnName(int col) {
        return COLUNAS[col];
    }
    
    public void addCompra(CompraDB compra) {
        linha.add(compra);
        fireTableDataChanged();
    }    
    
    public final void addCompra(List resultado) {        
        for(Object o: resultado) {
            compra = (CompraDB) o;
            addCompra(compra);
        }        
    }    
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BigDecimal bd;
        try {
            compra = linha.get(rowIndex);
            switch(columnIndex) {
                case Constantes.COLUNA_CODIGO_COMPRA:
                    return dataBase.getCodigoByID(compra.getIdCodigo());
                case Constantes.COLUNA_QUANT_COMPRA:
                    return compra.getQuantidade();                    
                case Constantes.COLUNA_VALOR_COMPRA:
                    return Constantes.MOEDA + arredonda(compra.getValor(),2);
                case Constantes.COLUNA_VALOR_ATUAL_COMPRA:
                    return Constantes.MOEDA + arredonda(compra.getValorAtual(), 2);
                case Constantes.COLUNA_VARIACAO_COMPRA:               
                    return arredonda(((compra.getValorAtual()/compra.getValor() - 1) * 100), 2) + "%";
                case Constantes.COLUNA_STOP_LOSS_COMPRA:               
                    bd = new BigDecimal(compra.getValorLoss());
                    return Constantes.MOEDA + bd.setScale(3, RoundingMode.HALF_UP).doubleValue();
                case Constantes.COLUNA_STOP_GAIN_COMPRA:               
                    bd = new BigDecimal(compra.getValorGain());
                    return Constantes.MOEDA + bd.setScale(3, RoundingMode.HALF_UP).doubleValue();                    
                case Constantes.COLUNA_INVESTIDO_COMPRA:
                    return Constantes.MOEDA + arredonda(compra.getQuantidade() * compra.getValor(),2);
                case Constantes.COLUNA_DATA_COMPRA:
                    return sdf.format(compra.getDataCompra());                    
                default:
                    throw new IndexOutOfBoundsException("Valor incorreto");
            }
        } catch(IndexOutOfBoundsException e) {
            return "";            
        }        
    }

   @Override
    public void setValueAt(Object compra, int row, int col) {
        CompraDB atual = linha.get(row);
        CompraDB novo = (CompraDB) compra;
                  
        switch(col) {
                case Constantes.COLUNA_CODIGO_COMPRA:
                    atual.setIdCodigo(novo.getIdCodigo());
                    break;
                case Constantes.COLUNA_QUANT_COMPRA:
                    atual.setQuantidade(novo.getQuantidade());
                    break;
                case Constantes.COLUNA_VALOR_COMPRA:
                    atual.setValor(novo.getValor());
                    break;
                case Constantes.COLUNA_VALOR_ATUAL_COMPRA:
                    atual.setValorAtual(novo.getValorAtual());
                    break;
                case Constantes.COLUNA_STOP_LOSS_COMPRA:               
                    atual.setValorLoss(novo.getValorLoss());
                    break;
                case Constantes.COLUNA_STOP_GAIN_COMPRA:               
                    atual.setValorGain(novo.getValorGain());
                    break;
                case Constantes.COLUNA_DATA_COMPRA:
                    atual.setDataCompra(novo.getDataCompra());
                    break;
                default:
        }
        fireTableDataChanged();
    }
    
    public void setValueAt(Object compra, int row) {
        CompraDB atual = linha.get(row);
        CompraDB novo = (CompraDB) compra;
        
        atual.setIdCodigo(novo.getIdCodigo());
        atual.setIdCompra(novo.getIdCompra());
        atual.setValor(novo.getValor());
        atual.setValorAtual(novo.getValorAtual());
        atual.setQuantidade(novo.getQuantidade());
        atual.setValorGain(novo.getValorGain());
        atual.setValorLoss(novo.getValorLoss());
        atual.setDataCompra(novo.getDataCompra());
        
        fireTableDataChanged();
    }

   public void removeCompra(int index) {
        linha.remove(index);
        fireTableDataChanged();
    }    
   
    public void atualizaLista() { 
        for(int indice = 0; indice < getRowCount(); indice++) {
            String codigo = getValueAt(indice, Constantes.COLUNA_CODIGO_COMPRA).toString();        
            
            atualizaPreco a = new atualizaPreco(codigo);
            Thread t = new Thread(a);
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            CompraDB c = new CompraDB();            
//            c.setIdCodigo(dataBase.getIDByCodigo(codigo));            
//            c.setQuantidade(Integer.parseInt(getValueAt(indice, Constantes.COLUNA_QUANT_COMPRA).toString()));

            c.setValorAtual(Double.parseDouble(a.getPreco()));
//            c.setValorLoss(Double.parseDouble(getValueAt(indice,Constantes.COLUNA_STOP_LOSS_COMPRA).toString()));
//            c.setValorGain(Double.parseDouble(getValueAt(indice,Constantes.COLUNA_STOP_LOSS_COMPRA).toString()));
//            Calendario.setData(getValueAt(indice,Constantes.COLUNA_DATA_COMPRA).toString());            
//            c.setDataCompra(Calendario.getData());
                  
              setValueAt(c, indice, Constantes.COLUNA_VALOR_ATUAL_COMPRA);
            
        }
        
    }
    @Override
    public void run() {
        while(true) {
            atualizaLista();  
            try {
                Thread.sleep(tempo * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(cotacaoTableModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
}

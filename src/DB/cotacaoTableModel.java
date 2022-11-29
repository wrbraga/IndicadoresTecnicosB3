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

public class cotacaoTableModel extends AbstractTableModel implements Runnable{
    private final List<monitorarCotacao> linha = new ArrayList<>();
    private static final String[] COLUNAS = {"CODIGO", "IFR","MM17", "MM74",/*MM144",*/ "ABERTURA", "VALOR", "VARIAÇÃO", "DATA", "HORA"};
    
    monitorarCotacao cotacao;
    SimpleDateFormat sdf;
    private Thread T;
    private final int tempo = 60;
    private BigDecimal bd;
    private DataBase db;
    
    public cotacaoTableModel(List resultado, DataBase db) {
         sdf = new SimpleDateFormat("dd/MM/yyyy");         
         T = new Thread(this);
         //T.start();          
         this.db = db;
         addCotacao(resultado);
    }
    
    public cotacaoTableModel(String sql) {       
        cotacao = new monitorarCotacao();     
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
    
    public Double getVariacao() {
        try {
            bd = new BigDecimal(cotacao.getVariacao());
        } catch(Exception e) {
            bd = new BigDecimal(0.0);
        }
        return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    
    private Double getDiferenca(String codigo, int tempo, Double compara) {
        Double m = arredonda(db.calcularMediaMovelExponencial(cotacao.getCodigo(), tempo),2);
        Double valor = arredonda((((arredonda(cotacao.getValor(),2) / m) - 1) * 100),2);
        return valor;
    }
        
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            cotacao = linha.get(rowIndex);
            switch(columnIndex) {
                case Constantes.COLUNA_CODIGO_COTACAO:                   
                    return cotacao.getCodigo();                    
                case Constantes.COLUNA_IFR_COTACAO:                   
                    return db.calcularIFR(cotacao.getCodigo());                     
                case Constantes.COLUNA_MM17_COTACAO:
                    return getDiferenca(cotacao.getCodigo(), 17, cotacao.getValor()) + "%";
                case Constantes.COLUNA_MM74_COTACAO:
                    return getDiferenca(cotacao.getCodigo(), 74, cotacao.getValor()) + "%";

//                case Constantes.COLUNA_MM144_COTACAO:
//                    //return arredonda(db.calcularMediaMovelSimples(cotacao.getCodigo(), 144),2);
//                    return 0;
                case Constantes.COLUNA_ABERTURA_COTACAO:
                    return Constantes.MOEDA + arredonda(cotacao.getAbetura(),2);
                case Constantes.COLUNA_VALOR_COTACAO:                    
                    return Constantes.MOEDA + arredonda(cotacao.getValor(),2);
                case Constantes.COLUNA_VARIACAO_COTACAO:               
                    return String.valueOf(getVariacao()) + "%";
                case Constantes.COLUNA_DATA_COTACAO:
                    return sdf.format(cotacao.getData());
                case Constantes.COLUNA_HORA_COTACAO:
                    return cotacao.getHora();
                default:
                    throw new IndexOutOfBoundsException("Valor incorreto");
            }
        } catch(IndexOutOfBoundsException e) {
            return "";            
        }
    }
    
    @Override
    public void setValueAt(Object cotacao, int row, int col) {
        monitorarCotacao atual = linha.get(row);
        monitorarCotacao novo = (monitorarCotacao) cotacao;
        
        switch(col) {
            case Constantes.COLUNA_CODIGO_COTACAO:
                atual.setCodigo(novo.getCodigo());
                break;
            case Constantes.COLUNA_IFR_COTACAO:
                atual.setIfr(novo.getIfr());
                break;
            case Constantes.COLUNA_MM17_COTACAO:
                atual.setMm17(novo.getMm17());
                break;                
            case Constantes.COLUNA_MM74_COTACAO:
                atual.setMm74(novo.getMm74());
                break;       
//            case Constantes.COLUNA_MM144_COTACAO:
//                atual.setMm144(novo.getMm144());
//                break;                                
            case Constantes.COLUNA_ABERTURA_COTACAO:
                atual.setAbertura(novo.getAbetura());
                break;
            case Constantes.COLUNA_VALOR_COTACAO:
                atual.setValor(novo.getValor());
                break;
            case Constantes.COLUNA_VARIACAO_COTACAO:
                atual.setVariacao(novo.getVariacao());
                break;
            case Constantes.COLUNA_DATA_COTACAO:
                atual.setData(novo.getData());
                break;
            case Constantes.COLUNA_HORA_COTACAO:
                atual.setHora(novo.getHora());
        }
        fireTableDataChanged();
    }
    
    public void setValueAt(Object cotacao, int row) {
        monitorarCotacao atual = linha.get(row);
        monitorarCotacao novo = (monitorarCotacao) cotacao;
        
        atual.setCodigo(novo.getCodigo());
        atual.setIfr(novo.getIfr());
        atual.setMm17(novo.getMm17());
        atual.setMm74(novo.getMm74());        
        atual.setMm144(novo.getMm144());        
        atual.setAbertura(novo.getAbetura());
        atual.setValor(novo.getValor());
        atual.setVariacao(novo.getVariacao());
        atual.setData(novo.getData());    
        atual.setHora(novo.getHora());        
        
        fireTableDataChanged();
    }
    
    public monitorarCotacao getCotacao(int index) {
        return linha.get(index);
    }
    
    public void addCotacao(monitorarCotacao cotacao) {
        linha.add(cotacao);
        fireTableDataChanged();
    }
    
    public final void addCotacao(List resultado) {        
        for(Object o: resultado) {
            cotacao = (monitorarCotacao) o;
            addCotacao(cotacao);
        }        
    }
    
    public void removeCotacao(int index) {
        try {
            linha.remove(index);
        } catch(Exception e) {
            System.err.println("Favor selecionar repetir a operação!");
        }
        fireTableDataChanged();
    }
    
    public void atualizaLista() { 
        for(int indice = 0; indice < getRowCount(); indice++) {
            String codigo = getValueAt(indice, Constantes.COLUNA_CODIGO_COTACAO).toString();        
            atualizaPreco a = new atualizaPreco(codigo);
            Thread t = new Thread(a);
            t.start();
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            Double abertura = Double.parseDouble(a.getAbertura());
            Double atual = Double.parseDouble(a.getPreco());            
            
//            Double abertura = 0.0;
//            Double atual = 0.0;            
            
            monitorarCotacao m = new monitorarCotacao();
            
            m.setCodigo(codigo);                                  
            m.setIfr(db.calcularIFR(codigo));
            m.setMm17(db.calcularMediaMovelExponencial(codigo, 17));
            m.setMm74(db.calcularMediaMovelExponencial(codigo, 74));

            //m.setMm144(db.calcularMediaMovelSimples(codigo, 144, atual));                                   
            m.setAbertura(abertura);
            m.setValor(atual);            
            
            Double resultado = ((atual/abertura) - 1) * 100;

            m.setVariacao(resultado);

            m.setData(getValueAt(indice, Constantes.COLUNA_DATA_COTACAO));        
            m.setHora(a.getHora());        
            //m.setHora(Calendario.getDataAtual());   

            setValueAt(m, indice);
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

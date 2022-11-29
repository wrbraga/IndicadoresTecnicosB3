package DB;

import static java.lang.System.err;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import principal.Calendario;
import principal.incluirCodigo;
import principal.util;

public class DataBase {
    private static final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String DATABASE_URL = "jdbc:derby:c:\\teste\\investimentos.db"; // ;create=true
    private static final String USUARIO = "wesley";
    private static final String SENHA = "wesley";    
    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    public static int linha;    
    private boolean conectado = false;
    private String sql;    
    private util Util;
    private Calendario cal;
    
    public DataBase() {
        Util = new util();
        cal = new Calendario();
    }
    
    public int conectar(boolean criar) {
        String url = DATABASE_URL;
        
        if(criar) {
            url = DATABASE_URL + ";create=true";
        }
        
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(url,USUARIO,SENHA);
            con.setAutoCommit(true);
            conectado = true;
            
            err.println("Conexao estabelecida!");
            

            if(existeTabela("CODIGO")) {
                err.println("Tabela CODIGO existe!");
            } else {
                //String sql = "CREATE TABLE APP.CODIGO(ID_CODIGO INTEGER NOT NULL PRIMARY KEY,CODIGO VARCHAR(15) NOT NULL,DESCRICAO VARCHAR(255),UNIQUE(CODIGO))";
                sql = "CREATE TABLE APP.CODIGO (" +
                "ID_CODIGO INTEGER NOT NULL," +
                "CODIGO VARCHAR(15) NOT NULL," +
                "DESCRICAO VARCHAR(255)," +
                "SETOR VARCHAR(255)," +
                "CONSTRAINT SQL160621133127430 PRIMARY KEY (ID_CODIGO))";
                criarTabela("CODIGO", sql);
            }

            if(existeTabela("OPERACAO")) {
                err.println("Tabela OPERACAO existe!");
            } else {
                sql = "CREATE TABLE APP.OPERACAO(" +
                        "ID_OPERACAO INTEGER NOT NULL PRIMARY KEY," +
                        "ID_CODIGO INTEGER NOT NULL," +
                        "QUANTIDADE INTEGER NOT NULL," +
                        "PRECO_COMPRA DOUBLE NOT NULL," +
                        "PRECO_VENDA DOUBLE," +
                        "STOP_LOSS DOUBLE," +
                        "STOP_GAIN DOUBLE," +
                        "DATA_COMPRA DATE," +
                        "DATA_VENDA DATE,"
                        + "CONCLUIDO INTEGER)";
                criarTabela("OPERACAO", sql);
            }            

            if(existeTabela("COTACAO")) {
                err.println("Tabela COTACAO existe!");
            } else {
                //String sql = "CREATE TABLE APP.COTACAO(ID_COTACAO INTEGER NOT NULL PRIMARY KEY,ID_CODIGO INTEGER NOT NULL,VALOR DOUBLE NOT NULL,DATA DATE)";
                sql = "CREATE TABLE APP.COTACAO (\n" +
                "	ID_COTACAO INTEGER NOT NULL,\n" +
                "	ID_CODIGO INTEGER NOT NULL,\n" +
                "	VALOR_FECHAMENTO DOUBLE NOT NULL,\n" +
                "	VALOR_ABERTURA DOUBLE,\n" +
                "	VALOR_MAXIMO DOUBLE,\n" +
                "	VALOR_MINIMO DOUBLE,\n" +                        
                "	\"DATA\" DATE,\n" +                
                "	CONSTRAINT SQL160621133250710 PRIMARY KEY (ID_COTACAO)\n" +
                ")";
                criarTabela("COTACAO", sql);
            } 
            
            if(existeTabela("COTACAOHISTORICA")) {
                err.println("Tabela COTACAO_HISTORICA existe!");
            } else {
                sql = "CREATE TABLE APP.COTACAOHISTORICA (\n" +
                "	ID_COTACAO INTEGER NOT NULL,\n" +
                "	CODIGO VARCHAR(15) NOT NULL,\n" +
                "	VALOR_FECHAMENTO DOUBLE NOT NULL,\n" +
                "	VALOR_ABERTURA DOUBLE,\n" +
                "	VALOR_MAXIMO DOUBLE,\n" +
                "	VALOR_MINIMO DOUBLE,\n" +                        
                "	VOLUME BIGINT,\n" +                                                
                "	\"DATA\" DATE,\n" +                
                "	CONSTRAINT COTACAOHISTORICA_PK PRIMARY KEY (ID_COTACAO)\n" +
                ")";
                System.out.println(sql);
                criarTabela("COTACAOHISTORICA", sql);
            } 
            
            if(existeTabela("COMPRA")) {
                err.println("Tabela COMPRA existe!");
            } else {
                //String sql = "CREATE TABLE APP.COTACAO(ID_COTACAO INTEGER NOT NULL PRIMARY KEY,ID_CODIGO INTEGER NOT NULL,VALOR DOUBLE NOT NULL,DATA DATE)";
                sql = "CREATE TABLE APP.COMPRA (\n" +
                "	ID_COMPRA INTEGER NOT NULL,\n" +
                "	ID_CODIGO INTEGER,\n" +
                "	VALOR DOUBLE,\n" +
                "	QUANTIDADE INTEGER,\n" +
                "	VALOR_LOSS DOUBLE,\n" +
                "	VALOR_GAIN DOUBLE,\n" +
                "	DATA_COMPRA DATE,\n" +
                "	CONSTRAINT COMPRA_PK PRIMARY KEY (ID_COMPRA)\n" +
                ")";
                criarTabela("COMPRA", sql);
            } 
            
            if(existeTabela("VENDA")) {
                err.println("Tabela VENDA existe!");
            } else {
                //String sql = "CREATE TABLE APP.COTACAO(ID_COTACAO INTEGER NOT NULL PRIMARY KEY,ID_CODIGO INTEGER NOT NULL,VALOR DOUBLE NOT NULL,DATA DATE)";
                sql = "CREATE TABLE APP.VENDA (\n" +
                "	ID_VENDA INTEGER NOT NULL,\n" +
                "	ID_COMPRA INTEGER,\n" +
                "	VALOR_VENDA DOUBLE,\n" +
                "	QUANTIDADE INTEGER,\n" +
                "	DATA_VENDA DATE,\n" +
                "	CONSTRAINT VENDA_PK PRIMARY KEY (ID_VENDA)\n" +
                ")";
                criarTabela("VENDA", sql);
            } 
            
            return 0;   // Tudo OK
            
        } catch(ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Driver não encontrado!");
            return 1;   // Não encontrou o driver
        } catch(SQLException e) {            
            try {
                stmt = con.createStatement();
                stmt.executeUpdate("CREATE DATABASE APP.investimentos.db");
            } catch (SQLException ex) {
                Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(null,"Banco de dados não encontrado!\nExecute novamente a aplicação para que o banco de dados possa ser criado!");
            return 2;   // Não encontrou o Banco de dados
        }  
    }
    
    public void desconectar() {
        try {
            con.close();
            conectado = false;
        } catch(SQLException | NullPointerException e) {}
    }
    
    public boolean existeTabela(String tbName) {               
        String[] tableTypes = { "TABLE" };
        DatabaseMetaData metaData;
        try {
            metaData = con.getMetaData();                 
            rs = metaData.getTables( null, null, tbName, tableTypes );            
            while(rs.next()) {                
                if (rs.getString("TABLE_NAME").equals(tbName)) return true;
            }
        } catch(Exception e) {}
        return false;
    }
    
    public void criarTabela(String tbNome, String sql) {
        if (executeUpdateSQL(sql) == 1) {
            System.out.println("Tabela " + tbNome + " criada com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null,"Tabela " + tbNome + " não encontrada!\nExecute novamente a aplicação para que a tabela possa ser criada.");
           // System.exit(0);
        }
    }
    
    public Connection getConexao() {
        return con;
    }
    
    public Statement getStatement() {
        return stmt;
    }
    
    public Statement setStatement() {
        Statement stmtLocal = null;
        try {
            stmtLocal = con.createStatement( 
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stmtLocal;        
    }
    
    public ResultSet executeSQL(String sql) {
        stmt = setStatement();
        try {            
            rs = stmt.executeQuery(sql);                                                
        } catch(SQLException e) {}
        
        return rs;
    }
    
    public void closeRS() {
        try {
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    public int executeUpdateSQL(String sql) {
        try {
            stmt = con.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY
            );
            int ret = stmt.executeUpdate(sql);           
            return ret;
        } catch(SQLException | NullPointerException e) { }
        return 0;
    }
     
    public void ultimoRegistro() {
        try {
            rs.last();
            linha = rs.getRow();
        } catch(SQLException e) {}
    }
    
    public void primeiroRegistro() {
        try {
            rs.first();
            linha = rs.getRow();
        } catch(SQLException e) {}
    }
    
    public void posicionarNoRegistro(int registro) {
        try {
            if (registro == 0) {
                registro = 1;
            }
            rs.absolute(registro);
            linha = registro;
        } catch(SQLException e) {}
    }
    
    public boolean avancarRegistro() {
        boolean ret = false;
        try {
            if(!rs.isLast()) {
                ret = rs.next();
                linha = rs.getRow();
            }
        } catch(SQLException e) {}
        return ret;
    }
    
    public boolean retrocederRegistro() {
        boolean ret = false;        
        try {
            if(!rs.isFirst()) {
                ret = rs.previous();
                linha = rs.getRow();
            }
        } catch(SQLException e) {}
        return ret;
    }

    public void procurarRegistro(String campo, String nome) {      
        try {
            while(rs.next()) {
                if(rs.getString(campo).toUpperCase().contains(nome.toUpperCase())) {
                    rs.absolute(rs.getRow());
                    break;
                }
            }                           
        } catch (SQLException e) {} 
    }
    
            
    public List cotacaoList() {
        List cotacao = new ArrayList();
        
        try {
            while(rs.next()) {
                DB.monitorarCotacao c = new DB.monitorarCotacao();
                
                int ifr = calcularIFR(rs.getString("CODIGO"));
                
                if(rs.getDouble("VOLUME") > 20000000)
                if(rs.getFloat("VALOR_FECHAMENTO") > 3 && rs.getFloat("VALOR_FECHAMENTO") < 15) {
                    if(ifr > 0 && ifr <= 60) {
                        c.setCodigo(rs.getString("CODIGO"));
                        c.setValor(rs.getFloat("VALOR_FECHAMENTO"));                        
                        c.setData(rs.getDate("DATA"));     

                        cotacao.add(c);
                    }
                }
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return cotacao;
    }
    
    public List compraList() {
        List compra = new ArrayList();
        try {
            while(rs.next()) {
                DB.CompraDB c = new DB.CompraDB();
                c.setIdCompra(rs.getInt(1));
                c.setIdCodigo(rs.getInt(2));
                c.setValor(rs.getDouble(3));
                c.setQuantidade(rs.getInt(4));
                c.setValorLoss(rs.getDouble(5));
                c.setValorGain(rs.getDouble(6));
                c.setDataCompra(rs.getDate(7));
                compra.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return compra;
    }    
    
    public int maiorID(String ID, String tabela) {
        sql = "SELECT MAX(" + ID + ") FROM APP." + tabela;
        ResultSet rs = executeSQL(sql);
        try {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(incluirCodigo.class.getName()).log(Level.SEVERE, null, ex);
        }
        //closeRS();
        return 0;
    }    
    public String getCodigoByID(int id_codigo) {
        sql = "SELECT CODIGO FROM APP.CODIGO WHERE ID_CODIGO = " + id_codigo;
        rs = executeSQL(sql);
        
        String cod = null;
        try {            
            while(rs.next()) {
                cod = rs.getString("CODIGO");
            }
        }  catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        //closeRS();
        return cod;
    }
    
    public int getIDByCodigo(String codigo) {
        sql = "SELECT ID_CODIGO FROM APP.CODIGO WHERE CODIGO = '" + codigo.trim() + "'";
        rs = executeSQL(sql);
        
        int cod = 0;
        try {            
            while(rs.next()) {
                cod = rs.getInt("ID_CODIGO");
            }
        }  catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        //closeRS();
        return cod;
    }
    
    public int getIdTableCotacao(String codigo) {
        sql = "SELECT COTACAO.ID_COTACAO FROM APP.CODIGO CODIGO, APP.COTACAO COTACAO " +
                "WHERE COTACAO.ID_CODIGO = CODIGO.ID_CODIGO AND CODIGO.CODIGO = '" + codigo.trim() +"'";
        
        rs = executeSQL(sql);
        
        int id = 0;
        try {            
            while(rs.next()) {
                id = rs.getInt("ID_COTACAO");
            }
        }  catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        //closeRS();
        return id;
    }

    public int getIdTableCompra(String codigo) {
        sql = "SELECT COMPRA.ID_COMPRA\n" +
            "FROM APP.CODIGO CODIGO, APP.COMPRA COMPRA \n" +
            "WHERE COMPRA.ID_CODIGO = CODIGO.ID_CODIGO AND CODIGO.CODIGO = '" + codigo.trim() +"'";
        
        rs = executeSQL(sql);
        
        int id = 0;
        try {            
            while(rs.next()) {
                id = rs.getInt("ID_COMPRA");
            }
        }  catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        //closeRS();
        return id;
    }        
    
    public void removerCotacao(String codigo) {       
        sql = "DELETE FROM APP.Cotacao WHERE ID_COTACAO=" + getIdTableCotacao(codigo);
        executeUpdateSQL(sql);
        //closeRS();
    }
    
    /**
     * Insere na tabela COMPRA os dados da ação comprada pelo formCompra.java
     * @param compra 
     */
    public void inserirCompra(CompraDB compra) {
        SimpleDateFormat d = new SimpleDateFormat("dd.MM.yyyy");
                
        sql = "INSERT INTO APP.COMPRA (ID_COMPRA, ID_CODIGO, VALOR, QUANTIDADE, VALOR_LOSS, VALOR_GAIN, DATA_COMPRA) \n" +
        "VALUES(" + compra.getIdCompra() + "," 
                + compra.getIdCodigo() + "," 
                + compra.getValor() + "," 
                + compra.getQuantidade() + "," 
                + compra.getValorLoss() + "," 
                + compra.getValorGain() + ",DATE('" + d.format(compra.getDataCompra()) + "'))";
        
        executeUpdateSQL(sql);
        //closeRS();
    }

    public void removerCompra(String codigo) {
        sql = "DELETE FROM APP.Compra WHERE ID_COMPRA=" + getIdTableCompra(codigo);
        executeUpdateSQL(sql);
        //closeRS();
    }

    public void inserirCotacaoHistorica(CotacaoHistorica cotacao) {
        SimpleDateFormat d = new SimpleDateFormat("dd.MM.yyyy");
        
        sql = "INSERT INTO APP.COTACAOHISTORICA (ID_COTACAO, CODIGO, VALOR_FECHAMENTO, VALOR_ABERTURA, VALOR_MAXIMO, VALOR_MINIMO, VOLUME, DATA)" + 
        "VALUES("
                + cotacao.getId_cotacao() + ","
                + "'" + cotacao.getCodigo() + "',"
                + cotacao.getValor_fechamento() + ","
                + cotacao.getValor_abertura() + ","
                + cotacao.getValor_maximo() + ","
                + cotacao.getValor_minimo() + ","
                + cotacao.getVolume() + ","
                + "DATE('" + d.format(cotacao.getData()) +"'))";
        
        System.out.println(cotacao.getCodigo());
        executeUpdateSQL(sql);
        
        if(getIDByCodigo(cotacao.getCodigo()) == 0) {
            sql = "INSERT INTO APP.Codigo (ID_CODIGO, CODIGO, DESCRICAO) VALUES(" + (maiorID("ID_CODIGO","CODIGO") + 1) + ",'" + cotacao.getCodigo() + "','')";        
            executeUpdateSQL(sql);
        } else {
            System.err.println("Código Já Cadastrado!");
        }
        //closeRS();
    }
    
    public List getIntervaloCotacaoHistorica(String codigo, java.util.Date dataInicial, java.util.Date dataFinal) {
        SimpleDateFormat d = new SimpleDateFormat("dd.MM.yyyy");
//        sql = "SELECT * FROM APP.COTACAOHISTORICA " +
//        "WHERE CODIGO = '" + codigo + "' AND DATA <= '" + d.format(dataFinal) + "' AND DATA >= '" + d.format(dataInicial) + "' " +
//        "ORDER BY DATA DESC";
        
        sql = "SELECT * FROM APP.COTACAOHISTORICA " +
        "WHERE CODIGO = '" + codigo + "' AND DATA <= '" + d.format(dataFinal) + "'";

       // System.out.println(sql);
        List cotacao = new ArrayList();                
        
        try(Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            //rs = executeSQL(sql);                        
            while(rs.next()) {
                CotacaoHistorica cotacaoHistorica = new CotacaoHistorica();
                cotacaoHistorica.setId_cotacao(rs.getInt("ID_COTACAO"));
                cotacaoHistorica.setCodigo(rs.getString("CODIGO"));
                cotacaoHistorica.setValor_fechamento(rs.getDouble("VALOR_FECHAMENTO"));
                cotacaoHistorica.setValor_abertura(rs.getDouble("VALOR_ABERTURA"));
                cotacaoHistorica.setValor_maximo(rs.getDouble("VALOR_MAXIMO"));
                cotacaoHistorica.setValor_minimo(rs.getDouble("VALOR_MINIMO"));
                cotacaoHistorica.setVolume(rs.getLong("VOLUME"));
                cotacaoHistorica.setData(rs.getDate("DATA"));
                cotacao.add(cotacaoHistorica);                
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
            
        }
       // closeRS();
        return cotacao;        
    }
    
    public int calcularIFR(String codigo) {
        java.util.Date df = Calendario.stringToDate(Calendario.getDataAtual());
        java.util.Date di = cal.determinarDataFinal(df,16);                 
        List ifr = getIntervaloCotacaoHistorica(codigo, di, df);                
        return util.IFR(ifr, 15);
    }
    
    public double calcularMediaMovelSimples(String codigo, int tempo, double ultima) {        
        java.util.Date di = Calendario.getData();        
        java.util.Date df = cal.determinarDataFinal(di,tempo-1);                 
        List mm = getIntervaloCotacaoHistorica(codigo, df, di);                   
        Double media = Util.MediaMovelSimples(mm, tempo, ultima);
                
        return media;
    }
    
    public double calcularMediaMovelExponencial(String codigo, int tempo) {        
        java.util.Date di = Calendario.getData();        
        java.util.Date df = cal.determinarDataFinal(di,tempo-1);                 
        List mm = getIntervaloCotacaoHistorica(codigo, df, di);                   
        Double media = Util.MediaMovelExponencial(mm, tempo);
                
        return media;
    }    
}

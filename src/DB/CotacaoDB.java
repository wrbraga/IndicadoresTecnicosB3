package DB;

import java.util.Date;

public class CotacaoDB {
    private int id_cotacao;
    private int id_codigo;
    private double valor_fechamento;
    private double valor_abertura;
    private double valor_maximo;
    private double valor_minimo;
    private Date data;
    
    public CotacaoDB() {
        
    }
    
    public CotacaoDB(int id_cotacao, int id_codigo, double valor_fechamento, double valor_abertura, double valor_maximo, double valor_minimo, Date data) {
        this.id_cotacao = id_cotacao;
        this.id_codigo = id_codigo;
        this.valor_fechamento = valor_fechamento;
        this.valor_abertura = valor_abertura;
        this.valor_maximo = valor_maximo;
        this.valor_minimo = valor_minimo;
        this.data = data;
    }
    
    public int getId_cotacao() {
        return id_cotacao;
    }

    public void setId_cotacao(int id_cotacao) {
        this.id_cotacao = id_cotacao;
    }

    public int getId_codigo() {
        return id_codigo;
    }

    public void setId_codigo(int id_codigo) {
        this.id_codigo = id_codigo;
    }

    public double getValor_fechamento() {
        return valor_fechamento;
    }

    public void setValor_fechamento(double valor_fechamento) {
        this.valor_fechamento = valor_fechamento;
    }

    public double getValor_abertura() {
        return valor_abertura;
    }

    public void setValor_abertura(double valor_abertura) {
        this.valor_abertura = valor_abertura;
    }

    public double getValor_maximo() {
        return valor_maximo;
    }

    public void setValor_maximo(double valor_maximo) {
        this.valor_maximo = valor_maximo;
    }

    public double getValor_minimo() {
        return valor_minimo;
    }

    public void setValor_minimo(double valor_minimo) {
        this.valor_minimo = valor_minimo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }    
            
}

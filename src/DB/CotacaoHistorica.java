package DB;

import java.util.Date;

public class CotacaoHistorica {
    private int id_cotacao;
    private String codigo;
    private double valor_fechamento;
    private double valor_abertura;
    private double valor_maximo;
    private double valor_minimo;
    private long volume;
    private Date data;
    
    public CotacaoHistorica() {
        
    }
    
    public CotacaoHistorica(int id_cotacao, String codigo, double valor_fechamento, double valor_abertura, double valor_maximo, double valor_minimo, long volume, Date data) {
        this.id_cotacao = id_cotacao;
        this.codigo = codigo;
        this.valor_fechamento = valor_fechamento;
        this.valor_abertura = valor_abertura;
        this.valor_maximo = valor_maximo;
        this.valor_minimo = valor_minimo;
        this.volume = volume;
        this.data = data;
    }
    
    public int getId_cotacao() {
        return id_cotacao;
    }

    public void setId_cotacao(int id_cotacao) {
        this.id_cotacao = id_cotacao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
    
    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }    
            
}

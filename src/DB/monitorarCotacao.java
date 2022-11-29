package DB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class monitorarCotacao {
    private String codigo;
    private double valor;
    private double variacao;
    private Date data;
    private String hora;
    private double abertura;
    private int ifr;
    private double mm17;
    private double mm34;
    private double mm74;
    private double mm144;
       
    public monitorarCotacao() {
        
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
    public void setData(Object data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.data = sdf.parse(data.toString());
        } catch (ParseException ex) {
            Logger.getLogger(monitorarCotacao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getHora() {
        return hora;
    }
    
    public void setHora(String hora) {
        this.hora = hora;
    }

    public double getVariacao() {
        return variacao;
    }
    
    public void setVariacao(double variacao) {
        this.variacao = variacao;
    }
    
    public void setVariacao(String variacao) {
        this.variacao = Float.parseFloat(variacao);
    }
    
    public void setAbertura(double a) {
        abertura = a;
    }
    
    public double getAbetura() {
        return abertura;
    }
    
    public int getIfr() {
        return ifr;
    }

    public void setIfr(int ifr) {
        this.ifr = ifr;
    }

    public double getMm17() {
        return mm17;
    }

    public void setMm17(double mm17) {
        this.mm17 = mm17;
    }

    public double getMm34() {
        return mm34;
    }

    public void setMm34(double mm34) {
        this.mm34 = mm34;
    }

    public double getMm74() {
        return mm74;
    }

    public void setMm74(double mm74) {
        this.mm74 = mm74;
    }

    public double getMm144() {
        return mm144;
    }

    public void setMm144(double mm144) {
        this.mm144 = mm144;
    }

    
}

package DB;

import java.util.Date;

public class CompraDB {
    private Integer idCompra;
    private Integer idCodigo;
    private double valor;
    private double valorAtual;
    private Integer quantidade;
    private Double valorLoss;
    private Double valorGain;
    private Date dataCompra;

    public CompraDB() {
    }

    public CompraDB(Integer idCompra) {
        this.idCompra = idCompra;
    }

    public Integer getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    public Integer getIdCodigo() {
        return idCodigo;
    }

    public void setIdCodigo(Integer idCodigo) {
        this.idCodigo = idCodigo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
    
    public Double getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(Double valor) {
        this.valorAtual = valor;
    }
    
    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValorLoss() {
        return valorLoss;
    }

    public void setValorLoss(Double valorLoss) {
        this.valorLoss = valorLoss;
    }

    public Double getValorGain() {
        return valorGain;
    }

    public void setValorGain(Double valorGain) {
        this.valorGain = valorGain;
    }

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCompra != null ? idCompra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompraDB)) {
            return false;
        }
        CompraDB other = (CompraDB) object;
        if ((this.idCompra == null && other.idCompra != null) || (this.idCompra != null && !this.idCompra.equals(other.idCompra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DB.Compra[ idCompra=" + idCompra + " ]";
    }
    
}

package principal;

import parserhtml.ParserInformacoesDasAcoes;

public class atualizaPreco implements Runnable{
    private volatile String preco;
    private ParserInformacoesDasAcoes informacao;
    private final String codigo;
    private String hora;
    private String abertura;
    private String fechamento;
    
    public atualizaPreco(String codigo) {
        this.codigo = codigo;
    }
    
    public String getPreco() {
        return preco;
    }
    
    public String getHora() {
        return hora;
    }
    
    public String getAbertura() {
        return abertura;
    }
    
    public String getFechamentoAnterior() {
        return fechamento;
    }
        
    @Override
    public void run() {        
        informacao = new ParserInformacoesDasAcoes();        
        informacao.analisar(codigo);
        preco = informacao.getUltimoPreco();
        hora = informacao.getHora();
        abertura = informacao.getPrecoAbertura();
        fechamento = informacao.getPrecoFechamento();        
    }
    
    @Override
    public String toString() {
        return preco;
    }
}

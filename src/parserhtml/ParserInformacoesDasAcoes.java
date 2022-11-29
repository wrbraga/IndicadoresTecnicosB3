package parserhtml;
import java.io.IOException;
import static org.jsoup.Jsoup.connect;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import principal.Calendario;

public final class ParserInformacoesDasAcoes {
    
    private Document doc;
    private String URL_ADVFN;
    private String URL_YAHOO;
    private String codigo;
    private String variacaoDiaPreco;
    private String variacaoDiaPerc;
    private String ultimoPreco;
    private String precoMaximo;
    private String precoMinimo;
    private String precoAbertura;
    private String precoFechamento;
    private String hora;
        
    public ParserInformacoesDasAcoes() {        
    }
    
    public ParserInformacoesDasAcoes(String codigo) {
        this.codigo = codigo;
        setURL(codigo);
    }        
    
    private void setURL(String codigo) {
        this.codigo = codigo.trim();
        URL_ADVFN = "http://br.advfn.com/bolsa-de-valores/bovespa/" + codigo + "/cotacao";
        
        URL_YAHOO = "https://br.financas.yahoo.com/q?s=" + this.codigo + ".SA";
        //System.out.println(URL);
    }
    
    private void open(String URL) {
        try {
            doc = connect(URL).get();            
        } catch (IOException ex) {
            
        }        
    }
    
    private void lerCotacaoYahoo() {        
        open(URL_YAHOO);
        // Variação do Dia (p) 
        try {
            Element conteudo = doc.getElementById("yfs_l84_" + codigo.toLowerCase().trim() + ".sa");        
            ultimoPreco = conteudo.text();
        } catch(NullPointerException e) {
            ultimoPreco = "0.0";
        }
                
        // Hora
        try {
            Element eHora = doc.getElementById("yfs_t53_" + codigo.toLowerCase().trim() + ".sa");
            hora = eHora.text();
        } catch(NullPointerException e) {
            hora = Calendario.getHoraAtual();
        }
        
        // Preco de abertura do dia e fechamento anterior
        try {                       
            for(Element tabela : doc.select("table")) {
                int i = 1;
                for(Element e : tabela.select("td")) {                    
                    for(Element row : e.getElementsByClass("yfnc_tabledata1")) {
                        switch(i) {
                            case 1:
                                precoFechamento = row.text();
                                break;
                            case 2:
                                precoAbertura = row.text();
                                break;
                        }
                        i++;
                        if(i == 3) return;
                    }                    
                }
            }            
        } catch(NullPointerException e) {
            precoFechamento = "0.0";
        }
        
    }
        
    private void lerADVFN() {        
        open(URL_ADVFN);
        // Variação do Dia (p)
        Element conteudo = doc.getElementById("quoteElementPiece4");
        variacaoDiaPreco = conteudo.text();

        // Variação do Dia %
        conteudo = doc.getElementById("quoteElementPiece5");
        variacaoDiaPerc = conteudo.text();

        // Último Preço
//        conteudo = doc.getElementById("quoteElementPiece6");
//        ultimoPreco = conteudo.text();

        // Preço Máximo
        conteudo = doc.getElementById("quoteElementPiece7");
        precoMaximo = conteudo.text();

        // Preço Mínimo
        conteudo = doc.getElementById("quoteElementPiece8");
        precoMinimo = conteudo.text();

        // Preço de Abertura
        conteudo = doc.getElementById("quoteElementPiece9");
        precoAbertura = conteudo.text();

        // Preço de Fechamento
        conteudo = doc.getElementById("quoteElementPiece10");
        precoFechamento = conteudo.text();

        // Hora
//        conteudo = doc.getElementById("quoteElementPiece11");
//        hora = conteudo.text();             
    }

    public void analisar(String codigo) {
        setURL(codigo);
        lerCotacaoYahoo();
        //lerADVFN();
    }
    
    public String getUltimoPreco() {
        return ultimoPreco.replace(",", ".");
    }
    
    public String getPrecoAbertura() {
        try {
            return precoAbertura.replace(",", ".");
        } catch(Exception e) {            
        }
        return "0.0";
    }
    
    public String getPrecoFechamento() {
        return precoFechamento.replace(",", ".");
    }
    
    public String getPrecoMaximo() {
        return precoMaximo.replace(",", ".");
    }
    
    public String getPrecoMinimo() {
        return precoMinimo.replace(",", ".");
    }
    
    public String getHora() {
        return hora;
    }
       
}

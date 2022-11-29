package CotacaoHistorica;

import DB.CotacaoHistorica;
import DB.DataBase;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import principal.Calendario;
import java.util.Date;

/*
    http://www.bmfbovespa.com.br/pt_br/servicos/market-data/historico/mercado-a-vista/series-historicas/
*/
public class LerCotacao {
    private final Charset cs = StandardCharsets.US_ASCII;
    private String linha = null;
    private final Path caminho;
    private CotacaoHistorica cotacao;
    private DataBase db = null;
            
    public LerCotacao(String arquivo) {
        caminho = Paths.get(arquivo);
        cotacao = new CotacaoHistorica();        
    }
    
    public void setDataBase(DataBase db) {
        this.db = db;
    }
    
    public boolean validarArquivo() {                   
        try(BufferedReader ler = Files.newBufferedReader(caminho, cs)) {
            linha = ler.readLine();

            if(tipoRegistro(linha).equalsIgnoreCase("00") && codigoOrigem(linha).equalsIgnoreCase("BOVESPA")) {
                    return true;               
            }
            ler.close();
        } catch (IOException ex) {
            Logger.getLogger(LerCotacao.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return false;
    }
    
    public void ler(String papel) {                   
        try(BufferedReader ler = Files.newBufferedReader(caminho, cs)) {
            while((linha = ler.readLine()) != null) {
                                
                if(tipoRegistro(linha).equalsIgnoreCase("01")) {
                    if(codigoBDI(linha).equalsIgnoreCase("02")) {
                        cotacao.setId_cotacao(db.maiorID("ID_COTACAO", "COTACAOHISTORICA") + 1);
                        cotacao.setCodigo(codigoPapel(linha));
                        cotacao.setValor_abertura(precoAbertura(linha));
                        cotacao.setValor_fechamento(precoFechamentoPregao(linha));
                        cotacao.setValor_maximo(precoMaximo(linha));
                        cotacao.setValor_minimo(precoMinimo(linha));
                        cotacao.setVolume(volume(linha));
                        cotacao.setData(dataPregao(linha));
                        db.inserirCotacaoHistorica(cotacao);                    
                    }
                }                                                
                
                if((tipoRegistro(linha).equalsIgnoreCase("99"))) {
                    System.out.println("Fim da Importação!");
                }
            }
            
            ler.close();
        } catch (IOException ex) {
            Logger.getLogger(LerCotacao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
        MÉTODOS PARA LEITURA DO CABEÇALHO DO ARQUIVO
    */
    /*
        00 - Header
        01 - Cotações dos papéis por dia
        99 - Trailer (fim do arquivo)
    */
    public String tipoRegistro(String linha) {
        return linha.substring(0, 2).trim();
    }
    
    public String nomeDoArquivo(String linha) {
        return linha.substring(2, 15).trim();
    }
    
    public String codigoOrigem(String linha) {
        return linha.substring(15, 23).trim();
    }
    
    public String dataGeracao(String linha) {
        return linha.substring(23, 31).trim();
    }
    
    public String reserva(String linha) {
        return linha.substring(32, 245);
    }
    
    /* 
        METODOS PARA LEITURA DAS COTAÇÕES DIÁRIAS
    */
    public Date dataPregao(String linha) {
        String d1 = linha.substring(2, 10).trim();
        
        String ano = d1.substring(0, 4);
        String mes = d1.substring(4, d1.length() - 2);
        String dia = d1.substring(6);        
        
        String data = dia + "." + mes + "." + ano;

        return Calendario.getDataDerbyDB(data);        
    }
    
    public String codigoBDI(String linha) {
        return linha.substring(10, 12).trim();
    }
    
    public String descricaoCodigoBDI(String codigo) {
        switch(codigo) {
            case "02": return "LOTE PADRÃO";
            case "06": return "CONCORDATÁRIAS";
            case "10": return "DIREITOS E RECIBOS";
            case "12": return "FUNDOS IMOBILIÁRIOS";
            case "14": return "CERTIFIC. INVESTIMENTO / DEBÊNTURES / TÍTULOS DIVIDA PÚBLICA";
            case "18": return "OBRIGAÇÕES";
            case "22": return "BÔNUS (PRIVADOS)";
            case "26": return "APÓLICES / BÔNUS / TÍTULOS PÚBLICOS";
            case "32": return "EXERCÍCIO DE OPÇÕES DE COMPRA DE ÍNDICE";
            case "33": return "EXERCÍCIO DE OPÇÕES DE VENDA DE ÍNDICE";
            case "38": return "EXERCÍCIO DE OPÇÕES DE COMPRA";
            case "42": return "EXERCÍCIO DE OPÇÕES DE VENDA";
            case "46": return "LEILÃO DE TÍTULOS NÃO COTADOS";
            case "48": return "LEILÃO DE PRIVATIZAÇÃO";
            case "50": return "LEILÃO";
            case "51": return "LEILÃO FINOR";
            case "52": return "LEILÃO FINAM";
            case "53": return "LEILÃO FISET";
            case "54": return "LEILÃO DE AÇÕES EM MORA";
            case "56": return "VENDAS POR ALVARÁ JUDICIAL";
            case "58": return "OUTROS";
            case "60": return "PERMUTA POR AÇÕES";
            case "61": return "META";
            case "62": return "TERMO";
            case "66": return "DEBÊNTURES COM DATA DE VENCIMENTO ATÉ 3 ANOS";
            case "68": return "DEBÊNTURES COM DATA DE VENCIMENTO MAIOR QUE 3 ANOS";
            case "70": return "FUTURO COM MOVIMENTAÇÃO CONTÍNUA";
            case "71": return "FUTURO COM RETENÇÃO DE GANHO";
            case "74": return "OPÇÕES DE COMPRA DE ÍNDICES";
            case "75": return "OPÇÕES DE VENDA DE ÍNDICES";
            case "78": return "OPÇÕES DE COMPRA";
            case "82": return "OPÇÕES DE VENDA";
            case "83": return "DEBÊNTURES E NOTAS PROMISSÓRIAS";
            case "96": return "FRACIONÁRIO";
            case "99": return "TOTAL GERAL";        
        }
        return null;
    }
    
    public String codigoPapel(String linha) {
        return linha.substring(12, 24).trim();
    }
    
    public String codigoMercado(String linha) {
        return linha.substring(24, 27).trim();
    }
    
    public String descricaoCodigoMercado(String codigo) {
        switch(codigo) {
            case "010": return "VISTA";
            case "012": return "EXERCÍCIO DE OPÇÕES DE COMPRA";
            case "013": return "EXERCÍCIO DE OPÇÕES DE VENDA";
            case "017": return "LEILÃO";
            case "020": return "FRACIONÁRIO";
            case "030": return "TERMO";
            case "050": return "FUTURO COM RETENÇÃO DE GANHO";
            case "060": return "FUTURO COM MOVIMENTAÇÃO CONTÍNUA";
            case "070": return "OPÇÕES DE COMPRA";
            case "080": return "OPÇÕES DE VENDA";
        }
        return null;
    }
    
    public String nomeResumidoPapel(String linha) {
        return linha.substring(27, 39).trim();
    }
    
    public String especificacaoPapel(String linha) {
        return linha.substring(39, 49).trim();
    }
      
    public double precoAbertura(String linha) {
        return Double.valueOf(linha.substring(56, 69)) / 100;
    }
    
    public double precoMaximo(String linha) {
        return Double.valueOf(linha.substring(69, 82)) / 100;
    }
    
    public double precoMinimo(String linha) {        
        return Double.valueOf(linha.substring(82, 95)) / 100;        
    }
    
    public double precoMedio(String linha) {
        return Double.valueOf(linha.substring(95, 108)) / 100;        
    }
    
    public double precoFechamentoPregao(String linha) {        
        return Double.valueOf(linha.substring(108, 121)) / 100;        
    }
    
    public double precoMelhorOfertaCompra(String linha) {        
        return Double.valueOf(linha.substring(121, 134)) / 100;        
    }
    
    public double precoMelhorOfertaVenda(String linha) {        
        return Double.valueOf(linha.substring(121, 134)) / 100;        
    }
    
    public String totalNegociacoesPregao(String linha) {
        return linha.substring(147, 152);
    }
    
    public String totalNegociacoesMercado(String linha) {
        return linha.substring(152, 170);
    }
    
    public long volume(String linha) {
        return Long.parseLong(linha.substring(170, 188));
    }
     
    
    
}

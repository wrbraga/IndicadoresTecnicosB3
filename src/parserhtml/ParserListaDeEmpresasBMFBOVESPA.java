package parserhtml;

import java.io.IOException;
import static java.lang.System.out;
import org.jsoup.Jsoup;
import static org.jsoup.Jsoup.connect;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParserListaDeEmpresasBMFBOVESPA {
    private String codigo;
    private String acao;
    private String tipo;
    
    private final String URL;
    private Document HTML;
    
    // %d = 104, 106
    private final String idCodigo="ctl00_contentPlaceHolderConteudo_grdResumoCarteiraTeorica_ctl00_ct%d_lblCodigo";
    private final String idAcao="ctl00_contentPlaceHolderConteudo_grdResumoCarteiraTeorica_ctl00_ct%d_lblAcao";
    private final String idTipo="ctl00_contentPlaceHolderConteudo_grdResumoCarteiraTeorica_ctl00_ct%d_lblTipo";
    
    private String[][] lista;
    
    public ParserListaDeEmpresasBMFBOVESPA() {
        URL = "http://www.bmfbovespa.com.br/indices/ResumoCarteiraTeorica.aspx?Indice=Ibovespa&idioma=pt-br";
    }
        
    public void lerConteudo()  {
        try {
            HTML = connect(URL).get();
        } catch (IOException IOex) {
            IOex.printStackTrace();
        } 
        
        Elements conteudo = null;
        out.println(HTML);
        
        conteudo = HTML.select("span[id*=ctl00_contentPlaceHolderConteudo_grdResumoCarteiraTeorica_ctl00_]");        
        lista = new String[conteudo.size()/5][3];
                  
        String tmp1 = "", tmp2 = "", tmp3 = "";
        int cont = 0;           

        for(Element e: conteudo) {            
            codigo = e.select("span[id*=lblCodigo]").text();
            if(!codigo.isEmpty() && !codigo.equals(tmp1)) {
                tmp1 = codigo;
                lista[cont][0] = codigo;                     
            }

            acao = e.select("span[id*=lblAcao]").text();
            if(!acao.isEmpty() && !acao.equals(tmp2)) {
                tmp2 = acao;
                lista[cont][1] = acao;
            }

            tipo = e.select("span[id*=lblTipo]").text();
            if(!tipo.isEmpty() && !tipo.equals(tmp3)) {
                tmp3 = tipo;
                lista[cont][2] = tipo;
                cont++; 
            }            
        }
        
    }
    
    public String[][] getListaEmpresas() {
        return lista;
    }
}

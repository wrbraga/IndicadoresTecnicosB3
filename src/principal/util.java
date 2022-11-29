package principal;

import DB.CotacaoHistorica;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class util {
    private static BigDecimal bd;
    
    public static Double arredonda(Double valor, int casas) {
        bd = new BigDecimal(valor);
        return bd.setScale(casas, RoundingMode.HALF_UP).doubleValue();
    }
    
    public static Double arredonda(Double valor) {
        DecimalFormat df = new DecimalFormat(",##0.##");
        return Double.parseDouble(df.format(valor));
    }
    
    /*
    AO = SMA (PREÇO MÉDIO, 5 períodos) - SMA (PREÇO MÉDIO, 34 períodos)

    Em que:

    SMA – Média móvel simples
    PREÇO MÉDIO = (ALTO+BAIXO)/2 (representado através dos pontos centrais das barras)
    */
    public double AwesomeOscillator(List cotacao, int tempo) {
        return 0.0;
    }
    
    public double MediaMovelSimples(List cotacao, int tempo, double ultimo) {
        double soma = 0.0;
        double media = 0.0;

        for(Object c : cotacao) {
            CotacaoHistorica valores = (CotacaoHistorica) c;                                  
            soma += valores.getValor_fechamento();                        
            //System.out.println("\t" +Calendario.dateToString(valores.getData()) + "\t" + valores.getValor_fechamento());
        }
        
        if (ultimo != 0.0) {
            soma += ultimo;            
        } else {
            tempo--;
        }
        media = soma / tempo;
        //System.out.println("\tMédia:" + media);
        return media;
    }
    
    public double MediaMovelExponencial(List cotacao, int tempo) {
        double soma = 0.0;
        double media = 0.0;
        Double k = (2.0 / (tempo + 1.0));
        int conta = 0;
        
        for(Object c : cotacao) {
            CotacaoHistorica valores = (CotacaoHistorica) c;                                  
                        
            if (conta == tempo) {
                media = media / tempo;
            }
            
            if(conta < tempo) {
                media += valores.getValor_fechamento();                                                                        
            } 
            
            if(conta > tempo) {
                media = ((valores.getValor_fechamento() - media) * k + media);                                        

                
            }
            
            conta++;
        }
       
        return media;
    }

    public static int IFR(List cotacao, int periodo) {
        int conta = 0;
        double alta = 0.0;
        double baixa = 0.0;
        double ultimo = 0.0;
        double atual;        
        double diferenca = 0.0;
        double mediaGanho = 0.0;
        double mediaPerda = 0.0;
        double FR = 0.0;
        int IFR = 0;
        
        
        for(Object c : cotacao) {
            CotacaoHistorica valores = (CotacaoHistorica) c;            
            
            if(conta == 0) {
                ultimo = valores.getValor_fechamento();
                conta++;                
                continue;
            }
            
            atual = valores.getValor_fechamento();
            
            if (conta > 1) {
                diferenca = atual - ultimo;

                if (diferenca > 0) {
                    alta = Math.abs(diferenca);                   
                    baixa = 0;
                } else {
                    baixa = Math.abs(diferenca);                      
                    alta = 0;
                }
                
                if(conta <= periodo) {
                    mediaGanho += alta;
                    mediaPerda += baixa;
                }
                
                if(conta > periodo) {                                                
                    mediaGanho = ((mediaGanho * (periodo - 1) + alta) / periodo);
                    mediaPerda = ((mediaPerda * (periodo - 1) + baixa) / periodo);    
                    
                    FR = mediaGanho / mediaPerda;
                    IFR = (int)(100-(100/(1+FR)));     
                }
                
                if(conta == periodo) {                                                
                    mediaGanho = mediaGanho / (periodo - 1);
                    mediaPerda = mediaPerda / (periodo - 1);        
                }

                
            }            
            
            ultimo = atual;                                                                     
            
            conta++;
        }
                        
        return IFR;
    }
    
    
}

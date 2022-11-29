package DB;

public class CodigoDB {
    private int id_codigo;
    private String codigo;
    private String descricao;
    private String setor;
     
    public CodigoDB(int id, String codigo, String descricao, String setor) {
        this.id_codigo = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.setor = setor;
    }
    
    public int getIdCodigo() {
        return id_codigo;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setIdCodigo(int id) {
        this.id_codigo = id;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }    
}

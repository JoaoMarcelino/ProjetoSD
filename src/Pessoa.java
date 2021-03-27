import java.util.*;

public class Pessoa {

    private String nome;
    private String password;
    private Departamento departamento;
    private String telefone;
    private String morada;
    private String numberCC;
    private Date expireCCDate;
    private Profissao profissao;

    public Pessoa(String nome, String password, Departamento departamento, String telefone, String morada, String numberCC, Date expireCCDate, Profissao profissao) {
        this.nome = nome;
        this.password = password;
        this.departamento = departamento;
        this.telefone = telefone;
        this.morada = morada;
        this.numberCC = numberCC;
        this.expireCCDate = expireCCDate;
        this.profissao = profissao;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public void setNumberCC(String numberCC) {
        this.numberCC = numberCC;
    }

    public void setExpireCCDate(Date expireCCDate) {
        this.expireCCDate = expireCCDate;
    }

    public void setProfissao(Profissao profissao) {
        this.profissao = profissao;
    }

    public String getNome() {
        return nome;
    }

    public String getPassword() {
        return password;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getMorada() {
        return morada;
    }

    public String getNumberCC() {
        return numberCC;
    }

    public Date getExpireCCDate() {
        return expireCCDate;
    }

    public Profissao getProfissao() {
        return profissao;
    }
}

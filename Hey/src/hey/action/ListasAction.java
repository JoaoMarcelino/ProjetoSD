/**
 * Raul Barbosa 2014-11-07
 */
package hey.action;

import com.company.*;
import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListasAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String titulo;
    private String nome;
    private List<String> membros = new ArrayList<String>(Arrays.asList(new String[20]));
    private int nMembros;
    private List<String> profs;
    private String yourProf = "";


    public String post() {
        Profissao profissao = getYourProf();
        ArrayList<Pessoa> pessoas = getYourMembros();

        if (nome != null && profissao != null && !pessoas.isEmpty()) {
            String status = getHeyBean().addLista(titulo, nome, new CopyOnWriteArrayList<>(pessoas), profissao);
            addFieldError("listas",status);
        }
        else {
            addFieldError("listas","Falta informacao para a adição da lista.");
        }
        return SUCCESS;
    }

    public String delete() {
        String status = getHeyBean().removeLista(titulo, nome);
        addFieldError("listas",status);
        return SUCCESS;
    }

    public String get() {
        return SUCCESS;
    }

    public ArrayList<Lista> getListas() {
        ArrayList<Lista> aux=getHeyBean().listListas(titulo);
        if(aux==null)
            addFieldError("listas","Erro RMI na listagem de listas ou eleição não existe.");
        return aux;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public ArrayList<Pessoa> getYourMembros() {
        ArrayList<Pessoa> aux = new ArrayList<>();
        int i = 0;
        while (membros.get(i) != null) {
            Pessoa p = new Pessoa(membros.get(i), "", Departamento.DA, "", "", "", new GregorianCalendar(), Profissao.Estudante);
            aux.add(p);
            i++;
        }
        return aux;
    }

    public Profissao getYourProf() {
        Profissao aux = null;
        switch (this.yourProf) {
            case "Estudante":
                aux = Profissao.Estudante;
                break;
            case "Docente":
                aux = Profissao.Docente;
                break;
            case "Funcionario":
                aux = Profissao.Funcionario;
                break;
        }
        return aux;
    }

    public void setYourProf(String yourProf) {
        this.yourProf = yourProf;
    }

    public List<String> getProfs() {
        List<String> aux = new ArrayList<>();
        for (Profissao prof : Profissao.values()) {
            aux.add(prof.name());
        }
        return aux;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getMembros() {
        return membros;
    }

    public void setMembros(List<String> membros) {
        this.membros = membros;
    }

    public HeyBean getHeyBean() {
        if (!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}

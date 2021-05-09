/**
 * Raul Barbosa 2014-11-07
 */
package hey.action;

import com.company.Eleicao;
import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VotarAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String titulo;
    private String nome;
    private String numero;
    private String pass;
    private String myChoice;
    private List<String> choices;

    public String view() {
        return SUCCESS;
    }

    public String votarAntecipado() {
        String status = getHeyBean().votarAntecipado(numero, pass, titulo, getMyChoice());
        getHeyBean().setMessage(status);
        return SUCCESS;
    }

    public String getMyChoice() {
        if (myChoice == "Lista") {
            return nome;
        }
        else {
            return myChoice;
        }
    }

    public void setMyChoice(String myChoice) {
        this.myChoice = myChoice;
    }

    public List<String> getChoices() {
        List<String> aux = new ArrayList<>();
        aux.add("Lista");
        aux.add("Nulo");
        aux.add("Branco");
        return aux;
    }

    public ArrayList<Eleicao> getEleicoesDisponiveis() {
        ArrayList<Eleicao> aux = getHeyBean().listEleicoes();

        ArrayList<Eleicao> disponiveis = new ArrayList<>();
        for (Eleicao ele : aux) {
            if (!ele.checkStart()) {
                disponiveis.add(ele);
            }
        }
        return disponiveis;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

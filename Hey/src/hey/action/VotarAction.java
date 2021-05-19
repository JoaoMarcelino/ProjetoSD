/**
 * Raul Barbosa 2014-11-07
 */
package hey.action;

import com.company.Eleicao;
import com.company.Lista;
import com.company.Pessoa;
import com.company.Voto;
import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.ArrayList;
import java.util.GregorianCalendar;
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

    private String myElection;
    private List<String> eleicoes;

    private boolean votarAntecipadamente = false;


    public String view() {
        if(getHeyBean().getUsername()==null){
            this.session.remove("heyBean");
            return ERROR;
        }
        return SUCCESS;
    }


    public String votar() {
        if(getHeyBean().getUsername()==null){
            this.session.remove("heyBean");
            return ERROR;
        }
        String status = "";
        if(getHeyBean().getUsername()!="Admin"){
            if (votarAntecipadamente)
                status = getHeyBean().votarAntecipado(getHeyBean().getUsername(), getHeyBean().getPassword(), getMyElection(), getMyChoice());
            else
                status = getHeyBean().votar(getHeyBean().getUsername(), getHeyBean().getPassword(), getMyElection(), getMyChoice());
        }
        else
            status="Faça login com uma conta normal. A usar conta Admin de momento.";


        addFieldError("votar", status);
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

    public List<String> getChoices(String titulo) {
        List<String> aux = new ArrayList<>();
        Eleicao eleicao = getHeyBean().getEleicaoByTitulo(titulo);
        if (eleicao != null) {
            List<Lista> listas = eleicao.getListas();

            for (Lista lst : listas) {
                aux.add(lst.getNome());
            }
        }

        aux.add("Nulo");
        aux.add("Branco");
        return aux;
    }


    public String getMyElection() {
        return myElection;
    }

    public void setMyElection(String myElection) {
        this.myElection = myElection;
    }

    public List<String> getEleicoes() {
        ArrayList<String> aux = new ArrayList<>();
        ArrayList<Eleicao> eleicoes = getHeyBean().listEleicoes();
        for (Eleicao ele : eleicoes) {
            aux.add(ele.getTitulo());

        }
        return aux;
    }

    public ArrayList<Eleicao> getEleicoesInfo() {
        ArrayList<Eleicao> eleicoes = getHeyBean().listEleicoes();
        return eleicoes;
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

    public boolean isVotarAntecipadamente() {
        return votarAntecipadamente;
    }

    public void setVotarAntecipadamente(boolean votarAntecipadamente) {
        this.votarAntecipadamente = votarAntecipadamente;
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

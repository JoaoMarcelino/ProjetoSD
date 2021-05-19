/**
 * Raul Barbosa 2014-11-07
 */
package hey.action;

import com.company.Departamento;
import com.company.Pessoa;
import com.company.Profissao;
import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PessoasAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String nome;
    private String password;
    private String telefone;
    private String morada;
    private String numberCC;
    private GregorianCalendar expireCCDate;

    private boolean admin;
    private String facebookId;

    private String loginNumberCC;
    private String loginPassword;

    private List<String> profs;
    private String yourProf = "";
    private List<String> deps;
    private String yourDep = "";


    public String login() {
        if (loginNumberCC.equals("admin") && loginPassword.equals("admin")) {
            getHeyBean().setUsername("Admin");
            getHeyBean().setLoggedInAsAdmin(true);
            logout();
            return SUCCESS;
        }
        else {
            String status = getHeyBean().login(loginNumberCC, loginPassword);
            if (status.equals("true | Login com Sucesso")) {
                getHeyBean().setUsername(loginNumberCC);
                getHeyBean().setPassword(loginPassword);
                Pessoa p = getHeyBean().getPessoaByCC(loginNumberCC);
                System.out.println(p.getAccessToken());
                if(p.getAccessToken()!=null)
                    getHeyBean().getFb().accessToken=p.getAccessToken();
                logout();
                getHeyBean().setLoggedInAsAdmin(p != null && p.isAdmin());
                return SUCCESS;
            }
            else {
                logout();
                return ERROR;
            }
        }
    }

    public void logout() {
        loginNumberCC = null;
        loginPassword = null;
    }

    public String logoutNotify() {
        getHeyBean().logout(getHeyBean().getUsername(), getHeyBean().getPassword());
        this.session.remove("heyBean");
        return SUCCESS;
    }

    public String post() {
        if(getHeyBean().getUsername()==null){
            this.session.remove("heyBean");
            return ERROR;
        }
        Profissao profissao = getYourProf();
        Departamento departamento = getYourDep();
        if (nome != null && password != null && numberCC != null && expireCCDate != null && profissao != null && departamento != null) {
            String status = getHeyBean().addPessoa(nome, password, departamento, telefone, morada, numberCC, expireCCDate, profissao, admin);
            addFieldError("pessoas", status);
        }
        else {
            addFieldError("pessoas", "Falta informacao para o registo do votante.");
        }

        return SUCCESS;
    }

    public String delete() {
        if(getHeyBean().getUsername()==null){
            this.session.remove("heyBean");
            return ERROR;
        }
        if (numberCC != null) {
            String status = getHeyBean().removeFacebookId(numberCC);
            addFieldError("removeId", status);
        }
        else {
            addFieldError("removeId", "Falta informacao para a disossiação do facebookId.");
        }
        return SUCCESS;
    }

    public ArrayList<Pessoa> getListPessoas() {
        ArrayList<Pessoa> aux = getHeyBean().listPessoas();

        if (aux == null)
            addFieldError("pessoas", "Erro RMI na listagem de listas ou eleição não existe.");
        return aux;
    }

    public String get() {
        if(getHeyBean().getUsername()==null){
            this.session.remove("heyBean");
            return ERROR;
        }
        return SUCCESS;
    }

    public String auxiliar() {
        if(getHeyBean().getUsername()==null){
            this.session.remove("heyBean");
            return ERROR;
        }
        return SUCCESS;
    }

    public void setUsername(String username) {
        getHeyBean().setUsername(username); // will you sanitize this input? maybe use a prepared statement?
    }

    public void setPass(String password) {
        getHeyBean().setPassword(password);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getNumberCC() {
        return numberCC;
    }

    public void setNumberCC(String numberCC) {
        this.numberCC = numberCC;
    }

    public GregorianCalendar getExpireCCDate() {
        return expireCCDate;
    }

    public void setExpireCCDate(String expireCCDate) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = df.parse(expireCCDate);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        this.expireCCDate = cal;
    }

    public Profissao getYourProf() {
        try {
            Profissao aux = Profissao.valueOf(this.yourProf);
            return aux;
        } catch (IllegalArgumentException e) {
            return null;
        }
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

    public Departamento getYourDep() {
        try {
            Departamento aux = Departamento.valueOf(this.yourDep);
            return aux;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void setYourDep(String yourDep) {
        this.yourDep = yourDep;
    }

    public List<String> getDeps() {
        List<String> aux = new ArrayList<>();
        for (Departamento dep : Departamento.values()) {
            aux.add(dep.name());
        }
        return aux;
    }

    public String getLoginNumberCC() {
        return loginNumberCC;
    }

    public void setLoginNumberCC(String loginNumberCC) {
        this.loginNumberCC = loginNumberCC;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
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

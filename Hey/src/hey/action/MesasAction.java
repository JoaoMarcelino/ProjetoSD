package hey.action;

import com.company.*;
import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MesasAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String ip;
    private String port;
    private List<String> membros = Arrays.asList("Membro1", "Membro2", "Membro3");
    private List<String> deps;
    private String yourDep = "";


    public String get() {
        return SUCCESS;
    }

    public String post() {
        Departamento departamento = getYourDep();
        CopyOnWriteArrayList<Pessoa> aux = createPessoasFromString(membros);

        if (ip != null && port != null && departamento != null && aux.size() == 3) {
            String status = getHeyBean().addMesa(departamento, aux, ip, port);
            getHeyBean().setMessage(status);
        }
        else {
            getHeyBean().setMessage("Falta informação para o registo da mesa.");
        }
        return SUCCESS;
    }

    public String delete() {
        String status = getHeyBean().removeMesa(yourDep);
        getHeyBean().setMessage(status);
        return SUCCESS;
    }

    public String put() {
        Departamento departamento = getYourDep();
        CopyOnWriteArrayList<Pessoa> aux = createPessoasFromString(membros);

        if (departamento != null && aux.size() == 3) {
            String status = getHeyBean().editMesa(departamento.name(), aux.get(0).getNome(), aux.get(1).getNome(), aux.get(2).getNome());
            getHeyBean().setMessage(status);
        }
        else {
            getHeyBean().setMessage("Falta informacao para a edição da mesa.");
        }
        return SUCCESS;
    }

    public ArrayList<MesaVoto> getListMesas(){
        ArrayList<MesaVoto> aux = getHeyBean().listMesas();
        return aux;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<String> getMembros() {
        return membros;
    }

    public void setMembros(List<String> membros) {
        this.membros = membros;
    }

    public Departamento getYourDep() {
        try {
            return Departamento.valueOf(this.yourDep);
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

    public CopyOnWriteArrayList<Pessoa> createPessoasFromString(List<String> membros) {
        CopyOnWriteArrayList<Pessoa> aux = new CopyOnWriteArrayList<>();
        for (String m : membros) {
            Pessoa p = new Pessoa(m, "", Departamento.DA, "", "", "", new GregorianCalendar(), Profissao.Estudante);
            aux.add(p);
        }
        return aux;
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

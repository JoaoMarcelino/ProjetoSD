/**
 * Raul Barbosa 2014-11-07
 */
package hey.action;

import com.company.Resultado;
import com.company.Voto;
import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class ResultadosAction extends ActionSupport implements SessionAware {
    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String titulo;
    private Resultado res;
    private Voto v;
    private String numeroCC;
    private String mesa;

    public String get() {
        return SUCCESS;
    }

    public Resultado getResultados() {
        res = getHeyBean().getResultados(this.titulo);
        if (res == null)
            getHeyBean().setMessage("Erro RMI na consulta dos resultados.");
        return res;
    }

    public String getVoto() {
        v = getHeyBean().getVoto(this.numeroCC, this.titulo);
        if (v == null)
            getHeyBean().setMessage("Erro RMI na consulta de voto ou pessoa não existe.");
        return SUCCESS;
    }

    public String getMesa() {
        if (v.getMesa() == null)
            return "Web/Admin Console";
        else
            return v.getMesa().getDepartamento().name();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Voto getV() {
        return this.v;
    }

    public void setV(Voto v) {
        this.v = v;
    }

    public String getNumeroCC() {
        return numeroCC;
    }

    public void setNumeroCC(String numeroCC) {
        this.numeroCC = numeroCC;
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

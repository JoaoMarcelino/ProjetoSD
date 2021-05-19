package hey.action;

import com.company.Pessoa;
import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

public class FacebookLoginAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String code;
    private String state;


    public String login() {
        HeyBean bean = getHeyBean();
        bean.getFb().setAccessToken(code);
        bean.getFb().setSecretState(state);

        Pessoa p = bean.loginByFacebookId();
        if (p != null) {
            bean.setUsername(p.getNumberCC());
            bean.setPassword(p.getPassword());
            bean.setLoggedInAsAdmin(p.isAdmin());
            bean.login(bean.getUsername(), bean.getPassword());
            return SUCCESS;
        }
        else{
            this.session.remove("heyBean");
            addFieldError("Loginfb", "Erro no login de Facebook.");
            return ERROR;
        }
    }

    public String associate(){
        HeyBean bean = getHeyBean();
        bean.getFb().setAccessTokenAssociation(code);
        bean.getFb().setSecretState(state);
        bean.associateFacebookAccount();
        return SUCCESS;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Map<String, Object> getSession() {
        return session;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public HeyBean getHeyBean() {
        if (!session.containsKey("heyBean"))
            this.setHeyBean(new HeyBean());
        return (HeyBean) session.get("heyBean");
    }

    public void setHeyBean(HeyBean heyBean) {
        this.session.put("heyBean", heyBean);
    }

}

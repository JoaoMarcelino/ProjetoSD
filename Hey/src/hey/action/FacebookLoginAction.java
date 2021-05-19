package hey.action;

import hey.model.FacebookBean;
import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;

public class FacebookLoginAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 4L;
    private Map<String, Object> session;
    private String code;
    private String state;


    @Override
    public String execute(){
        HeyBean bean = getHeyBean();
        bean.setAuthCode(this.code);
        bean.setSecretState(this.state);
        if(bean.getAccessToken()){
            boolean newAssociation = bean.associateFacebookAccount();
            if(!newAssociation) {
                boolean login = bean.loginByFacebookId();
                if (!login) {
                    addFieldError("Loginfb", "Erro no login de Facebook.");
                    return ERROR;
                }
            }
            session.put("loggedin", true);
            return SUCCESS;
        }

        addFieldError("Loginfb","Erro no login de Facebook.");
        return ERROR;
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

/**
 * Raul Barbosa 2014-11-07
 */
package server.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import java.util.Map;
import server.model.ServerBean;

public class ServerAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 5590830L;
	private Map<String, Object> session;

	@Override
	public String execute() throws Exception {
		// you could execute some business logic here, but for now
		// the result is "success" and struts.xml knows what to do
		return SUCCESS;
	}

	public ServerBean getServerBean() {
		if(!session.containsKey("serverBean"))
			this.setServerBean(new ServerBean());
		return (ServerBean) session.get("serverBean");
	}

	public void setServerBean(ServerBean serverBean) {
		this.session.put("serverBean", serverBean);
	}

	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}

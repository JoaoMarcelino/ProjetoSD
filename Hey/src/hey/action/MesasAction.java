/**
 * Raul Barbosa 2014-11-07
 */
package hey.action;

import com.company.Departamento;
import com.company.MesaVoto;
import com.company.Pessoa;
import com.company.Profissao;
import com.opensymphony.xwork2.ActionSupport;
import hey.model.HeyBean;
import org.apache.struts2.interceptor.SessionAware;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MesasAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String ip;
	private String port;
	private List<String> membros= Arrays.asList("Membro1","Membro2","Membro3");
	private List<String> deps;
	private String yourDep="";


	public String get(){
		return SUCCESS;
	}

	public String post(){
		Departamento departamento=getYourDep();
		CopyOnWriteArrayList<Pessoa> aux=new CopyOnWriteArrayList<>();
		for(String m:membros){
			Pessoa p=new Pessoa(m,"",Departamento.DA,"","","",new GregorianCalendar(), Profissao.Estudante);
			aux.add(p);
		}
		try{
			if( ip!=null && port!=null && departamento!=null && aux.size()==3){
				String status = getHeyBean().servidor.addMesa(departamento,aux,ip,port);
				getHeyBean().setMessage(status);
			}
			else{
				getHeyBean().setMessage("Falta informacao para o registo da mesa.");
			}
		}catch (RemoteException ignored){
			getHeyBean().setMessage("Erro RMI no registo da mesa.");
		}
		return SUCCESS;
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

	public void setMembros(List<String> membros) {
		this.membros = membros;
	}

	public List<String> getMembros(){
		return membros;
	}

	public Departamento getYourDep() {
		try{
			Departamento aux= Departamento.valueOf(this.yourDep);
			return aux;
		}catch (IllegalArgumentException e){
			return null;
		}
	}

	public void setYourDep(String yourDep) {
		this.yourDep = yourDep;
	}

	public List<String> getDeps() {
		List<String> aux=new ArrayList<>();
		for(Departamento dep: Departamento.values()){
			aux.add(dep.name());
		}
		return aux;
	}

	public HeyBean getHeyBean() {
		if(!session.containsKey("heyBean"))
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

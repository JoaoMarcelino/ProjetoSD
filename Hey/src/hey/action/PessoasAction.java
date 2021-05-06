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

import java.rmi.RemoteException;
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



	public String post(){
		Profissao profissao=Profissao.valueOf(getHeyBean().getYourProf());
		Departamento departamento=Departamento.valueOf(getHeyBean().getYourDep());
		try{
			if(nome!=null && password!=null && numberCC!=null && expireCCDate!=null ){
				String status = getHeyBean().servidor.addPessoa(nome,password,departamento,telefone,morada,numberCC,expireCCDate,profissao);
				getHeyBean().setMessage(status);
			}
			else{
				getHeyBean().setMessage("Falta informacao para o registo do votante.");
			}
		}catch (RemoteException ignored){
			getHeyBean().setMessage("Erro RMI no registo do votante.");
		}catch (IllegalArgumentException e){
		getHeyBean().setMessage("Falta informacao para o registo do votante.");
	}
		return SUCCESS;
	}

	public String get(){
		return SUCCESS;
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
		this.expireCCDate = cal ;
	}



	public ArrayList<Pessoa> getAllUsers() throws RemoteException {
		return new ArrayList<>(getHeyBean().getAllUsers());
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

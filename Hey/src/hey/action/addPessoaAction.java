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

public class addPessoaAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String nome;
	private String password;
	private Departamento departamento;
	private String telefone;
	private String morada;
	private String numberCC;
	private GregorianCalendar expireCCDate;
	private Profissao profissao;



	@Override
	public String execute(){
		try{
			if(nome!=null && password!=null && departamento!=null && numberCC!=null && expireCCDate!=null && profissao!=null){

				String status = getHeyBean().servidor.addPessoa(nome,password,departamento,telefone,morada,numberCC,expireCCDate,profissao);
				System.out.println(status);
			}
			else{
				System.out.println("Falta informacao para o registo da pessoa.");
			}
		}catch (RemoteException ignored){
			System.out.println("Erro RMI no registo da pessoa.");
		}
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

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = Departamento.valueOf(departamento);
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

	public Profissao getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = Profissao.valueOf(profissao);
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

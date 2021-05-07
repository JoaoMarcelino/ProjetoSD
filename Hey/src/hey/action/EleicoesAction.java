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

import javax.naming.InitialContext;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class EleicoesAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 4L;
	private Map<String, Object> session;
	private String titulo;
	private String descricao;
	private GregorianCalendar dataInicio;
	private GregorianCalendar dataFim;

	private List<String> profs;
	private String yourProf="";

	public String post(){
		ArrayList<Profissao> profissoes=getYourProf();
		CopyOnWriteArrayList<Departamento> departamentosPermitidos = new CopyOnWriteArrayList<Departamento>(Arrays.asList(Departamento.values()));

		try{
			if(titulo!=null && descricao!=null && profissoes!=null && dataInicio!=null && dataFim!=null){
				String status = getHeyBean().servidor.addEleicao(titulo,descricao,dataInicio,dataFim,new CopyOnWriteArrayList<>(profissoes),departamentosPermitidos);
				getHeyBean().setMessage(status);
			}
			else{
				getHeyBean().setMessage("Falta informacao para a criacao da eleicao.");
			}
		}catch (RemoteException e){
			getHeyBean().setMessage("Erro RMI no registo da pessoa.");
		}catch (IllegalArgumentException e){
			getHeyBean().setMessage("Falta informacao para a criacao da eleicao.");
		}
		return SUCCESS;
	}

	public String get(){
		return SUCCESS;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public GregorianCalendar getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio)throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		Date date = df.parse(dataInicio);
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		this.dataInicio = cal;
	}

	public GregorianCalendar getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim)throws ParseException{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		Date date = df.parse(dataFim);
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		this.dataFim = cal;
	}

	public ArrayList<Profissao> getYourProf() {
		ArrayList<Profissao> aux=new ArrayList<>();
		switch (this.yourProf){
			case "Estudante":
				aux.add(Profissao.Estudante);
				break;
			case "Docente":
				aux.add(Profissao.Docente);
				break;
			case "Funcionario":
				aux.add(Profissao.Funcionario);
				break;
			case "Geral":
				aux.add(Profissao.Estudante);
				aux.add(Profissao.Docente);
				aux.add(Profissao.Funcionario);
				break;
		}
		if(aux.isEmpty())
			return null;
		else
			return	aux;
	}

	public void setYourProf(String yourProf) {
		this.yourProf = yourProf;
	}

	public List<String> getProfs() {
		List<String> aux=new ArrayList<>();
		for(Profissao prof: Profissao.values()){
			aux.add(prof.name());
		}
		aux.add("Geral");
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

import javax.xml.stream.events.ProcessingInstruction;
import java.net.*;
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.util.*;
import java.rmi.*;
import java.time.*;

public class AdminConsole extends Thread {
    public static void main(String[] args) {
        System.getProperties().put("java.security.policy", "policy.all");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String escolha = "";
        try {
            RMI_S_Interface servidor = (RMI_S_Interface) LocateRegistry.getRegistry(7000).lookup("ServidorRMI");
            while (true) {
                System.out.println(printMenu());
                System.out.print("Opcao: ");
                escolha = reader.readLine();

                switch (escolha) {
                    case "1.1":
                        addPessoa(reader,servidor);
                        break;
                    case "1.2":
                        editPessoa(reader,servidor);
                        break;
                    case "1.3":
                        listPessoas(reader,servidor);
                        break;
                    case "2.1":
                        addEleicao(reader,servidor);
                        break;
                    case "2.2":
                        editEleicao(reader,servidor);
                        break;
                    case "2.3":
                        getResultados(reader,servidor);
                        break;
                    case "2.4":
                        getVoto(reader,servidor);
                        break;
                    case "2.5":
                        votar(reader,servidor);
                        break;
                    case "2.6":
                        listEleicoes(reader,servidor);
                        break;
                    case "3.1":
                        addLista(reader,servidor);
                        break;
                    case "3.2":
                        removeLista(reader,servidor);
                        break;
                    case "3.3":
                        editLista(reader,servidor);
                        break;
                    case "3.4":
                        listListas(reader,servidor);
                        break;
                    case "4.1":
                        addMesa(reader,servidor);
                        break;
                    case "4.2":
                        removeMesa(reader,servidor);
                        break;
                    case "4.3":
                        editMesa(reader,servidor);
                        break;
                    case "5":
                        Exception e = new Exception("Consola encerrada.");
                        throw e;
                    default:
                        System.out.println("Escolha invalida.Tente 1.1, por exemplo.");
                        break;
                }
                System.out.println("Pressione Enter para continuar.");
                reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String printMenu() {
        String menu ="OPCOES DISPONIVEIS. DIGITE 1.1 por exemplo.\n"
                + "___________________________________________\n" +
                "1.Gerir Pessoas\n" +
                "   1.1.Registar Pessoa\n" +
                "   1.2.Alterar dados pessoais\n" +
                "   1.3.Listar Pessoas\n" +
                "2.Gerir Eleicao\n" +
                "   2.1.Criar Eleicao\n" +
                "   2.2.Alterar informacao de eleicao\n" +
                "   2.3.Consultar Resultados\n" +
                "   2.4.Consultar Informacao de Voto\n" +
                "   2.5.Voto Antecipado\n" +
                "   2.6.Listar Eleicoes\n"+
                "3.Gerir Candidatos\n" +
                "   3.1.Adicionar Lista\n" +
                "   3.2 Remover Lista\n" +
                "   3.3 Alterar Lista\n" +
                "   3.4.Listar Listas\n"+
                "4.Gerir Mesas de Voto\n" +
                "   4.1.Adicionar Mesa\n" +
                "   4.2.Remover mesa\n" +
                "   4.3.Alterar membros de mesa\n" +
                "5.Sair.\n";
        return menu;
    }

    public static void addPessoa(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        String nome;
        Profissao prof;
        String password;
        String  telefone, morada, cc;
        GregorianCalendar validadeCC;
        Departamento dep;

        System.out.println("Preencha todos os campos.");
        System.out.print("Nome:");
        nome = reader.readLine();
        System.out.print("Estudante(1), Docente(2) ou Funcionario(3)?:");
        String type = reader.readLine();
        switch (type){
            case "1":
                prof=Profissao.Estudante;
                break;
            case "2":
                prof=Profissao.Docente;
                break;
            case "3":
                prof=Profissao.Funcionario;
                break;
            default:
                prof=Profissao.Estudante;
        }
        System.out.print("Departamento?("+ Arrays.toString(Departamento.values()) +"):");
        String departamento = reader.readLine();
        dep=Departamento.valueOf(departamento);
        System.out.print("Telefone:");
        telefone = reader.readLine();
        System.out.print("Morada:");
        morada = reader.readLine();
        System.out.print("Numero Cartao Cidado:");
        cc = reader.readLine();
        validadeCC=new GregorianCalendar();

        System.out.print("Password:");
        password = reader.readLine();

        System.out.println("Dados Introduzidos:\n" + nome + "\n" + type + "\n" + departamento + "\n" + telefone + "\n" + morada + cc + "\n" + validadeCC + "\n" + password);
        String status=servidor.addPessoa(nome,password,dep,telefone,morada,cc,validadeCC,prof);
        System.out.println(status);
    }

    public static void editPessoa(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        // list pessoas
        String idPessoa;
        System.out.print("Pessoa a editar:");
        idPessoa = reader.readLine();
        // uni.removePessoa(idPessoa);
        // uni.addPessoa();
    }

    public static void listPessoas(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        ArrayList<Pessoa> pessoas=servidor.listPessoas();
        System.out.println("PESSOAS REGISTADAS:");
        for(Pessoa p:pessoas){
            System.out.println(p.getNome()+" "+p.getNumberCC()+ " "+p.getDepartamento()+" "+p.getProfissao());
        }
    }

    public static void addEleicao(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        String nome;
        String descricao;
        String hora, dia, mes, ano;
        ArrayList<Profissao> profs=new ArrayList<Profissao>();
        ArrayList<Departamento> deps=new ArrayList<Departamento>(Arrays.asList(Departamento.values()));
        System.out.println("Preencha todos os campos.");
        System.out.print("Nome:");
        nome = reader.readLine();
        System.out.print("Descricao:");
        descricao = reader.readLine();
        System.out.print("Eleicao de Estudantes(1), Docentes(2), Funcionarios(3) ou Geral(4)?:");
        String type = reader.readLine();
        switch (type){
            case "1":
                profs.add(Profissao.Estudante);
                break;
            case "2":
                profs.add(Profissao.Docente);
                break;
            case "3":
                profs.add(Profissao.Funcionario);
                break;
            case "4":
                profs.add(Profissao.Estudante);
                profs.add(Profissao.Docente);
                profs.add(Profissao.Funcionario);
                break;
            default:
                profs.add(Profissao.Estudante);
                break;
        }

        System.out.print("Data de Inicio:\nAno:");
        ano = reader.readLine();
        System.out.print("Mes:");
        mes = reader.readLine();
        System.out.print("Dia:");
        dia = reader.readLine();
        System.out.print("Hora:");
        hora = reader.readLine();

        GregorianCalendar dataInicio=new GregorianCalendar(Integer.parseInt(ano), Integer.parseInt(mes), Integer.parseInt(dia), Integer.parseInt(hora), 0, 0);

        System.out.print("Data de Termino:\nAno:");
        ano = reader.readLine();
        System.out.print("Mes:");
        mes = reader.readLine();
        System.out.print("Dia:");
        dia = reader.readLine();
        System.out.print("Hora:");
        hora = reader.readLine();

        GregorianCalendar dataFim=new GregorianCalendar(Integer.parseInt(ano), Integer.parseInt(mes), Integer.parseInt(dia), Integer.parseInt(hora), 0, 0);

        String status=servidor.addEleicao(nome,descricao,dataInicio,dataFim,profs,deps);
        System.out.println(status);

    }

    public static void editEleicao(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        // list eleicoes
        String idEleicao;
        System.out.print("Eleicao a editar:");
        idEleicao = reader.readLine();
        // uni.removeEleicao(idEleicao);
        // uni.addEleicao(idEleicao);
    }

    public static void getResultados(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        // list eleicoes
        System.out.print("Nome da eleicao:");
        String idEleicao = reader.readLine();
        String resultados = "";
        // resultados=uni.getResultados(idEleicao);
        System.out.println(resultados);
    }

    public static void getVoto(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        // list eleicoes
        // list pessoas
        System.out.print("Nome da eleicao:");
        String idEleicao = reader.readLine();
        System.out.print("Nome da pessoa:");
        String idNome = reader.readLine();
        String resultados = "";
        // resultados=uni.getResultados(idEleicao);
        System.out.println(resultados);
    }

    public static void votar(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        // list eleicoes
        // list listas
        String nome, password, idEleicao, idLista;
        System.out.print("Nome:");
        nome = reader.readLine();
        System.out.print("Password:");
        password = reader.readLine();
        System.out.print("Eleicao:");
        idEleicao = reader.readLine();
        System.out.print("Lista a votar:");
        idLista = reader.readLine();

        // uni.vote(nome,password,eleicao,lista,"antecipado");
        System.out.println("Acao bem sucedida");
    }

    public static void listEleicoes(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        ArrayList<Eleicao> eleicoes=servidor.listEleicoes();
        System.out.println("ELEICOES REGISTADAS:");
        for(Eleicao ele:eleicoes){
            System.out.println(ele.getTitulo()+" "+ele.getDescricao()+" "+ele.getProfissoesPermitidas() +" "+printGregorianCalendar(ele.getDataInicio())+" "+printGregorianCalendar(ele.getDataFim()));
        }
    }

    public static void addLista(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        ArrayList<String> lista = new ArrayList<String>();
        String idEleicao, nome, aux;
        // list Eleicoes
        System.out.print("Eleicao:");
        idEleicao = reader.readLine();
        System.out.print("Nome:");
        nome = reader.readLine();
        int i = 1;
        System.out.print("Nome Elemento #" + i + ":");
        aux = reader.readLine();
        while (aux != "") {
            lista.add(aux);
            i++;
            System.out.print("Nome Elemento #" + i + ":");
            aux = reader.readLine();
        }
        // uni.addLista(idEleicao,nome,lista);
        System.out.println("Acao bem sucedida");

    }

    public static void removeLista(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        // list Eleicoes
        String idEleicao, idLista;
        System.out.print("Eleicao:");
        idEleicao = reader.readLine();
        // lista listas
        System.out.print("Lista:");
        idLista = reader.readLine();
        // uni.removeLista(idEleicao,idLista);

    }

    public static void editLista(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        // list Eleicoes
        String idEleicao, idLista;
        System.out.print("Eleicao:");
        idEleicao = reader.readLine();
        // lista listas
        System.out.print("Lista:");
        idLista = reader.readLine();
        // uni.removeLista(idEleicao,idLista);
        addLista(reader, servidor);

    }

    public static void listListas(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
    }

    public static void addMesa(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        String departamento;
        String membro1, membro2, membro3;

        System.out.println("Preencha todos os campos.");
        System.out.print("Departamento:");
        departamento = reader.readLine();
        System.out.print("Membro de mesa #1:");
        membro1 = reader.readLine();
        System.out.print("Membro de mesa #2:");
        membro2 = reader.readLine();
        System.out.print("Membro de mesa #3:");
        membro3 = reader.readLine();

        System.out.println(
                "Dados Introduzidos:\n" + departamento + "\n" + membro1 + "\n" + membro2 + "\n" + membro3 + "\n");
        // uni.addMesa(...);
        System.out.println("Acao bem sucedida");
    }

    public static void removeMesa(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        // list mesas
        String idMesa;
        System.out.print("Mesa:");
        idMesa = reader.readLine();
        // uni.removeMesa(idmesa);
    }

    public static void editMesa(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        // list mesas
        String idMesa;
        System.out.print("Mesa:");
        idMesa = reader.readLine();
        // uni.removeLista(idMesa);
        addMesa(reader, servidor);
    }

    public static String printGregorianCalendar(GregorianCalendar data){
        int hora=data.get(Calendar.HOUR);
        int dia=data.get(Calendar.DATE);
        int mes=data.get(Calendar.MONTH);
        int ano=data.get(Calendar.YEAR);

        return hora+"h "+dia+" "+mes+" "+ano;

    }
}
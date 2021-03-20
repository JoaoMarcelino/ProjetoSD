import java.net.*;
import java.io.*;
import java.util.*;
import java.time.*;

public class AdminConsole extends Thread {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String escolha="";
        while(true){
            System.out.println(printMenu());
            try{
                System.out.print("Opcao: ");
                escolha=reader.readLine();
                
                switch (escolha) {
                    case "1.1":
                        addPessoa(reader);                        
                        break;
                    case "1.2":
                        editPessoa(reader);
                        break;
                    case "2.1":
                        addEleicao(reader);
                        break;
                    case "2.2":
                        editEleicao(reader);
                        break;
                    case "2.3":
                        getResultados(reader);
                        break;
                    case "2.4":
                        getVoto(reader);
                        break;
                    case "2.5":
                        votar(reader);
                        break;
                    case "3.1":
                        addLista(reader);
                        break;
                    case "3.2":
                        removeLista(reader);
                        break;
                    case "3.3":
                        editLista(reader);
                        break;
                    case "4.1":
                        addMesa(reader);
                        break;
                    case "4.2":
                        removeMesa(reader);
                        break;
                    case "4.3":
                        editMesa(reader);
                        break;
                    default:
                        System.out.println("Escolha invalida.Tente 1.1, por exemplo.");
                        break;
                }
                System.out.println("Pressione Enter para continuar.");
                reader.readLine();

            }catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                System.out.println("Nao foi possivel terminar a acao desejada devido a um erro");
            }
            
        }
    }

    public static String printMenu() {
        String menu = "" +
                "OPCOES DISPONIVEIS. DIGITE 1.1 por exemplo.\n" +
                "___________________________________________\n" +
                "1.Gerir Pessoas\n" +
                "   1.1.Registar Pessoa\n" +
                "   1.2.Alterar dados pessoais\n" +
                "2.Gerir Eleicao\n" +
                "   2.1.Criar Eleicao\n" +
                "   2.2.Alterar informacao de eleicao\n" +
                "   2.3.Consultar Resultados\n" +
                "   2.4.Consultar Informacao de Voto\n" +
                "   2.5.Voto Antecipado\n" +
                "3.Gerir Candidatos\n" +
                "   3.1.Adicionar Lista\n" +
                "   3.2 Remover Lista\n" +
                "   3.3 Alterar Lista\n" +
                "4.Gerir Mesas de Voto\n" +
                "   4.1.Adicionar Mesa\n" +
                "   4.2.Remover mesa\n" +
                "   4.3.Alterar membros de mesa\n";
        return menu;
    }

    public static void addPessoa(BufferedReader reader) throws Exception{
        String nome;
        String type;
        String password;
        String departamento,telefone,morada,cc,validadeCC;

        System.out.println("Preencha todos os campos.");
        System.out.print("Nome:");
        nome=reader.readLine();
        System.out.print("Estudante(1), Docente(2) ou Funcionario(3)?:");
        type=reader.readLine();
        System.out.print("Departamento:");
        departamento=reader.readLine();
        System.out.print("Telefone:");
        telefone=reader.readLine();
        System.out.print("Morada:");
        morada=reader.readLine();
        System.out.print("Numero Cartao Cidado:");
        cc=reader.readLine();
        System.out.print("Validade Cartao Cidado:");
        validadeCC=reader.readLine();
        System.out.print("Password:");
        password=reader.readLine();

        System.out.println( "Dados Introduzidos:\n"+nome+"\n"+type+"\n"+departamento+"\n"+telefone+"\n"+morada+cc+"\n"+validadeCC+"\n"+password);
        //uni.addPesoa(...);
        System.out.println("Acao bem sucedida");
    }
    public static void editPessoa(BufferedReader reader) throws Exception{
        //list pessoas
        String idPessoa;
        System.out.print("Pessoa a editar:");
        idPessoa=reader.readLine();
        //uni.removePessoa(idPessoa);
        //uni.addPessoa();
    }
    public static void addEleicao(BufferedReader reader) throws Exception{
        String nome;
        String descricao;
        String hora,dia,mes,ano;
        String type;

        System.out.println("Preencha todos os campos.");
        System.out.print("Nome:");
        nome=reader.readLine();
        System.out.print("Descricao:");
        descricao=reader.readLine();
        System.out.print("Eleicao de Estudantes(1), Docentes(2), Funcionarios(3) ou Geral(4)?:");
        type=reader.readLine();
        System.out.print("Data de Termino:\nAno:");
        ano=reader.readLine();
        System.out.print("Mes:");
        mes=reader.readLine();
        System.out.print("Dia:");
        dia=reader.readLine();
        System.out.print("Hora:");
        hora=reader.readLine();
        
        LocalDateTime dataFim=LocalDateTime.of(Integer.parseInt(ano), Integer.parseInt(mes), Integer.parseInt(dia), Integer.parseInt(hora), 0, 0);  
        System.out.println( "Dados Introduzidos:\n"+nome+"\n"+descricao+"\n"+type+"\n"+dataFim.toString()+"\n");
        //uni.addEleicao(...);
        System.out.println("Acao bem sucedida");

    }
    public static void editEleicao(BufferedReader reader) throws Exception{
        //list eleicoes
        String idEleicao;
        System.out.print("Eleicao a editar:");
        idEleicao=reader.readLine();
        //uni.removeEleicao(idEleicao);
        //uni.addEleicao(idEleicao);
        System.out.println("Acao bem sucedida");
    }
    public static void getResultados(BufferedReader reader) throws Exception{
        //list eleicoes
        String idEleicao=reader.readLine();
        String resultados="";
        //resultados=uni.getResultados(idEleicao);
        System.out.println(resultados);
        System.out.println("Acao bem sucedida");
    }
    public static void getVoto(BufferedReader reader) throws Exception{
        //list eleicoes
        //list pessoas
        String idEleicao=reader.readLine();
        String resultados="";
        //resultados=uni.getResultados(idEleicao);
        System.out.println(resultados);
        System.out.println("Acao bem sucedida");
    }
    public static void votar(BufferedReader reader) throws Exception{
        String nome,password,idEleicao,idLista;
        System.out.print("Nome:");
        nome=reader.readLine();
        System.out.print("Password:");
        password=reader.readLine();
        System.out.print("Eleicao:");
        idEleicao=reader.readLine();
        System.out.print("Lista a votar:");
        idLista=reader.readLine();
        //list eleicoes
        //list listas
        //uni.vote(nome,password,eleicao,lista,"antecipado");
        System.out.println("Acao bem sucedida");
    }
    public static void addLista(BufferedReader reader) throws Exception{
        ArrayList<String> lista=new ArrayList<String>();
        String idEleicao,nome,aux;
        //list Eleicoes
        System.out.print("Eleicao:");
        idEleicao=reader.readLine();
        System.out.print("Nome:");
        nome=reader.readLine();
        int i=1;
        System.out.print("Nome Elemento #"+i+":");
        while((aux=reader.readLine())!=null){
            lista.add(aux);
            i++;
            System.out.print("Nome Elemento #"+i+":");
        }
        //uni.addLista(idEleicao,nome,lista);
        System.out.println("Acao bem sucedida");

    }
    public static void removeLista(BufferedReader reader) throws Exception{
        //list Eleicoes
        String idEleicao,idLista;
        System.out.print("Eleicao:");
        idEleicao=reader.readLine();
        //lista listas
        System.out.print("Lista:");
        idLista=reader.readLine();
        //uni.removeLista(idEleicao,idLista);

    }
    public static void editLista(BufferedReader reader) throws Exception{
        //list Eleicoes
        String idEleicao,idLista;
        System.out.print("Eleicao:");
        idEleicao=reader.readLine();
        //lista listas
        System.out.print("Lista:");
        idLista=reader.readLine();
        //uni.removeLista(idEleicao,idLista);
        addLista(reader);

    }
    public static void addMesa(BufferedReader reader) throws Exception{
        String departamento;
        String membro1,membro2,membro3;

        System.out.println("Preencha todos os campos.");
        System.out.print("Departamento:");
        departamento=reader.readLine();
        System.out.print("Membro de mesa #1:");
        membro1=reader.readLine();
        System.out.print("Membro de mesa #2:");
        membro2=reader.readLine();
        System.out.print("Membro de mesa #3:");
        membro3=reader.readLine();
        
        System.out.println( "Dados Introduzidos:\n"+departamento+"\n"+membro1+"\n"+membro2+"\n"+membro3+"\n");
        //uni.addMesa(...);
        System.out.println("Acao bem sucedida");
    }
    public static void removeMesa(BufferedReader reader) throws Exception{
        //list mesas
        String idMesa;
        System.out.print("Mesa:");
        idMesa=reader.readLine();
        //uni.removeMesa(idmesa);
    }
    public static void editMesa(BufferedReader reader)throws Exception{
        //list mesas
        String idMesa;
        System.out.print("Mesa:");
        idMesa=reader.readLine();
        //uni.removeLista(idMesa);
        addMesa(reader);
    }
}
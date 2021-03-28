import java.net.*;
import java.io.*;
import java.util.*;
import java.rmi.*;
import java.time.*;

public class AdminConsole extends Thread {
    public static void main(String[] args) {
        System.getProperties().put("java.security.policy", "policy.all");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String escolha = "";
        try {
            // Universidade uni = (Universidade) Naming.lookup("uni");
            while (true) {
                System.out.println(printMenu());
                System.out.print("Opcao: ");
                escolha = reader.readLine();

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
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String printMenu() {
        String menu = "" + "OPCOES DISPONIVEIS. DIGITE 1.1 por exemplo.\n"
                + "___________________________________________\n" + "1.Gerir Pessoas\n" + "   1.1.Registar Pessoa\n"
                + "   1.2.Alterar dados pessoais\n" + "2.Gerir Eleicao\n" + "   2.1.Criar Eleicao\n"
                + "   2.2.Alterar informacao de eleicao\n" + "   2.3.Consultar Resultados\n"
                + "   2.4.Consultar Informacao de Voto\n" + "   2.5.Voto Antecipado\n" + "3.Gerir Candidatos\n"
                + "   3.1.Adicionar Lista\n" + "   3.2 Remover Lista\n" + "   3.3 Alterar Lista\n"
                + "4.Gerir Mesas de Voto\n" + "   4.1.Adicionar Mesa\n" + "   4.2.Remover mesa\n"
                + "   4.3.Alterar membros de mesa\n" + "5.Sair.\n";
        return menu;
    }

    public static String readInteger(BufferedReader reader, String info) throws Exception{
        String input = "";
        int intInputValue = 0;
        boolean isValid = false;

        while (!isValid){
            System.out.print(info);
            input = reader.readLine();
            intInputValue = 0;
            try {
                intInputValue = Integer.parseInt(input);
                if (intInputValue > 0){
                    isValid = true;
                }
            } catch (NumberFormatException ne) {
                System.out.println("Not a valid Input");
            }
        }
        return input;
    }

    public static String readInteger(BufferedReader reader, String info, int max) throws Exception{
        String input = "";
        int intInputValue = 0;
        boolean isValid = false;

        while (!isValid){
            System.out.print(info);
            input = reader.readLine();
            intInputValue = 0;
            try {
                intInputValue = Integer.parseInt(input);
                if (intInputValue <= max && intInputValue > 0) isValid = true;
                else System.out.println("Not a valid Input");
            } catch (NumberFormatException ne) {
                System.out.println("Not a valid Input");
            }
        }
        return input;
    }

    public static Calendar readDate(BufferedReader reader, String info, boolean flagHora) throws Exception{
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        String ano, mes, dia, hora;
        boolean isValid = false;

        while(!isValid){

            System.out.println(info);

            ano = readInteger(reader, "Ano:");
            mes = readInteger(reader, "Mes:", 12);

            if(mes == "4" || mes == "6" || mes == "9" || mes == "11" )
                dia = readInteger(reader, "Dia:", 30);
            else if(mes == "2" && Integer.parseInt(ano) % 4 ==0)
                dia = readInteger(reader, "Dia:", 29);
            else if (mes == "2")
                dia = readInteger(reader, "Dia:", 28);
            else
                dia = readInteger(reader, "Dia:", 31);

            if (flagHora){
                hora = readInteger(reader, "Hora:", 24);
                calendar.set(Integer.parseInt(ano), Integer.parseInt(mes) - 1, Integer.parseInt(dia), Integer.parseInt(hora), 0);
            }
            else{
                calendar.set(Integer.parseInt(ano), Integer.parseInt(mes) - 1, Integer.parseInt(dia));

            }
            isValid = true;

        }

        return calendar;
    }

    public static void addPessoa(BufferedReader reader) throws Exception {
        String nome;
        String type;
        String password;
        String departamento, telefone, morada, cc;
        Calendar validadeCC;

        System.out.println("Preencha todos os campos.");
        System.out.print("Nome:");
        nome = reader.readLine();

        type = readInteger(reader, "Profissao: Estudante(1), Docente(2) ou Funcionario(3)?:", 3);

        departamento = readInteger(reader, "\"Departamento: DA (1), DCT (2), DCV (3), DEC (4), DEEC (5), DEI (6), DEM (7), DEQ (8), DF(9), DM (10), DQ (11)?", 11);

        telefone = readInteger(reader, "Telefone:");
        System.out.print("Morada:");
        morada = reader.readLine();

        cc = readInteger(reader, "Numero Cartao Cidado:");

        validadeCC = readDate(reader, "Validade Cartao Cidado:", false);
        System.out.print("Password:");
        password = reader.readLine();

        System.out.println("Dados Introduzidos:\n" + nome + "\n" + type + "\n" + departamento + "\n" + telefone + "\n"
                + morada + cc + "\n" + validadeCC.getTime().toString() + "\n" + password);
        // uni.addPesoa(...);
        System.out.println("Acao bem sucedida");
    }

    public static void editPessoa(BufferedReader reader) throws Exception {
        // list pessoas
        String idPessoa;
        System.out.print("Pessoa a editar:");
        idPessoa = reader.readLine();
        // uni.removePessoa(idPessoa);
        // uni.addPessoa();
    }

    public static void addEleicao(BufferedReader reader) throws Exception {
        String nome;
        String descricao;
        String type;
        Calendar dataInicio = new GregorianCalendar(), dataTermino = new GregorianCalendar();
        boolean notValid = true;

        System.out.println("Preencha todos os campos.");
        System.out.print("Nome:");
        nome = reader.readLine();
        System.out.print("Descricao:");
        descricao = reader.readLine();
        //
        type = readInteger(reader, "Eleicao de Estudantes(1), Docentes(2), Funcionarios(3) ou Geral(4)?:", 4);

        while(notValid){
            dataInicio = readDate(reader, "Data de Inicio", true);
            dataTermino = readDate(reader, "Data de Termino", true);

            if (dataInicio.before(dataTermino) == true)
                notValid = false;
            else
                System.out.printf("Data Final deve ser depois da data Inical");
        }

        System.out.println(
                "Dados Introduzidos:\n" + nome + "\n" + descricao + "\n" + type + "\n" + dataInicio.getTime().toString() + "\n" + dataTermino.getTime().toString() + "\n");
        // uni.addEleicao(...);
        System.out.println("Acao bem sucedida");

    }

    public static void editEleicao(BufferedReader reader) throws Exception {
        // list eleicoes
        String idEleicao;
        System.out.print("Eleicao a editar:");
        idEleicao = reader.readLine();
        // uni.removeEleicao(idEleicao);
        // uni.addEleicao(idEleicao);
    }

    public static void getResultados(BufferedReader reader) throws Exception {
        // list eleicoes
        System.out.print("Nome da eleicao:");
        String idEleicao = reader.readLine();
        String resultados = "";
        // resultados=uni.getResultados(idEleicao);
        System.out.println(resultados);
    }

    public static void getVoto(BufferedReader reader) throws Exception {
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

    public static void votar(BufferedReader reader) throws Exception {
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

    public static void addLista(BufferedReader reader) throws Exception {
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

    public static void removeLista(BufferedReader reader) throws Exception {
        // list Eleicoes
        String idEleicao, idLista;
        System.out.print("Eleicao:");
        idEleicao = reader.readLine();
        // lista listas
        System.out.print("Lista:");
        idLista = reader.readLine();
        // uni.removeLista(idEleicao,idLista);

    }

    public static void editLista(BufferedReader reader) throws Exception {
        // list Eleicoes
        String idEleicao, idLista;
        System.out.print("Eleicao:");
        idEleicao = reader.readLine();
        // lista listas
        System.out.print("Lista:");
        idLista = reader.readLine();
        // uni.removeLista(idEleicao,idLista);
        addLista(reader);

    }

    public static void addMesa(BufferedReader reader) throws Exception {
        String departamento;
        String membro1, membro2, membro3;

        System.out.println("Preencha todos os campos.");

        departamento = readInteger(reader, "\"Departamento: DA (1), DCT (2), DCV (3), DEC (4), DEEC (5), DEI (6), DEM (7), DEQ (8), DF(9), DM (10), DQ (11)?", 11);

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

    public static void removeMesa(BufferedReader reader) throws Exception {
        // list mesas
        String idMesa;
        System.out.print("Mesa:");
        idMesa = reader.readLine();
        // uni.removeMesa(idmesa);
    }

    public static void editMesa(BufferedReader reader) throws Exception {
        // list mesas
        String idMesa;
        System.out.print("Mesa:");
        idMesa = reader.readLine();
        // uni.removeLista(idMesa);
        addMesa(reader);
    }
}
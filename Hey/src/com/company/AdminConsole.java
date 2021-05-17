package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.net.*;
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminConsole extends UnicastRemoteObject implements RMI_C_Interface {
    public static String RMIHostIP;
    public static int RMIHostPort;
    public static int totalTries = 5;//n tentativas de invocacao de metodo RMI ate desistir
    public static Registry r = null;
    public static RMI_S_Interface servidor;
    public static AdminConsole consola;

    public AdminConsole() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Bad arguments. run java AdminConsole {RMIHostIP} {RMIHostPort}");
            System.exit(1);
        }
        RMIHostIP = args[0];
        RMIHostPort = Integer.parseInt(args[1]);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String escolha;
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), 5050);
            String ip = socket.getLocalAddress().getHostAddress();
            System.setProperty("java.rmi.server.hostname", ip);

            r = LocateRegistry.getRegistry(RMIHostIP, RMIHostPort);
            servidor = (RMI_S_Interface) r.lookup("ServidorRMI");

            consola = new AdminConsole();
            servidor.subscribe(consola);

            while (true) {
                System.out.println(printMenu());
                System.out.print("Opcao: ");
                escolha = reader.readLine();
                try {
                    switch (escolha) {
                        case "1.1":
                            addPessoa(reader);
                            break;
                        case "1.2":
                            listPessoas(reader);
                            break;
                        case "2.1":
                            addEleicao(reader);
                            break;
                        case "2.2":
                            getResultados(reader);
                            break;
                        case "2.3":
                            getVoto(reader);
                            break;
                        case "2.4":
                            votar(reader);
                            break;
                        case "2.5":
                            listEleicoes(reader);
                            break;
                        case "2.6":
                            editEleicao(reader);
                            break;
                        case "3.1":
                            addLista(reader);
                            break;
                        case "3.2":
                            listListas(reader);
                            break;
                        case "3.3":
                            removeLista(reader);
                            break;
                        case "4.1":
                            addMesa(reader);
                            break;
                        case "4.2":
                            removeMesa(reader);
                            break;
                        case "4.3":
                            addMesaEleicao(reader);
                            break;
                        case "4.4":
                            removeMesaEleicao(reader);
                            break;
                        case "4.5":
                            editMesa(reader);
                            break;
                        case "4.6":
                            listMesas(reader);
                            break;
                        case "5":
                            servidor.unsubscribe(consola);
                            System.exit(0);
                        default:
                            System.out.println("Escolha invalida.Tente 1.1, por exemplo.");
                            break;
                    }
                } catch (RemoteException e) {
                    System.out.println("Servidor RMI indisponivel. A tentar conectar novamente...");
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }
                System.out.println("Pressione Enter para continuar.");
                reader.readLine();
            }
        } catch (RemoteException | NotBoundException e) {
            System.out.println("Servidor RMI indisponivel");
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String printMenu() {
        String menu = "OPCOES DISPONIVEIS. DIGITE 1.1 por exemplo.\n"
                + "___________________________________________\n" +
                "1.Gerir Pessoas\n" +
                "   1.1.Registar Pessoa\n" +
                "   1.2.Listar Pessoas registadas\n" +
                "2.Gerir Eleicao\n" +
                "   2.1.Criar Eleicao\n" +
                "   2.2.Consultar Resultados\n" +
                "   2.3.Consultar Informacao de Voto\n" +
                "   2.4.Voto Antecipado\n" +
                "   2.5.Listar Eleicoes\n" +
                "   2.6.Alterar Dados de Eleicao\n" +
                "3.Gerir Candidatos\n" +
                "   3.1.Adicionar Lista\n" +
                "   3.2.Listar Listas de Eleicao\n" +
                "   3.3.Remover Lista\n" +
                "4.Gerir Mesas de Voto\n" +
                "   4.1.Adicionar Mesa\n" +
                "   4.2.Remover Mesa\n" +
                "   4.3.Associar Mesa a Eleicao\n" +
                "   4.4.Desassociar Mesa a Eleicao\n" +
                "   4.5.Alterar Membros de Mesa\n" +
                "   4.6.Listar Mesas existentes\n" +
                "5.Sair\n";
        return menu;
    }


    public static void addPessoa(BufferedReader reader) throws IOException {

        String nome, password;
        String telefone, morada, cc;
        GregorianCalendar validadeCC;
        Profissao prof;
        Departamento dep;

        System.out.println("Preencha todos os campos.");
        System.out.print("Nome:");
        nome = reader.readLine();
        prof = readProfissao(reader, "Profissao: Estudante(1), Docente(2) ou Funcionario(3)?:");
        dep = readDepartamento(reader, "Departamento: DA (1), DCT (2), DCV (3), DEC (4), DEEC (5), DEI (6), DEM (7), DEQ (8), DF(9), DM (10), DQ (11)?");
        telefone = readInteger(reader, "Telefone:", -1);
        System.out.print("Morada:");
        morada = reader.readLine();
        cc = readInteger(reader, "Numero Cartao Cidado:", -1);
        validadeCC = readDate(reader, "Validade Cartao Cidado:", false);
        System.out.print("Password:");
        password = reader.readLine();

        String status = "";
        for (int i = 0; i < totalTries; i++) {
            try {
                status = servidor.addPessoa(nome, password, dep, telefone, morada, cc, validadeCC, prof);
                System.out.println(status);
                break;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1) {
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }


    public static void listPessoas(BufferedReader reader) throws RemoteException {
        CopyOnWriteArrayList<Pessoa> pessoas = new CopyOnWriteArrayList<>();
        pessoas.addAll(servidor.listPessoas()); //metodo RMI que se quer chamar

        System.out.println("PESSOAS REGISTADAS:");
        for (Pessoa p : pessoas) {
            System.out.println("Nome: " + p.getNome());
            System.out.println("Numero CC: " + p.getNumberCC() + " " + printGregorianCalendar(p.getExpireCCDate(), false));
            System.out.println("Departamento: " + p.getDepartamento());
            System.out.println("Profissao: " + p.getProfissao());
            System.out.println();
        }
    }

    public static void addEleicao(BufferedReader reader) throws IOException {
        String nome, descricao, type, dep;
        CopyOnWriteArrayList<Profissao> profs = new CopyOnWriteArrayList<Profissao>();
        CopyOnWriteArrayList<Departamento> deps = new CopyOnWriteArrayList<Departamento>(Arrays.asList(Departamento.values()));
        GregorianCalendar dataInicio = new GregorianCalendar(), dataTermino = new GregorianCalendar();
        boolean notValid = true;

        System.out.println("Preencha todos os campos.");
        System.out.print("Nome:");
        nome = reader.readLine();
        System.out.print("Descricao:");
        descricao = reader.readLine();

        type = readInteger(reader, "Eleicao de Estudantes(1), Docentes(2), Funcionarios(3) ou Geral(4)?:", 4);
        switch (type) {
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
        }

        while (notValid) {
            dataInicio = readDate(reader, "Data de Inicio", true);
            dataTermino = readDate(reader, "Data de Termino", true);

            if (dataInicio.before(dataTermino))
                notValid = false;
            else
                System.out.println("Data Final deve ser depois da data Inical");
        }

        //RMI Method call
        for (int i = 0; i < totalTries; i++) {
            try {
                String status = servidor.addEleicao(nome, descricao, dataInicio, dataTermino, profs, deps);
                System.out.println(status);
                break;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1) {
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }

    public static void getResultados(BufferedReader reader) throws IOException {
        String nomeEleicao;

        System.out.print("Nome da eleicao:");
        nomeEleicao = reader.readLine();

        Resultado res = null;
        //RMI Method call
        for (int i = 0; i < totalTries; i++) {
            try {
                res = servidor.getResultados(nomeEleicao);
                break;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1) {
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }

        if (res == null) {
            System.out.println("Eleicao nao existe ou ainda nao terminou.");
        }
        else {
            System.out.println("Eleicao:" + res.getTitulo());
            System.out.println("Total de Votos:" + res.getTotalVotos());
            System.out.println("Votos em Branco:" + res.getBrancos());
            System.out.println("Votos Nulo:" + res.getNulos());
            System.out.println("Vencedores: ");
            for (String venc : res.getVencedores()) {
                System.out.println("\t" + venc);
            }

            CopyOnWriteArrayList<String> listas = res.getNomesListas();
            CopyOnWriteArrayList<Integer> results = res.getResultados();
            System.out.println("N Votos em cada Lista: ");
            for (int i = 0; i < listas.size(); i++) {
                System.out.println("\t" + listas.get(i) + ":" + results.get(i));
            }
        }
    }

    public static void getVoto(BufferedReader reader) throws IOException {
        String numeroCC, nomeEleicao;

        System.out.print("Numero CC:");
        numeroCC = reader.readLine();
        System.out.print("Nome da eleicao:");
        nomeEleicao = reader.readLine();

        Voto v = null;
        servidor.getVoto(numeroCC, nomeEleicao);
        //RMI Method call
        for (int i = 0; i < totalTries; i++) {
            try {
                v = servidor.getVoto(numeroCC, nomeEleicao);
                break;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1) {
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }

        if (v == null) {
            System.out.println("Pessoa ou eleicao nao existe.");
        }
        else {
            System.out.println("Nome: " + v.getPessoa().getNome());
            System.out.println("Eleicao: " + nomeEleicao);
            System.out.println("Hora de Voto: " + printGregorianCalendar(v.getData(), true));
            System.out.print("Departamento: ");
            if (v.getMesa() == null) {
                System.out.println("Admin Console");
            }
            else {
                System.out.println(v.getMesa().getDepartamento());
            }
        }
    }

    public static void votar(BufferedReader reader) throws IOException {
        String numeroCC, password, nomeEleicao, nomeLista;
        System.out.print("Numero CC:");
        numeroCC = reader.readLine();
        System.out.print("Password:");
        password = reader.readLine();
        System.out.print("Eleicao:");
        nomeEleicao = reader.readLine();
        System.out.print("Lista a votar (ou Nulo/Branco):");
        nomeLista = reader.readLine();

        //RMI Method call
        for (int i = 0; i < totalTries; i++) {
            try {
                String status = servidor.addVotoAntecipado(numeroCC, password, nomeEleicao, nomeLista);
                System.out.println(status);
                break;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1) {
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }

    public static void listEleicoes(BufferedReader reader) throws RemoteException {
        CopyOnWriteArrayList<Eleicao> eleicoes = new CopyOnWriteArrayList<Eleicao>();
        eleicoes.addAll(servidor.listEleicoes());

        System.out.println("ELEICOES REGISTADAS:");
        for (Eleicao ele : eleicoes) {
            System.out.println("Nome: " + ele.getTitulo());
            System.out.println("Descricao: " + ele.getDescricao());
            System.out.println("Profissoes: " + ele.getProfissoesPermitidas());
            System.out.println("Hora de Inicio: " + printGregorianCalendar(ele.getDataInicio(), true));
            System.out.println("Hora de Fim: " + printGregorianCalendar(ele.getDataFim(), true));
            System.out.println();
        }
    }

    public static void editEleicao(BufferedReader reader) throws IOException {
        String nomeAntigo, nomeNovo, descricaoNova;
        String hora, dia, mes, ano;
        boolean notValid = true;
        GregorianCalendar dataInicio = new GregorianCalendar(), dataTermino = new GregorianCalendar();

        System.out.println("Preencha todos os campos.");
        System.out.print("Titulo Antigo:");
        nomeAntigo = reader.readLine();
        System.out.print("Titulo Novo:");
        nomeNovo = reader.readLine();
        System.out.print("Descricao Nova:");
        descricaoNova = reader.readLine();

        while (notValid) {
            dataInicio = readDate(reader, "Data de Inicio", true);
            dataTermino = readDate(reader, "Data de Termino", true);


            if (dataInicio.before(dataTermino))
                notValid = false;
            else
                System.out.println("Data Final deve ser depois da data Inical");
        }

        String status = "";
        //RMI Method call
        for (int i = 0; i < totalTries; i++) {
            try {
                status = servidor.editEleicao(nomeAntigo, nomeNovo, descricaoNova, dataInicio, dataTermino);
                System.out.println(status);
                break;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1) {
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }

    }

    public static void addLista(BufferedReader reader) throws IOException {
        CopyOnWriteArrayList<Pessoa> membros = new CopyOnWriteArrayList<Pessoa>();
        String eleicao, nome, aux;
        Profissao prof = Profissao.Estudante;
        int i = 1;

        System.out.print("Nome da eleicao:");
        eleicao = reader.readLine();
        System.out.print("Nome da Lista:");
        nome = reader.readLine();
        prof = readProfissao(reader, "Lista de Estudantes(1), Docentes(2) ou Funcionarios(3)?:");

        while (true) {
            System.out.print("Nome Elemento #" + i + ":");
            aux = reader.readLine();
            if (aux.equals(""))
                break;
            Pessoa p = new Pessoa(aux, "-1", Departamento.DA, "-1", "-1", "-1", new GregorianCalendar(), Profissao.Estudante);
            membros.add(p);
            i++;
        }


        String status = "";
        //RMI Method call
        for (int j = 0; j < totalTries; j++) {
            try {
                status = servidor.addLista(eleicao, nome, membros, prof);
                System.out.println(status);
                break;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (j == totalTries - 1) {
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }


    public static void listListas(BufferedReader reader) throws IOException {
        String eleicao;
        System.out.print("Nome da eleicao:");
        eleicao = reader.readLine();

        CopyOnWriteArrayList<Lista> listas = null;
        listas = servidor.listListas(eleicao);

        if (listas == null)
            System.out.println("Eleicao nao existe.");
        else {
            System.out.println("LISTAS REGISTADAS:");
            for (Lista lis : listas) {
                System.out.println("Nome: " + lis.getNome());
                System.out.println("Tipo: " + lis.getTipoLista());
                System.out.print("Membros: ");
                for (int i = 0; i < lis.getListaPessoas().size(); i++) {
                    if (i == lis.getListaPessoas().size() - 1)
                        System.out.print(lis.getListaPessoas().get(i).getNome());
                    else
                        System.out.print(lis.getListaPessoas().get(i).getNome() + ", ");
                }
                System.out.println();
                System.out.println();
            }
        }

    }

    public static void removeLista(BufferedReader reader) throws IOException {
        String eleicao, nome, aux, type;

        System.out.print("Nome da eleicao:");
        eleicao = reader.readLine();
        System.out.print("Nome da Lista:");
        nome = reader.readLine();

        String status = "";
        //RMI Method call
        for (int j = 0; j < totalTries; j++) {
            try {
                status = servidor.removeLista(eleicao, nome);
                System.out.println(status);
                break;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (j == totalTries - 1) {
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }

    public static void addMesa(BufferedReader reader) throws IOException {
        String departamento;
        String membro1, membro2, membro3, ip, port;
        Departamento dep = Departamento.DA;
        CopyOnWriteArrayList<Pessoa> membros = new CopyOnWriteArrayList<Pessoa>();

        System.out.println("Preencha todos os campos.");
        dep = readDepartamento(reader, "Departamento: DA (1), DCT (2), DCV (3), DEC (4), DEEC (5), DEI (6), DEM (7), DEQ (8), DF(9), DM (10), DQ (11)?");
        System.out.print("Membro de mesa #1:");
        membro1 = reader.readLine();
        System.out.print("Membro de mesa #2:");
        membro2 = reader.readLine();
        System.out.print("Membro de mesa #3:");
        membro3 = reader.readLine();
        System.out.print("IP grupo multicast:");
        ip = reader.readLine();
        System.out.print("Port grupo multicast:");
        port = reader.readLine();
        GregorianCalendar date = new GregorianCalendar();

        Pessoa p1 = new Pessoa(membro1, membro1, Departamento.DA, "-1", "-1", "-1", date, Profissao.Estudante);
        Pessoa p2 = new Pessoa(membro2, membro2, Departamento.DA, "-1", "-1", "-1", date, Profissao.Estudante);
        Pessoa p3 = new Pessoa(membro3, membro3, Departamento.DA, "-1", "-1", "-1", date, Profissao.Estudante);
        membros.add(p1);
        membros.add(p2);
        membros.add(p3);

        String status = "";
        //RMI Method call
        for (int i = 0; i < totalTries; i++) {
            try {
                status = servidor.addMesa(dep, membros, ip, port);
                System.out.println(status);
                break;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1) {
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }

    public static void removeMesa(BufferedReader reader) throws IOException {
        String nomeMesa;

        System.out.print("Mesa(departamento):");
        nomeMesa = reader.readLine();

        String status = "";
        //RMI Method call
        for (int i = 0; i < totalTries; i++) {
            try {
                status = servidor.removeMesa(nomeMesa);
                System.out.println(status);
                break;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1) {
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }

    public static void addMesaEleicao(BufferedReader reader) throws IOException {
        String nomeMesa, nomeEleicao;

        System.out.print("Mesa(departamento):");
        nomeMesa = reader.readLine();
        System.out.print("Eleicao:");
        nomeEleicao = reader.readLine();

        String status = "";
        //RMI Method call
        for (int i = 0; i < totalTries; i++) {
            try {
                status = servidor.addMesaEleicao(nomeMesa, nomeEleicao);
                System.out.println(status);
                break;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1) {
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }

    public static void removeMesaEleicao(BufferedReader reader) throws IOException {
        String nomeMesa, nomeEleicao;
        System.out.print("Mesa(departamento):");
        nomeMesa = reader.readLine();
        System.out.print("Eleicao:");
        nomeEleicao = reader.readLine();

        String status = "";
        //RMI Method call
        for (int i = 0; i < totalTries; i++) {
            try {
                status = servidor.removeMesaEleicao(nomeMesa, nomeEleicao);
                System.out.println(status);
                break;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1) {
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }

    public static void editMesa(BufferedReader reader) throws IOException {
        String nomeMesa, membro1, membro2, membro3;
        System.out.print("Mesa(departamento):");
        nomeMesa = reader.readLine();
        System.out.print("Membro de mesa #1:");
        membro1 = reader.readLine();
        System.out.print("Membro de mesa #2:");
        membro2 = reader.readLine();
        System.out.print("Membro de mesa #3:");
        membro3 = reader.readLine();

        String status = "";
        //RMI Method call
        for (int i = 0; i < totalTries; i++) {
            try {
                status = servidor.editMesa(nomeMesa, membro1, membro2, membro3);
                System.out.println(status);
                break;
            } catch (RemoteException e) {
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP, RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                } catch (RemoteException | NotBoundException ignored) {
                }
                if (i == totalTries - 1) {
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }

    public static void listMesas(BufferedReader reader) throws RemoteException {

        CopyOnWriteArrayList<MesaVoto> mesas = new CopyOnWriteArrayList<>();
        mesas.addAll(servidor.listMesas());

        System.out.println("MESSAS EXISTENTES:");
        for (MesaVoto mesa : mesas) {
            //{NomeDepardamento} {Desilgada/Ligada} {Membro1} {Membro2} {Membro3} {IP} {Port}
            System.out.println("Departamento: " + mesa.getDepartamento());
            if (mesa.getStatus())
                System.out.println("Estado: Ligada");
            else
                System.out.println("Estado: Desligada");
            System.out.println("Membros: ");
            System.out.print(mesa.getMembros().get(0).getNome() + ", ");
            System.out.print(mesa.getMembros().get(1).getNome() + ", ");
            System.out.println(mesa.getMembros().get(2).getNome());
            System.out.println("Endereco: " + mesa.getIp() + " " + mesa.getPort());
            System.out.println();
        }

    }


    public static String printGregorianCalendar(GregorianCalendar data, boolean flagHora) {
        int minuto = data.get(Calendar.MINUTE);
        int hora = data.get(Calendar.HOUR_OF_DAY);
        int dia = data.get(Calendar.DATE);
        int mes = data.get(Calendar.MONTH) + 1;
        int ano = data.get(Calendar.YEAR);
        if (flagHora)
            return dia + "/" + mes + "/" + ano + " " + hora + ":" + minuto;
        else
            return dia + "/" + mes + "/" + ano;
    }

    public static String readInteger(BufferedReader reader, String info, int max) throws IOException {
        String input = "";
        int intInputValue = 0;
        boolean isValid = false;

        while (!isValid) {
            System.out.print(info);
            input = reader.readLine();
            intInputValue = 0;
            try {
                intInputValue = Integer.parseInt(input);
                if (max == -1 || (intInputValue <= max && intInputValue > 0)) isValid = true;
                else System.out.println("Not a valid Input");
            } catch (NumberFormatException ne) {
                System.out.println("Not a valid Input");
            }
        }
        return input;
    }

    public static GregorianCalendar readDate(BufferedReader reader, String info, boolean flagHora) throws IOException {
        GregorianCalendar calendar = new GregorianCalendar();
        String ano, mes, dia, hora, minuto;
        boolean isValid = false;

        while (!isValid) {
            System.out.print(info);
            String data = reader.readLine();

            if (flagHora) {
                Pattern p = Pattern.compile("\\d{2}(\\/)\\d{2}(\\/)\\d{4} \\d{2}(\\:)\\d{2}$");    // 01/02/2022 12:30
                Matcher m = p.matcher(data);

                if (m.find()) {
                    dia = data.substring(0, 2);
                    mes = data.substring(3, 5);
                    ano = data.substring(6, 10);
                    hora = data.substring(11, 13);
                    minuto = data.substring(14, 16);
                    calendar.set(Integer.parseInt(ano), Integer.parseInt(mes) - 1, Integer.parseInt(dia), Integer.parseInt(hora), Integer.parseInt(minuto) - 1);
                    isValid = true;
                }
            }
            else {
                Pattern p = Pattern.compile("\\d{2}(\\/)\\d{2}(\\/)\\d{4}$");    // 01/02/2022 12:30
                Matcher m = p.matcher(data);

                if (m.find()) {
                    dia = data.substring(0, 2);
                    mes = data.substring(3, 5);
                    ano = data.substring(6, 10);
                    calendar.set(Integer.parseInt(ano), Integer.parseInt(mes) - 1, Integer.parseInt(dia));
                    isValid = true;
                }
            }

        }
        return calendar;
    }

    public static Departamento readDepartamento(BufferedReader reader, String info) throws IOException {
        String departamento = readInteger(reader, info, 11);
        Departamento dep = Departamento.DA;
        switch (departamento) {
            case "1":
                dep = Departamento.DA;
                break;
            case "2":
                dep = Departamento.DCT;
                break;
            case "3":
                dep = Departamento.DCV;
                break;
            case "4":
                dep = Departamento.DEC;
                break;
            case "5":
                dep = Departamento.DEEC;
                break;
            case "6":
                dep = Departamento.DEI;
                break;
            case "7":
                dep = Departamento.DEM;
                break;
            case "8":
                dep = Departamento.DEQ;
                break;
            case "9":
                dep = Departamento.DF;
                break;
            case "10":
                dep = Departamento.DM;
                break;
            case "11":
                dep = Departamento.DQ;
                break;
        }
        return dep;
    }

    public static Profissao readProfissao(BufferedReader reader, String info) throws IOException {
        String type = readInteger(reader, "Profissao: Estudante(1), Docente(2) ou Funcionario(3)?:", 3);
        Profissao prof = Profissao.Estudante;
        switch (type) {
            case "1":
                prof = Profissao.Estudante;
                break;
            case "2":
                prof = Profissao.Docente;
                break;
            case "3":
                prof = Profissao.Funcionario;
                break;
            default:
                prof = Profissao.Estudante;
        }
        return prof;
    }

    public void printOnClient(String s) throws RemoteException {
        try {
            JSONObject rj = (JSONObject) JSONValue.parse(s);
            String tipo = rj.get("tipo").toString();
            switch (tipo) {
                case "mesa":
                    System.out.println("Mesa " + rj.get("mesa") + " foi " + rj.get("estado") + " a " + rj.get("data") + ".");
                    break;
                case "eleicao":
                    System.out.println("Eleicao " + rj.get("nome") + " " + rj.get("estado") + " a " + rj.get("data") + ".");
                    break;
                case "utilizador":
                    System.out.println("Utilizador " + rj.get("nome") + " " + rj.get("estado") + " a " + rj.get("data") + ".");
                    break;
                case "voto":
                    System.out.println("Votante " + rj.get("nome") + " (" + rj.get("profissao") + ") " + " votou na eleicao " + rj.get("eleicao") + " ,na mesa " + rj.get("mesa") + "a" + rj.get("data") + ".");
                    break;
                default:
                    System.out.println("Notificacao com forma desconhecida.");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Erro a processar notificacao.");
        }

    }
}
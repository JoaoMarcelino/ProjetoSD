import java.net.*;
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.rmi.*;
import java.time.*;

import java.rmi.server.*;

public class AdminConsole extends UnicastRemoteObject implements RMI_C_Interface {
    public static String RMIHostIP;
    public static int RMIHostPort;
    public static int totalTries=10;//n tentativas de invocacao de metodo RMI ate desistir
    public static Registry r=null;
    public static RMI_S_Interface servidor;

    public AdminConsole() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        if(args.length!=2){
            System.out.println("Bad arguments. run java AdminConsole {RMIHostIP} {RMIHostPort}");
            System.exit(1);
        }
        RMIHostIP=args[0];
        RMIHostPort=Integer.parseInt(args[1]);

        //System.getProperties().put("java.security.policy", "policy.all");
        //System.setSecurityManager(new RMISecurityManager());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String escolha = "";

        try {
            r = LocateRegistry.getRegistry(RMIHostIP,RMIHostPort);
            servidor = (RMI_S_Interface) r.lookup("ServidorRMI");

            AdminConsole consola = new AdminConsole();
            servidor.subscribe((RMI_C_Interface) consola);

            while (true) {
                System.out.println(printMenu());
                System.out.print("Opcao: ");
                escolha = reader.readLine();

                try {
                    switch (escolha) {
                        case "1.1":
                            addPessoa(reader, servidor);
                            break;
                        case "1.2":
                            listPessoas(reader, servidor);
                            break;
                        case "2.1":
                            addEleicao(reader, servidor);
                            break;
                        case "2.2":
                            getResultados(reader, servidor);
                            break;
                        case "2.3":
                            getVoto(reader, servidor);
                            break;
                        case "2.4":
                            votar(reader, servidor);
                            break;
                        case "2.5":
                            listEleicoes(reader, servidor);
                            break;
                        case "2.6":
                            editEleicao(reader, servidor);
                            break;
                        case "3.1":
                            addLista(reader, servidor);
                            break;
                        case "3.2":
                            listListas(reader, servidor);
                            break;
                        case "4.1":
                            addMesa(reader, servidor);
                            break;
                        case "4.2":
                            removeMesa(reader, servidor);
                            break;
                        case "4.3":
                            addMesaEleicao(reader, servidor);
                            break;
                        case "4.4":
                            removeMesaEleicao(reader, servidor);
                            break;
                        case "4.5":
                            editMesa(reader, servidor);
                            break;
                        case "4.6":
                            listMesas(reader, servidor);
                            break;
                        case "5":
                             System.exit(0);

                        default:
                            System.out.println("Escolha invalida.Tente 1.1, por exemplo.");
                            break;
                    }
                }catch ( RemoteException e){
                    System.out.println("Falha de ligacao ao servidor. Tente Novamente");
                    servidor=(RMI_S_Interface) r.lookup("ServidorRMI");
                }
                System.out.println("Pressione Enter para continuar.");
                reader.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String printMenu() {
        String menu ="OPCOES DISPONIVEIS. DIGITE 1.1 por exemplo.\n"
                + "___________________________________________\n" +
                "1.Gerir Pessoas\n" +
                "   1.1.Registar Pessoa\n" +
                "   1.2.Listar Pessoas registadas\n" +
                "2.Gerir Eleicao\n" +
                "   2.1.Criar Eleicao\n" +
                "   2.2.Consultar Resultados\n" +
                "   2.3.Consultar Informacao de Voto\n" +
                "   2.4.Voto Antecipado\n" +
                "   2.5.Listar Eleicoes\n"+
                "   2.6.Alterar Dados de Eleicao\n"+
                "3.Gerir Candidatos\n" +
                "   3.1.Adicionar Lista\n" +
                "   3.2.Listar Listas de Eleicao\n"+
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


    public static void addPessoa(BufferedReader reader, RMI_S_Interface servidor) throws Exception {

        String nome,password;
        String  telefone, morada, cc, departamento, type;
        GregorianCalendar validadeCC;

        Profissao prof;
        Departamento dep=Departamento.DA;

        System.out.println("Preencha todos os campos.");
        System.out.print("Nome:");
        nome = reader.readLine();

        type = readInteger(reader, "Profissao: Estudante(1), Docente(2) ou Funcionario(3)?:", 3);

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

        departamento = readInteger(reader, "Departamento: DA (1), DCT (2), DCV (3), DEC (4), DEEC (5), DEI (6), DEM (7), DEQ (8), DF(9), DM (10), DQ (11)?", 11);

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

        telefone = readInteger(reader, "Telefone:");
        System.out.print("Morada:");
        morada = reader.readLine();

        cc = readInteger(reader, "Numero Cartao Cidado:");

        validadeCC = readDate(reader, "Validade Cartao Cidado:", false);
        System.out.print("Password:");
        password = reader.readLine();

        String status=servidor.addPessoa(nome,password,dep,telefone,morada,cc,validadeCC,prof);
        System.out.println(status);

    }


    public static void listPessoas(BufferedReader reader,RMI_S_Interface servidor)throws NotBoundException {
        ArrayList<Pessoa> pessoas=new ArrayList<>();
        for(int i=0;i<totalTries;i++){
            try{
                pessoas=servidor.listPessoas(); //metodo RMI que se quer chamar
                break;
            }catch (RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP,RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }catch (RemoteException ignored){}
                if(i==totalTries-1){
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }

        System.out.println("PESSOAS REGISTADAS:");
        for(Pessoa p:pessoas){
            System.out.println(p.getNome()+" "+p.getNumberCC()+ " "+p.getDepartamento()+" "+p.getProfissao());
        }
    }

    public static void addEleicao(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        String nome, descricao,type;
        ArrayList<Profissao> profs=new ArrayList<Profissao>();
        ArrayList<Departamento> deps=new ArrayList<Departamento>(Arrays.asList(Departamento.values()));
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

        while(notValid){
            dataInicio = readDate(reader, "Data de Inicio", true);
            dataTermino = readDate(reader, "Data de Termino", true);

            if (dataInicio.before(dataTermino))
                notValid = false;
            else
                System.out.println("Data Final deve ser depois da data Inical");
        }

        //RMI Method call
        for(int i=0;i<totalTries;i++){
            try{
                String status=servidor.addEleicao(nome,descricao,dataInicio,dataTermino,profs,deps);
                System.out.println(status);
                break;
            }catch (RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP,RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }catch (RemoteException ignored){}
                if(i==totalTries-1){
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }

    }

    public static void getResultados(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        String nomeEleicao;

        System.out.print("Nome da eleicao:");
        nomeEleicao = reader.readLine();

        Resultado res=null;
        //RMI Method call
        for(int i=0;i<totalTries;i++){
            try{
                res=servidor.getResultados(nomeEleicao);
                break;
            }catch (RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP,RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }catch (RemoteException ignored){}
                if(i==totalTries-1){
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }

        if(res==null){
            System.out.println("Eleicao nao existe");
        }
        else{
            System.out.println("Eleicao:"+res.getTitulo());
            System.out.println("Total de Votos:"+res.getTotalVotos());
            System.out.println("Votos em Branco:" + res.getBrancos());
            System.out.println("Votos Nulo:" + res.getNulos());
            System.out.println("Vencedores: ");
            for(String venc:res.getVencedores()){
                System.out.println("\t"+venc);
            }

            ArrayList<String> listas=res.getNomesListas();
            ArrayList<Integer> results=res.getResultados();
            System.out.println("N Votos em cada Lista: ");
            for(int i=0;i<listas.size();i++){
                System.out.println("\t"+listas.get(i)+":"+results.get(i));
            }
        }
    }

    public static void getVoto(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        String numeroCC,nomeEleicao;

        System.out.print("Numero CC:");
        numeroCC = reader.readLine();
        System.out.print("Nome da eleicao:");
        nomeEleicao = reader.readLine();

        Voto v=null; servidor.getVoto(numeroCC,nomeEleicao);
        //RMI Method call
        for(int i=0;i<totalTries;i++){
            try{
                v=servidor.getVoto(numeroCC,nomeEleicao);
                break;
            }catch (RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP,RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }catch (RemoteException ignored){}
                if(i==totalTries-1){
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }

        if(v==null){
            System.out.println("Pessoa ou eleicao nao existe.");
        }
        else{
            System.out.print(v.getPessoa().getNome()+" "+nomeEleicao+" "+printGregorianCalendar(v.getData()));
            if(v.getMesa()==null){
                System.out.print(" Admin Console");
            }
            else {
                System.out.print(v.getMesa().getDepartamento());
            }
            System.out.print("\n");
        }
    }

    public static void votar(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
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
        for(int i=0;i<totalTries;i++){
            try{
                String status=servidor.addVotoAntecipado(numeroCC,password,nomeEleicao,nomeLista);
                System.out.println(status);
                break;
            }catch (RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP,RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }catch (RemoteException ignored){}
                if(i==totalTries-1){
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }


    }

    public static void listEleicoes(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        ArrayList<Eleicao> eleicoes=null;
        //RMI Method call
        for(int i=0;i<totalTries;i++){
            try{
                eleicoes=servidor.listEleicoes();
                break;
            }catch (RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP,RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }catch (RemoteException ignored){}
                if(i==totalTries-1){
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }

        System.out.println("ELEICOES REGISTADAS:");
        for(Eleicao ele:eleicoes){
            System.out.println(ele.getTitulo()+" "+ele.getDescricao()+" "+ele.getProfissoesPermitidas() +" "+printGregorianCalendar(ele.getDataInicio())+" "+printGregorianCalendar(ele.getDataFim()));
        }
    }

    public static void editEleicao(BufferedReader reader,RMI_S_Interface servidor) throws Exception{
        String nomeAntigo,nomeNovo, descricaoNova;
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

        while(notValid) {
            dataInicio = readDate(reader, "Data de Inicio", true);
            dataTermino = readDate(reader, "Data de Termino", true);


            if (dataInicio.before(dataTermino))
                notValid = false;
            else
                System.out.println("Data Final deve ser depois da data Inical");
        }

        String status="";
        //RMI Method call
        for(int i=0;i<totalTries;i++){
            try{
                status=servidor.editEleicao(nomeAntigo,nomeNovo,descricaoNova,dataInicio,dataTermino);
                System.out.println(status);
                break;
            }catch (RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP,RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }catch (RemoteException ignored){}
                if(i==totalTries-1){
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }

    }

    public static void addLista(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        ArrayList<Pessoa> membros=new ArrayList<Pessoa>();
        String  eleicao,nome, aux,type;
        Profissao prof=Profissao.Estudante;
        int i = 1;

        System.out.print("Nome da eleicao:");
        eleicao = reader.readLine();
        System.out.print("Nome da Lista:");
        nome = reader.readLine();
        type = readInteger(reader, "Lista de Estudantes(1), Docentes(2) ou Funcionarios(3)?:", 3);
        switch (type) {
            case "1":
                prof=Profissao.Estudante;
                break;
            case "2":
                prof=Profissao.Docente;
                break;
            case "3":
                prof=Profissao.Funcionario;
                break;
        }

        do{
            System.out.print("Nome Elemento #" + i + ":");
            aux = reader.readLine();
            Pessoa p=new Pessoa(aux,"-1",Departamento.DA,"-1","-1","-1",new GregorianCalendar(),Profissao.Estudante);
            membros.add(p);
            i++;
        }while (!aux.equals(""));

        String status="";
        //RMI Method call
        for(int j=0;j<totalTries;j++){
            try{
                status=servidor.addLista(eleicao,nome,membros,prof);
                System.out.println(status);
                break;
            }catch (RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP,RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }catch (RemoteException ignored){}
                if(j==totalTries-1){
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }


    public static void listListas(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        String eleicao;
        System.out.print("Nome da eleicao:");
        eleicao=reader.readLine();

        ArrayList<Lista> listas=null;
        //RMI Method call
        for(int i=0;i<totalTries;i++){
            try{
                listas=servidor.listListas(eleicao);
                break;
            }catch (RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP,RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }catch (RemoteException ignored){}
                if(i==totalTries-1){
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }

        if(listas==null){
            System.out.println("Eleicao nao existe.");

        }
        else{
            System.out.println("LISTAS REGISTADAS:");
            for(Lista lis:listas){
                System.out.print(lis.getNome()+" "+lis.getTipoLista()+" [ ");
                for(Pessoa p:lis.getListaPessoas()){
                    System.out.print(p.getNome()+" ");
                }
                System.out.println("] ");
            }
        }

    }

    public static void addMesa(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        String departamento;
        String membro1, membro2, membro3,ip,port;
        Departamento dep=Departamento.DA;
        ArrayList<Pessoa> membros=new ArrayList<Pessoa>();

        System.out.println("Preencha todos os campos.");
        departamento = readInteger(reader, "\"Departamento: DA (1), DCT (2), DCV (3), DEC (4), DEEC (5), DEI (6), DEM (7), DEQ (8), DF(9), DM (10), DQ (11)?", 11);

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

        Pessoa p1=new Pessoa(membro1,membro1,Departamento.DA,"-1","-1","-1", date,Profissao.Estudante);
        Pessoa p2=new Pessoa(membro2,membro2,Departamento.DA,"-1","-1","-1", date,Profissao.Estudante);
        Pessoa p3=new Pessoa(membro3,membro3,Departamento.DA,"-1","-1","-1", date,Profissao.Estudante);
        membros.add(p1);
        membros.add(p2);
        membros.add(p3);

        String status="";
        //RMI Method call
        for(int i=0;i<totalTries;i++){
            try{
                status=servidor.addMesa(dep,membros,ip, port);
                System.out.println(status);
                break;
            }catch (RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP,RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }catch (RemoteException ignored){}
                if(i==totalTries-1){
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }
    public static void removeMesa(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        String nomeMesa;

        System.out.print("Mesa(departamento):");
        nomeMesa = reader.readLine();

        String status="";
        //RMI Method call
        for(int i=0;i<totalTries;i++){
            try{
                status=servidor.removeMesa(nomeMesa);
                System.out.println(status);
                break;
            }catch (RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP,RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }catch (RemoteException ignored){}
                if(i==totalTries-1){
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }

    public static void addMesaEleicao(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        String nomeMesa,nomeEleicao;

        System.out.print("Mesa(departamento):");
        nomeMesa = reader.readLine();
        System.out.print("Eleicao:");
        nomeEleicao = reader.readLine();

        String status="";
        //RMI Method call
        for(int i=0;i<totalTries;i++){
            try{
                status=servidor.addMesaEleicao(nomeMesa,nomeEleicao);
                System.out.println(status);
                break;
            }catch (RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP,RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }catch (RemoteException ignored){}
                if(i==totalTries-1){
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }

    public static void removeMesaEleicao(BufferedReader reader,RMI_S_Interface servidor) throws Exception{
        String nomeMesa,nomeEleicao;
        System.out.print("Mesa(departamento):");
        nomeMesa = reader.readLine();
        System.out.print("Eleicao:");
        nomeEleicao = reader.readLine();

        String status="";
        //RMI Method call
        for(int i=0;i<totalTries;i++){
            try{
                status=servidor.removeMesaEleicao(nomeMesa,nomeEleicao);
                System.out.println(status);
                break;
            }catch (RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP,RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }catch (RemoteException ignored){}
                if(i==totalTries-1){
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }

    public static void editMesa(BufferedReader reader,RMI_S_Interface servidor) throws Exception {
        String nomeMesa,membro1,membro2,membro3;
        System.out.print("Mesa(departamento):");
        nomeMesa = reader.readLine();
        System.out.print("Membro de mesa #1:");
        membro1 = reader.readLine();
        System.out.print("Membro de mesa #2:");
        membro2 = reader.readLine();
        System.out.print("Membro de mesa #3:");
        membro3 = reader.readLine();

        String status="";
        //RMI Method call
        for(int i=0;i<totalTries;i++){
            try{
                status=servidor.editMesa(nomeMesa,membro1,membro2, membro3);
                System.out.println(status);
                break;
            }catch (RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP,RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }catch (RemoteException ignored){}
                if(i==totalTries-1){
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }
    }

    public static void listMesas(BufferedReader reader,RMI_S_Interface servidor) throws Exception{

        ArrayList<MesaVoto> mesas=null;
        //RMI Method call
        for(int i=0;i<totalTries;i++){
            try{
                mesas=servidor.listMesas();
                break;
            }catch (RemoteException e){
                try {
                    servidor = (RMI_S_Interface) LocateRegistry.getRegistry(RMIHostIP,RMIHostPort).lookup("ServidorRMI");   //ir a procura novamente do objeto RMI
                }catch (RemoteException ignored){}
                if(i==totalTries-1){
                    System.out.println("Servidor RMI indisponivel.");
                    return;
                }
            }
        }

        System.out.println("MESSAS EXISTENTES:");
        for(MesaVoto mesa:mesas){
            //{NomeDepardamento} {Desilgada/Ligada} {Membro1} {Membro2} {Membro3} {IP} {Port}
            System.out.print(mesa.getDepartamento()+" ");
            if(mesa.isStatus()){
                System.out.print("Ligada ");
            }
            else{
                System.out.print("Desligada ");
            }
            System.out.print(mesa.getMembros().get(0).getNome()+" ");
            System.out.print(mesa.getMembros().get(1).getNome()+" ");
            System.out.print(mesa.getMembros().get(2).getNome()+" ");

            System.out.println(mesa.getIp()+" "+mesa.getPort());
        }

    }

    public static void modoMonitor(BufferedReader reader,RMI_S_Interface servidor) throws Exception{
        while(true){
            String status=servidor.accessNovidades(false,"");
            System.out.println(status);
        }
    }

    public static String printGregorianCalendar(GregorianCalendar data){
        int hora=data.get(Calendar.HOUR);
        int dia=data.get(Calendar.DATE);
        int mes=data.get(Calendar.MONTH)+1;
        int ano=data.get(Calendar.YEAR);

        return hora+"h "+dia+" "+mes+" "+ano;
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

    public static GregorianCalendar readDate(BufferedReader reader, String info, boolean flagHora) throws Exception{
        GregorianCalendar calendar = new GregorianCalendar();
        String ano, mes, dia, hora;
        boolean isValid = false;

        while(!isValid){

            System.out.println(info);

            ano = readInteger(reader, "Ano:");
            mes = readInteger(reader, "Mes:", 12);

            if(mes.equals("4") || mes.equals("6") || mes.equals("9") || mes.equals("11"))
                dia = readInteger(reader, "Dia:", 30);
            else if(mes.equals("2") && Integer.parseInt(ano) % 4 ==0)
                dia = readInteger(reader, "Dia:", 29);
            else if (mes.equals("2"))
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

    public void printOnClient(String s) throws RemoteException {
        System.out.println("> " + s);
    }
}
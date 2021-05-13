package com.company;

import org.json.simple.JSONObject;

import java.io.*;
import java.nio.file.Paths;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Thread.sleep;

public class RMIServer extends UnicastRemoteObject implements RMI_S_Interface {
    private static final String pathToProperties = "src/resources/config.properties";
    public static String RMIHostIP;
    public static int RMIHostPort;
    public static int frequency = 1000; // frequencia de pings entre servidores (milisegundos)
    public static int totalTries = 3;// n tentativas ate assumir papel de servidor principal
    public CopyOnWriteArrayList<RMI_C_Interface> consolas = new CopyOnWriteArrayList<RMI_C_Interface>();
    public CopyOnWriteArrayList<Eleicao> eleicoes = new CopyOnWriteArrayList<Eleicao>();
    public CopyOnWriteArrayList<Pessoa> pessoas = new CopyOnWriteArrayList<Pessoa>();
    public CopyOnWriteArrayList<MesaVoto> mesas = new CopyOnWriteArrayList<MesaVoto>();
    public HashMap<Pessoa, String> loggedInUsers = new HashMap<>();


    public RMIServer() throws RemoteException {
        super();
        load();
        for (Eleicao eleicao : eleicoes) {
            if (eleicao.getDataFim().after(new GregorianCalendar())) {
                Timer time = new Timer(); // Instantiate Timer Object
                time.schedule(new CustomTask(this, eleicao), eleicao.getDataFim().getTimeInMillis() - (new GregorianCalendar()).getTimeInMillis());
            }
        }
    }

    public static void main(String[] args) {
        loadProperties();
        try {
            Registry r = LocateRegistry.getRegistry(RMIHostIP, RMIHostPort);
            RMI_S_Interface servidorPrincipal = (RMI_S_Interface) r.lookup("ServidorRMI");
            int failed = 0;
            boolean didFail = false;

            System.out.println("Servidor RMI Secundario em execucao.");
            while (failed < totalTries) {
                sleep(frequency);
                try {
                    servidorPrincipal = (RMI_S_Interface) r.lookup("ServidorRMI");
                    servidorPrincipal.ping();
                    failed = 0;
                } catch (RemoteException e) {
                    failed++;
                    System.out.println("Heartbeat falhou." + failed + "/" + totalTries);
                    didFail = true;
                }
                if (didFail && failed == 0) {
                    didFail = false;
                    System.out.println("Heartbeat recuperado.");
                }

            }
            servidorPrincipal();

        } catch (NotBoundException | RemoteException e) {
            servidorPrincipal();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.exit(0);
        }
    }

    public static void servidorPrincipal() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.setProperty("java.rmi.server.hostname", RMIHostIP);
            Registry r = LocateRegistry.createRegistry(RMIHostPort);
            RMIServer servidor = new RMIServer();
            r.rebind("ServidorRMI", servidor);

            System.out.println("Servidor RMI Principal em execucao. [" + System.getProperty("java.rmi.server.hostname")
                    + " " + RMIHostPort + "]");
            System.out.println("Pressione Enter para encerrar servidor.");
            reader.readLine();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException ignored) {
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

    public static void loadProperties() {
        try {
            InputStream input = new FileInputStream(pathToProperties);
            Properties prop = new Properties();
            prop.load(input);
            RMIHostIP = prop.getProperty("RMIHostIP");
            RMIHostPort = Integer.parseInt(prop.getProperty("RMIHostPort"));
        } catch (FileNotFoundException e) {
            System.out.println("Ficheiro de configurações não encontrado");
        } catch (IOException e) {
            System.out.println("Erro na leitura de ficheiro de configurações");
        }
    }

    public String addPessoa(String nome, String password, Departamento departamento, String telefone, String morada,
                            String numberCC, GregorianCalendar expireCCDate, Profissao profissao) throws RemoteException {
        if (getPessoaByCC(numberCC) == null) {
            Pessoa pessoa = new Pessoa(nome, password, departamento, telefone, morada, numberCC, expireCCDate,
                    profissao);
            this.pessoas.add(pessoa);
            save("pessoas");
            return nome + "(" + numberCC + ") adicionado.";
        }
        else {
            return nome + "(" + numberCC + ") jA existe.";
        }

    }

    public String addPessoa(String nome, String password, Departamento departamento, String telefone, String morada,
                            String numberCC, GregorianCalendar expireCCDate, Profissao profissao, boolean isAdmin) throws RemoteException {
        if (getPessoaByCC(numberCC) == null) {
            Pessoa pessoa = new Pessoa(nome, password, departamento, telefone, morada, numberCC, expireCCDate,
                    profissao, isAdmin);
            this.pessoas.add(pessoa);
            save("pessoas");
            return nome + "(" + numberCC + ") adicionado.";
        }
        else {
            return nome + "(" + numberCC + ") jA existe.";
        }

    }

    public String addEleicao(String titulo, String descricao, GregorianCalendar dataInicio, GregorianCalendar dataFim,
                             CopyOnWriteArrayList<Profissao> profissoes, CopyOnWriteArrayList<Departamento> departamentos) throws RemoteException {
        if (getEleicaoByName(titulo) == null) {
            Eleicao eleicao = new Eleicao(dataInicio, dataFim, titulo, descricao, profissoes, departamentos);
            this.eleicoes.add(eleicao);
            save("eleicoes");
            Timer time = new Timer(); // Instantiate Timer Object
            time.schedule(new CustomTask(this, eleicao), eleicao.getDataFim().getTimeInMillis() - (new GregorianCalendar()).getTimeInMillis());
            return titulo + " adicionada.";
        }
        else {
            return titulo + " ja existe.";
        }
    }

    public String editEleicao(String tituloAntigo, String tituloNovo, String descricaoNova,
                              GregorianCalendar dataInicio, GregorianCalendar dataFim) throws RemoteException {
        Eleicao escolhida = getEleicaoByName(tituloAntigo);
        if (escolhida == null || getEleicaoByName(tituloNovo) != null) {
            return tituloAntigo + " nao existe ou titulo novo ja em uso.";
        }
        String status = escolhida.editDados(tituloNovo, descricaoNova, dataInicio, dataFim);
        save("eleicoes");
        return status;
    }

    public String addMesa(Departamento departamento, CopyOnWriteArrayList<Pessoa> membros, String ip, String port)
            throws RemoteException {
        if (getMesaByDepartamento(departamento.name()) == null && getMesaByMulticastGroup(ip, port) == null) {
            MesaVoto mesa = new MesaVoto(departamento, membros, ip, port);
            this.mesas.add(mesa);
            save("mesas");
            return departamento + " adicionada.";
        }
        else {
            return departamento + " ou grupo Multicast ja existe.";
        }
    }

    public String removeMesa(String nomeMesa) throws RemoteException {
        MesaVoto mesa = getMesaByDepartamento(nomeMesa);
        if (mesa == null) {
            return "Mesa nao existe.";
        }
        if (mesa.getStatus()) {
            return "Mesa ligada ao servidor. Desligue mesa primeiro.";
        }
        for (Eleicao ele : eleicoes) {
            ele.removeMesa(mesa);
        }
        mesas.remove(mesa);
        save("eleicoes");
        save("mesas");
        return "Mesa removida.";
    }

    public String addMesaEleicao(String nomeMesa, String nomeEleicao) throws RemoteException {
        MesaVoto mesa = getMesaByDepartamento(nomeMesa);
        Eleicao ele = getEleicaoByName(nomeEleicao);
        if (mesa == null) {
            return "Mesa nao existe.";
        }
        if (ele == null) {
            return "Eleicao nao existe.";
        }
        String status = ele.addMesa(mesa);
        save("eleicoes");
        save("mesas");
        return status;
    }

    public String removeMesaEleicao(String nomeMesa, String nomeEleicao) throws RemoteException {
        MesaVoto mesa = getMesaByDepartamento(nomeMesa);
        Eleicao ele = getEleicaoByName(nomeEleicao);
        if (mesa == null) {
            return "Mesa nao existe.";
        }
        if (ele == null) {
            return "Eleicao nao existe.";
        }
        String status = ele.removeMesa(mesa);
        save("eleicoes");
        return status;
    }

    public String editMesa(String nomeMesa, String membro1, String membro2, String membro3) throws RemoteException {
        MesaVoto mesa = getMesaByDepartamento(nomeMesa);
        if (mesa == null) {
            return "Mesa nao existe.";
        }
        CopyOnWriteArrayList<Pessoa> membros = new CopyOnWriteArrayList<Pessoa>();
        Pessoa p1 = new Pessoa(membro1, membro1, Departamento.DA, "-1", "-1", "-1", new GregorianCalendar(),
                Profissao.Estudante);
        Pessoa p2 = new Pessoa(membro2, membro2, Departamento.DA, "-1", "-1", "-1", new GregorianCalendar(),
                Profissao.Estudante);
        Pessoa p3 = new Pessoa(membro3, membro3, Departamento.DA, "-1", "-1", "-1", new GregorianCalendar(),
                Profissao.Estudante);
        membros.add(p1);
        membros.add(p2);
        membros.add(p3);
        mesa.setMembros(membros);
        save("mesas");
        return "Membros de Mesa alterados.";
    }

    public String addLista(String nomeEleicao, String nomeLista, CopyOnWriteArrayList<Pessoa> listaPessoas, Profissao tipoLista)
            throws RemoteException {
        Eleicao election = getEleicaoByName(nomeEleicao);
        if (election == null) {
            return nomeEleicao + " nao existe.";
        }

        Lista lista = new Lista(listaPessoas, tipoLista, nomeLista);
        String status = election.addLista(lista);
        save("eleicoes");
        return status;

    }

    public String removeLista(String nomeEleicao, String nomeLista) throws RemoteException {
        Eleicao election = getEleicaoByName(nomeEleicao);
        if (election == null) {
            return nomeEleicao + " nao existe.";
        }
        String status = election.removeLista(nomeLista);
        save("eleicoes");
        return status;
    }

    public String adicionarVoto(String nomeEleicao, Voto voto, String nomeLista, Departamento dep) throws RemoteException {
        Eleicao ele = null;
        if (dep == null) {
            ele = getEleicaoByName(nomeEleicao);
        }
        else {
            ele = getEleicaoByName(nomeEleicao, dep);
        }


        if (ele == null) {
            return "false | Voto nao aceite (getElecao)";
        }

        String tipo = "";

        Lista candidato = ele.getListaByName(nomeLista);

        if (candidato != null) {
            tipo = "Valido";
        }
        else if (nomeLista.equals("Branco")) {
            tipo = "Branco";
        }
        else {
            tipo = "Nulo";
        }

        boolean hasVoted = ele.addVoto(voto, nomeLista, tipo);

        if (hasVoted) {
            save("eleicoes");
            JSONObject data = new JSONObject();
            data.put("tipo", "voto");
            data.put("nome", voto.getPessoa().getNome());
            data.put("profissao", voto.getPessoa().getProfissao().name());
            data.put("eleicao", ele.getTitulo());
            data.put("mesa", dep.name());
            data.put("data", printGregorianCalendar(new GregorianCalendar(), true));
            sendToAll(data);
            return "true | Voto com Sucesso.";
        }
        return "false | Voto nao aceite.";
    }

    public String addVotoAntecipado(String numeroCC, String password, String nomeEleicao, String nomeLista)
            throws RemoteException {
        Pessoa p = getPessoaByCC(numeroCC);
        Eleicao ele = getEleicaoByName(nomeEleicao);
        if (p == null || !(p.getPassword().equals(password))) {
            return "Pessoa ou password incorreta.";
        }
        if (ele == null) {
            return "Eleicao nao existe.";
        }
        Voto v = new Voto(p, new GregorianCalendar());
        String tipo = "";

        Lista candidato = ele.getListaByName(nomeLista);
        if (candidato != null) {
            tipo = "Valido";
        }
        else if (nomeLista.equals("Branco")) {
            tipo = "Branco";
        }
        else {
            tipo = "Nulo";
        }

        String status = ele.addVotoAntecipado(v, nomeLista, tipo);
        if (status.equals("Voto realizado com sucesso.")) {
            save("eleicoes");
            JSONObject data = new JSONObject();
            data.put("tipo", "voto");
            data.put("nome", p.getNome());
            data.put("profissao", p.getProfissao().name());
            data.put("eleicao", ele.getTitulo());
            data.put("mesa", null);
            data.put("data", printGregorianCalendar(new GregorianCalendar(), true));
            sendToAll(data);
        }

        return status;
    }

    public Resultado getResultados(String nomeEleicao) throws RemoteException {
        Eleicao ele = getEleicaoByName(nomeEleicao);

        if (ele == null) {
            return null;
        }
        if (!ele.checkEnd()) {
            return null;
        }
        return ele.getResultados();
    }

    public Voto getVoto(String numeroCC, String nomeEleicao) throws RemoteException {
        Pessoa p = getPessoaByCC(numeroCC);
        Eleicao ele = getEleicaoByName(nomeEleicao);

        if (p == null) {
            return null;
        }
        if (ele == null) {
            return null;
        }
        return ele.getVotoByCC(numeroCC);
    }

    public Pessoa getPessoaByCC(String numberCC) throws RemoteException {
        for (Pessoa pessoa : pessoas) {
            if (pessoa.getNumberCC().equals(numberCC))
                return pessoa;
        }
        return null;
    }

    public Eleicao getEleicaoByName(String nome) throws RemoteException {
        for (Eleicao eleicao : eleicoes) {
            if (eleicao.getTitulo().equals(nome))
                return eleicao;
        }
        return null;
    }

    public Eleicao getEleicaoByName(String nome, Departamento dep) throws RemoteException {
        for (Eleicao eleicao : eleicoes) {
            if (eleicao.getTitulo().equals(nome) && eleicao.getMesaVotoByDepartamento(dep) != null)
                return eleicao;
        }
        return null;
    }

    public MesaVoto getMesaByDepartamento(String dep) throws RemoteException {
        for (MesaVoto mesa : mesas) {
            if (mesa.getDepartamento().name().equals(dep)) {
                return mesa;
            }
        }
        return null;
    }

    public void turnMesa(MesaVoto mesa, Boolean flag) throws RemoteException {
        MesaVoto mesavoto = this.getMesaByDepartamento(mesa.getDepartamento().toString());

        if (flag) {
            JSONObject data = new JSONObject();
            data.put("tipo", "mesa");
            data.put("estado", "ligado");
            data.put("mesa", mesavoto.getDepartamento().name());
            data.put("data", printGregorianCalendar(new GregorianCalendar(), true));
            sendToAll(data);
        }
        else {
            mesavoto.turnOff();
            JSONObject data = new JSONObject();
            data.put("tipo", "mesa");
            data.put("estado", "desligado");
            data.put("mesa", mesavoto.getDepartamento().name());
            data.put("data", printGregorianCalendar(new GregorianCalendar(), true));
            sendToAll(data);
        }

    }

    public MesaVoto getMesaByMulticastGroup(String ip, String port) throws RemoteException {
        for (MesaVoto mesa : mesas) {
            if (mesa.getIp().equals(ip) && mesa.getPort().equals(port)) {
                return mesa;
            }
        }
        return null;
    }

    public CopyOnWriteArrayList<Pessoa> listPessoas() throws RemoteException {
        return pessoas;
    }

    public CopyOnWriteArrayList<Eleicao> listEleicoes() throws RemoteException {
        return eleicoes;
    }

    public CopyOnWriteArrayList<Eleicao> listEleicoes(MesaVoto mesa) throws RemoteException {
        CopyOnWriteArrayList<Eleicao> lista = new CopyOnWriteArrayList<>();
        Departamento departamento = mesa.getDepartamento();
        for (Eleicao eleicao : this.eleicoes) {
            if (eleicao.getMesaVotoByDepartamento(departamento) != null)
                lista.add(eleicao);
        }
        return lista;
    }

    public CopyOnWriteArrayList<Lista> listListas(String nomeEleicao) throws RemoteException {
        CopyOnWriteArrayList<Lista> lista = new CopyOnWriteArrayList<>();

        Eleicao escolhida = getEleicaoByName(nomeEleicao);
        if (escolhida != null) {
            lista = escolhida.getListas();
        }
        return lista;
    }

    public CopyOnWriteArrayList<MesaVoto> listMesas() throws RemoteException {
        return mesas;
    }

    public String login(String cc, String password, boolean web) throws RemoteException {

        for (Pessoa pessoa : pessoas) {
            if (pessoa.getNumberCC().equals(cc) && pessoa.getPassword().equals(password)) {
                JSONObject data = new JSONObject();
                data.put("tipo", "utilizador");
                data.put("nome", pessoa.getNome());
                data.put("cc", pessoa.getNumberCC());
                data.put("estado", "login");
                if (web) {
                    data.put("ligacao", "web");
                    loggedInUsers.put(pessoa, "web");
                }
                else {
                    data.put("ligacao", "multicast");
                    loggedInUsers.put(pessoa, "multicast");
                }
                data.put("data", printGregorianCalendar(new GregorianCalendar(), true));
                sendToAll(data);

                return "true | Login com Sucesso";
            }


        }
        return "false | Login Incorreto";
    }

    public void logout(String cc, String password, boolean web) throws RemoteException {
        for (Pessoa pessoa : pessoas) {
            if (pessoa.getNumberCC().equals(cc) && pessoa.getPassword().equals(password)) {
                JSONObject data = new JSONObject();
                data.put("tipo", "utilizador");
                data.put("nome", pessoa.getNome());
                data.put("cc", pessoa.getNumberCC());
                data.put("estado", "logout");
                if (web) {
                    data.put("ligacao", "web");
                    loggedInUsers.remove(pessoa);
                }
                else {
                    data.put("ligacao", "multicast");
                    loggedInUsers.remove(pessoa);
                }
                data.put("data", printGregorianCalendar(new GregorianCalendar(), true));
                sendToAll(data);
            }
        }
    }

    public HashMap<Pessoa, String> getUsersLoggedIn() throws RemoteException {
        return loggedInUsers;
    }

    public void ping() throws RemoteException {
    }

    public void save(String arrayName) {
        File file = new File("./ObjectFiles/" + arrayName + ".obj");
        writeObjects(arrayName, file);
    }

    public void saveAll() {

        File eleicoes = new File("./ObjectFiles/eleicoes.obj");
        File pessoas = new File("./ObjectFiles/pessoas.obj");
        File mesas = new File("./ObjectFiles/mesas.obj");
        File consolas = new File("./ObjectFiles/consolas.obj");

        writeObjects("eleicoes", eleicoes);
        writeObjects("pessoas", pessoas);
        writeObjects("mesas", mesas);
        writeObjects("consolas", consolas);
    }

    synchronized public void writeObjects(String aux, File f) {

        try {
            f.getParentFile().mkdirs();
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            switch (aux) {
                case "eleicoes":
                    oos.writeObject(eleicoes);
                    break;
                case "pessoas":
                    oos.writeObject(pessoas);
                    break;
                case "mesas":
                    oos.writeObject(mesas);
                    break;
                case "consolas":
                    oos.writeObject(consolas);
                    break;
                default:
                    System.out.println("Erro: Array nao existente.");
            }
            oos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Erro a criar ficheiro.");
        } catch (IOException ex) {
            System.out.println("Erro a escrever para o ficheiro.");
        }
    }

    public void load() {

        File eleicoes = new File("./ObjectFiles/eleicoes.obj");
        File pessoas = new File("./ObjectFiles/pessoas.obj");
        File mesas = new File("./ObjectFiles/mesas.obj");
        File consolas = new File("./ObjectFiles/consolas.obj");

        readObjects("eleicoes", eleicoes);
        readObjects("pessoas", pessoas);
        readObjects("mesas", mesas);
        readObjects("consolas", consolas);
    }

    synchronized public void readObjects(String aux, File f) {

        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);

            switch (aux) {
                case "eleicoes":
                    eleicoes = (CopyOnWriteArrayList<Eleicao>) ois.readObject();
                    break;
                case "pessoas":
                    pessoas = (CopyOnWriteArrayList<Pessoa>) ois.readObject();
                    break;
                case "mesas":
                    mesas = (CopyOnWriteArrayList<MesaVoto>) ois.readObject();
                    break;
                case "consolas":
                    consolas = (CopyOnWriteArrayList<RMI_C_Interface>) ois.readObject();
                    break;
            }
            ois.close();
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro a converter objeto");
        } catch (FileNotFoundException ex) {
            System.out.println("Erro a abrir ficheiro.");
        } catch (IOException ex) {
            System.out.println("Erro a ler ficheiro.");
        }
    }

    public void subscribe(RMI_C_Interface c) throws RemoteException {
        consolas.add(c);
        save("consolas");
    }

    public void unsubscribe(RMI_C_Interface c) throws RemoteException {
        consolas.remove(c);
        save("consolas");
    }


    public void sendToAll(JSONObject data) {
        StringWriter out = new StringWriter();
        try {
            data.writeJSONString(out);
            String jsonText = out.toString();
            for (RMI_C_Interface consola : consolas) {
                try {
                    consola.printOnClient(jsonText);
                } catch (RemoteException e) {
                    consolas.remove(consola);
                    save("consolas");
                }
            }
        } catch (IOException e) {
            System.out.println("Erro a processar notificação.");
        }
    }
}

class CustomTask extends TimerTask {
    Eleicao ele;
    RMIServer servidor;

    public CustomTask(RMIServer serv, Eleicao eleicao) {
        super();
        ele = eleicao;
        servidor = serv;
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

    public void run() {
        try {
            while (true) {
                if (ele != null && ele.getDataFim().compareTo(new GregorianCalendar()) < 60000) {
                    JSONObject data = new JSONObject();
                    data.put("tipo", "eleicao");
                    data.put("nome", ele.getTitulo());
                    data.put("estado", "terminou");
                    data.put("data", printGregorianCalendar(new GregorianCalendar(), true));

                    servidor.sendToAll(data);
                    break;
                }
                else if (ele != null && !ele.getDataFim().before(new GregorianCalendar())) {
                    sleep(ele.getDataFim().getTimeInMillis() - (new GregorianCalendar().getTimeInMillis()));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
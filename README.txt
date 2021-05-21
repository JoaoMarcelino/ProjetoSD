COMO EXECUTAR O PROJETO

1.Executar "java -jar rmiserver.jar" numa consola para ligar o servidor RMI principal. Executar este comando uma segunda vez caso pretenda um servidor RMI secundário em caso de falha.
    Junto com o ficheiro .jar deverá estar um ficheiro de configuração nomeado config.properties contendo o IP e porto onde o servidor irá correr.
    Ex .:
        RMIHostIP=127.0.0.1
        RMIHostPort=8000
    Caso tenha ficheiros .obj para carregamento de dados no servidor, tais ficheiros deverão estar numa pasta "ObjectFiles" na mesma diretoria que o executável e nomeados com os seguintes nomes: "mesas.obj","pessoas.obj","consolas.obj","eleicoes.obj".
    Exemplo de árvore de diretorias esperada:
    .
    |__rmiserver.jar
    |
    |_config.properties
    |
    |__ObjectFiles
        |
        |__pessoas.obj
        |__eleicoes.obj
        |__consolas.obj
        |__mesas.obj

2.Executar "java -jar console.jar {RMIHostIP} {RMIHostPort}" numa consola para ligar uma consola de administracao. Os paramêtros deverão ser os mesmos estabelecidos no comando anterior de forma a ser possivel estabelecer conexão com o servidor RMI.
3.Ligar um servidor multicast (Mesa de voto)  que tenha sido previamente criado na consola de administracao: executar "java -jar server.jar {departamento} {RMIHostIP} {RMIHostPort}" numa consola.
    O departamento deverá ser o mesmo a que foi associada a mesa na sua criacao(ex:DEI) e os argumentos restantes iguais aos dos comandos anteriores.
4.Conectar um terminal de voto à mesa correspondente que tenha sido previamente criado na consola de administracao: executar "java -jar terminal.jar  {MulticastIP} {MulticastPort}" numa consola. 
    Os argumentos deverão ser iguais aos mostrados no arranque da mesa de voto a que se pretende conectar (ex:224.0.0.1 4000).

5.WebServer
    O ficheiro .war relativo ao servidor web desenvolvido denomina-se Hey.war. É importante que este tenha também acesso ao ficheiro de configurações, à semelhança do servidor RMI. Neste caso tal ficheiro deve ser colocado na diretoria ...\apache-tomcat-8.5.65\webapps\Hey\WEB-INF\classes\resources.
    É lá que está a informação necessária para a conexão com o servidor RMI(IP e porto) e as chaves de acesso ao serviço REST do Facebook
    Ex .:
        RMIHostIP=127.0.0.1
        RMIHostPort=8000
        apiKey=5024XX907XX1472
        apiSecret=d8b4XXXX10b6XXXX0c0d6XXXX2fca5e1
    
    O endereço de entrada da aplicação web é http://{host}/Hey/index
    Tratando-se de uma aplicação desenvolvida em contexto académico,foi deixada a possibilidade de realizar login com as seguintes credenciais: (Numero CC:admin;Password:admin) de modo a que seja possível aceder à aplicação mais facilmente.

Consideracões:
    Caso ligue componentes do projeto em máquinas diferentes, o arranque de um dado componente pode levar ~30s devido á conexão ao servidor RMI demorar particularmente mais tempo entre máquinas distintas.
    Durante os testes não foi possível estabelecer conexão entre uma mesa de voto e um terminal em máquinas distintas devido às limitações do Multicast.
    É importante que estabeleça um IP no servidor RMI que todos os componentes consigam aceder. Os testes realizados incidiram sempre na mesma rede local.
    Nos testes realizados não foi necessário desligar a firewall do SO, no entanto, poderá fazê-lo caso encontre dificuldades no arranque dos executáveis do projeto.


COMPILAR E GERAR .JAR DO PROJETO
    Os ficheiros .jar fornecidos foram gerados a partir do software Itellij IDEA. pelo que, se pretender gerá-los novamente, aconselha-se a fazê-lo também através do software referido.
    As classes java principais a ter em conta serão RMIServer, AdminConsole, MulticastServer, MulticastClient.

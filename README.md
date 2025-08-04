
# Gerenciador de Roteiros

Este projeto tem o intuito de fazer um gerenciador de roteiros em um cenário fictício criado pela empresa NAVA.

Este projeto foi feito para rodar em docker, porém tem a possibilidade de rodar os projetos nativamente.



## Instalação e Uso

### Requisitos
Os requisitos para a instalação e utilização desse projeto são:
- docker e docker compose
- java 21
- maven 3.9.x

Se for rodar fora de docker, serão necessário o requisito:
- mariadb

### Como instalar
Primeiro clone este repositório com

```bash
  git clone https://github.com/Islaifer/script-manager-challenge.git
```

Após isso, vá até o diretório que o projeto foi clonado.

(Opcional)
Na pasta raíz do projeto possui o arquivo de docker-compose (docker-compose.yml), nele será possível fazer as modificações de acordo com suas necessidades, como para onde vão os logs por exemplo. Para alterar a pasta de log, modifique sua linha

```bash
  volumes:
      - /tmp/logs/script-service:/logs
```

Onde a primeira parte dos : seria o destino do log deste modulo.

Nota: se deseja alterar usuário e senha do mariadb, não esqueça de mudar também no "DB_USER" e "DB_PASS" da sessão de environment de cada módulo no arquivo.

Com isso, basta apenas executar o script de build and start para buildar os projetos e rodar via docker.

#### Para Windows

Utilize o power shell e execute o script build-and-start.ps1 com:

```bash
  .\build-and-start.ps1
```
Certifique-se de que o diretório esteja em um caminho onde as pastas correspondentes NÃO tenham nenhum espaço ou acento no nome.  

Nota para usuários de windows:
Caso seu sistema de política de execução do power shell seja Restricted, então será necessário mudar temporariamente para executar o script. Para fazer a alteração temporária, utilize:

```bash
  Set-ExecutionPolicy RemoteSigned -Scope Process
```

#### Para Linux

Dê permissão de execução ao script e o execute:

```bash
  chmod +x build-and-start.sh
  ./build-and-start.sh
```

#### Nota: Tanto para Windows quanto para Linux, NÃO execute o script de build e run como root (ou adm), quando for rodar o docker dentro do script ele pedirá a permissão.

### Rodando sem docker
Para rodar sem docker, primeiro configure as variáveis de ambiente:
- DB_URL com o host para o banco de dados (ex: localhost)
- DB_PORT com a porta do seu banco de dados
- DB_USER com o nome do usuario de banco de dados
- DB_PASS com a senha do usuario de banco de dados
- JWT_SECRET com o secret de encode para usar na geração do token jwt

Rode na pasta raiz do projeto clonado as seguintes linhas de código:

```bash
  mvn clean install -N
  mvn clean install
```

Depois de buildar, vá no diretório dos dois módulos do projeto (auth-service e script-service) e rode o jar que estará no diretório target/ da seguinte forma:

auth-service:
```bash
  java -jar auth-service-1.0.0.jar
```

script-service:
```bash
  java --add-opens java.base/java.lang=ALL-UNNAMED -jar script-service-1.0.0.jar
```

Você também pode executar os modulos dentro de uma IDE desejada, só não esqueça de configurar as variáveis de ambiente e subir o mariadb.



## Uso
Após isso, sempre que quiser executá-lo por docker pode apenas utilizar o script de build and start.

Com o projeto rodando via docker, basta acessar as chamadas de api pelo host localhost:8080.

Foi criado 5 usuários de teste com permissões para utilizar as rotas protegidas, as credenciais e permissão deles são:
  -Analista: username:jorge | email:jorge@fakemail.com | password:jorge123
  -Revisor: username:amanda | email:amanda@fakemail.com | password:amanda123
  -Aprovador: username:sabrina | email:sabrina@fakemail.com | password:sabrina123
  -Aprovador: username:joao | email:joao@fakemail.com | password:joao123
  -Aprovador: username:mirian | email:mirian@fakemail.com | password:mirian123

Com eles, você consegue usar as rotas protegidas com as permissões necessárias.


## Documentação da API

O projeto disponibiliza das seguintes rotas de api:

### /auth/login
Verbo http: POST

Request Body:
```json
{
    "username": "string",
    "email": "string",
    "password": "string"
}
```

Response Body:
```json
{
    "token": "string"
}
```
Descrição: Essa rota serve para autenticação via JWT. Ela recebe um json contendo as informações de login (funciona tanto por username quanto por email), e retorna um token jwt, que é usado para autenticar nas rotas protegidas

### /client/v1
Verbo http: POST

Request Body:
```json
{
    "clientFullName": "string",
    "clientEmail": "string",
    "clientPhone": "string",
    "script": "string"
}
```
Descrição: Essa rota é uma rota não protegida que serve para criar um novo roteiro a ir para a esteira de avaliação, revisão e aprovação. Essa api recebe um objeto contendo informações do cliente e o roteiro, e retorna o Status Code com uma string simples.

### /client/v1/{clientFullName}/{clientEmail}/{clientPhone}
Verbo http: GET

Path Params:

  -clientFullName: Uma String com o nome completo do cliente (use %20 para o espaço do nome);

  -clientEmail: Uma String com o email do cliente (use %40 para o @ do email);

  -clientPhone: Uma String com o telefone do cliente

Response Body:
```json
[
    {
        "id": 0,
        "clientFullName": "string",
        "clientEmail": "string",
        "clientPhone": "string",
        "script": "string",
        "status": "string",
        "analystNotes": "string",
        "proofreaderNotes": "string",
        "createDate": "date",
        "actualUser": {},
        "approversVotes": []
    }
]
```
Descrição: Essa rota é uma rota não protegida que serve para verificar um roteiro que foi criado, passando no path os dados necessários do cliente e retornando os dados completos dos roteiros relacionados aos dados do cliente que foram passados, incluindo o status (se está em analise, aguardando aprovação ou afins), notas do analista e do revisor, usuário atual que está trabalhando no roteiro e os votos.

### /user/v1/script
Verbo http: GET

Requer passar Token jwt no Authorization como bearer:
```bash
Authorization: Bearer TOKEN
```

Query Params:

  -status: Filtra pelo status atual do roteiro, podendo ser: aguardando_analise, em_analise, aguardando_revisao, em_revisao, aguardando_aprovacao, em_aprovacao, aprovado, recusado;

  -beforeDate: Filtra para roteiros que foram criados antes da data inserida;

  -afterDate: Filtra para roteiros que foram criados depois da data inserida;

Response Body:
```json
[
    {
        "id": 0,
        "clientFullName": "string",
        "clientEmail": "string",
        "clientPhone": "string",
        "script": "string",
        "status": "string",
        "analystNotes": "string",
        "proofreaderNotes": "string",
        "createDate": "date",
        "actualUser": {},
        "approversVotes": []
    }
]
```
Descrição: Essa é uma rota protegida que retorna todos os scripts, sendo possível passar query params para filtrar os roteiros existentes. Essa chamada retorna os dados completos dos roteiros, incluindo o status (se está em analise, aguardando aprovação ou afins), notas do analista e do revisor, usuário atual que está trabalhando no roteiro e os votos.

### /user/v1/script/{scriptId}
Verbo http: GET

Path Params:

  -scriptId: Um long contendo o id do roteiro que quer ler

Requer passar Token jwt no Authorization como bearer:
```bash
Authorization: Bearer TOKEN
```

Response Body:
```json
{
    "id": 0,
    "clientFullName": "string",
    "clientEmail": "string",
    "clientPhone": "string",
    "script": "string",
    "status": "string",
    "analystNotes": "string",
    "proofreaderNotes": "string",
    "createDate": "date",
    "actualUser": {},
    "approversVotes": []
}
```

Descrição: Essa é uma rota protegida que retorna um roteiro em especifico para o id passado via path param. Essa chamada retorna os dados completos dos roteiros, incluindo o status (se está em analise, aguardando aprovação ou afins), notas do analista e do revisor, usuário atual que está trabalhando no roteiro e os votos.

### /user/v1/script/assigned
Verbo http: GET

Requer passar Token jwt no Authorization como bearer:
```bash
Authorization: Bearer TOKEN
```

Query Params:

  -status: Filtra pelo status atual do roteiro, podendo ser: aguardando_analise, em_analise, aguardando_revisao, em_revisao, aguardando_aprovacao, em_aprovacao, aprovado, recusado;

  -beforeDate: Filtra para roteiros que foram criados antes da data inserida;

  -afterDate: Filtra para roteiros que foram criados depois da data inserida;

Response Body:
```json
[
    {
        "id": 0,
        "clientFullName": "string",
        "clientEmail": "string",
        "clientPhone": "string",
        "script": "string",
        "status": "string",
        "analystNotes": "string",
        "proofreaderNotes": "string",
        "createDate": "date",
        "actualUser": {},
        "approversVotes": []
    }
]
```

Descrição: Essa é uma rota protegida que retorna todos os roteiros que estão sendo trabalhados por algum usuário (valido apenas para analista e revisor). Essa chamada retorna os dados completos dos roteiros, incluindo o status (se está em analise, aguardando aprovação ou afins), notas do analista e do revisor, usuário atual que está trabalhando no roteiro e os votos.

### /user/v1/script/{scriptId}/assign
Verbo http: PATCH

Path Params:

  -scriptId: Um long contendo o id do roteiro que quer assinar

Requer passar Token jwt no Authorization como bearer:
```bash
Authorization: Bearer TOKEN
```

Descrição: Essa é uma rota protegida que serve para direcionar um roteiro a um usuário (valido somente para analista e revisor), mudando o status do roteiro se for conveniente. Essa chamada retorna apenas o Status Code com uma string simples.

### /user/v1/script/{scriptId}/analyse/{approve}
Verbo http: PATCH

Path Params:

  -scriptId: Um long contendo o id do roteiro que quer analisar

  -approve: Um boolean contendo a informação se passa ou não na análise

Requer passar Token jwt no Authorization como bearer:
```bash
Authorization: Bearer TOKEN
```

Request Body:
```json
{
    "note": "string"
}
```

Descrição: Essa é uma rota protegida que serve para o analista passar sua analise sobre o roteiro, passando pelo path se ele foi aprovado nessa fase ou não e no body passando a suas anotações sobre o roteiro. Essa chamada retorna apenas o Status Code com uma string simples.

### /user/v1/{scriptId}/5/review/{approve}
Verbo http: PATCH

Path Params:

  -scriptId: Um long contendo o id do roteiro que quer revisar

  -approve: Um boolean contendo a informação se passa ou não na revisão

Requer passar Token jwt no Authorization como bearer:
```bash
Authorization: Bearer TOKEN
```

Request Body:
```json
{
    "note": "string"
}
```

Descrição: Essa é uma rota protegida que serve para o revisor passar sua revisão sobre o roteiro, passando pelo path se ele foi aprovado nessa fase ou não e no body passando a suas anotações sobre o roteiro. Essa chamada retorna apenas o Status Code com uma string simples.

### /user/v1/script/{scriptId}/vote/{approve}
Verbo http: PATCH

Path Params:

  -scriptId: Um long contendo o id do roteiro que quer votar na aprovação

  -approve: Um boolean contendo se seu voto foi para aprovar (true) ou reprovar (false)

Requer passar Token jwt no Authorization como bearer:
```bash
Authorization: Bearer TOKEN
```

Descrição: Essa é uma rota protegida que serve para o aprovador dar seu voto se aprova ou não o roteiro, passando pelo path o voto, se foi de aprovação ou não. Essa chamada retorna apenas o Status Code com uma string simples.



## Documentação

A aplicação utiliza de 2 módulos, um que serve como serviço de authenticação e outro que serve para os serviços da regra de negócio, tendo as rotas orquestradas pelo nginx, ou seja, não precisa de 2 rotas para acessar os projetos, apenas estando rodando via docker é possível fazer a chamada com um host único que é direcionado ao serviço especifico.

A aplicação possuí também um shared-lib, para centralizar os objetos principais da aplicação, como entidades e dtos. Essa biblioteca é inserida nos serviços principais atravéz do maven.

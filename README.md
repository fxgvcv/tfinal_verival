# informativo-para-imigrantes-backend

# Documentação
 Acesse Swagger em  localhost:8080/swagger-ui/index.html

# Estrutura do Projeto 

A estrutura é organizada da seguinte forma:

- :file_folder: `src/main/java/com/ages/informativoparaimigrantes`
  - :file_folder: `config/`              - Configurações personalizadas do app
  - :file_folder: `controller/`              - Requisições HTTP e respostas
  - :file_folder: `domain/`          - Classes para objetos centrais do negócio
  - :file_folder: `dto/`            - Estrutura dos dados que são trocados entre o front-end e o back-end
  - :file_folder: `repository/`                - Acesso e interação com o banco de dados
  - :file_folder: `security/`               - Configurações de autenticação e autorização
  - :file_folder: `services/`              - Implementa a lógica de negócios
  - :file_folder: `util/`             - Funções utilitárias

# Pré-requisitos
* [Java SDK](https://www.techspot.com/downloads/5553-java-jdk.html) (v 11.0.14)
Para verificar instalação: 
```
java -version
```

* [Gradle](https://gradle.org/releases/) 
Para verificar instalação: 
```
gradle -v
```

* [IntelliJ](https://www.jetbrains.com/pt-br/idea/download/) 

## Instalar dependências do projeto
 [Sugerida a utilização de package manager]
* [Gradle](https://gradle.org/install/)


# Executar
## Clonar repositório em sua pasta
```bash 
$ cd PLUGIN_FOLDER
$ git clone https://tools.ages.pucrs.br/informativo-para-imigrantes/informativo-para-imigrantes-backend.git
$ cd informativo-para-imigrantes-backend
```

## Executar o projeto no ambiente de desenvolvimento
```bash
$ gradle bootRun -Dspring.profiles.active=dev
```

# Git Workflow
O Gitflow é um modelo alternativo de ramificação do Git que consiste no uso de ramificações de recursos (features) e várias ramificações primárias (master e develop).


 ## Branches
Cada branch relacionada à features será criada a partir da branch develop. Nos tópicos abaixo será explicado as nomenclatura que serão utilizadas para o desenvolvimento.

### Nomes

O nome da branch será em inglês e deve seguir o padrão **feature/<user-story>-<nome-da-feature>**. Quando for necessário fazer alguma correção, o prefixo utilizado deverá ser **fix/<user-story>-<nome-da-feature>**.

 ### Requisitos

Para garantir que o processo de desenvolvimento esteja sempre atualizado, lembre-se de executar o seguinte comando na branch dev antes de criar uma branch nova:

```
git pull origin dev
```

 ### Criando branchs

Para criar uma nova branch, execute o comando:
```
git checkout -b <nomeDaBranch>
```

Por exemplo:

```
git checkout -b feature/US01-userType
```

Assim que a branch for criada execute o seguinte comando:

```
git push --set-upstream origin <nome da branch>
```

Dessa forma a branch será enviada para o repositório remoto no GitLab

## Commits


Antes de fazer o commit é necessário preparar as alterações. Temos 2 maneiras de fazer isso:


O comando `git add .` prepara todas as alterações que foram feitas localmente sejam adicionadas ao commit:

```
git add .
```

 O comando `git add <nomeDoArquivo>` prepara apenas as alterações do arquivo informado sejam adicionadas ao commit.

```
git add <nomeDoArquivo>
```



Após adicionar as alterações é necessário commitar elas usando o comando 
```
git commit -m "comentario-do-commit"
```
Faça commit sempre que alguma funcionalidade for alterada, assim garantindo um método fácil de recuperação do código (caso necessário).

Após o commit, compartilhe as alterações no repositório remoto utilizando o comando `git push`


```
git push 
```


## Merge Requests

Assim que uma tarefa for finalizada execute o seguinte comando:

```
git pull origin develop
```

O mesmo irá garantir que sua branch está atualizada com a branch develop (caso haja conflitos, resolva-os) e realize um commit com o seguinte nome:

```
Merge branch 'dev' into '<nome da branch>'
```

Depois de estar com a sua branch remota pronta para merge, crie um Merge Request no GitLab e preencha com as seguintes informações:

* Source Branch: Sua branch.
* Target Branch: branch `develop`.
* Título: `<código da tarefa>-<nome da tarefa utilizando camelCase quando necessário>`
* Descrição: Descrição da tarefa e/ou das mudanças no código

Assim que for criado o Merge Request, passe o card da sua tarefa no trello para `"Review"` e avise um *AGES 3*, *AGES 4* ou líder da Squad.

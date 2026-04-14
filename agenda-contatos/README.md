# 📒 Agenda de Contatos

Aplicação desktop para gerenciar contatos, construída com **Java Swing**, **SQLite** e **Maven**.

## 🧰 Tecnologias

| Tecnologia | Versão | Uso |
|------------|--------|-----|
| Java       | 11+    | Linguagem principal |
| Maven      | 3.8+   | Gerenciamento de dependências |
| Swing      | (JDK)  | Interface gráfica |
| SQLite     | 3.45.1 | Banco de dados local |

## 📁 Estrutura do Projeto

```
agenda-contatos/
├── pom.xml
└── src/main/java/com/agenda/
    ├── Main.java                  ← Ponto de entrada
    ├── model/
    │   └── Contato.java           ← Entidade
    ├── dao/
    │   └── ContatoDAO.java        ← Acesso ao banco de dados
    ├── util/
    │   └── DatabaseUtil.java      ← Conexão SQLite
    └── ui/
        ├── MainFrame.java         ← Janela principal
        ├── ContatoDialog.java     ← Formulário novo/editar
        └── ContatoTableModel.java ← Modelo da tabela
```

## 🚀 Como executar

### Pré-requisitos
- Java 11 ou superior
- Maven 3.8 ou superior

### Compilar e executar

```bash
# 1. Entrar na pasta do projeto
cd agenda-contatos

# 2. Compilar
mvn clean compile

# 3. Executar direto pelo Maven
mvn exec:java -Dexec.mainClass="com.agenda.Main"

# OU gerar JAR único e executar
mvn clean package
java -jar target/agenda-contatos-1.0.0.jar
```

## ✨ Funcionalidades

- ✅ **Adicionar** contato (Nome, Telefone, E-mail, Grupo)
- ✅ **Editar** contato com duplo clique ou botão
- ✅ **Excluir** com confirmação
- ✅ **Busca em tempo real** por nome
- ✅ **Filtro por grupo** (Família, Amigos, Trabalho...)
- ✅ **Ordenação** clicando no cabeçalho da coluna
- ✅ **Persistência** automática no SQLite (arquivo `agenda.db`)

## 🗄️ Banco de Dados

O arquivo `agenda.db` é criado automaticamente na pasta onde o programa é executado.

```sql
CREATE TABLE contatos (
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    nome     TEXT NOT NULL,
    telefone TEXT,
    email    TEXT,
    grupo    TEXT DEFAULT 'Geral'
);
```

## 📈 Próximos Passos (sugestões)

- [ ] Exportar contatos para CSV
- [ ] Importar de arquivo VCF (vCard)
- [ ] Foto do contato
- [ ] Campo de aniversário com alertas
- [ ] Busca por telefone ou e-mail

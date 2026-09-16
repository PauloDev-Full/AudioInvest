# Fundamentação Teórica do Projeto

Este projeto foi desenvolvido como exercício prático no **DIO Santander
Bootcamp 2026 — AI Java Backend**, com o objetivo de consolidar, em uma única
aplicação, conceitos de backend tradicional e de integração com Inteligência
Artificial Generativa. Abaixo estão as bases teóricas que sustentam cada
parte do sistema.

---

## 1. API REST

A aplicação expõe seus recursos (`/investments`) seguindo os princípios REST
(Representational State Transfer):

- **Recursos identificados por URI**: cada investimento é acessado por um
  caminho previsível (`/investments/{category}`).
- **Verbos HTTP com semântica própria**: `POST` para criação,
  `GET` para consulta — sem sobrecarregar um único verbo com múltiplas
  responsabilidades.
- **Statelessness**: cada requisição contém todas as informações necessárias
  para ser processada; o servidor não mantém estado de sessão entre chamadas.
- **Representação via JSON**: entrada e saída trafegam em um formato
  desacoplado da implementação interna, através de DTOs (`InvestmentRequest`,
  `InvestmentResponse`) que isolam o contrato HTTP do modelo de domínio.

## 2. Spring Boot e Inversão de Controle

O framework é a espinha dorsal da aplicação e materializa, na prática, dois
conceitos centrais da engenharia de software moderna:

- **Inversão de Controle (IoC) e Injeção de Dependência (DI)**: classes como
  `InvestmentController` e `PersistInvestmentUseCase` não instanciam suas
  dependências diretamente — elas as recebem prontas via construtor. Isso
  reduz acoplamento e viabiliza testes unitários com substituição de
  implementações (mocks/stubs).
- **Auto-configuração e servidor embarcado**: o Spring Boot elimina a
  necessidade de configurar manualmente um servidor de aplicação, permitindo
  que o foco esteja na regra de negócio.

## 3. Arquitetura em Camadas (Domain / Application / Infrastructure)

O código está organizado seguindo princípios próximos aos de **Domain-Driven
Design (DDD)** e **Arquitetura Limpa**:

- **`domain`**: contém as regras de negócio puras (`Investment`, `Category`,
  `InvestmentRepository` como interface), sem qualquer dependência de
  frameworks. É o núcleo estável do sistema.
- **`application`**: orquestra os casos de uso (`PersistInvestmentUseCase`,
  `ListInvestmentsByCategoryUseCase`), traduzindo intenções externas em
  operações sobre o domínio.
- **`infrastructure`**: implementa os detalhes técnicos — HTTP
  (`InvestmentController`), persistência (`JpaInvestmentRepository`) — de
  forma que poderiam ser trocados sem afetar o núcleo do domínio.

Essa separação segue o **Princípio da Inversão de Dependência** (o "D" do
SOLID): a camada de domínio define a interface `InvestmentRepository`, e é a
infraestrutura que depende do domínio — nunca o contrário.

## 4. Persistência com Spring Data JPA

- **ORM (Mapeamento Objeto-Relacional)**: a entidade `InvestmentEntity`
  traduz o agregado de domínio `Investment` para uma tabela relacional,
  mantendo o modelo de negócio livre de anotações de persistência.
- **Repository Pattern**: `InvestmentEntityRepository` abstrai o acesso a
  dados atrás de uma interface, e `JpaInvestmentRepository` faz a ponte entre
  essa interface técnica e o contrato de domínio.
- **PostgreSQL em contêiner** (via `docker-compose.yaml`): reproduz, em
  ambiente de estudo, a prática de infraestrutura como código, isolando a
  dependência externa (banco de dados) do ciclo de vida da aplicação.

## 5. Spring AI e Integração com Modelos de Linguagem

Este é o eixo mais recente do projeto, correspondente ao módulo de IA do
bootcamp:

- **`ChatClient`**: abstração do Spring AI que unifica o acesso a diferentes
  provedores de LLM (neste caso, OpenAI) sob uma API consistente,
  independente do fornecedor.
- **Tool Calling (Function Calling)**: os métodos anotados com `@Tool`
  (`persist-investment`, `list-investment-by-category`) são expostos ao
  modelo como funções que ele pode decidir invocar. O modelo não executa
  código diretamente — ele interpreta a linguagem natural do usuário, decide
  qual ferramenta é apropriada e gera os argumentos estruturados; a aplicação
  é quem efetivamente executa a chamada. Esse é o conceito central por trás
  de **agentes de IA**.
- **System Prompt como contrato de comportamento**: o arquivo
  `system-message.st` define regras determinísticas (conversão de reais para
  centavos, critérios de categorização) para reduzir a variabilidade natural
  de um modelo generativo — uma prática essencial quando IA generativa é
  aplicada a domínios que exigem precisão, como o financeiro.
- **Modelos de áudio (Whisper e TTS)**: a aplicação encadeia três modelos
  distintos em uma única requisição — transcrição de fala em texto
  (`whisper-1`), geração de linguagem (`gpt-4o-mini`) e síntese de texto em
  fala (`gpt-4o-mini-tts`) —, demonstrando um pipeline multimodal simples:
  voz → texto → decisão/ação → voz.

## 6. Contêineres e Infraestrutura como Código

O uso do Docker Compose para provisionar o PostgreSQL reflete a separação
entre o ciclo de vida da aplicação e o de suas dependências externas,
prática consolidada em ambientes de desenvolvimento e produção modernos.

---

## Intenção por trás do projeto

Este programa não tem como objetivo final ser um produto financeiro real,
mas sim servir como **exercício de consolidação prática** dos conteúdos
trabalhados no **DIO Santander Bootcamp 2026 — AI Java Backend**. A escolha
de um domínio financeiro simples (registro e consulta de investimentos) foi
deliberada: é complexo o suficiente para justificar camadas bem definidas,
mas simples o bastante para que a atenção do estudo recaia sobre a
arquitetura e a integração com IA, e não sobre regras de negócio elaboradas.

A intenção pedagógica se manifesta em três frentes:

1. **Backend tradicional sólido** — aplicar separação de responsabilidades,
   injeção de dependência e persistência de forma correta antes de
   adicionar complexidade.
2. **IA como camada de interface, não como substituta da engenharia** — o
   modelo de linguagem interpreta intenção e linguagem natural, mas quem
   valida regras, garante consistência de dados e persiste informação
   continua sendo código determinístico, escrito e compreendido pelo
   desenvolvedor.
3. **Portfólio e evidência de aprendizado** — o projeto documenta, na
   prática, a capacidade de integrar tecnologias emergentes (Spring AI,
   tool calling, modelos de áudio) a fundamentos já estabelecidos de
   desenvolvimento backend, refletindo o propósito do próprio bootcamp: unir
   engenharia de software convencional a Inteligência Artificial aplicada.
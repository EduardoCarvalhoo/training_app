# training-app
## Sobre o projeto:

Este app é o desafio de estágio em Desenvolvimento Android da empresa Leal Apps. 
No aplicativo é possível cadastrar um novo usuário, fazer a autenticação, visualizar uma lista de treinos e também adicionar
e excluir exercícios, no entanto, ainda não tem associação a cada treino. 

Neste app foi utilizado:
-  Firebase Firestore para a realização do CRUD (criação, listagem, atualização, remoção) das listas de exercício e de treino; 
-  Firebase Authentication para autenticação de usuários;
-  Glide para download e cache das imagens;
-  Os componentes do Material design para a construção dos layouts do app.

1. Tela login:
- Para fazer o login pela primeira vez tem que fazer o cadastro.
- As senhas devem ser maiores que 6 digitos.
- Nessa tela foram tratados os casos de erros em que o email e a senha foram inválidos.
- Na tela de registro segue o mesmo modelo da de login

2. Tela de listagem dos treinos:
- Nesta tela é possível visualizar a lista de treinos cadastrados;
- Ser direcionado para a tela de cadastrar treino;

3. Tela de criar treino:
- Nesta tela é possível cadastrar o nome de um treino.
- Visualizar uma lista de exercícios quando forem adicionados, e excluir os mesmos antes de salvar.

4. Tela de adicionar exercícios:
- Nesta tela é possível adicionar um ou mais exercícios clicando no check box;

## Tecnologias Utilizadas
● Kotlin <br/> 
● Arquitetura MVVM <br/> 
● Injeção de dependência com Koin <br/> 
● Coroutines <br/>

## Funcionalidades que faltam desenvolver
● Associar treinos e exercícios salvando no Firestore <br/>
● Criar tela de detalhes de treinos, possibilitando ver a quantidade de exercícios cadastrados em cada treino  <br/> 
● Criar tela de atualizar treino (Permitindo editar, adionar e excluir exercícios) <br/>
Após isso o CRUD (criação, listagem, atualização, remoção) ficará completo.


# training-app
## Sobre o projeto:

Este app é um o desafio de estágio em Desenvolvimento Android.
No aplicativo é possível cadastrar um novo usuário, fazer a autenticação, cadastrar um treino com os exercícios selecionados, visualizar uma lista de treinos do usuário quando cadastrados, cada treino é possível vizualizar seus detalhes podendo atualizar os mesmos ou excluí-los.

Neste app foi utilizado:
-  Firebase Firestore para a realização do CRUD (criação, listagem, atualização, remoção) das listas de exercício e de treino; 
-  Firebase Authentication para autenticação de usuários;
-  Glide para download e cache das imagens;
-  Os componentes do Material design para a construção dos layouts do app.

1. Tela login:
- Para fazer o login pela primeira vez tem que fazer o cadastro.
- As senhas devem ser maiores que 6 digitos.
- Nessa tela foram tratados os casos de erros em que o e-mail e a senha foram inválidos.
- Na tela de registro segue o mesmo modelo da de login

2. Tela Home:
- Nesta tela é possível visualizar a lista de treinos cadastrados;
- Ser direcionado para a tela de criar treino;

3. Tela de criar treino:
- Nesta tela é possível cadastrar o nome de um treino.
- Visualizar uma lista de exercícios quando forem adicionados, e excluir os mesmos antes de salvar.

4. Tela de adicionar exercícios:
- Nesta tela é possível adicionar um ou mais exercícios clicando no check box;

5. Tela detalhes:
- Nesta tela é possível visualizar os detalhes de um treino com seus exercícios associados, sendo possível excluir e atualizar.

## Tecnologias Utilizadas
● Kotlin <br/> 
● Arquitetura MVVM <br/> 
● Injeção de dependência com Koin <br/> 
● Coroutines <br/>



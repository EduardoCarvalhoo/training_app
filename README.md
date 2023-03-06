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

## Layout Mobile
 <div align="center">

  ![Screenshot_20230222-102513]()

  <img src= "https://user-images.githubusercontent.com/102394401/220635875-3c98b91a-1911-4ca7-8d01-33a666e13219.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636089-d2e2b42a-ae12-471b-9299-9abfa707861e.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636093-aa405320-86df-4291-91c4-e96d89043e6a.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636094-7eeafb3a-8593-4134-87b5-c9dc8cefb8d9.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636095-2108cb17-270c-435e-bf1f-710b0b88bfc8.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636098-adc5eec6-53a7-41c1-b91b-857be204be26.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636101-fad6314d-40a7-43d7-a79f-70e61ed13974.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636103-d5cae8f6-a315-4826-9884-3e0dca379c32.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636107-d4f316e6-1835-4504-87ff-55bf583916a4.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636109-e541f822-09a1-4aab-ba45-c804a7ee2173.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636113-91031aeb-2cd8-4cd4-a9f3-339c137e2801.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636116-e04edb06-8e8b-42eb-abe6-78149f8a2b83.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636120-1c7fb7cd-53f9-4a86-9078-6fc4eeaf8dd9.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636122-f301db64-2704-4964-884d-05881fa70e9e.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636126-21e5bdfa-a255-4e23-9fdc-a124a2c28b8e.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636127-d18f1dbf-449f-44a5-b4fe-79e2d6ede9fd.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636128-1256c898-24bd-4804-bf37-15cc8f8585f8.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636133-cdc025b4-c2be-40d7-b6b2-f7bfa7ef6f85.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636136-75028707-533f-4a30-b663-3f223134276e.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/220636145-9773ff20-71cd-4700-a939-45c8d0fdc2cb.png" width="300"/>
  <img src= "https://user-images.githubusercontent.com/102394401/223276198-5929c9b0-1ece-4f95-9f3b-cbef07b46ee7.png" width="300"/>
 </div>

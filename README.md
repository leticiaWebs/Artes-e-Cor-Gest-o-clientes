## API - Gestão de clientes 
O microsserviço de gestão de clientes realiza o processo de cadastro de clientes, busca dos clientes pelo id,  busca de todos os clientes cadastrados na base e a atualização dos dados cadastrais dos clientes. 
## Pré requisitos

Para executar a aplicação localmente é preciso usar : 
- Java 17 
- Maven 

Utilizar fazer o comando 

    mvn clean install 
    
  **Pontos de atenção:** 
  Importante adaptar os dados do application.properties para os dados da base existentes localmente. O projeto está criado e configurado para as credenciais da base local. 

### Tecnologias Usadas 
- Para o desenvolvimento do batch foi usado : 
- Spring Web
- Spring JPA
- Spring Starter Validation 
-  PostgreSQL

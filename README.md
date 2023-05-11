# SDK-Java

Esse SDK foi construído para abstrair e facilitar o processo de integração com a maxiPago! Smart Payments. Nesse momento
o SDK oferece os seguintes recursos:

* Autorização
* Autorização com autenticação - 3DS
* Venda com autenticação - 3DS V2
* Captura
* Venda direta
* Cancelamento
* Estorno
* Autorização com Antifraude
* Verificação de cartão - 0 dolar
* Venda direta com cartão de débito
* Boleto bancário
* Transferência bancária
* Cadastro, atualização e remoção de clientes
    * Adição e remoção de cartões dos clientes
* Consultas

Para acesso às documentações de todos os tipos de requisições disponíveis, acesse: [maxiPago! Developers](https://developers.maxipago.com/).
Essa documentação descreverá os endpoints da API e os valores esperados. O SDK irá abstrair toda a complexidade da
criação dos XMLs, envio e recebimento das requisições, porém é importante que esteja com a documentação para saber os 
valores esperados em cada requisição.

## Inicialização

O SDK pode utilizar as credenciais de sandbox ou de produção. Para isso, basta passar o ambiente como parâmetro para o
construtor da classe MaxiPago:

### Produção

```java
MaxiPago maxiPago = new MaxiPago(Environment.production(
    merchantId, merchantKey
));
```

### Sandbox

```java
MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
    merchantId, merchantKey
));
```

## Resposta de transação

Todas as requisições transacionais, retornam uma instância de `com.maxipago.request.TransactionResponse`. Essa instância
pode ser utilizada para obter os dados de resposta da transação, como `transactionId`, `orderId`, `status` e mensagens e
códigos de retorno.

A estrutura da classe é a seguinte:

|Tipo|Propriedade|Descrição|
|----|-----------|---------|
|String|authCode|Código de autorização retornado pela Adquirente|
|String|orderID|ID do pedido gerado pela maxiPago! Esse ID será utilizado em outras requisições e, por isso, deverá ser salvo.|
|String|referenceNum|ID do pedido gerado pela loja. Esse é o código de referência que a loja enviou na requisição.|
|String|transactionID|ID da transação, gerado pela maxiPago! Assim como o orderId, esse ID será utilizado em outras requisições e deverá ser salvo.|
|String|transactionTimestamp|Data/hora da transação.|
|String|responseCode|Indicador do status da transação na maxiPago! Veja detalhes sobre os código de resposta logo abaixo.|
|String|responseMessage|Mensagem de resposta da transação|
|String|avsResponseCode|Resposta da verificação AVS, se houver. Sugerimos que a resposta do AVS seja usada para avaliação manual do risco|
|String|processorCode|Código de retorno da Adquirente. |
|String|processorMessage|Mensagem de retorno da Adquirente|
|String|processorName|Nome do adquirente.|
|String|creditCardBin|BIN do cartão.|
|String|creditCardLast4|Últimos 4 dígitos do cartão.|
|String|errorMessage|Mensagem de erro|
|String|processorTransactionID|ID da transação na Adquirente. Cielo: TID - REDE: NSU|
|String|processorReferenceNumber|Número de referência da Adquirente. Cielo: NSU - REDE: Comprovante de Venda (CV)|
|String|creditCardScheme|Identificação do tipo da bandeira|
|String|authenticationURL|URL de autenticação. O cliente deve ser redirecionado para esta URL para completar a etapa de autenticação|
|String|fraudScore|Valor de score retornado pelo fraudControl! Quanto menor o valor, menor o risco da transação.|
|String|onlineDebitUrl|URL para redirecionamento do Débito Online. O cliente deve ser redirecionado para esta URL para completar a transação|
|String|boletoUrl|URL para geração do boleto|

#### responseCode

Você utilizar **apenas este campo** para validar o resultado de uma transação. Não utilize outros campos da resposta para determinar o sucesso ou falha de uma transação.

* 0 = Aprovada*
* 1 = Negada
* 2 = Negada por Duplicidade ou Fraude
* 5 = Em revisão (Análise Manual de Fraude)
* 1022 = Erro na operadora do cartão
* 1024 = Erro nos paramêtros enviados
* 1025 = Erro nas credenciais da loja
* 2048 = Erro interno na maxiPago!
* 4097 = Timeout com a adquirente

> *Para adquirente com estorno online, o `responseCode` com valor 0 significa que o estorno já foi processado, para os
> offline significa que o estorno está sendo processado (neste caso pode ser posteriomente verificado pela API de Consulta.

## Transaction request

Transaction request são as requisições relacionadas à criação, edição e remoção de transações - incluindo cartões de crédito
e débito, boleto, débito online e recorrências.
 
### Exemplos de requisições

Os códigos de exemplo disponibilizados aqui, também estão disponíveis na suite de testes. Para utilizá-los, seja em
sandbox, seja em produção, você precisará definir seu `merchantId` e `merchantKey`. Assim que tivé-os definidos,
basta criar uma instância de `Environment` com esses dados. A classe `Environment` possui dois métodos de criação:

* `sandbox`
* `production`

Utilize esses métodos para facilitar a crianção do ambiente. Você pode, inclusive, abstrair o ambiente; por exemplo:

```java
// A variável `test` permite a variação do ambiente; se usarmos `true`, a integração vai acontecer no sandbox, mas
// se usarmos `false`, a integração acontecerá em produção.
Boolean test = false;

// Usamos o ambiente de produção por padrão
Environment environment = Environment.production("123-merchant-id", "123-merchant-key");

// mas se estivermos testando, usamos o sandbox
if (test) {
    environment = Environment.sandbox("123-merchant-id", "123-merchant-key");
}

// Agora o ambiente está abstraído e podemos só mudar o valor de test para mudarmos o ambiente sem precisar
// mudar o restante do código. 
MaxiPago maxiPago = new MaxiPago(environment);
```

#### Autorização

A autorização ou pré-autorização é a ação que sensibiliza o limite do cartão de crédito do cliente, porém não há a
confirmação (captura) da transação, ou seja, não gera cobrança para o consumidor. Para se criar uma autorização utilizando
o SDK, basta usar o código abaixo:

```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.auth()
        .setProcessorId("5")
        .setReferenceNum("Teste 123")
        .setIpAddress("127.0.0.1")
        .billingAndShipping(
                (new Customer()).setName("Fulano de Tal")
                        .setAddress("Rua dos bobos")
                        .setAddress2("0")
                        .setDistrict("District")
                        .setCity("Cidade")
                        .setState("Estado")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setEmail("fulano@de.tal"))
        .setCreditCard(
                (new Card()).setNumber("5105105105105100")
                        .setExpMonth("12")
                        .setExpYear("2028")
                        .setCvvNumber("123"))
        .setPayment(new Payment(100.0));

TransactionResponse response = maxiPago.transactionRequest().execute();
```

#### Autorização com autenticação

```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.auth()
        .setProcessorId("5")
        .setReferenceNum("Teste 123")
        .setIpAddress("127.0.0.1")
        .billingAndShipping(
                (new Customer()).setName("Fulano de Tal")
                        .setAddress("Rua dos bobos")
                        .setAddress2("0")
                        .setDistrict("District")
                        .setCity("Cidade")
                        .setState("Estado")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setEmail("fulano@de.tal"))
        .setCreditCard(
                (new Card()).setNumber("5105105105105100")
                        .setExpMonth("12")
                        .setExpYear("2028")
                        .setCvvNumber("123"))
        .setAuthentication("41", Authentication.DECLINE)
        .setPayment(new Payment(100.0));

TransactionResponse transactionResponse = maxiPago.transactionRequest().execute();
```
#### Venda com 3DS 2.0
```java
MaxiPago maxiPago = new MaxiPago(environment);
maxiPago.sale()
        .setProcessorId("5")
        .setReferenceNum("Teste 012")
        .setIpAddress("127.0.0.1")
        .billingAndShipping(
        (new Customer()).setName("Fulano de Tal")
                        .setAddress("Rua dos bobos")
                        .setAddress2("0")
                        .setDistrict("District")
                        .setCity("Cidade")
                        .setState("Estado")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setEmail("fulano@de.tal"))
                        .setAuthentication("41", Authentication.DECLINE)
                        .setCreditCard(
                        (new Card()).setNumber("5448280000000007")
                        .setExpMonth("12")
                        .setExpYear("2028")
                        .setCvvNumber("123"))
                        .setPayment(new Payment(100.0))
                         //device informations
                           .device(new Device()
                		      .setDeviceType3ds("BROWSER")
                		      .setSdkEncData("c2RrRW5jRGF0YQ==")
                		      .setSdkAppId("17dd3e6f-20e5-452c-a638-f106ece38b7f")
                		      .setSdkEphemeralPubKey("ai7q834kal0984545")
                		      .setSdkMaxTimeout("05")
                		      .setSdkReferenceNumber("17dd3e6f20e5452ca638f106ece38b7f")
                		      .setSdkTransId("733aa357-285b-41ba-943e-a2c4569739fe")
                		      .setRenderOptions(new RenderOptions()
                				.setSdkInterface(SdkInterfaceEnum.NATIVE.value)
                				.setSdkUiType(SdkUiTypeEnum.HTML_OTHER.value)));

       TransactionResponse response =  maxiPago.transactionRequest().execute();
```
#### Capturando a pré-autorização

```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.capture()
        .setOrderId(orderId)
        .setReferenceNum(referenceNumber)
        .setPayment(new Payment(100.0));

TransactionResponse transactionResponse = maxiPago.transactionRequest().execute();
```

#### Cancelando a transaão

```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.cancel()
        .setTransactionId(transactionId);

TransactionResponse transactionResponse = maxiPago.transactionRequest().execute();
```

#### Verificando o cartão com uma transação zero dolar

```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.zeroDollar()
        .setProcessorId("1")
        .setReferenceNum("123")
        .setCreditCard(
                (new Card()).setNumber("5105105105105100")
                        .setExpMonth("12")
                        .setExpYear("2028")
                        .setCvvNumber("123"));

TransactionResponse transactionResponse = maxiPago.transactionRequest().execute();
```

#### Criando uma venda direta com cartão de crédito

Ao contrário da pré-autorização com captura posterior, a venda direta é o ato de autorizar e capturar na mesma requisição.

```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.sale()
        .setProcessorId("5")
        .setReferenceNum("Teste 012")
        .setIpAddress("127.0.0.1")
        .billingAndShipping(
                (new Customer()).setName("Fulano de Tal")
                        .setAddress("Rua dos bobos")
                        .setAddress2("0")
                        .setDistrict("District")
                        .setCity("Cidade")
                        .setState("Estado")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setEmail("fulano@de.tal"))
        .setCreditCard(
                (new Card()).setNumber("5105105105105100")
                        .setExpMonth("12")
                        .setExpYear("2028")
                        .setCvvNumber("123"))
        .setPayment(new Payment(100.0));

TransactionResponse transactionResponse = maxiPago.transactionRequest().execute();
```

#### Criando uma venda direta com cartão de crédito e autenticação

```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.sale()
        .setProcessorId("5")
        .setReferenceNum("Teste 012")
        .setIpAddress("127.0.0.1")
        .billingAndShipping(
                (new Customer()).setName("Fulano de Tal")
                        .setAddress("Rua dos bobos")
                        .setAddress2("0")
                        .setDistrict("District")
                        .setCity("Cidade")
                        .setState("Estado")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setEmail("fulano@de.tal"))
        .setAuthentication("41", Authentication.DECLINE)
        .setCreditCard(
                (new Card()).setNumber("5105105105105100")
                        .setExpMonth("12")
                        .setExpYear("2028")
                        .setCvvNumber("123"))
        .setPayment(new Payment(100.0));

TransactionResponse transactionResponse = maxiPago.transactionRequest().execute();
```

#### Criando uma venda direta com cartão de débito

Assim como a venda direta com cartão de crédito e autenticação, a venda direta com cartão de débito também exige
autenticação. Assim que o cliente mostrar o interesse em fazer o pagamento, a loja deverá criar a venda direta e redirecionar
o cliente para a página de autenticação. Assim que feita a autenticação, o cliente retornará para a página da loja.

```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.sale()
        .setProcessorId("1")
        .setReferenceNum("Teste 012")
        .setIpAddress("127.0.0.1")
        .billingAndShipping(
                (new Customer()).setName("Fulano de Tal")
                        .setAddress("Rua dos bobos")
                        .setAddress2("0")
                        .setDistrict("District")
                        .setCity("Cidade")
                        .setState("Estado")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setEmail("fulano@de.tal"))
        .setAuthentication("41", Authentication.DECLINE)
        .setDebitCard(
                (new Card()).setNumber("4000000000000002")
                        .setExpMonth("12")
                        .setExpYear("2028")
                        .setCvvNumber("123"))
        .setPayment(new Payment(100.0));

TransactionResponse transactionResponse = maxiPago.transactionRequest().execute();
```

#### Criando uma autorização com anti-fraude

```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.auth()
        .setProcessorId("5")
        .setReferenceNum("Teste 123")
        .setIpAddress("127.0.0.1")
        .setFraudCheck("Y")
        .setBilling(
                (new Customer()).setName("Fulano de Tal")
                        .setAddress("Rua dos bobos")
                        .setAddress2("0")
                        .setDistrict("District")
                        .setCity("Cidade")
                        .setState("Estado")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setCompanyName("Uma empresa qualquer")
                        .setEmail("fulano@de.tal")
                        .addPhone(
                                (new Phone()).setPhoneCountryCode("55")
                                        .setPhoneAreaCode("16")
                                        .setPhoneExtension("123")
                                        .setPhoneType("Residential")
                                        .setPhoneNumber("99999999"))
                        .addPhone(
                                (new Phone()).setPhoneCountryCode("55")
                                        .setPhoneAreaCode("16")
                                        .setPhoneExtension("123")
                                        .setPhoneType("Commercial")
                                        .setPhoneNumber("99999999"))
                        .addDocument(
                                (new Document()).setDocumentType("CPF")
                                        .setDocumentValue("11111111111")))

        .setShipping(
                (new Customer()).setName("Fulano de Tal")
                        .setAddress("Rua dos bobos")
                        .setAddress2("0")
                        .setDistrict("District")
                        .setCity("Cidade")
                        .setState("Estado")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setEmail("fulano@de.tal")
                        .addPhone(
                                (new Phone()).setPhoneCountryCode("55")
                                        .setPhoneAreaCode("16")
                                        .setPhoneExtension("123")
                                        .setPhoneType("Residential")
                                        .setPhoneNumber("99999999"))
                        .addDocument(
                                (new Document()).setDocumentType("CPF")
                                        .setDocumentValue("11111111111")))
        .setCreditCard(
                (new Card()).setNumber("5105105105105100")
                        .setExpMonth("12")
                        .setExpYear("2028")
                        .setCvvNumber("123"))
        .setPayment(new Payment(100.0))
        .addItem(1, "123", "Um item qualquer", 2, 10.0, 5.0)
        .addItem(2, "456", "Outro item qualquer", 2, 10.0, 5.0);

TransactionResponse transactionResponse = maxiPago.transactionRequest().execute();
```

#### Criando uma venda com cartão de débito e anti-fraude

```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.sale()
        .setProcessorId("5")
        .setReferenceNum("Teste 123")
        .setIpAddress("127.0.0.1")
        .setFraudCheck("Y")
        .setBilling(
                (new Customer()).setName("Fulano de Tal")
                        .setAddress("Rua dos bobos")
                        .setAddress2("0")
                        .setDistrict("District")
                        .setCity("Cidade")
                        .setState("Estado")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setCompanyName("Uma empresa qualquer")
                        .setEmail("fulano@de.tal")
                        .addPhone(
                                (new Phone()).setPhoneCountryCode("55")
                                        .setPhoneAreaCode("16")
                                        .setPhoneExtension("123")
                                        .setPhoneType("Residential")
                                        .setPhoneNumber("99999999"))
                        .addPhone(
                                (new Phone()).setPhoneCountryCode("55")
                                        .setPhoneAreaCode("16")
                                        .setPhoneExtension("123")
                                        .setPhoneType("Commercial")
                                        .setPhoneNumber("99999999"))
                        .addDocument(
                                (new Document()).setDocumentType("CPF")
                                        .setDocumentValue("11111111111")))

        .setShipping(
                (new Customer()).setName("Fulano de Tal")
                        .setAddress("Rua dos bobos")
                        .setAddress2("0")
                        .setDistrict("District")
                        .setCity("Cidade")
                        .setState("Estado")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setEmail("fulano@de.tal")
                        .addPhone(
                                (new Phone()).setPhoneCountryCode("55")
                                        .setPhoneAreaCode("16")
                                        .setPhoneExtension("123")
                                        .setPhoneType("Residential")
                                        .setPhoneNumber("99999999"))
                        .addDocument(
                                (new Document()).setDocumentType("CPF")
                                        .setDocumentValue("11111111111")))
        .setCreditCard(
                (new Card()).setNumber("5105105105105100")
                        .setExpMonth("12")
                        .setExpYear("2028")
                        .setCvvNumber("123"))
        .setPayment(new Payment(100.0))
        .addItem(1, "123", "Um item qualquer", 2, 10.0, 5.0)
        .addItem(2, "456", "Outro item qualquer", 2, 10.0, 5.0);

TransactionResponse transactionResponse = maxiPago.transactionRequest().execute();
```

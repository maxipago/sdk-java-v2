# SDK-Java

Esse SDK foi construído para facilitar o processo de integração com o gateway maxipago. Atualmente 
a SDK oferece os seguintes recursos:

* Autorização
* Captura
* Venda direta
* Cancelamento
* Estorno
* Autorização com Antifraude
* Venda direta com Autenticação 3DS V2 (cartão de crédito e débito)
* Venda direta com Autenticação 3DS V2 e SDWO (cartão de crédito e débito)
* Venda direta com Autenticação 3DS V2 e MCC Dinâmico (cartão de crédito e débito)
* Verificação de cartão - Zero Dollar
* Boleto bancário
* Cadastro, atualização e remoção de clientes
* Adição e remoção de cartões dos clientes


Para acesso as documentações de todos os tipos de requisições disponíveis, acesse: [maxiPago! Developers](https://developers.maxipago.com/).
Essa documentação descreverá os endpoints da API e os valores esperados. A SDK irá abstrair toda a complexidade da
criação dos XMLs, envio e recebimento das requisições, porém é importante que esteja com a documentação para saber os 
valores esperados em cada requisição.

## Inicialização

A SDK pode utilizar as credenciais de sandbox ou de produção. Para isso, basta passar o ambiente como parâmetro para o
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

Utilize **apenas este campo** para validar o resultado de uma transação. Não utilize outros campos da resposta para determinar o sucesso ou falha de uma transação.

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
sandbox ou em produção, você precisará definir seu `merchantId` e `merchantKey`. Assim que os obter,
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
			.setReferenceNum("CreateAuth")
			.setIpAddress("127.0.0.1")
			.billingAndShipping((new Customer())
					.setName("Nome como esta gravado no cartao")
					.setAddress("Rua Volkswagen, 100")
					.setAddress2("0")
					.setDistrict("Jabaquara")
					.setCity("Sao Paulo")
					.setState("SP")
					.setPostalCode("11111111")
					.setCountry("BR")
					.setPhone("11111111111")
					.setEmail("email.pagador@gmail.com"))
	        .setCreditCard((new Card())
	        		.setNumber("5448280000000007")
	        		.setExpMonth("12")
	        		.setExpYear("2028")
	        		.setCvvNumber("123"))
	        .setPayment(new Payment(100.0));

TransactionResponse response = maxiPago.transactionRequest().execute();
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

#### Cancelando a transação

```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.cancel()
        .setTransactionId(transactionId);

TransactionResponse transactionResponse = maxiPago.transactionRequest().execute();
```

#### Verificando o cartão com uma transação Zero Dollar

```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.zeroDollar()
        .setProcessorId("1")
        .setReferenceNum("123")
        .setCreditCard(
                (new Card()).setNumber("3550464082005915")
                        .setExpMonth("12")
                        .setExpYear("2040")
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
                (new Customer()).setName("Seu Cliente")
                        .setAddress("Rua Volkswagen 100")
                        .setAddress2("0")
                        .setDistrict("Jabaquara")
                        .setCity("Sao Paulo")
                        .setState("SP")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setEmail("email.pagador@gmail.com")
        .setCreditCard(
                (new Card()).setNumber("3550464082005915")
                        .setExpMonth("12")
                        .setExpYear("2040")
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
                (new Customer()).setName("SNome como esta gravado no cartao")
                        .setAddress("Rua Volkswagen 100")
                        .setAddress2("0")
                        .setDistrict("Jabaquara")
                        .setCity("Sao Paulo")
                        .setState("SP")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setEmail("email.pagador@gmail.com"))
        .setAuthentication("41", Authentication.DECLINE)
        .setCreditCard(
                (new Card()).setNumber("3550464082005915")
                        .setExpMonth("12")
                        .setExpYear("2040")
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
                (new Customer()).setName("Nome como esta gravado no cartao")
                        .setAddress("Rua Volkswagen 100")
                        .setAddress2("0")
                        .setDistrict("Jabaquara")
                        .setCity("Sao Paulo")
                        .setState("SP")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setEmail("email.pagador@gmail.com"))
        .setAuthentication("41", Authentication.DECLINE)
        .setDebitCard(
                (new Card()).setNumber("3550464082005915")
                        .setExpMonth("12")
                        .setExpYear("2040")
                        .setCvvNumber("123"))
        .setPayment(new Payment(100.0));

TransactionResponse transactionResponse = maxiPago.transactionRequest().execute();
```

#### Criando uma autorização com antifraude

```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.auth()
        .setProcessorId("5")
        .setReferenceNum("Teste 123")
        .setIpAddress("127.0.0.1")
        .setFraudCheck("Y")
        .setBilling(
                (new Customer()).setName("Nome como esta gravado no cartao")
                        .setAddress("Rua Volkswagen 100")
                        .setAddress2("0")
                        .setDistrict("Jabaquara")
                        .setCity("Sao Paulo")
                        .setState("SP")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setCompanyName("Nome da Empresa")
                        .setEmail("email.pagador@gmail.com"))
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
                (new Customer()).setName("Seu Cliente")
                        .setAddress("Rua Volkswagen 100")
                        .setAddress2("0")
                        .setDistrict("Jabaquara")
                        .setCity("Sao Paulo")
                        .setState("SP")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setEmail("email.pagador@gmail.com"))
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
                (new Card()).setNumber("3550464082005915")
                        .setExpMonth("12")
                        .setExpYear("2040")
                        .setCvvNumber("123"))
        .setPayment(new Payment(100.0))
        .addItem(1, "123", "Um item qualquer", 2, 10.0, 5.0)
        .addItem(2, "456", "Outro item qualquer", 2, 10.0, 5.0);

TransactionResponse transactionResponse = maxiPago.transactionRequest().execute();
```

#### Criando uma venda com cartão de débito e antifraude

```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.sale()
        .setProcessorId("5")
        .setReferenceNum("Teste 123")
        .setIpAddress("127.0.0.1")
        .setFraudCheck("Y")
        .setBilling(
                (new Customer()).setName("Nome como esta gravado no cartao")
                        .setAddress("Rua Volkswagen 100")
                        .setAddress2("0")
                        .setDistrict("Jabaquara")
                        .setCity("Sao Paulo")
                        .setState("SP")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setCompanyName("Nome da Empresa")
                        .setEmail("email.pagador@gmail.com"))
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
                (new Customer()).setName("Seu Cliente")
                        .setAddress("Rua Volkswagen 100")
                        .setAddress2("0")
                        .setDistrict("Jabaquara")
                        .setCity("Sao Paulo")
                        .setState("SP")
                        .setPostalCode("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setEmail("email.pagador@gmail.com"))
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
                (new Card()).setNumber("3550464082005915")
                        .setExpMonth("12")
                        .setExpYear("2040")
                        .setCvvNumber("123"))
        .setPayment(new Payment(100.0))
        .addItem(1, "123", "Um item qualquer", 2, 10.0, 5.0)
        .addItem(2, "456", "Outro item qualquer", 2, 10.0, 5.0);

TransactionResponse transactionResponse = maxiPago.transactionRequest().execute();
```

#### Venda direta com Autenticação 3DS V2 (cartão de crédito e débito)
```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.sale()
	.setProcessorId("5")
	.setReferenceNum("CreateSaleWith3DS")
	.setAuthentication("41", Authentication.DECLINE, ChallengePreference.NO_PREFERENCE)
	.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36")
	.device(new Device()
			.setColorDepth("1")
			.setJavaEnabled(true)
			.setLanguage("BR")
			.setScreenHeight("550")
			.setScreenWidth("550")
			.setTimeZoneOffset(3))
	.setIpAddress("127.0.0.1")
	.billingAndShipping((new Customer())
			.setName("Nome como esta gravado no cartao")
			.setAddress("Rua Volkswagen 100")
			.setAddress2("0")
			.setDistrict("Jabaquara")
			.setCity("Sao Paulo")
			.setState("SP")
			.setPostalCode("11111111")
			.setCountry("BR")
			.setPhone("11111111111")
			.setEmail("email.pagador@gmail.com"))
	.setCreditCard((new Card())
			.setNumber("5221834791042066")
			.setExpMonth("12")
			.setExpYear("2030")
			.setCvvNumber("123"))
	.setPayment(new Payment(100.0));
                		  

       TransactionResponse response =  maxiPago.transactionRequest().execute();
```

#### Venda direta com Autenticação 3DS V2 e SDWO (cartão de crédito e débito)
```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.sale()
	.setProcessorId("5")
	.setReferenceNum("CreateSaleWith3DS")
	.setAuthentication("41", Authentication.DECLINE, ChallengePreference.NO_PREFERENCE, "POSTBACK", "Y")
	.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36")
	.device(new Device()
			.setColorDepth("1")
			.setJavaEnabled(true)
			.setLanguage("BR")
			.setScreenHeight("550")
			.setScreenWidth("550")
			.setTimeZoneOffset(3))
	.setIpAddress("127.0.0.1")
	.billingAndShipping((new Customer())
			.setName("Nome como esta gravado no cartao")
			.setAddress("Rua Volkswagen 100")
			.setAddress2("0")
			.setDistrict("Jabaquara")
			.setCity("Sao Paulo")
			.setState("SP")
			.setPostalCode("11111111")
			.setCountry("BR")
			.setPhone("11111111111")
			.setEmail("email.pagador@gmail.com"))
	.setCreditCard((new Card())
			.setNumber("5221834791042066")
			.setExpMonth("12")
			.setExpYear("2030")
			.setCvvNumber("123"))
	.setPayment(new Payment(100.0))
	.setWallet(new Wallet()
			.setSDWO(new SDWO()
					.setId("12345")
					.setProcessingType(SDWOProcessingType.CASH_IN)
					.setSenderTaxIdentification("56326738000106")
					.setBusinessApplicationIdentifier(BusinessApplicationIdentifier.CBPS)
                                        .setPaymentDestination("04")
                                        .setMerchantTaxId("11122233344455")
                                        .setReceiverData(new ReceiverData()
                                                                .setFirstName("Jose")
                                                                .setLastName("Silva")
                                                                .setTaxIdNumber("123556")
                                                                .setWalletAccountIdentification("342432409"))
                                        .setSenderData(new SenderData()
                                                                .setTaxIdNumber("123456")
                                                                .setFirstName("Nome")
                                                                .setLastNam("Sobrenome")
                                                                .setAddress("Rua Volkswagen 100")
                                                                .setCity("Sao Paulo")
                                                                .setCountry("BRA"))));
				  

TransactionResponse response =  maxiPago.transactionRequest().execute();
```

#### Venda direta com o campo sellerTaxIdName
```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.sale()
	.setProcessorId("5")
	.setReferenceNum("CreateSaleWith3DS")
	.setAuthentication("41", Authentication.DECLINE, ChallengePreference.NO_PREFERENCE, "POSTBACK", "Y")
	.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36")
	.device(new Device()
			.setColorDepth("1")
			.setJavaEnabled(true)
			.setLanguage("BR")
			.setScreenHeight("550")
			.setScreenWidth("550")
			.setTimeZoneOffset(3))
	.setIpAddress("127.0.0.1")
	.billingAndShipping((new Customer())
			.setName("Nome como esta gravado no cartao")
			.setAddress("Rua Volkswagen 100")
			.setAddress2("0")
			.setDistrict("Jabaquara")
			.setCity("Sao Paulo")
			.setState("SP")
			.setPostalCode("11111111")
			.setCountry("BR")
			.setPhone("11111111111")
			.setEmail("email.pagador@gmail.com"))
	.setCreditCard((new Card())
			.setNumber("5221834791042066")
			.setExpMonth("12")
			.setExpYear("2030")
			.setCvvNumber("123"))
	.setPayment(new Payment(100.0))
	.addItem(1, "mcc", "sellerId", "sellerAddress", "sellerCity", "sellerState", "sellerCountry", "sellerCep", "sellerTaxId", "sellerTaxIdName");

TransactionResponse response =  maxiPago.transactionRequest().execute();
```

#### Venda direta com Autenticação 3DS V2 e MCC Dinâmico (cartão de crédito e débito)
```java
MaxiPago maxiPago = new MaxiPago(environment);

maxiPago.sale()
	.setProcessorId("5")
	.setReferenceNum("CreateSaleWith3DS")
	.setAuthentication("41", Authentication.DECLINE, ChallengePreference.NO_PREFERENCE, "POSTBACK", "Y")
	.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36")
	.device(new Device()
			.setColorDepth("1")
			.setJavaEnabled(true)
			.setLanguage("BR")
			.setScreenHeight("550")
			.setScreenWidth("550")
			.setTimeZoneOffset(3))
	.setIpAddress("127.0.0.1")
	.billingAndShipping((new Customer())
			.setName("Nome como esta gravado no cartao")
			.setAddress("Rua Volkswagen 100")
			.setAddress2("0")
			.setDistrict("Jabaquara")
			.setCity("Sao Paulo")
			.setState("SP")
			.setPostalCode("11111111")
			.setCountry("BR")
			.setPhone("11111111111")
			.setEmail("email.pagador@gmail.com"))
	.setCreditCard((new Card())
			.setNumber("5221834791042066")
			.setExpMonth("12")
			.setExpYear("2030")
			.setCvvNumber("123"))
	.setPayment(new Payment(100.0))
        .setPaymentFacilitatorID(249171)
        .addItem(1, "1234", "56789", "Rua um", "Capivari", "SP", "BRA", "000000000", "12345678901234")
        .addItem(2, "5678", "90123", "Rua dois", "Capivari", "SP", "BRA", "000000000", "12345678901234");
				  

       TransactionResponse response =  maxiPago.transactionRequest().execute();
```

#### Criando um Pix
```java
MaxiPago maxiPago = new MaxiPago(environment);
maxiPago.pix()
	.setProcessorId("205")
	.setReferenceNum("CreatePix")
	.setIpAddress("127.0.0.1")
	.setPayment(new Payment(20.00))
	.setPix((new Pix())
		.setExpirationTime(86400) // 1 dia
		.setPaymentInfo("Uma informação sobre o pagamento")
		.addInfo("Info adicional", "R$ 20,00"));

```
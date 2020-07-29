# SDK-Java

Esse SDK foi construído para abstrair e facilitar o processo de integração com a maxiPago! Smart Payments. Nesse momento
o SDK oferece os seguintes recursos:

* Autorização
* Autorização com autenticação - 3DS
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

Os códigos de exemplo disponibilizados aqui, também estão disponíveis na suite de testes e poderão ser executados, todos,
conforme o caso de uso.

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

## Autorização

A autorização ou pré-autorização é a ação que sensibiliza o limite do cartão de crédito do cliente, porém não há a
confirmação (captura) da transação, ou seja, não gera cobrança para o consumidor. Para se criar uma autorização utilizando
o SDK, basta usar o código abaixo:

```java
MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
        merchantId, merchantKey
));

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
                (new Card()).setNumber("5448280000000007")
                        .setExpMonth("12")
                        .setExpYear("2028")
                        .setCvvNumber("123"))
        .setPayment(new Payment(100.0));

TransactionResponse response = maxiPago.transactionRequest().execute();

System.out.println(response.processorCode);
System.out.println(response.orderID);
System.out.println(response.transactionID);
```

### Autorização com autenticação

```java
MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
        merchantId, merchantKey
));

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
                (new Card()).setNumber("5448280000000007")
                        .setExpMonth("12")
                        .setExpYear("2028")
                        .setCvvNumber("123"))
        .setAuthentication("41", Authentication.DECLINE)
        .setPayment(new Payment(100.0));

maxiPago.transactionRequest().execute();
```

### Capturando a pré-autorização

```java
MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
        merchantId, merchantKey
));

maxiPago.capture()
        .setOrderId(orderId)
        .setReferenceNum(referenceNumber)
        .setPayment(new Payment(100.0));

maxiPago.transactionRequest().execute();
```

### Cancelando a transaão

```java
MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
        merchantId, merchantKey
));

maxiPago.cancel()
        .setTransactionId(transactionId);

maxiPago.transactionRequest().execute();
```

### Verificando o cartão com uma transação zero dolar

```java
MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
        merchantId, merchantKey
));

maxiPago.zeroDollar()
        .setProcessorId("1")
        .setReferenceNum("123")
        .setCreditCard(
                (new Card()).setNumber("5448280000000007")
                        .setExpMonth("12")
                        .setExpYear("2028")
                        .setCvvNumber("123"));

maxiPago.transactionRequest().execute();
```

### Criando uma venda direta com cartão de crédito

Ao contrário da pré-autorização com captura posterior, a venda direta é o ato de autorizar e capturar na mesma requisição.

```java
MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
        merchantId, merchantKey
));

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
                (new Card()).setNumber("5448280000000007")
                        .setExpMonth("12")
                        .setExpYear("2028")
                        .setCvvNumber("123"))
        .setPayment(new Payment(100.0));

maxiPago.transactionRequest().execute();
```

### Criando uma venda direta com cartão de crédito e autenticação

```java
MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
        merchantId, merchantKey
));

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
        .setPayment(new Payment(100.0));

maxiPago.transactionRequest().execute();
```

### Criando uma venda direta com cartão de débito

Assim como a venda direta com cartão de crédito e autenticação, a venda direta com cartão de débito também exige
autenticação. Assim que o cliente mostrar o interesse em fazer o pagamento, a loja deverá criar a venda direta e redirecionar
o cliente para a página de autenticação. Assim que feita a autenticação, o cliente retornará para a página da loja.

```java
MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
        merchantId, merchantKey
));

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

maxiPago.transactionRequest().execute();
```

### Criando uma autorização com anti-fraude

```java
MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
        merchantId, merchantKey
));

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
                (new Card()).setNumber("5448280000000007")
                        .setExpMonth("12")
                        .setExpYear("2028")
                        .setCvvNumber("123"))
        .setPayment(new Payment(100.0))
        .addItem(1, "123", "Um item qualquer", 2, 10.0, 5.0)
        .addItem(2, "456", "Outro item qualquer", 2, 10.0, 5.0);

TransactionResponse transactionResponse = maxiPago.transactionRequest().execute();

System.out.println(transactionResponse.fraudScore);
```

### Criando uma venda com cartão de débito e anti-fraude

```java
MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
        merchantId, merchantKey
));

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
                (new Card()).setNumber("5448280000000007")
                        .setExpMonth("12")
                        .setExpYear("2028")
                        .setCvvNumber("123"))
        .setPayment(new Payment(100.0))
        .addItem(1, "123", "Um item qualquer", 2, 10.0, 5.0)
        .addItem(2, "456", "Outro item qualquer", 2, 10.0, 5.0);

TransactionResponse transactionResponse = maxiPago.transactionRequest().execute();

System.out.println(transactionResponse.fraudScore);
```
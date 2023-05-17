package com.maxipago;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.xml.bind.PropertyException;

import org.junit.jupiter.api.Test;

import com.maxipago.enums.ChallengePreference;
import com.maxipago.paymentmethod.Boleto;
import com.maxipago.paymentmethod.Card;
import com.maxipago.paymentmethod.OnlineDebit;
import com.maxipago.paymentmethod.Pix;
import com.maxipago.paymentmethod.Token;
import com.maxipago.request.RApiResponse;
import com.maxipago.request.TransactionResponse;

public class MaxiPagoTest {
    String merchantId = "11631";
    String merchantKey = "hbsjs242px5vzpnmqu04xcd2";

    public MaxiPagoTest() {}

    

    @Test
    void shouldCreateAuth() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

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

        System.out.println(response.processorCode);
        System.out.println(response.orderID);
        System.out.println(response.transactionID);
    }

    @Test
    public void shouldCreateSaleWith3DS() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

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
       assertEquals("1", response.responseCode);
    }
    
    @Test
    void shouldCaptureAuth() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.capture()
                .setOrderId("0A010492:0172EB977CBC:8CDA:22FB4887")
                .setReferenceNum("Teste 123")
                .setPayment(new Payment(100.0));

        maxiPago.transactionRequest().execute();
    }

    @Test
    void shouldCancelTransaction() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.cancel()
                .setTransactionId("9044089");

        maxiPago.transactionRequest().execute();
    }
    
    @Test
    void shouldCreateZeroDollarTransaction() throws PropertyException {
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
    }
    
    @Test
    void shouldCreateSaleWithCredit() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.sale()
                .setProcessorId("5")
                .setReferenceNum("Teste 012")
                .setIpAddress("127.0.0.1")
                .billingAndShipping(
                        (new Customer()).setName("Nome como esta gravado no cartao")
                                .setAddress("Rua Volkswagen, 100")
                                .setAddress2("0")
                                .setDistrict("Jabaquara")
                                .setCity("Sao Paulo")
                                .setState("SP")
                                .setPostalCode("11111111")
                                .setCountry("BR")
                                .setPhone("11111111111")
                                .setEmail("email.pagador@gmail.com"))
                .setCreditCard(
                        (new Card()).setNumber("5448280000000007")
                                .setExpMonth("12")
                                .setExpYear("2028")
                                .setCvvNumber("123"))
                .setPayment(new Payment(100.0));

        maxiPago.transactionRequest().execute();
    }

    @Test
    void shouldCreateSaleWithDebit() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.sale()
                .setProcessorId("1")
                .setReferenceNum("Teste 012")
                .setIpAddress("127.0.0.1")
                .billingAndShipping(
                        (new Customer()).setName("Nome como esta gravado no cartao")
                                .setAddress("Rua Volkswagen, 100")
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
                        (new Card()).setNumber("4000000000000002")
                                .setExpMonth("12")
                                .setExpYear("2028")
                                .setCvvNumber("123"))
                .setPayment(new Payment(100.0));

        maxiPago.transactionRequest().execute();
    }

    @Test
    void shouldCreateAuthWithAntiFraud() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.auth()
                .setProcessorId("5")
                .setReferenceNum("Teste 123")
                .setIpAddress("127.0.0.1")
                .setFraudCheck("Y")
                .setBilling(
                        (new Customer()).setName("Nome como esta gravado no cartao")
                                .setAddress("Rua Volkswagen, 100")
                                .setAddress2("0")
                                .setDistrict("Jabaquara")
                                .setCity("Sao Paulo")
                                .setState("SP")
                                .setPostalCode("11111111")
                                .setCountry("BR")
                                .setPhone("11111111111")
                                .setCompanyName("Uma empresa qualquer")
                                .setEmail("email.pagador@gmail.com")
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
                        (new Customer()).setName("Nome como esta gravado no cartao")
                                .setAddress("Rua Volkswagen, 100")
                                .setAddress2("0")
                                .setDistrict("Jabaquara")
                                .setCity("Sao Paulo")
                                .setState("SP")
                                .setPostalCode("11111111")
                                .setCountry("BR")
                                .setPhone("11111111111")
                                .setEmail("email.pagador@gmail.com")
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
    }

    @Test
    void shouldCreateSaleWithAntiFraud() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.sale()
                .setProcessorId("5")
                .setReferenceNum("Teste 123")
                .setIpAddress("127.0.0.1")
                .setFraudCheck("Y")
                .setBilling(
                        (new Customer()).setName("Nome como esta gravado no cartao")
                                .setAddress("Rua Volkswagen, 100")
                                .setAddress2("0")
                                .setDistrict("Jabaquara")
                                .setCity("Sao Paulo")
                                .setState("SP")
                                .setPostalCode("11111111")
                                .setCountry("BR")
                                .setPhone("11111111111")
                                .setCompanyName("Uma empresa qualquer")
                                .setEmail("email.pagador@gmail.com")
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
                        (new Customer()).setName("Nome como esta gravado no cartao")
                                .setAddress("Rua Volkswagen, 100")
                                .setAddress2("0")
                                .setDistrict("Jabaquara")
                                .setCity("Sao Paulo")
                                .setState("SP")
                                .setPostalCode("11111111")
                                .setCountry("BR")
                                .setPhone("11111111111")
                                .setEmail("email.pagador@gmail.com")
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
    }

    @Test
    void shouldCreateBoleto() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.boleto()
                .setProcessorId("12")
                .setReferenceNum("Teste 456")
                .setIpAddress("127.0.0.1")
                .setCustomerIdExt("999")
                .billingAndShipping(
                        (new Customer()).setName("Nome como esta gravado no cartao")
                                .setAddress("Rua Volkswagen, 100")
                                .setAddress2("0")
                                .setDistrict("Jabaquara")
                                .setCity("Sao Paulo")
                                .setState("SP")
                                .setPostalCode("11111111")
                                .setCountry("BR")
                                .setPhone("11111111111")
                                .setEmail("email.pagador@gmail.com"))
                .setBoleto(
                        (new Boleto()).setNumber("1212")
                                .setExpirationDate("2020-05-09")
                                .setInstructions("Algumas instruções aleatórias"))
                .setPayment(new Payment(100.0));

        maxiPago.transactionRequest().execute();
    }

    @Test
    void shouldCreateOnlineDebit() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        // 17 - BRADESCO
        // 18 - ITAU

        maxiPago.onlineDebit()
                .setProcessorId("17")
                .setReferenceNum("Teste 123")
                .setIpAddress("127.0.0.1")
                .billingAndShipping(
                        (new Customer()).setName("Nome como esta gravado no cartao")
                                .setAddress("Rua Volkswagen, 100")
                                .setAddress2("0")
                                .setDistrict("Jabaquara")
                                .setCity("Sao Paulo")
                                .setState("SP")
                                .setPostalCode("11111111")
                                .setCountry("BR")
                                .setPhone("11111111111")
                                .setEmail("email.pagador@gmail.com"))
                .setOnlineDebit(
                        (new OnlineDebit()).setParametersURL("param=123"))
                .setPayment(new Payment(100.0));

        maxiPago.transactionRequest().execute();
    }

    @Test
    void shouldRefundTransaction() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.refund()
                .setTransactionId("9044118");

        maxiPago.transactionRequest().execute();
    }

    @Test
    void shouldRefundPixTransaction() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.pixRefund()
                .setOrderId("0123")
                .setReferenceNum("ref-123")
                .setPayment(new Payment(100.0));

        maxiPago.transactionRequest().execute();
    }

    @Test
    void shouldCreateRecurringPayment() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.recurringPayment()
                .setProcessorId("5")
                .setReferenceNum("Teste 123")
                .setIpAddress("127.0.0.1")
                .billingAndShipping(
                        (new Customer()).setName("Nome como esta gravado no cartao")
                                .setAddress("Rua Volkswagen, 100")
                                .setAddress2("0")
                                .setDistrict("Jabaquara")
                                .setCity("Sao Paulo")
                                .setState("SP")
                                .setPostalCode("11111111")
                                .setCountry("BR")
                                .setPhone("11111111111")
                                .setEmail("email.pagador@gmail.com"))
                .setCreditCard(
                        (new Card()).setNumber("5448280000000007")
                                .setExpMonth("12")
                                .setExpYear("2028")
                                .setCvvNumber("123"))
                .setPayment(new Payment(100.0))
                .setRecurring((new Recurring()).setAction("new")
                        .setStartDate("2020-05-01")
                        .setPeriod(Recurring.MONTHLY)
                        .setFrequency(1)
                        .setLastDate("2020-12-31")
                        .setLastAmount(22.0)
                        .setFailureThreshold(15)
                        .setInstallments(7));

        maxiPago.transactionRequest().execute();
    }

    @Test
    void shouldModifyRecurringPayment() {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.modifyRecurring()
                .setOrderId("0A01048D:01717A02CF80:7FC7:5639916D")
                .setPaymentInfo("4716870704643843", "07", "2021", 50.0)
                .setRecurring((new Recurring()).setAction("enable")
                        .setProcessorID("1")
                        .setNextFireDate("2020-06-01")
                        .setPeriod(Recurring.QUARTERLY)
                        .setLastDate("2020-12-31")
                        .setLastAmount(22.0)
                        .setInstallments(10))
                .setBillingInfo((new Customer()).setName("Nome como esta gravado no cartao")
                        .setAddress1("Rua Volkswagen, 100")
                        .setAddress2("0")
                        .setCity("Sao Paulo")
                        .setState("SP")
                        .setZip("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setEmail("email.pagador@gmail.com"))
                .setShippingInfo((new Customer()).setName("Nome como esta gravado no cartao")
                        .setAddress1("Rua Volkswagen, 100")
                        .setAddress2("0")
                        .setCity("Sao Paulo")
                        .setState("SP")
                        .setZip("11111111")
                        .setCountry("BR")
                        .setPhone("11111111111")
                        .setEmail("email.pagador@gmail.com"));

        maxiPago.apiRequest().execute();
    }

    @Test
    void shouldCancelRecurringPayment() {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.cancelRecurring()
                .setOrderId("0A01048D:01717A02CF80:7FC7:5639916D");

        maxiPago.apiRequest().execute();
    }

    @Test
    void shouldCreateConsumer() {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.addConsumer()
                .setCustomerIdExt("123")
                .setFirstName("PrimeiroNome")
                .setLastName("UltimoNome")
                .setAddress1("Rua Volkswagen, 100")
                .setAddress2("0")
                .setCity("Sao Paulo")
                .setState("SP")
                .setZip("11111111")
                .setCountry("BR")
                .setPhone("11111111111")
                .setEmail("email.pagador@gmail.com")
                .setDob("01/01/1900")
                .setSex("M");

        maxiPago.apiRequest().execute();
    }

    @Test
    void shouldUpdateConsumer() {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.updateConsumer()
                .setCustomerId("3039749")
                .setCustomerIdExt("123")
                .setFirstName("Felisbino")
                .setLastName("de Lima")
                .setAddress1("Rua Volkswagen, 100")
                .setAddress2("0")
                .setCity("Sao Paulo")
                .setState("SP")
                .setZip("11111111")
                .setCountry("BR")
                .setPhone("11111111111")
                .setEmail("email.pagador@gmail.com")
                .setDob("01/01/1900")
                .setSex("M");

        maxiPago.apiRequest().execute();
    }

    @Test
    void shouldAddCardOnFile() {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.addCardOnFile()
                .setCustomerId("3039749")
                .setCreditCardNumber("5448280000000007")
                .setExpirationMonth("12")
                .setExpirationYear("2025")
                .setBillingName("Nome como esta gravado no cartao")
                .setBillingAddress1("Av. Marcos Penteado de Ulhoa Rodrigues, 939")
                .setBillingAddress2("11 Andar")
                .setBillingCity("Barueri")
                .setBillingState("SP")
                .setBillingZip("06360040")
                .setBillingCountry("BR")
                .setBillingPhone("01121218536")
                .setBillingEmail("fulano@dominio.com")
                .setOnFilePermissions("ongoing")
                .setOnFileMaxChargeAmount(1000.0);

        maxiPago.apiRequest().execute();
    }

    @Test
    void shouldCreateAuthWithToken() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.auth()
                .setProcessorId("5")
                .setReferenceNum("Teste 123")
                .setIpAddress("127.0.0.1")
                .billingAndShipping(
                        (new Customer()).setName("Nome como esta gravado no cartao")
                                .setAddress("Rua Volkswagen, 100")
                                .setAddress2("0")
                                .setDistrict("Jabaquara")
                                .setCity("Sao Paulo")
                                .setState("SP")
                                .setPostalCode("11111111")
                                .setCountry("BR")
                                .setPhone("11111111111")
                                .setEmail("email.pagador@gmail.com"))
                .setToken(new Token("3039749", "35IbiFgNfJQ=", "123"))
                .setPayment(new Payment(100.0));

        maxiPago.transactionRequest().execute();
    }

    void shouldCreateSaleWithToken() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.sale()
                .setProcessorId("5")
                .setReferenceNum("Teste 012")
                .setIpAddress("127.0.0.1")
                .billingAndShipping(
                        (new Customer()).setName("Nome como esta gravado no cartao")
                                .setAddress("Rua Volkswagen, 100")
                                .setAddress2("0")
                                .setDistrict("Jabaquara")
                                .setCity("Sao Paulo")
                                .setState("SP")
                                .setPostalCode("11111111")
                                .setCountry("BR")
                                .setPhone("11111111111")
                                .setEmail("email.pagador@gmail.com"))
                .setToken(new Token("3039749", "35IbiFgNfJQ=", "123"))
                .setPayment(new Payment(100.0));

        maxiPago.transactionRequest().execute();
    }

    @Test
    void shouldCreateZeroDollarTransactionAndSaveOnFile() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.zeroDollar()
                .setProcessorId("5")
                .setReferenceNum("123")
                .setCreditCard(
                        (new Card()).setNumber("4235647728025682")
                                .setExpMonth("12")
                                .setExpYear("2028")
                                .setCvvNumber("123"))
                .saveOnFile("3039749", "01/06/2021");

        maxiPago.transactionRequest().execute();
    }

    @Test
    void shouldCreateAuthAndSaveOnFile() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.auth()
                .setProcessorId("5")
                .setReferenceNum("Teste 123")
                .setIpAddress("127.0.0.1")
                .billingAndShipping(
                        (new Customer()).setName("Nome como esta gravado no cartao")
                                .setAddress("Rua Volkswagen, 100")
                                .setAddress2("0")
                                .setDistrict("Jabaquara")
                                .setCity("Sao Paulo")
                                .setState("SP")
                                .setPostalCode("11111111")
                                .setCountry("BR")
                                .setPhone("11111111111")
                                .setEmail("email.pagador@gmail.com"))
                .setCreditCard(
                        (new Card()).setNumber("4235647728025682")
                                .setExpMonth("12")
                                .setExpYear("2028")
                                .setCvvNumber("123"))
                .saveOnFile("3039749", "01/06/2021")
                .setPayment(new Payment(100.0));

        maxiPago.transactionRequest().execute();
    }

    @Test
    void shouldCreateSaleAndSaveOnFile() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.sale()
                .setProcessorId("5")
                .setReferenceNum("Teste 123")
                .setIpAddress("127.0.0.1")
                .billingAndShipping(
                        (new Customer()).setName("Nome como esta gravado no cartao")
                                .setAddress("Rua Volkswagen, 100")
                                .setAddress2("0")
                                .setDistrict("Jabaquara")
                                .setCity("Sao Paulo")
                                .setState("SP")
                                .setPostalCode("11111111")
                                .setCountry("BR")
                                .setPhone("11111111111")
                                .setEmail("email.pagador@gmail.com"))
                .setCreditCard(
                        (new Card()).setNumber("4235647728025682")
                                .setExpMonth("12")
                                .setExpYear("2028")
                                .setCvvNumber("123"))
                .saveOnFile("3039749", "01/06/2021")
                .setPayment(new Payment(100.0));

        maxiPago.transactionRequest().execute();
    }

    @Test
    void shouldCreateRecurringPaymentWithToken() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.recurringPayment()
                .setProcessorId("5")
                .setReferenceNum("CreateRecurringPaymentWithToken")
                .setIpAddress("127.0.0.1")
                .billingAndShipping(
                        (new Customer()).setName("Nome como esta gravado no cartao")
                                .setAddress("Rua Volkswagen, 100")
                                .setAddress2("0")
                                .setDistrict("Jabaquara")
                                .setCity("Sao Paulo")
                                .setState("SP")
                                .setPostalCode("11111111")
                                .setCountry("BR")
                                .setPhone("11111111111")
                                .setEmail("email.pagador@gmail.com"))
                .setToken(new Token("3039749", "35IbiFgNfJQ=", "123"))
                .setPayment(new Payment(100.0))
                .setRecurring((new Recurring()).setAction("new")
                        .setStartDate("2020-05-01")
                        .setPeriod(Recurring.MONTHLY)
                        .setFrequency(1)
                        .setFirstAmount(10.0)
                        .setLastDate("2020-12-31")
                        .setLastAmount(22.0)
                        .setFailureThreshold(15)
                        .setInstallments(7));

        maxiPago.transactionRequest().execute();
    }

    @Test
    void shouldDeleteCardOnFile() {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.deleteCardOnFile()
                .setCustomerId("3039749")
                .setToken("35IbiFgNfJQ=");

        maxiPago.apiRequest().execute();
    }

    @Test
    void shouldDeleteConsumer() {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.deleteConsumer()
                .setCustomerId("3039749");

        maxiPago.apiRequest().execute();
    }

    @Test
    void shouldConsultTransaction() {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.consultTransaction("8704233");

        RApiResponse response = maxiPago.rapiRequest().execute();

        for (Record record : response.records) {
            System.out.println(record.transactionId);
        }
    }

    @Test
    void shouldConsultOrder() {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.consultOrder("0A0104BA:017270E43DE2:ACBF:0B4E365C");

        RApiResponse response = maxiPago.rapiRequest().execute();

        for (Record record : response.records) {
            System.out.println(record.orderId);
        }
    }

    @Test
    void shouldConsultOrderList() {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.consultOrderList("lastmonth")
                .setPageSize(5)
                .setPageNumber(1);

        RApiResponse response = maxiPago.rapiRequest().execute();

        maxiPago.consultOrderList("lastmonth")
                .setPageSize(5)
                .setPageNumber(2)
                .setPageToken(response.resultSetInfo.pageToken);

        response = maxiPago.rapiRequest().execute();

        assertEquals(2, (int) response.resultSetInfo.pageNumber);
    }
    
    @Test
    void shouldCreatePix() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.pix()
                .setProcessorId("205")
                .setReferenceNum("CreatePix")
                .setIpAddress("127.0.0.1")
                .setPayment(new Payment(20.00))
                .setPix((new Pix())
                		.setExpirationTime(86400) // 1 dia
                        .setPaymentInfo("Uma informação sobre o pagamento")
                        .addInfo("Info adicional", "R$ 20,00"));

        TransactionResponse response = maxiPago.transactionRequest().execute();
        assertEquals("0", response.responseCode);
    }
    
    @Test
    void shouldCreatePixWithCustomerInfo() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(Environment.sandbox(
                merchantId, merchantKey
        ));

        maxiPago.pix()
                .setProcessorId("205")
                .setReferenceNum("CreatePixWithCustomerInfo")
                .setIpAddress("127.0.0.1")
                .setPayment(new Payment(20.00))
                .setBilling(new Customer()
                		.setName("Nome do Pagador")
                        .setEmail("email.pagador@gmail.com")
                        .addDocument(new Document("CPF", "58877649020")))
                .setPix((new Pix())
                		.setExpirationTime(86400) // 1 dia
                        .setPaymentInfo("Uma informação sobre o pagamento")
                        .addInfo("Info adicional", "R$ 20,00"));

        TransactionResponse response = maxiPago.transactionRequest().execute();
        assertEquals("0", response.responseCode);
    }
}
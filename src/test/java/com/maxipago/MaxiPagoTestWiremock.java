package com.maxipago;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.PropertyException;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.maxipago.enums.ChallengePreference;
import com.maxipago.enums.ReportsPeriodEnum;
import com.maxipago.paymentmethod.Boleto;
import com.maxipago.paymentmethod.Card;
import com.maxipago.paymentmethod.OnlineDebit;
import com.maxipago.paymentmethod.Pix;
import com.maxipago.paymentmethod.Token;
import com.maxipago.request.RApiResponse;
import com.maxipago.request.TransactionResponse;


@RunWith(MockitoJUnitRunner.class)
public class MaxiPagoTestWiremock {
	private static String merchantId = "11631";
	private static String merchantKey = "hbsjs242px5vzpnmqu04xcd2";
    private static String CAPTURED_RESPONSE="capturedResponse.xml";
    private static String RAPI_RESPONSE="rapiResponse.xml";
    private static String CARDONFILE_RESPONSE="cardOnFileResponse.xml";
    private static String SALE_DECLINED_RESPONSE="saleDeclinedResponse.xml";
    private static String AUTH_ERROR_RESPONSE="authErrorResponse.xml";
    private static String UNIVERSAL_API="/UniversalAPI/postXML";
    private static String REPORTS_API="/ReportsAPI/servlet/ReportsAPI";
    private static final String RAPI_REFERENCE_NUMBER = "2023059999355845";

    @Rule
	public WireMockRule wireMockRule = new WireMockRule(8080);
    @Mock
	public HttpServletRequest request;
    
    @Test
    public void shouldCreateSaleWith3DS() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
	        .setPayment(new Payment(100.0));

       TransactionResponse response =  maxiPago.transactionRequest().execute();
       assertEquals("0", response.responseCode);
    }
    
    @Test
    public void shouldCaptureAuth() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

        maxiPago.capture()
                .setOrderId("0A010492:0172EB977CBC:8CDA:22FB4887")
                .setReferenceNum("Teste 123")
                .setPayment(new Payment(100.0));

        maxiPago.transactionRequest().execute();
    }

    @Test
    public void shouldCancelTransaction() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

        maxiPago.cancel()
                .setTransactionId("9044089");

        maxiPago.transactionRequest().execute();
    }
    
    @Test
    public void shouldCreateZeroDollarTransaction() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
    public void shouldCreateSaleWithCredit() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
    public void shouldCreateSaleWithTokenCryptogram() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

        maxiPago.sale()
                .setProcessorId("5")
                .setReferenceNum("CreateSaleWithTokenCryptogram")
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
                                .setCvvNumber("123")
                                .setTokenCryptogram("abc1238759uiokjflahdfkjlahs"))
                .setPayment(new Payment(100.0));

        TransactionResponse response = maxiPago.transactionRequest().execute();
        assertEquals("0", response.responseCode);
    }

    @Test
    public void shouldCreateSaleWithDebit() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
    public void shouldCreateAuthWithAntiFraud() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
    public void shouldCreateSaleWithAntiFraud() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
    public void shouldCreateBoleto() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
    public void shouldCreateOnlineDebit() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
    public void shouldRefundTransaction() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

        maxiPago.refund()
                .setTransactionId("9044118");

        maxiPago.transactionRequest().execute();
    }

    @Test
    public void shouldRefundPixTransaction() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

        maxiPago.pixRefund()
                .setOrderId("0123")
                .setReferenceNum("ref-123")
                .setPayment(new Payment(100.0));

        maxiPago.transactionRequest().execute();
    }

    @Test
    public void shouldCreateRecurringPayment() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
    public void shouldModifyRecurringPayment() {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
    public void shouldCancelRecurringPayment() {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

        maxiPago.cancelRecurring()
                .setOrderId("0A01048D:01717A02CF80:7FC7:5639916D");

        maxiPago.apiRequest().execute();
    }

    @Test
    public void shouldCreateConsumer() {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
    public void shouldUpdateConsumer() {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
    public void shouldAddCardOnFile() {
    	MaxiPago maxiPago = prepareResponse(CARDONFILE_RESPONSE, UNIVERSAL_API);

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
    public void shouldCreateAuthWithToken() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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

    public void shouldCreateSaleWithToken() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
    public void shouldCreateZeroDollarTransactionAndSaveOnFile() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
    public void shouldCreateAuthAndSaveOnFile() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CARDONFILE_RESPONSE, UNIVERSAL_API);

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
    public void shouldCreateSaleAndSaveOnFile() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CARDONFILE_RESPONSE, UNIVERSAL_API);

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
    public void shouldCreateRecurringPaymentWithToken() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
    public void shouldDeleteCardOnFile() {
    	MaxiPago maxiPago = prepareResponse(CARDONFILE_RESPONSE, UNIVERSAL_API);

        maxiPago.deleteCardOnFile()
                .setCustomerId("3039749")
                .setToken("35IbiFgNfJQ=");

        maxiPago.apiRequest().execute();
    }

    @Test
    public void shouldDeleteConsumer() {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

        maxiPago.deleteConsumer()
                .setCustomerId("3039749");

        maxiPago.apiRequest().execute();
    }

    @Test
    public void shouldConsultTransaction() {
    	MaxiPago maxiPago = prepareResponse(RAPI_RESPONSE, REPORTS_API);

        maxiPago.consultTransaction("8704233");

        RApiResponse response = maxiPago.rapiRequest().execute();

        for (Record record : response.records) {
            System.out.println(record.transactionId);
        }
    }

    @Test
    public void shouldConsultOrderList() {
    	MaxiPago maxiPago = prepareResponse(RAPI_RESPONSE, REPORTS_API);

        maxiPago.consultOrderList(ReportsPeriodEnum.LAST_MONTH)
                .setPageSize(5)
                .setPageNumber(1);

        RApiResponse response = maxiPago.rapiRequest().execute();

        maxiPago.consultOrderList(ReportsPeriodEnum.LAST_MONTH)
                .setPageSize(5)
                .setPageNumber(2)
                .setPageToken(response.resultSetInfo.pageToken);

        response = maxiPago.rapiRequest().execute();

        assertEquals(1, (int) response.resultSetInfo.pageNumber);
    }
    
    
    @Test
    public void shouldCreatePix() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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
    public void shouldCreatePixWithCustomerInfo() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(CAPTURED_RESPONSE, UNIVERSAL_API);

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

    @Test
    public void shouldCreateAuth() throws PropertyException {
    	MaxiPago maxiPago = prepareResponse(SALE_DECLINED_RESPONSE, UNIVERSAL_API);

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
        
        assertEquals("01", response.brandMac);
        assertEquals("83", response.brandCode);
        assertEquals("Fraud or security (Mastercard use only)", response.brandMessage);
        assertEquals("MPLN16FPG8594", response.brandTransactionID);
        
    }
    
    @Test
    public void shouldConsultOrder() {
    	MaxiPago maxiPago = prepareResponse(RAPI_RESPONSE, REPORTS_API);
        
        maxiPago.consultOrder("0A0104BA:017270E43DE2:ACBF:0B4E365C");

        RApiResponse response = maxiPago.rapiRequest().execute();

        for (Record record : response.records) {
        	if (record.transactionId!=null && "549470346".equals(record.transactionId) )
            {
        		assertEquals("00", record.brandCode);
        		assertEquals("Success.", record.brandMessage);
        		assertEquals("MPLMMHZV76543", record.brandTransactionID);
        		assertEquals("01", record.brandMac);
            }
        }
    }
    
	@Test
	public void shouldConsultOrderByReferenceNum() {
		MaxiPago maxiPago = prepareResponse(RAPI_RESPONSE, REPORTS_API);

		maxiPago.consultReferenceNum(RAPI_REFERENCE_NUMBER);

		RApiResponse response = maxiPago.rapiRequest().execute();

		boolean rightRecord = false;
		for (Record record : response.records) {
			if (record.referenceNum != null) {
				if (RAPI_REFERENCE_NUMBER.equals(record.referenceNum)) {
					rightRecord = true;
					break;
				}
			}
		}
		Assert.assertTrue(rightRecord);
	}

	@Test
	public void shouldConsultOrderByReferenceNumYesterday() {
		MaxiPago maxiPago = prepareResponse(RAPI_RESPONSE, REPORTS_API);

		maxiPago.consultReferenceNumPeriod(RAPI_REFERENCE_NUMBER, ReportsPeriodEnum.YESTERDAY);

		RApiResponse response = maxiPago.rapiRequest().execute();

		boolean rightRecord = false;
		for (Record record : response.records) {
			if (record.referenceNum != null) {
				if (RAPI_REFERENCE_NUMBER.equals(record.referenceNum)) {
					rightRecord = true;
					break;
				}
			}
		}
		Assert.assertTrue(rightRecord);
	}

	@Test
	public void shouldConsultOrderByReferenceNumLastSevenDays() {
		MaxiPago maxiPago = prepareResponse(RAPI_RESPONSE, REPORTS_API);

		maxiPago.consultReferenceNumPeriod(RAPI_REFERENCE_NUMBER, ReportsPeriodEnum.LAST_SEVEN);

		RApiResponse response = maxiPago.rapiRequest().execute();

		boolean rightRecord = false;
		for (Record record : response.records) {
			if (record.referenceNum != null) {
				if (RAPI_REFERENCE_NUMBER.equals(record.referenceNum)) {
					rightRecord = true;
					break;
				}
			}
		}
		Assert.assertTrue(rightRecord);
	}

	@Test
	public void shouldConsultOrderByReferenceNumLastThirtyDays() {
		MaxiPago maxiPago = prepareResponse(RAPI_RESPONSE, REPORTS_API);

		maxiPago.consultReferenceNumPeriod(RAPI_REFERENCE_NUMBER, ReportsPeriodEnum.LAST_THIRTY);

		RApiResponse response = maxiPago.rapiRequest().execute();

		boolean rightRecord = false;
		for (Record record : response.records) {
			if (record.referenceNum != null) {
				if (RAPI_REFERENCE_NUMBER.equals(record.referenceNum)) {
					rightRecord = true;
					break;
				}
			}
		}
		Assert.assertTrue(rightRecord);
	}

	@Test
	public void shouldConsultOrderByReferenceNumThisWeek() {
		MaxiPago maxiPago = prepareResponse(RAPI_RESPONSE, REPORTS_API);

		maxiPago.consultReferenceNumPeriod(RAPI_REFERENCE_NUMBER, ReportsPeriodEnum.THIS_WEEK);

		RApiResponse response = maxiPago.rapiRequest().execute();

		boolean rightRecord = false;
		for (Record record : response.records) {
			if (record.referenceNum != null) {
				if (RAPI_REFERENCE_NUMBER.equals(record.referenceNum)) {
					rightRecord = true;
					break;
				}
			}
		}
		Assert.assertTrue(rightRecord);
	}

	@Test
	public void shouldConsultOrderByReferenceNumThisMonth() {
		MaxiPago maxiPago = prepareResponse(RAPI_RESPONSE, REPORTS_API);

		maxiPago.consultReferenceNumPeriod(RAPI_REFERENCE_NUMBER, ReportsPeriodEnum.THIS_WEEK);

		RApiResponse response = maxiPago.rapiRequest().execute();

		boolean rightRecord = false;
		for (Record record : response.records) {
			if (record.referenceNum != null) {
				if (RAPI_REFERENCE_NUMBER.equals(record.referenceNum)) {
					rightRecord = true;
					break;
				}
			}
		}
		Assert.assertTrue(rightRecord);
	}

	@Test
	public void shouldConsultOrderByReferenceNumLastMonth() {
		MaxiPago maxiPago = prepareResponse(RAPI_RESPONSE, REPORTS_API);

		maxiPago.consultReferenceNumPeriod(RAPI_REFERENCE_NUMBER, ReportsPeriodEnum.LAST_MONTH);

		RApiResponse response = maxiPago.rapiRequest().execute();

		boolean rightRecord = false;
		for (Record record : response.records) {
			if (record.referenceNum != null) {
				if (RAPI_REFERENCE_NUMBER.equals(record.referenceNum)) {
					rightRecord = true;
					break;
				}
			}
		}
		Assert.assertTrue(rightRecord);
	}

	@Test
	public void shouldConsultOrderByReferenceNumRange() {
		MaxiPago maxiPago = prepareResponse(RAPI_RESPONSE, REPORTS_API);

		// mm/dd/yyyy
		String startDate = "05/05/2023";
		String endDate = "06/05/2023";

		// hh:mm:ss
		String startTime = "00:00:01";
		String endTime = "23:59:59";

		maxiPago.consultReferenceNumPeriodRange(RAPI_REFERENCE_NUMBER, startDate, endDate, startTime, endTime);

		RApiResponse response = maxiPago.rapiRequest().execute();

		boolean rightRecord = false;
		for (Record record : response.records) {
			if (record.referenceNum != null) {
				if (RAPI_REFERENCE_NUMBER.equals(record.referenceNum)) {
					rightRecord = true;
					break;
				}
			}
		}
		Assert.assertTrue(rightRecord);
	}

    @Test
    public void invalidProcessorIdTest(){
        MaxiPago maxiPago = prepareResponse(AUTH_ERROR_RESPONSE, UNIVERSAL_API);
        maxiPago.auth()
                .setProcessorId("0")
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
        assertTrue(StringUtils.isNotBlank(response.errorMsg));
        assertEquals("1", response.errorCode);
    }
    
    private String getXMLContextToParse(String strfile) {
		StringBuilder xmlData = new StringBuilder();
		try {
			File file = new File(strfile);

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String dataRead = reader.readLine();

			while (dataRead != null) {
				xmlData.append(dataRead);
				dataRead = reader.readLine();
			}
			
			reader.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return xmlData.toString();
	}
    
    private MaxiPago prepareResponse(String file, String path) {
		MaxiPago maxiPago = new MaxiPago(new Environment(merchantId, merchantKey, "http://localhost:8080"));
    	
    	String responseXML = getXMLContextToParse("src/test/resources/"+file);
        
        stubFor(any(urlPathMatching(path))
				  .willReturn(aResponse()
				  .withStatus(200)
				  .withHeader("Content-Type", "application/xml")
				  .withBody(responseXML)));
		return maxiPago;
	}
}
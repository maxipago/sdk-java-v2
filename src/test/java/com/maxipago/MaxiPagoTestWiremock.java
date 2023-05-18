package com.maxipago;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.PropertyException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.maxipago.paymentmethod.Card;
import com.maxipago.request.RApiResponse;
import com.maxipago.request.TransactionResponse;


@RunWith(MockitoJUnitRunner.class)
public class MaxiPagoTestWiremock {
    String merchantId = "11631";
    String merchantKey = "hbsjs242px5vzpnmqu04xcd2";

    @Rule
	public WireMockRule wireMockRule = new WireMockRule(8080);
    @Mock
	public HttpServletRequest request;

    @Test
    public void shouldCreateAuth() throws PropertyException {
        MaxiPago maxiPago = new MaxiPago(new Environment(merchantId, merchantKey, "http://localhost:8080"));
        
        String responseXML = getXMLContextToParse("src/test/resources/saleResponse.xml");
        
        stubFor(any(urlPathMatching("/UniversalAPI/postXML"))
				  .willReturn(aResponse()
				  .withStatus(200)
				  .withHeader("Content-Type", "application/xml")
				  .withBody(responseXML)));

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
        MaxiPago maxiPago = new MaxiPago(new Environment(merchantId, merchantKey, "http://localhost:8080"));

        String responseXML = getXMLContextToParse("src/test/resources/rapiResponse.xml");
        
        stubFor(any(urlPathMatching("/ReportsAPI/servlet/ReportsAPI"))
				  .willReturn(aResponse()
				  .withStatus(200)
				  .withHeader("Content-Type", "application/xml")
				  .withBody(responseXML)));
        
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
    
    public String getXMLContextToParse(String strfile) {
		StringBuilder xmlData = new StringBuilder();
		try {
			File file = new File(strfile);

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String dataRead = reader.readLine();

			while (dataRead != null) {
				xmlData.append(dataRead);
				dataRead = reader.readLine();
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return xmlData.toString();
	}
}
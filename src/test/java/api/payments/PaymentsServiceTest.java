package api.payments;

import api.business.services.PaymentsService;
import api.business.services.interfaces.IPaymentsService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class PaymentsServiceTest {
    IPaymentsService paymentsService;

    @Before
    public void setup(){

        paymentsService = new PaymentsService();
    }

    @Test
    public void correctEncodesUrlTest(){
        Map<String,String> map = new HashMap<>();
        map.put("test1","123");
        map.put("test2", "456");
        String urlEncoded = paymentsService.encodeUrl(map);

        Assert.assertEquals("test2=456&test1=123", urlEncoded);
    }

    @Test
    public void prepareUrlEncodedTest(){
        String input = "test17+testdx";

        String encoded = paymentsService.prepareUrlEncoded(input);

        Assert.assertEquals("dGVzdDE3K3Rlc3RkeA==", encoded);
    }

    @Test
    public void md5EncodeTest(){
        String input = "clubby";
        String md5 = paymentsService.toMd5(input);

        Assert.assertEquals("52de1c7342ec898a49c1d9e06824afab", md5);
    }
}

package com.binary.api;

import com.binary.api.models.requests.AuthorizeRequest;
import com.binary.api.models.requests.LoginHistoryRequest;
import com.binary.api.models.responses.AuthorizeResponse;
import com.binary.api.models.responses.LoginHistory;
import com.binary.api.models.responses.LoginHistoryResponse;
import com.binary.api.models.responses.ResponseBase;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author Morteza Tavanarad
 * @version 1.0.0
 * @since 8/7/2017
 */
public class LoginHistoryTest {

    private ApiWrapper api;
    private Properties properties;

    @Before
    public void setUp() throws Exception {
        TestUtility testUtility = new TestUtility();
        this.api = testUtility.getApi();
        this.properties = testUtility.getProperties();
    }

    @Test
    public void getLoginHistoryTest() throws Exception {
        AuthorizeRequest authRequest = new AuthorizeRequest(properties.getProperty("CR_READ"), false);
        LoginHistoryRequest request = new LoginHistoryRequest();
        TestObserver<ResponseBase> testObserver = new TestObserver<>();

        this.api.sendRequest(authRequest)
                .subscribe( o -> {
                    AuthorizeResponse auth = (AuthorizeResponse) o;
                    assertNotEquals(auth.getAuthorize(), null);

                    this.api.sendRequest(request)
                            .subscribe(testObserver);
                });

        testObserver.await(10, TimeUnit.SECONDS);

        LoginHistoryResponse response = (LoginHistoryResponse) testObserver.values().get(0);

        assertEquals(response.getType(), "login_history");
        assertEquals(response.getError(), null);
        assertNotEquals(response.getLoginHistories(), null);
    }
}

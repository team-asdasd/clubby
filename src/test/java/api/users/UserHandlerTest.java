package api.users;

import api.business.services.interfaces.ILoginService;
import api.business.services.interfaces.IUserService;
import api.handlers.users.GetCurrentUserHandler;
import clients.facebook.interfaces.IFacebookClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserHandlerTest {
    @Mock
    IUserService userServiceMock;

    @Mock
    ILoginService loginServiceMock;

    @Mock
    IFacebookClient facebookClientMock;

    @InjectMocks
    GetCurrentUserHandler getCurrentUserHandler;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void injectsAllTest(){
        Assert.assertNotNull(getCurrentUserHandler);
        Assert.assertNotNull(userServiceMock);
        Assert.assertNotNull(loginServiceMock);
        Assert.assertNotNull(facebookClientMock);
    }
}

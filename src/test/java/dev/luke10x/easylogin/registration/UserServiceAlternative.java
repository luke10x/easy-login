package dev.luke10x.easylogin.registration;

import dev.luke10x.easylogin.community.User;
import dev.luke10x.easylogin.community.UserService;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.enterprise.inject.Default;
import java.util.List;

@Default
public class UserServiceAlternative implements UserService {

    public UserService getMock() {
        return mock;
    }

//    @Mock
//    private UserService mock;
    private static final UserService mock = Mockito.mock(UserServiceAlternative.class);

    @Override
    public List<User> getAllUsers() {
        return mock.getAllUsers();
    }
}
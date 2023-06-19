package dev.luke10x.easylogin.registration;

import org.mockito.Mock;
import org.mockito.Mockito;

import javax.enterprise.inject.Default;

@Default
public class RegistrationServiceAlternative implements RegistrationService {

    public RegistrationService getMock() {
        return mock;
    }

//    @Mock
//    RegistrationService mock;

    private static RegistrationService mock = Mockito.mock(RegistrationServiceAlternative.class);

    @Override
    public void registerNewHandle(Handle handle) throws HandleAlreadyTakenException, HandleSizeException, HandleStorageException {
        mock.registerNewHandle(handle);
    }
}
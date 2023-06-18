package dev.luke10x.easylogin.registration;

public interface RegistrationService {

    void registerNewHandle(final Handle handle) throws
            HandleAlreadyTakenException,
            HandleSizeException, HandleStorageException;
}

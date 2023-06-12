package dev.luke10x.easylogin.registration;


import jakarta.mvc.binding.MvcBinding;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.FormParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class RegisterForm implements Serializable {

    @MvcBinding
    @NotNull
    @Size(min = 1, message = "Name cannot be blank")
    @Size(max = 16, message = "Handle cannot be longer than 16")
    @FormParam("handle")
    private String handle;
}

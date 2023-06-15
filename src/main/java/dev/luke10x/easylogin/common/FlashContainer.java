package dev.luke10x.easylogin.common;

import java.io.Serializable;

import jakarta.inject.Named;
import jakarta.mvc.RedirectScoped;

import lombok.Data;

@RedirectScoped
@Data
@Named("flashContainer")
public class FlashContainer implements Serializable {
  private static final long serialVersionUID = 4L;

  private String message = null;
}

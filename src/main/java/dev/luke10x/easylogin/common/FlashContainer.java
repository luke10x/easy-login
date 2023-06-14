package dev.luke10x.easylogin.common;

import java.util.List;
import java.io.Serializable;

import jakarta.mvc.RedirectScoped;

import lombok.Data;

@RedirectScoped
@Data
public class FlashContainer implements Serializable {
  private String message;
}

package br.com.zera.exception;

public class ErroServlet extends RuntimeException {
  public ErroServlet(String message) {
    super(message);
  }
}

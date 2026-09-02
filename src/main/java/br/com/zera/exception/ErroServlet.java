package br.com.zera.exception;

import br.com.zera.exception.ZeraException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ErroServlet extends RuntimeException {
    public ErroServlet(String message) {
        super(message);
    }
}


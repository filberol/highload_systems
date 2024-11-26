package ru.itmo.order.clients.exception;

public class InternalServerException extends RuntimeException {

  public InternalServerException(String message) {
    super(message);
  }
}

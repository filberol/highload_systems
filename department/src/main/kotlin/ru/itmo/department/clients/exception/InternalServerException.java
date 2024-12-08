package ru.itmo.department.clients.exception;

public class InternalServerException extends RuntimeException {

  public InternalServerException(String message) {
    super(message);
  }
}

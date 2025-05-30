package com.k218b.vehicleregistration.exception;

public class JsonSerializationException extends RuntimeException {

  /**
   * @param type the record class being serialized
   * @param field the record component name
   * @param cause the underlying reflection error
   */
  public JsonSerializationException(Class<?> type, String field, Throwable cause) {
    super("Failed to serialize %s#%s".formatted(type.getSimpleName(), field), cause);
  }
}

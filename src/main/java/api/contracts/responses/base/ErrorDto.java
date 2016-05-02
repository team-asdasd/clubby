package api.contracts.responses.base;

public class ErrorDto {
    public String Message;
    public ErrorCodes Code;

    public ErrorDto(String message, ErrorCodes errorCode) {
        Message = message;
        Code = errorCode;
    }

    @Override
    public String toString(){
        return String.format("%s - %s",Code, Message);
    }
}

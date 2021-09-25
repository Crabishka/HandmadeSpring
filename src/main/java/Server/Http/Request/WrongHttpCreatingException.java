package Server.Http.Request;

public class WrongHttpCreatingException extends Exception{
    public WrongHttpCreatingException(){
        System.out.println("Can not create HttpRequest");
    }
}

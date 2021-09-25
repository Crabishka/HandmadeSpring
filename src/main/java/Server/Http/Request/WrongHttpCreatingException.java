package Server.Http.Request;

public class WrongHttpCreatingException extends Exception{

    // неочемный экспепшн
    public WrongHttpCreatingException(){
        System.out.println("Can not create HttpRequest");
    }
}

package Server.Http;

public class WrongHttpCreatingException extends Exception{

    // неочемный экспепшн
    public WrongHttpCreatingException(){
        System.out.println("Can not create Http");
    }
}

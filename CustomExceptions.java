public class CustomExceptions {
    
      public static class NoresultException extends Exception{
          public NoresultException(){
            super();
        }
        public NoresultException(String msg){
            super(msg);
        }}
    
   public static class EmptyException extends Exception {
         public EmptyException(){
            super();
        }
        public EmptyException(String msg) {
            super(msg);
        }
    }
     public static class ErrorAdd extends Exception {
          public ErrorAdd(){
            super();
        }
        public ErrorAdd(String msg) {
            super(msg);
        }
    }
 public static class EmptyFieldsException extends Exception {
      public EmptyFieldsException(){
            super();
        }
        public EmptyFieldsException(String message) {
            super(message);
        }
    }

    public static class DatabaseException extends Exception {
        public DatabaseException(){
            super();
        }
        public DatabaseException(String message) {
            super(message);
        }
    }
    
    
    
     public static class ErrorLogin extends Exception{
          public ErrorLogin(){
            super();
        }
        public ErrorLogin(String msg){
            super(msg);
        }
    }
}

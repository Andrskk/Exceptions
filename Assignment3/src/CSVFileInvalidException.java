/**
 * Exception class for attribute(s) missing cases. Child class of InvalidException.
 */
public class CSVFileInvalidException extends InvalidException{

    //Default constructor. Simply display a message
    public CSVFileInvalidException()
    {
        super("line not converted to JSON: missing field");
    }
    //Parameterized constructor. Receives a message as a String. Prints using the parent class.
    public CSVFileInvalidException(String message)
    {
        super(message);
    }

}
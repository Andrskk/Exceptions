/**
 * Exception class for data missing cases. Child class of InvalidException.
 */
public class CSVDataMissingException  extends InvalidException{

    //Default constructor. Simply display a message
    public CSVDataMissingException()
    {
        super("line not converted to JSON: missing data");
    }

    //Parameterized constructor. Receives a message as a String. Prints using the parent class.
    public CSVDataMissingException(String message)
    {
        super(message);
    }

}

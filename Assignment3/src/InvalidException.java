/**
 * Exception class for attribute(s) missing cases. Inherits from Exception class.
 */
public class InvalidException extends Exception {

    //Default constructor. Simply display a message
    public InvalidException()
    {
        super("Error: Input row cannot be parsed due to missing information");
    }

    //Parameterized constructor. Receives a message as a String. Prints using the parent class.
    public InvalidException(String message)
    {
        super(message);
    }
}

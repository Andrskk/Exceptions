import java.io.*;
import java.util.Scanner;
//-------------------------------------
// Assignment 3 due to April 03, 2021
// Written by: Andrei Skachkou 40134189
// Comp 249
// The program is a converter from CSV(comma separated values) files into JSON(JavaScript Object Notation) format. It opens
// an existing CSV file, checks for all attributes/data and creates according json file. In case some
// attribute is missing, json file will not be created, and the error will be logged in log file. In case some
// data is missing, json file will be created without missing object, adding missing information to log file.
//-------------------------------------
/**
 * Driver class for program test.
 */
public class Driver {
    /**
     * main() method for the driver program.
     * Tries to opens two input CSV files, if any of them doesn't exist throw an exception and display a message to user.
     * If the input files can successfully be opened, it will call a method to open/create according json files.
     * @param args stores incoming command argument for the program.
     */
    public static void main(String[] args)
    {

        String fileName="Car Rental Record";
        String fileName2="Car Rental Record no DrivLic";

        //try block to attempt to open CSV files. Outer try block for the first CSV file, inner for the second one.
        try
        {
            // File and Scanner objects of first file instantiation
            File file = new File(fileName + ".csv");
            Scanner myFileReader = new Scanner(new FileInputStream(fileName + ".csv"));

            // Inner try block to attempt to open second CSV file and call a method to proceed the file
            // It's inside the outer try block in order to control json file creation for the first file
            // in case the second one does not exist
            try
            {
                // File and Scanner objects of second file instantiation
                File file2 = new File(fileName2+".csv");
                Scanner myFileReader2 = new Scanner(new FileInputStream(fileName2+".csv"));
                // Calling a core engine method to proceed the file
                processFilesForValidation(file2,myFileReader2,fileName2);
            }
            // catch block for the second file, if the file does not exist. Closes the outer first file Scanner. Terminates the program
            catch (FileNotFoundException e)
            {
                System.out.println("Could not open input file "+fileName2+".CSV for reading. Please check if file exists! Program " +
                        "will terminate after closing any opened files.");
                myFileReader.close();
                System.exit(0);
            }
            processFilesForValidation(file, myFileReader, fileName);
            myFileReader.close();
        }
        // catch block for the first file, if the file does not exist. Display message for user and terminates the program
        catch (FileNotFoundException e)
        {
            System.out.println("Could not open input file "+fileName+".CSV for reading. Please check if file exists! Program " +
                    "will terminate after closing any opened files.");
            System.exit(0);
        }

        //Prompt user to enter a name of a file to display
        System.out.print("Please enter a file name to display: ");
        Scanner keyboard = new Scanner(System.in);

        //try block for the first attempt. If file exists, call myBufferReader method to output a file containing
        try
        {
            String userInput = keyboard.nextLine();
            BufferedReader br=new BufferedReader(new FileReader(userInput + ".json"));
            myBufferReader(br);
            br.close();
        }
        //catch block in case the first user attempt was unsuccessful and input file does not exist. Ask the user again
        catch (FileNotFoundException e)
        {
            System.out.print("File with this name does not exist. Please enter a correct name: ");
            String userInput = keyboard.nextLine();

            //try block for the second attempt. If file exists, call myBufferReader method to output a file containing
            try
            {
                BufferedReader br=new BufferedReader(new FileReader(userInput + ".json"));
                myBufferReader(br);
                br.close();
            }
            //catch block in case the second user attempt was unsuccessful and input file does not exist. Ask the user last time
            catch (FileNotFoundException a)
            {
                System.out.print("File with this name does not exist. Please enter a correct name (This is the last attempt): ");
                userInput = keyboard.nextLine();

                //try block for the last attempt. If file exists, call myBufferReader method to output a file containing
                try
                {
                    BufferedReader br=new BufferedReader(new FileReader(userInput + ".json"));
                    myBufferReader(br);
                    br.close();
                }
                //catch block in case the last user attempt was unsuccessful and input file does not exist. Terminates the program
                catch (FileNotFoundException b)
                {
                    System.out.print("Wrong name, program will be terminated.");
                    System.exit(0);
                }
                //another catch block for nested try block above
                catch (IOException ioException)
                {
                    ioException.printStackTrace();
                }
            }
            //another catch block for nested try block above
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method to process the input file and create the output one. Creates 2D array of receiving CSV file, stores all file
     * attributes and data into it. Checks for missing attributes/data. Creates log file with according information.
     * Creates json file.
     * @param file Receives a File object to proceed with.
     * @param myFileReader Receives a Scanner object to proceed with.
     * @param fileName Receives a file name as a String.
     */
    public static void processFilesForValidation(File file,Scanner myFileReader,String fileName)
    {

        boolean isValid=true;
        // Counting number of lines to create 2D array this length (rows)
        Scanner lineCounter = null;
        try
        {
            lineCounter = new Scanner(new FileInputStream(file));
        }
        catch (FileNotFoundException e)
        {
            e.getMessage();
        }

        int lines = 0;
        while(lineCounter.hasNextLine()&&!(lineCounter.nextLine().isEmpty()))
        {
            lines++;
        }

        // Split the first line into array of String type in order to know the column length of future 2D array
        String line = myFileReader.nextLine();
        String [] arrLine1 = line.split(",");

        // 2D Array initializing and storing first line attributes
        String[][] arrayOfRecords = new String[lines][arrLine1.length];
        for (int i=0;i<arrayOfRecords[0].length;i++)
        {
            arrayOfRecords[0][i]=arrLine1[i];
        }
        // Split all the rest lines and Store file information into 2D array
        for (int i=1;i<arrayOfRecords.length;i++)
        {
            line = myFileReader.nextLine();
            arrLine1 = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            for (int j=0;j<arrayOfRecords[i].length;j++)
            {
                arrayOfRecords[i][j]= arrLine1[j];
            }
        }
        myFileReader.close();

        //Testing for missing fields
        //Counting missing fields, if there are
        int missingFields=0;
        for (int i=0;i<arrayOfRecords[0].length;i++)
        {
            if (arrayOfRecords[0][i].equals(""))
            {
                missingFields++;
            }
        }

        //loop to log file open/create
        for (int i=0;i<arrayOfRecords[0].length;i++)
        {
            try
            {
                if (arrayOfRecords[0][i].equals(""))
                {
                    isValid=false;
                    //if there is empty(missed) element, replace it with "***"
                    arrayOfRecords[0][i]="***";
                    //output stream object initializing to append lines into log file
                    PrintWriter myOutputStream = new PrintWriter(new FileOutputStream("logFile.txt",true));
                    //printing a message into log file along with missing attribute(s) information
                    myOutputStream.print("File "+file+" is invalid.\nMissing field: "+((arrayOfRecords[0].length)-missingFields)
                            +" detected, "+missingFields+" missing\n");
                    for (int k=0;k<arrayOfRecords[0].length;k++)
                    {
                        myOutputStream.print(arrayOfRecords[0][k]+",  ");
                    }
                    myOutputStream.println();
                    myOutputStream.flush();
                    myOutputStream.close();
                    //Exception thrown in case some attribute(s) is missing
                    throw new CSVFileInvalidException("File "
                            +file+" is invalid: field is missing.\nFile is not converted to JSON");
                }
            }
            //catch block to handle exception
            catch (CSVFileInvalidException | FileNotFoundException e)
            {
                //message for user
                System.out.println(e.getMessage());
            }
        }

        //Testing for missing data
        for (int i=1;i<arrayOfRecords.length;i++)
        {
            for (int j=0;j<arrayOfRecords[i].length;j++)
            {
                try
                {
                    if (arrayOfRecords[i][j].equals(""))
                    {
                        //if there is empty(missed) element, replace it with "***"
                        arrayOfRecords[i][j]="***";
                        //output stream object initializing to append lines into log file
                        PrintWriter myOutputStream = new PrintWriter(new FileOutputStream("logFile.txt",true));
                        //printing a message into log file along with missing data information
                        myOutputStream.print("In file "+file+" line "+(i+1)+" \n");
                        for (int k=0;k<arrayOfRecords[i].length;k++)
                        {
                            myOutputStream.print(arrayOfRecords[i][k]+"  ");
                        }
                        myOutputStream.println();
                        myOutputStream.println("Missing: "+arrayOfRecords[0][j]);
                        myOutputStream.flush();
                        myOutputStream.close();
                        //Exception thrown in case some data is missing
                        throw new CSVDataMissingException("In file "
                                +file+" line "+(i+1));
                    }
                }
                //catch block to handle missing data exception
                catch (CSVDataMissingException e)
                {
                    System.out.println(e.getMessage()+" not converted to JSON : missing data.");
                }
                //catch block to handle exception, when the file not found
                catch (FileNotFoundException e)
                {
                    System.out.println("File not found");
                }
            }
        }
        //if a file does not have any missing fields/data, simply create json file with all information
        if (isValid==true)
        {
            boolean hasMissingData=false;
            try
            {
                //output stream object initializing to append lines into json file
                PrintWriter myJsonWriter = new PrintWriter(new FileOutputStream(fileName+".json", true));

                // Clear before writing
                PrintWriter pw = new PrintWriter(fileName+".json");
                pw.write("");
                pw.flush();
                pw.close();

                //Converting from CSV to JSON (printing JSON objects)
                myJsonWriter.println("[");

                //loop to check missing information to do not create a json object of this line
                for (int k=1;k<arrayOfRecords.length;k++)
                {
                   for (int l=0;l<arrayOfRecords[k].length;l++)
                   {
                       if (arrayOfRecords[k][l].equals("***"))
                       {
                           hasMissingData=true;
                       }
                   }
                   //if the line contains all data fields, create an json object
                   if (!hasMissingData)
                   {
                        int a = 0;
                        myJsonWriter.println("  {");
                        while(a<arrayOfRecords[0].length)
                        {
                            if ((arrayOfRecords[k][a].charAt(0))==('\"'))
                                myJsonWriter.print("    \""+arrayOfRecords[0][a]+"\""+": "+arrayOfRecords[k][a]+",\n");
                            else
                                myJsonWriter.print("    \""+arrayOfRecords[0][a]+"\""+": "+"\""+arrayOfRecords[k][a]+"\",\n");
                                a++;
                           }
                           myJsonWriter.println("  },");
                   }
                   hasMissingData=false;
                }

                myJsonWriter.println("]");
                myJsonWriter.flush();
                myJsonWriter.close();
            //catch block to handle exception, when the file not found
            }
            catch (FileNotFoundException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Method to display a file containing. Read file chars one by one and prints them.
     * @param br Receives BufferReader type variable
     * @throws IOException
     */
    public static void myBufferReader(BufferedReader br) throws IOException
    {
        int myChar;
        while((myChar=br.read())!=-1)
        {
            System.out.print((char)myChar);
        }
        br.close();
    }
}
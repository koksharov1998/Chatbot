import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class QuizReader
{
    private String m_fileName;

    QuizReader(String fileName)
    {
        m_fileName = fileName;
    }

    public Pair[] readAndPrintFromFile()
    {
        ArrayList<Pair> text = new ArrayList<Pair>();
        try{
            FileReader fr = new FileReader(m_fileName);
            Scanner scan = new Scanner(fr);
            int i = 1;
            String first ="";
            String second = "";
            while (scan.hasNextLine())
            {
                if (i%2==1) {
                    first = scan.nextLine().substring(10);
                }
                else {
                        second = scan.nextLine().substring(8);
                        text.add(new Pair(first, second));
                }
                i++;
            }
            scan.close();
            fr.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        for(Pair line : text)
        {
            System.out.println(line.m_first);
            System.out.println(line.m_second);
        }
        return text.toArray(new Pair[text.size()]);
    }
}

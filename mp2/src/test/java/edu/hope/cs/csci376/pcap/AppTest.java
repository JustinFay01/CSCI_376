package edu.hope.cs.csci376.pcap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        //Load Strings
        File testFile = new File("testFile.txt");
        String[] ans = loadArr(testFile);

        File resultFile = new File("myResults.txt");
        String[] myResults = loadArr(resultFile);

        //Pacekt 174
        assertEquals(myResults[0].replaceAll("\\s+",""), ans[0].replaceAll("\\s+",""));
        //Pacekt 19
        assertTrue(myResults[0].equals(ans[0]));
        //Pacekt 64
        assertTrue(myResults[0].equals(ans[0]));

    }

    public String[] loadArr(File f){
        String packet174ans = "";
        String packet19ans = "";
        String packet64ans = "";

        String[] ans = {packet174ans, packet19ans, packet64ans};
        try{
            Scanner sc = new Scanner(f);
                for(int i = 0; i < 3; i++){
                    String tmp = "";
                    while(sc.hasNextLine()){
                        if(tmp.contains("STOP")) break;
                        ans[i] += tmp = sc.nextLine();
                    } 
                }
            sc.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return ans;
    }

}

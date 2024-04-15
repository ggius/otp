/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giuseppevitolo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 *
 * @author giuseppe
 */
public class OTPGenerator implements Runnable { 
    private int r; 

    public int getR() {
        return r;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) { 
            r = new Random().nextInt(100) + 1; 
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("otp.txt")))) {
                pw.print("Benvenuti!Il codice di sblocco OTP temporaneo è :" + r);
                System.out.println(r);
                Thread.sleep(10000);
            } catch (IOException e) {
                System.err.println("Errore scrittura  file: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoneyLaundering
{
    private static TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private int amountOfFilesTotal;
    private static AtomicInteger amountOfFilesProcessed;
    private static boolean pausa=false;
    private static ArrayList<MoneyThread> lista;
    private int numHilos = 0;

    public MoneyLaundering(int numHilos)
    {
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
        this.numHilos=numHilos;
    }

    public void processTransactionData() {
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        
        int porcion = amountOfFilesTotal/numHilos;
        int inicio=0;
        int fin=porcion;
        lista = new ArrayList<MoneyThread>();
        
        for(int i=0;i<numHilos;i++){
            if(i+1==numHilos && fin<amountOfFilesTotal){
            	fin=amountOfFilesTotal;
            }
            System.out.println("inicio: "+inicio+"fin: "+fin);
            MoneyThread hilo = new MoneyThread(transactionFiles.subList(inicio, fin));
            inicio = fin;
            fin += porcion;
            hilo.start();
            lista.add(hilo);
            //System.out.println("lis"+lis);
        }
        
        for (MoneyThread hi : lista) {
        	try {
				hi.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }

    public List<String> getOffendingAccounts()
    {
        return transactionAnalyzer.listOffendingAccounts();
    }

    private List<File> getTransactionFileList()
    {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }
    
    public static boolean getPausa(){
        return pausa;
    }
    
	public static TransactionAnalyzer getTransactionAnalyzer() {
		return transactionAnalyzer;
	}
	
	public static AtomicInteger getAmountOfFilesProcessed() {
		return amountOfFilesProcessed;
	}

    public static void main(String[] args)
    {
        MoneyLaundering moneyLaundering = new MoneyLaundering(5);
        Thread processingThread = new Thread(() -> moneyLaundering.processTransactionData());
        processingThread.start();
        while(true) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.contains("exit"))
                break;
            if(line.isEmpty()){
                if(pausa==false){
                    pausa=true;
                    System.out.println("Pausado");
                    
                }
                else if(pausa==true){
                    pausa=false;
                    System.out.println("Ejecutando");
                    for(MoneyThread hi:lista){
                        hi.despausar();
                    }
                }
            }
            String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
            List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
            String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
            message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
            System.out.println(message);
        }

    }

}

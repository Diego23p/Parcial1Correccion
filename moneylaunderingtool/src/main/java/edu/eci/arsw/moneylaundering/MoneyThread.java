package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.util.List;

public class MoneyThread extends Thread{
	
	private TransactionReader transactionReader;
    private List<File> transactionFiles;

    MoneyThread(List<File> lista){
    	transactionReader = new TransactionReader();
        transactionFiles=lista;
    }
    
    @Override
    public void run(){
        for (File transactionFile : transactionFiles) {
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFile);
            for (Transaction transaction : transactions) {
                MoneyLaundering.getTransactionAnalyzer().addTransaction(transaction);
            }
        }
       MoneyLaundering.getAmountOfFilesProcessed().incrementAndGet();
    }

}

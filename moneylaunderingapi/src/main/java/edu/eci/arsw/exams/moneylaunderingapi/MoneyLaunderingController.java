package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MoneyLaunderingController
{
	@Autowired
	@Qualifier("MoneyLaunderingServiceStub")
    MoneyLaunderingService moneyLaunderingService;

    @RequestMapping( value = "/fraud-bank-accounts",method = RequestMethod.GET)
    public ResponseEntity<?> offendingAccounts() {
    	try {
	    	List<SuspectAccount> datos = moneyLaunderingService.getSuspectAccounts();
	        return new ResponseEntity<>(datos, HttpStatus.ACCEPTED);
    	} catch (Exception e) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    @RequestMapping( value = "/fraud-bank-accounts",method = RequestMethod.POST)
    public ResponseEntity<?> postOffendingAccount(@RequestBody SuspectAccount suspectAccount) {
    	try {
	        moneyLaunderingService.updateSuspectAccounts(suspectAccount);
	        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    	} catch (Exception e) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    
    @RequestMapping( value = "/fraud-bank-accounts/{accountId}",method = RequestMethod.GET)
    public ResponseEntity<?> getAccountStatus(@PathVariable String accountId) {
    	try {
	    	SuspectAccount datos = moneyLaunderingService.getAccountStatus(accountId);
	        return new ResponseEntity<>(datos, HttpStatus.ACCEPTED);
    	} catch (Exception e) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    @RequestMapping( value = "/fraud-bank-accounts/{accountId}",method = RequestMethod.PUT)
    public ResponseEntity<?> updateAccountStatus(@RequestBody SuspectAccount suspectAccount) {
    	try {
	        moneyLaunderingService.updateAccountStatus(suspectAccount);
	        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    	} catch (Exception e) {
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }

}

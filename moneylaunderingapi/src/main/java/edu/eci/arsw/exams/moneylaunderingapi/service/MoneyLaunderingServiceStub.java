package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

@Service("MoneyLaunderingServiceStub")
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {
	
	CopyOnWriteArrayList<SuspectAccount> sospechosos;
	
	public MoneyLaunderingServiceStub() {
		this.sospechosos = new CopyOnWriteArrayList<SuspectAccount>();
	}
	
    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) {
    	for (SuspectAccount SA: sospechosos) {
			if (suspectAccount.accountId == SA.accountId) {
				SA.amountOfSmallTransactions += suspectAccount.amountOfSmallTransactions;
			}
		}
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) {
    	for (SuspectAccount SA: sospechosos) {
    		if (accountId == SA.accountId) {
    			return SA;
			}
    	}
        return null;
    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() {
        return this.sospechosos;
    }

	@Override
	public void updateSuspectAccounts(SuspectAccount suspectAccount) {
		boolean existe = false;
		for (SuspectAccount SA: sospechosos) {
			if (suspectAccount.accountId == SA.accountId) {
				existe = true;
			}
		}
		if (existe) {
			this.updateAccountStatus(suspectAccount);
		}
		else {
			sospechosos.add(suspectAccount);
		}
	}
}

package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.List;

public interface MoneyLaunderingService {
	List<SuspectAccount> getSuspectAccounts();
	void updateSuspectAccounts(SuspectAccount suspectAccount);
	SuspectAccount getAccountStatus(String accountId);
    void updateAccountStatus(SuspectAccount suspectAccount);
}

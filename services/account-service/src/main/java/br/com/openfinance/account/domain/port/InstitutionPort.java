package br.com.openfinance.account.domain.port;

import br.com.openfinance.account.domain.model.Account;

import java.util.List;

public interface InstitutionPort {

    List<Account> fetchAccounts(String customerId, String consentId);
}

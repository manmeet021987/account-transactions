package com.account.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hibernate.mapping.Array;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.account.bean.Account;
import com.account.bean.Transaction;
import com.account.exception.AccountNotFoundException;
import com.account.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTestNew {
	
	@Autowired
	private MockMvc mockMvc;

	  
	  @MockBean 
	  private AccountRepository accrepository;
	  

		@MockBean
		private AccountController controller;
	  
	  private static String BASE_PATH = "http://localhost:8080/accounts";
	  private static String TRANSACTION_PATH = "/accounts/585309209/transactions";
	 
	  private Account account;
	  private Transaction transaction;
	  
	  @Before	  
	  public void setupAccount() {
		     account = new Account();
			account.setAccountName("SGSaving726");
			account.setAccountNumber(585309209);
			account.setAccountType("Savings");
			account.setBalance(1234);
			
		
	}
	  
	  @Before
	  public void setUpTransaction(){
		 		  transaction=new Transaction();
		  transaction.setAccountName("Savings Account");
			transaction.setCreditAmount(1234);
			transaction.setCurrency("AUD");
			transaction.setDebitAmount(0);
			transaction.setDescription(null);
			transaction.setTransactionDate("08/12/2012");
			transaction.setType("credit");
			
			
			
	  }


	@Test
	  public void getAllAccountsReturnsCorrectResponse() throws Exception {
		 List<Account> allAccounts = Arrays.asList(account);
		 Mockito.when(controller.getAllAccounts()).thenReturn(allAccounts);
		   

		 final ResultActions result=mockMvc.perform(get(BASE_PATH)
		            .contentType(MediaType.APPLICATION_JSON))
		            .andExpect(status().isOk());

	    verifyJson(result);
	  }
	
	private void verifyJson(final ResultActions action) throws Exception {
	    action
	        .andExpect(jsonPath("$[0].accountName", is(account.getAccountName())))
	        .andExpect(jsonPath("$[0].accountType", is(account.getAccountType())))
	        .andExpect(jsonPath("$[0].balanceDate", is(account.getBalanceDate())))
	        .andExpect(jsonPath("$[0].currency", is(account.getCurrency())))
	        .andExpect(jsonPath("$[0].balance", is(account.getBalance())));
	  }

	@Test
	public void getAllTransactionsForAccountReturnsCorrectResponse() throws Exception  {
		 
		List<Transaction> transactionList = Arrays.asList(transaction);
		account.setTransactions(transactionList);
		long accNo=account.getAccountNumber();
		Resources<Transaction> resources = new Resources<>(account.getTransactions());
		
		// resources.add(accountLink);
		Mockito.when(controller.getAllTransactionsForAccount(accNo)).thenReturn(resources);
		   

		 final ResultActions result=mockMvc.perform(get(TRANSACTION_PATH)
		            .contentType("application/hal+json"))
		            .andExpect(status().isOk());

	    verifyTransactionJson(result);
	}
	
	
	
	private void verifyTransactionJson(ResultActions action) throws Exception {
		 action
		    .andExpect(jsonPath("$._embedded.transactions[0].transactionDate", is(transaction.getTransactionDate())))
	        .andExpect(jsonPath("$._embedded.transactions[0].currency", is(transaction.getCurrency())))	        
	        .andExpect(jsonPath("$._embedded.transactions[0].creditAmount", is(transaction.getCreditAmount())))
	        .andExpect(jsonPath("$._embedded.transactions[0].type", is(transaction.getType())))
	        .andExpect(jsonPath("$._embedded.transactions[0].description", is(transaction.getDescription())))
	        .andExpect(jsonPath("$._embedded.transactions[0].accountName", is(transaction.getAccountName())));
	  }
		


	@Test
	  public void getAccountThatDoesNotExistReturnsError() throws Exception {
		long id=account.getAccountNumber();
	    final AccountNotFoundException exception = new AccountNotFoundException("ID:"+id);
	    Mockito.when(accrepository.findById(id)).thenReturn(Optional.empty());
	    final ResultActions result = mockMvc.perform(get(BASE_PATH + "/" + id));
	    result.andExpect(status().isNotFound());
	   
	  }
	
	
	
	
	
}

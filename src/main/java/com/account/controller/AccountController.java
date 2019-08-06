package com.account.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.account.repository.AccountRepository;
import com.account.bean.Account;
import com.account.bean.Transaction;
import com.account.exception.AccountNotFoundException;

@RestController
public class AccountController {
	
	@Autowired
	AccountRepository repository;
	
	@GetMapping("/accounts")
	public List<Account> getAllAccounts(){		
		return repository.findAll();	
		
	}
	
	@GetMapping(value="/accounts/{accNo}/transactions",produces = "application/hal+json")
	public  Resources<Transaction> getAllTransactionsForAccount(@PathVariable("accNo") long accNo){
		
		 Optional<Account> account=repository.findById(accNo);
		 if(!account.isPresent())
				throw new AccountNotFoundException("ID: "+accNo);
		 List<Transaction> transactions=account.get().getTransactions();
		
		 
	
		Link accountLink = ControllerLinkBuilder
         .linkTo(ControllerLinkBuilder.methodOn(this.getClass())
         .getAllAccounts())
         .withRel("all-accounts");
		
		 Resources<Transaction> resources = new Resources<Transaction>(transactions,accountLink);
		
		 
		 
		 /*HttpHeaders responseHeaders = new HttpHeaders();
		   responseHeaders.set("Content-Type", "application/hal+json");*/
		 
		    return  resources;
		      
		      
		 

	}

	
}

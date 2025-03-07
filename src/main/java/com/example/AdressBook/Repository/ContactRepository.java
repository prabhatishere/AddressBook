package com.example.AdressBook.Repository;


import com.example.AdressBook.Model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {}
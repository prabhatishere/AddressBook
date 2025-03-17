package com.example.addressBook.controller;

import com.example.addressBook.model.Contact;
import com.example.addressBook.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // Get all contacts
    @GetMapping
    public List<Contact> getAllContacts() {
        return contactService.getAllContacts();
    }

    // Get contact by ID
    @GetMapping("/{id}")
    public Optional<Contact> getContactById(@PathVariable Long id) {
        return contactService.getContactById(id);
    }

    // Add a new contact
    @PostMapping
    public Contact addContact(@RequestBody Contact contact) {
        return contactService.saveContact(contact);
    }

    // Update a contact
    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable Long id, @RequestBody Contact contactDetails) {
        return contactService.getContactById(id).map(existingContact -> {
            existingContact.setName(contactDetails.getName());
            existingContact.setEmail(contactDetails.getEmail());
            existingContact.setPhone(contactDetails.getPhone());
            return contactService.saveContact(existingContact);
        }).orElseThrow(() -> new RuntimeException("Contact not found with id " + id));
    }

    // Delete a contact
    @DeleteMapping("/{id}")
    public String deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return "Contact deleted successfully!";
    }
}

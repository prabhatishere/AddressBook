package com.example.addressBook.controller;

import com.example.addressBook.dto.ResponseDTO;
import com.example.addressBook.model.Contact;
import com.example.addressBook.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Address Book API", description = "API for managing contacts in an address book")
@RestController
@RequestMapping("/api/addressbook")
public class AddressBookController {

    @Autowired
    private ContactService contactService;

    // GET: Fetch all contacts
    @Operation(summary = "Fetch all contacts", description = "Retrieves a list of all contacts stored in the address book")
    @GetMapping
    public ResponseEntity<ResponseDTO<List<Contact>>> getAllContacts() {
        List<Contact> contacts = contactService.getAllContacts();
        return ResponseEntity.ok(new ResponseDTO<>("All contacts fetched successfully", contacts));
    }

    // GET: Fetch contact by ID
    @Operation(summary = "Fetch contact by ID", description = "Retrieves a specific contact using their unique ID")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Contact>> getContactById(@PathVariable Long id) {
        Optional<Contact> contact = contactService.getContactById(id);
        return contact.map(value ->
                        ResponseEntity.ok(new ResponseDTO<>("Contact found", value)))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(new ResponseDTO<>("Contact not found", null)));
    }

    // POST: Add a new contact
    @Operation(summary = "Add a new contact", description = "Creates a new contact in the address book")
    @PostMapping
    public ResponseEntity<ResponseDTO<Contact>> addContact(@RequestBody Contact contact) {
        Contact savedContact = contactService.saveContact(contact);
        return ResponseEntity.status(201).body(new ResponseDTO<>("Contact added successfully", savedContact));
    }

    // PUT: Update an existing contact
    @Operation(summary = "Update a contact", description = "Updates the details of an existing contact based on their ID")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<Contact>> updateContact(@PathVariable Long id, @RequestBody Contact contactDetails) {
        Optional<Contact> contactOptional = contactService.getContactById(id);

        if (contactOptional.isPresent()) {
            Contact existingContact = contactOptional.get();
            existingContact.setName(contactDetails.getName());
            existingContact.setEmail(contactDetails.getEmail());
            existingContact.setPhone(contactDetails.getPhone());

            Contact updatedContact = contactService.saveContact(existingContact);
            return ResponseEntity.ok(new ResponseDTO<>("Contact updated successfully", updatedContact));
        } else {
            return ResponseEntity.status(404).body(new ResponseDTO<>("Contact not found", null));
        }
    }

    // DELETE: Delete a contact
    @Operation(summary = "Delete a contact", description = "Removes a contact from the address book using their ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteContact(@PathVariable Long id) {
        boolean deleted = contactService.deleteContact(id);
        if (deleted) {
            return ResponseEntity.ok(new ResponseDTO<>("Contact deleted successfully", "ID: " + id));
        } else {
            return ResponseEntity.status(404).body(new ResponseDTO<>("Contact not found", null));
        }
    }
}

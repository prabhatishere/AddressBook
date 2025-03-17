package com.example.addressBook.controller;

import com.example.addressBook.dto.ContactDTO;
import com.example.addressBook.dto.ResponseDTO;
import com.example.addressBook.service.IAddressBookService;
import com.example.addressBook.service.JwtTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Address Book API", description = "API for managing contacts in an address book")
@RestController
@RequestMapping("/api/addressbook")
public class AddressBookController {

    @Autowired
    private IAddressBookService addressBookService;

    @Autowired
    private JwtTokenService jwtTokenService;

    // ✅ GET: Fetch all contacts (protected by JWT)
    @Operation(summary = "Fetch all contacts", description = "Retrieves a list of all contacts stored in the address book")
    @GetMapping
    public ResponseEntity<ResponseDTO<List<ContactDTO>>> getAllContacts() {
        List<ContactDTO> contactDTOs = addressBookService.getAllContacts();
        return ResponseEntity.ok(new ResponseDTO<>("All contacts fetched successfully", contactDTOs));
    }

    // ✅ GET: Fetch contact by ID (protected by JWT)
    @Operation(summary = "Fetch contact by ID", description = "Retrieves a specific contact using their unique ID")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<ContactDTO>> getContactById(@PathVariable Long id) {
        ContactDTO contactDTO = addressBookService.getContactById(id);
        if (contactDTO != null) {
            return ResponseEntity.ok(new ResponseDTO<>("Contact found", contactDTO));
        } else {
            return ResponseEntity.status(404).body(new ResponseDTO<>("Contact not found", null));
        }
    }

    // ✅ POST: Add a new contact (protected by JWT)
    @Operation(summary = "Add a new contact", description = "Creates a new contact in the address book")
    @PostMapping
    public ResponseEntity<ResponseDTO<ContactDTO>> addContact(@RequestBody ContactDTO contactDTO) {
        ContactDTO savedContactDTO = addressBookService.saveContact(contactDTO);
        return ResponseEntity.status(201).body(new ResponseDTO<>("Contact added successfully", savedContactDTO));
    }

    // ✅ PUT: Update an existing contact (protected by JWT)
    @Operation(summary = "Update a contact", description = "Updates the details of an existing contact based on their ID")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<ContactDTO>> updateContact(@PathVariable Long id, @RequestBody ContactDTO contactDTO) {
        ContactDTO updatedContactDTO = addressBookService.updateContact(id, contactDTO);
        if (updatedContactDTO != null) {
            return ResponseEntity.ok(new ResponseDTO<>("Contact updated successfully", updatedContactDTO));
        } else {
            return ResponseEntity.status(404).body(new ResponseDTO<>("Contact not found", null));
        }
    }

    // ✅ DELETE: Delete a contact (protected by JWT)
    @Operation(summary = "Delete a contact", description = "Removes a contact from the address book using their ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteContact(@PathVariable Long id) {
        boolean deleted = addressBookService.deleteContact(id);
        if (deleted) {
            return ResponseEntity.ok(new ResponseDTO<>("Contact deleted successfully", "ID: " + id));
        } else {
            return ResponseEntity.status(404).body(new ResponseDTO<>("Contact not found", null));
        }
    }

    // Utility method to get user ID from the token
    private Long getUserIdFromToken() {
        String token = getAuthorizationToken();
        return jwtTokenService.verifyToken(token);
    }

    // Extract the token from the Authorization header
    private String getAuthorizationToken() {
        return SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
    }
}

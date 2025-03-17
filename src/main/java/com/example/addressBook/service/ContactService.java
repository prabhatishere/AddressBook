package com.example.addressBook.service;

import com.example.addressBook.dto.ContactDTO;
import com.example.addressBook.model.Contact;
import com.example.addressBook.repository.ContactRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ModelMapper modelMapper;

    // ✅ Get all contacts (returns DTOs)
    public List<ContactDTO> getAllContacts() {
        return contactRepository.findAll().stream()
                .map(contact -> modelMapper.map(contact, ContactDTO.class))
                .collect(Collectors.toList());
    }

    // ✅ Get contact by ID (returns DTO)
    public ContactDTO getContactById(Long id) {
        Optional<Contact> contactOptional = contactRepository.findById(id);
        return contactOptional.map(contact -> modelMapper.map(contact, ContactDTO.class)).orElse(null);
    }

    // ✅ Save a new contact (accepts DTO, returns DTO)
    public ContactDTO saveContact(ContactDTO contactDTO) {
        Contact contact = modelMapper.map(contactDTO, Contact.class);
        Contact savedContact = contactRepository.save(contact);
        return modelMapper.map(savedContact, ContactDTO.class);
    }

    // ✅ Update contact (accepts DTO, returns DTO)
    public ContactDTO updateContact(Long id, ContactDTO contactDTO) {
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if (contactOptional.isPresent()) {
            Contact existingContact = contactOptional.get();
            existingContact.setName(contactDTO.getName());
            existingContact.setEmail(contactDTO.getEmail());
            existingContact.setPhone(contactDTO.getPhone());
            Contact updatedContact = contactRepository.save(existingContact);
            return modelMapper.map(updatedContact, ContactDTO.class);
        }
        return null;
    }

    // ✅ Delete contact (returns boolean)
    public boolean deleteContact(Long id) {
        if (contactRepository.existsById(id)) {
            contactRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

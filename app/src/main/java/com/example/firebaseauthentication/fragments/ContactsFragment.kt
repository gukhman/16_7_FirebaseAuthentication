package com.example.firebaseauthentication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseauthentication.databinding.FragmentContactsBinding
import com.example.firebaseauthentication.modal.Contact
import com.example.firebaseauthentication.service.ContactsAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ContactsFragment : Fragment() {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var contactsAdapter: ContactsAdapter
    private val contactsList = mutableListOf<Contact>()
    private val dbReference = FirebaseDatabase.getInstance().getReference("contacts")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(inflater, container, false)

        // Инициализация RecyclerView
        contactsAdapter = ContactsAdapter(contactsList) { position ->
            deleteContact(position)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = contactsAdapter

        // Получаем данные из Firebase
        getContacts()

        // Обработчик сохранения нового контакта
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()

            if (name.isNotEmpty() && phone.isNotEmpty()) {
                val contact = Contact(name, phone)
                saveContact(contact)
            } else {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }

        // Обработчик кнопки назад
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Опция меню для выхода
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun getContacts() {
        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                contactsList.clear()
                for (dataSnapshot in snapshot.children) {
                    val contact = dataSnapshot.getValue(Contact::class.java)
                    contact?.id = dataSnapshot.key
                    contact?.let { contactsList.add(it) }
                }
                contactsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveContact(contact: Contact) {
        dbReference.push().setValue(contact).addOnSuccessListener {
            Toast.makeText(requireContext(), "Контакт сохранен", Toast.LENGTH_SHORT).show()
            binding.etName.text.clear()
            binding.etPhone.text.clear()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Ошибка сохранения контакта", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteContact(position: Int) {
        if (contactsList.isNotEmpty() && position < contactsList.size) {
            val contactId = contactsList[position].id
            if (contactId != null) {
                dbReference.child(contactId).removeValue()
                    .addOnSuccessListener {
                        if (position < contactsList.size) {
                            contactsList.removeAt(position)
                            contactsAdapter.notifyItemRemoved(position)
                            Toast.makeText(requireContext(), "Контакт удален", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Ошибка удаления контакта", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Ошибка: контакт не найден", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Ошибка: список контактов пуст или недопустимый индекс", Toast.LENGTH_SHORT).show()
        }
    }

}

package com.example.firebaseauthentication.service

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseauthentication.databinding.ItemContactBinding
import com.example.firebaseauthentication.modal.Contact

class ContactsAdapter(
    private val contacts: List<Contact>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bind(contact, position)
    }

    override fun getItemCount(): Int = contacts.size

    inner class ContactViewHolder(private val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Contact, position: Int) {
            binding.tvName.text = contact.name
            binding.tvPhone.text = contact.phone

            // Обработчик для кнопки удаления
            binding.btnDelete.setOnClickListener {
                onDeleteClick(position)
            }
        }
    }
}


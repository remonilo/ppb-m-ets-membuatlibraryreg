package com.remonilo.libraryreg.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.remonilo.libraryreg.data.Anggota
import com.remonilo.libraryreg.databinding.ItemMemberBinding
import com.remonilo.libraryreg.util.Tampilan

class MemberAdapter(
    private val onItemClick: (Anggota) -> Unit,
) : ListAdapter<Anggota, MemberAdapter.ViewHolder>(Diff) {

    class ViewHolder(val binding: ItemMemberBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val anggota = getItem(position)
        with(holder.binding) {
            tvAvatar.text = Tampilan.inisial(anggota.nama)
            tvNama.text = anggota.nama
            tvMeta.text = "${anggota.nomorInduk} · ${anggota.programStudi}"
            Tampilan.terapkanBadge(tvBadge, anggota.jenisKeanggotaan)
            root.setOnClickListener { onItemClick(anggota) }
        }
    }

    private object Diff : DiffUtil.ItemCallback<Anggota>() {
        override fun areItemsTheSame(oldItem: Anggota, newItem: Anggota) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Anggota, newItem: Anggota) = oldItem == newItem
    }
}

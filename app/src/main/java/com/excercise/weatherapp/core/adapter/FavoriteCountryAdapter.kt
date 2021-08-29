package com.excercise.weatherapp.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.excercise.weatherapp.core.data.source.local.entity.FavoriteCountryByCoordinate
import com.excercise.weatherapp.databinding.ItemFavoriteLocationBinding

class FavoriteCountryAdapter :
    RecyclerView.Adapter<FavoriteCountryAdapter.FavoriteCountryViewHolder>() {
    inner class FavoriteCountryViewHolder(private val binding: ItemFavoriteLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemData: FavoriteCountryByCoordinate) {
            binding.txtNameLocation.text = itemData.nameCountry
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<FavoriteCountryByCoordinate>() {
        override fun areItemsTheSame(
            oldItem: FavoriteCountryByCoordinate,
            newItem: FavoriteCountryByCoordinate
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FavoriteCountryByCoordinate,
            newItem: FavoriteCountryByCoordinate
        ): Boolean {
            return oldItem.pkFavoriteCountery == newItem.pkFavoriteCountery
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCountryViewHolder =
        FavoriteCountryViewHolder(
            ItemFavoriteLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: FavoriteCountryViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

}
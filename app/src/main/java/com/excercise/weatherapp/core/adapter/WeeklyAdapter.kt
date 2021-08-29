package com.excercise.weatherapp.core.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.excercise.weatherapp.core.domain.model.DailyWeather
import com.excercise.weatherapp.databinding.ItemEstimatedWeatherBinding
import com.excercise.weatherapp.utils.Constants
import com.excercise.weatherapp.utils.toDate

class WeeklyAdapter : RecyclerView.Adapter<WeeklyAdapter.WeeklyViewHolder>() {
    inner class WeeklyViewHolder(private val binding: ItemEstimatedWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(itemData: DailyWeather) {
            Glide.with(itemView)
                .load(Constants.ICON_URL + itemData.iconUrl + ".png")
                .into(binding.imgLogoWeather)
            binding.txtDateWeather.text = itemData.dt.toDate("ddd")
            binding.txtTemp.text = "${itemData.temp} \u2103"
        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<DailyWeather>() {
        override fun areItemsTheSame(
            oldItem: DailyWeather,
            newItem: DailyWeather
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DailyWeather,
            newItem: DailyWeather
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyViewHolder =
        WeeklyViewHolder(
            ItemEstimatedWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: WeeklyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size
}
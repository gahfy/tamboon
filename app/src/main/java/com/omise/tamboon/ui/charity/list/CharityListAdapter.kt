package com.omise.tamboon.ui.charity.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.omise.tamboon.R
import com.omise.tamboon.databinding.ItemCharityBinding
import com.omise.tamboon.model.Charity
import com.omise.tamboon.ui.charity.CharityViewModel

class CharityListAdapter(private val parentViewModel: CharityViewModel): Adapter<CharityViewHolder>(){
    private var charityList: List<Charity> = listOf()

    fun updateList(charityList: List<Charity>){
        this.charityList = charityList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharityViewHolder {
        val binding: ItemCharityBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_charity,
            parent,
            false
        )
        return CharityViewHolder(parentViewModel, binding)
    }

    override fun getItemCount(): Int {
        return charityList.count()
    }

    override fun onBindViewHolder(holder: CharityViewHolder, position: Int) {
        holder.bind(charityList[position])
    }

}

class CharityViewHolder(parentViewModel: CharityViewModel, binding: ItemCharityBinding): ViewHolder(binding.root){
    private val viewModel = CharityItemViewModel(parentViewModel)

    init{
        binding.viewModel = viewModel
    }

    fun bind(charity: Charity){
        viewModel.bind(charity)
    }
}
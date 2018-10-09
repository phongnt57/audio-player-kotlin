package com.pntstudio.buzz.tedaudio.model

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.pntstudio.buzz.tedaudio.R
import android.graphics.Movie






public class MediaItemAdapter(internal var mCtx: Context,
                              internal var heroList: ArrayList<MediaItemData>,
                              internal var heroListFilter: ArrayList<MediaItemData>,
                              internal var onClickItem: OnClickItem,
                              internal var currentItemSelect: Int
) : RecyclerView.Adapter<MediaItemAdapter.HeroViewHolder>(),Filterable {
    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) {
                    heroListFilter = heroList
                } else {
                    val filteredList: ArrayList<MediaItemData> = arrayListOf()
                    for (item in heroList) {
                        if (item.title!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(item)
                        }
                    }
                    heroListFilter = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = heroListFilter
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                heroListFilter = results!!.values as ArrayList<MediaItemData>
                notifyDataSetChanged()
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val view = LayoutInflater.from(mCtx).inflate(R.layout.fragment_mediaitem, parent, false)
        return HeroViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val hero = heroListFilter[position]

        Glide.with(mCtx)
                .load(hero.imageUrl)
                .into(holder.imageView)

        holder.title.setText(hero.title)
        holder.subTitle.setText(hero.dateTime)
        holder.itemLayout.setOnClickListener{
            onClickItem.onClick(hero,position)
        }
        if(position==currentItemSelect){
            holder.itemLayout.setBackgroundResource(R.drawable.media_item_background_click)

        }else{
            holder.itemLayout.setBackgroundResource(R.drawable.media_item_background)

        }
    }

    override fun getItemCount(): Int {
        return heroListFilter.size
    }
    fun setCurrentSelect(item: Int){
        currentItemSelect = item
    }

    inner class HeroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView
        var title: TextView
        var  subTitle: TextView
        var itemLayout: ConstraintLayout

        init {

            imageView = itemView.findViewById(R.id.albumbArt)
            title = itemView.findViewById(R.id.title)
            subTitle = itemView.findViewById(R.id.subtitle)
            itemLayout = itemView.findViewById(R.id.item_layout)
        }
    }
    public interface OnClickItem {
        fun onClick(item:MediaItemData,position: Int)
    }
}
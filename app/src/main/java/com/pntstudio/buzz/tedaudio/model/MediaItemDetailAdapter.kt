package com.pntstudio.buzz.tedaudio.model

import android.content.Context
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pntstudio.buzz.tedaudio.R
import com.pntstudio.buzz.tedaudio.model.MediaItemData


public class MediaItemDetailAdapter(internal var mCtx: Context, internal var heroList: ArrayList<MediaItemData>,
                              internal var onClickItem: OnClickItem,
                              internal var currentItemSelect: Int
) : RecyclerView.Adapter<MediaItemDetailAdapter.HeroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val view = LayoutInflater.from(mCtx).inflate(R.layout.fragment_mediaitem_detail, parent, false)
        return HeroViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val hero = heroList[position]

        Glide.with(mCtx)
                .load(hero.imageUrl)
                .apply(RequestOptions().placeholder(R.drawable.ic_album_black_24dp).error(R.drawable.ic_album_black_24dp))

                .into(holder.imageView)

        holder.title.setText(hero.title)
        holder.subTitle.setText(hero.dateTime)
        holder.itemLayout.setOnClickListener{
            onClickItem.onClick(hero,position)
        }
        if(position==currentItemSelect){
//            holder.itemLayout.setBackgroundResource(R.drawable.media_item_background_click)

        }else{
//            holder.itemLayout.setBackgroundResource(R.drawable.media_item_background)


        }
    }

    override fun getItemCount(): Int {
        return heroList.size
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
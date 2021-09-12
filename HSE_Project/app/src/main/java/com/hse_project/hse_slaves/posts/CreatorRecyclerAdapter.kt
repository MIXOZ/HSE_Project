package com.hse_project.hse_slaves.posts

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hse_project.hse_slaves.R
import com.hse_project.hse_slaves.activities.MainActivity
import com.hse_project.hse_slaves.current.IS_TMP_USER
import com.hse_project.hse_slaves.current.TMP_USER_ID
import com.hse_project.hse_slaves.image.getBitmapByString
import com.hse_project.hse_slaves.model.User
import kotlinx.android.synthetic.main.layout_blog_list_item.view.*

class CreatorRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = ArrayList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_blog_list_item, parent, false)

        return CreatorViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CreatorViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun submitList(user: List<User>) {
        items.addAll(user)
        notifyDataSetChanged()
    }

    class CreatorViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        var id = 0
        private val nikName: TextView = itemView.nik_name
        private val date: TextView = itemView.date
        private val specialization: TextView = itemView.specialization
        private val ratio: TextView = itemView.ratio
        private val geo: TextView = itemView.geo


        fun bind(user: User) {
            nikName.text = user.username
            date.text = ""
            specialization.text = user.specialization
            ratio.text = user.rating.toString()
            geo.text = ""

            id = user.id
            if (user.images.size != 0) {
                itemView.imageButton.setImageBitmap(getBitmapByString(user.images[0]))
            }
            itemView.setOnClickListener {
                TMP_USER_ID = id
                IS_TMP_USER = true
                itemView.context.startActivity(Intent(itemView.context, MainActivity::class.java))
            }
        }

    }

}
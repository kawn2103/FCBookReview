package kst.app.fcbookreview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kst.app.fcbookreview.databinding.ItemBookBinding
import kst.app.fcbookreview.model.Book
import javax.net.ssl.SSLSessionBindingEvent

class BookAdapter: ListAdapter<Book,BookAdapter.BookItemViewHolder>(diffUtil) {

    inner class  BookItemViewHolder(private val binding: ItemBookBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(bookModel: Book){
            binding.titleTv.text = bookModel.title
            binding.descriptionTv.text = bookModel.description
            Glide
                .with(binding.bookIv.context)
                .load(bookModel.coverSmallUrl)
                .into(binding.bookIv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {

        return BookItemViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Book>() {
            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}
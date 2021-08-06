package kst.app.fcbookreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.bumptech.glide.Glide
import kst.app.fcbookreview.databinding.ActivityDetailBinding
import kst.app.fcbookreview.model.Book
import kst.app.fcbookreview.model.Review

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = getAppDatabase(this)

        val model = intent.getParcelableExtra<Book>("bookModel")

        binding.tileTv.text = model?.title.orEmpty()
        binding.descriptionTv.text = model?.description.orEmpty()
        Glide.with(binding.coverIv.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverIv)


        Thread{
            val review = db.reviewDao().getOneReview(model?.id?.toInt() ?: 0)
            runOnUiThread {
                binding.reviewEt.setText(review?.review.orEmpty())
            }
        }.start()

        binding.saveBt.setOnClickListener {
            Thread{
                db.reviewDao().saveReview(
                    Review(
                        model?.id?.toInt() ?:0,
                        binding.reviewEt.text.toString()
                    )
                )
            }.start()
        }
    }
}
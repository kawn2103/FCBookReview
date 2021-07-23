 package kst.app.fcbookreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import kst.app.fcbookreview.adapter.BookAdapter
import kst.app.fcbookreview.api.BookService
import kst.app.fcbookreview.databinding.ActivityMainBinding
import kst.app.fcbookreview.model.BestSellerDto
import kst.app.fcbookreview.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var bookService: BookService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks(getString(R.string.interparkApiKey))
            .enqueue(object: Callback<BestSellerDto>{
                override fun onResponse(
                    call: Call<BestSellerDto>,
                    response: Response<BestSellerDto>
                ) {
                    if (!response.isSuccessful){
                        Log.e("gwan2103_$TAG","NOT SUCCESS")
                        return
                    }

                    response.body()?.let {
                        Log.d("gwan2103_$TAG",it.toString())
                        it.books.forEach { book ->
                            Log.d("gwan2103_$TAG",book.toString())
                        }
                        adapter.submitList(it.books)
                    }
                }

                override fun onFailure(call: Call<BestSellerDto>, t: Throwable) {
                    TODO("Not yet implemented")
                    Log.e("gwan2103_$TAG",t.toString())
                }
            })

        binding.searchET.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN){
                search(binding.searchET.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun initViews(){
        adapter = BookAdapter()
        binding.bookRv.layoutManager = LinearLayoutManager(this)
        binding.bookRv.adapter = adapter
    }

    private fun search(keyword: String){
        bookService.getBooksByName(getString(R.string.interparkApiKey),keyword)
            .enqueue(object: Callback<SearchBookDto>{
                override fun onResponse(
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>
                ) {
                    if (!response.isSuccessful){
                        Log.e("gwan2103_$TAG","NOT SUCCESS")
                        return
                    }
                    adapter.submitList(response.body()?.books.orEmpty())
                }

                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    TODO("Not yet implemented")
                    Log.e("gwan2103_$TAG",t.toString())
                }
            })
    }

    companion object{
        private const val TAG = "MainActivity"
    }
}
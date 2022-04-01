package com.dicoding.picodiploma.githubuserapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.picodiploma.githubuserapp.apater.SectionPagerAdapter
import com.dicoding.picodiploma.githubuserapp.databinding.ActivityDetailUserBinding
import com.dicoding.picodiploma.githubuserapp.data.model.DetailUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {
    companion object{
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar_url = intent.getStringExtra(EXTRA_URL)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)

        if (username != null) {
            viewModel.setUserDetail(username)
        }
        viewModel.getUserDetail().observe(this,{
                binding.apply {
                    showLoading(false)
                    tvUsername.text = defaultValue(it.login)
                    tvName.text = defaultValue(it.name)
                    tvRepository.text = defaultValue("${it.public_repos} Repository")
                    tvFollowers.text = defaultValue("${it.followers} Followers")
                    tvFollowing.text =defaultValue("${it.following} Following")
                    tvLocation.text = it.location?:"Unknown"
                    tvCompany.text = it.company?:"Unknown"
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatar_url)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .circleCrop()
                        .into(ivAvatar)
                }
        })

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if (count != null){
                    if (count > 0) {
                        binding.tbFavorite.isChecked = true
                        _isChecked = true
                    } else {
                        binding.tbFavorite.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.tbFavorite.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                viewModel.addToFavorite(username!!, id, avatar_url!!)
            } else {
                viewModel.removeFromFavorite(id)
            }
            binding.tbFavorite.isChecked = _isChecked
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tabs.setupWithViewPager(viewPager)
        }
    }

    private fun defaultValue(x: String) : String {
        val text: String
        if (x.isNullOrEmpty()){
            text = "Unknown"
        } else {
            text = x
        }
        return text
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
package com.docshare.docshare.fragments.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.docshare.docshare.R
import com.docshare.docshare.databinding.FragmentSearchBinding
import com.docshare.docshare.ui.adapters.SearchAdapter
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: SearchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = SearchAdapter { user ->
            val action = com.docshare.docshare.fragments.search.SearchFragmentDirections.actionSearchFragmentToContactCardFragment(user.id)
            findNavController().navigate(action)
        }
        binding.recyclerResults.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerResults.adapter = adapter

        setupSearchBar(binding.searchBar, binding.searchView)

        viewModel.searchResults.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            updateStates(binding.searchView.text?.toString().orEmpty(), list)
            fadeIn(binding.recyclerResults)
        }
    }

    private fun setupSearchBar(searchBar: SearchBar, searchView: SearchView) {
        searchBar.setOnClickListener { searchView.show() }
        searchView.addTransitionListener { _, _, newState ->
            if (newState == SearchView.TransitionState.HIDDEN) {
                searchBar.setText(searchView.text)
            }
        }
        searchView.getEditText().addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.onQueryChanged(s?.toString().orEmpty())
            }
        })
    }

    private fun updateStates(query: String, results: List<com.docshare.docshare.data.model.AppUser>) {
        val q = query.trim()
        val showEmpty = q.length < 2
        val showNoResults = !showEmpty && results.isEmpty()

        binding.tvEmpty.visibility = if (showEmpty) View.VISIBLE else View.GONE
        binding.tvNoResults.visibility = if (showNoResults) View.VISIBLE else View.GONE
        binding.recyclerResults.visibility = if (!showEmpty && results.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun fadeIn(target: View) {
        target.alpha = 0f
        target.animate().alpha(1f).setDuration(200).start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

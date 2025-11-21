package com.docshare.docshare.fragments.contact

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.docshare.docshare.R
import com.docshare.docshare.databinding.FragmentContactCardBinding
import com.google.android.material.chip.Chip

class ContactCardFragment : Fragment() {
    private var _binding: FragmentContactCardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactCardViewModel by viewModels()
    private val args: ContactCardFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentContactCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.progressCenter.visibility = View.VISIBLE
        binding.content.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.loadUser(args.userId)
            viewModel.selectedUser.observe(viewLifecycleOwner) { user ->
                binding.progressCenter.visibility = View.GONE
                binding.content.visibility = View.VISIBLE
                if (user != null) {
                    binding.tvName.text = user.name
                    binding.tvPhone.text = user.phone
                    binding.ivAvatar.setImageResource(R.drawable.ic_image)

                    setupDocumentChips()
                }
            }
        }, 300)

        binding.btnRequestDocs.isEnabled = false
        binding.btnViewHistory.isEnabled = false
        binding.btnViewHistory.alpha = 0.6f
    }

    private fun setupDocumentChips() {
        val docs = listOf("Aadhaar", "PAN", "Driving License", "Passport", "Ration Card")
        binding.chipGroupDocs.removeAllViews()
        for (d in docs) {
            val chip = Chip(requireContext())
            chip.text = d
            chip.isClickable = true
            chip.setOnClickListener { Toast.makeText(requireContext(), "Document info preview coming soon", Toast.LENGTH_SHORT).show() }
            binding.chipGroupDocs.addView(chip)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_contact_card, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_trusted -> { Toast.makeText(requireContext(), "Add to Trusted", Toast.LENGTH_SHORT).show(); true }
            R.id.action_block -> { Toast.makeText(requireContext(), "Block", Toast.LENGTH_SHORT).show(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


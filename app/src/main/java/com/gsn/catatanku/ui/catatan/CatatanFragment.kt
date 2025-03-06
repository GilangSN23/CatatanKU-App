package com.gsn.catatanku.ui.catatan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gsn.catatanku.ViewModelFactory
import com.gsn.catatanku.databinding.FragmentCatatanBinding
import com.gsn.catatanku.ui.insert.NoteAddUpdateActivity

class CatatanFragment : Fragment() {
    private var _binding: FragmentCatatanBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatatanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NoteAdapter()
        binding.rvNotes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotes.setHasFixedSize(true)
        binding.rvNotes.adapter = adapter

        Log.d("CatatanFragment", "FAB ditemukan: ${binding.fabAdd}")

        binding.fabAdd.setOnClickListener {
            val intent = Intent(requireContext(), NoteAddUpdateActivity::class.java)
            startActivity(intent)
        }

        val factory = ViewModelFactory.getInstance(requireActivity().application)
        val catatanViewModel = ViewModelProvider(this, factory)[CatatanViewModel::class.java]
        catatanViewModel.getAllNotes().observe(viewLifecycleOwner) { notes ->
            adapter.setListNotes(notes)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

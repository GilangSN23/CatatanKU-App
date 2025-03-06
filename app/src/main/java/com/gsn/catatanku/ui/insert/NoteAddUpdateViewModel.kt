package com.gsn.catatanku.ui.insert

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gsn.catatanku.database.Note
import com.gsn.catatanku.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteAddUpdateViewModel (application: Application) : AndroidViewModel(application) {
    private val mNoteRepository: NoteRepository = NoteRepository(application)

    fun insert(note: Note) = viewModelScope.launch { mNoteRepository.insert(note) }
    fun update(note: Note) = viewModelScope.launch { mNoteRepository.update(note) }
    fun delete(note: Note) = viewModelScope.launch { mNoteRepository.delete(note) }
}
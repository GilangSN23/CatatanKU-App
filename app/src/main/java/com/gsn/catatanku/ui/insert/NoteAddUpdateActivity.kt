package com.gsn.catatanku.ui.insert

import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gsn.catatanku.R
import com.gsn.catatanku.ViewModelFactory
import com.gsn.catatanku.database.Note
import com.gsn.catatanku.databinding.ActivityNoteAddUpdateBinding
import com.gsn.catatanku.helper.DateHelper
import com.gsn.catatanku.repository.NoteRepository

class NoteAddUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteAddUpdateBinding
    private val noteAddUpdateViewModel: NoteAddUpdateViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    private var isEdit = false
    private var note: Note? = null

    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupNoteData()
        setupButtonListener()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = if (isEdit) getString(R.string.change) else getString(R.string.add)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupNoteData() {
        note = intent.getParcelableExtra(EXTRA_NOTE)
        isEdit = note != null

        if (isEdit) {
            binding.edtCourse.setText(note?.course)
            binding.edtTitle.setText(note?.title)
            binding.btnSubmit.text = getString(R.string.update)
        } else {
            note = Note()
            binding.btnSubmit.text = getString(R.string.save)
        }
    }

    private fun setupButtonListener() {
        binding.btnSubmit.setOnClickListener {
            val course = binding.edtCourse.text.toString().trim()
            val title = binding.edtTitle.text.toString().trim()

            if (course.isEmpty()) {
                binding.edtCourse.error = getString(R.string.empty)
                return@setOnClickListener
            }
            if (title.isEmpty()) {
                binding.edtTitle.error = getString(R.string.empty)
                return@setOnClickListener
            }

            note?.apply {
                this.course = course
                this.title = title
                if (!isEdit) this.date = DateHelper.getCurrentDate()
            }

            if (isEdit) {
                noteAddUpdateViewModel.update(note!!)
                showToast(getString(R.string.changed))
            } else {
                noteAddUpdateViewModel.insert(note!!)
                showToast(getString(R.string.added))
            }
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showAlertDialog(ALERT_DIALOG_CLOSE)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEdit) menuInflater.inflate(R.menu.menu_form, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle = if (isDialogClose) getString(R.string.cancel) else getString(R.string.delete)
        val dialogMessage = if (isDialogClose) getString(R.string.message_cancel) else getString(R.string.message_delete)

        AlertDialog.Builder(this).apply {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                if (!isDialogClose) {
                    noteAddUpdateViewModel.delete(note!!)
                    showToast(getString(R.string.deleted))
                }
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        }.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

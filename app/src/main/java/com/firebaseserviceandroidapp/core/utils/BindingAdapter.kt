package com.firebaseserviceandroidapp.core.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.firebaseserviceandroidapp.R
import com.firebaseserviceandroidapp.features.add_note_dialog.data.model.NoteItem
import com.firebaseserviceandroidapp.features.home.logic.HomeViewModel
import com.firebaseserviceandroidapp.features.home.ui.adapter.NoteAdapter
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper

@BindingAdapter("selectedDateAttrChanged")
fun setDateChangedListener(view: MaterialCalendarView, listener: InverseBindingListener?) {
    if (listener != null) {
        view.setOnDateChangedListener(OnDateSelectedListener { _, date, _ ->
            listener.onChange()
        })
    }
}

@InverseBindingAdapter(attribute = "selectedDate")
fun getSelectedDate(view: MaterialCalendarView): CalendarDay? {
    return view.selectedDate
}

@BindingAdapter("app:onDateSelected")
fun setOnDateSelectedListener(view: MaterialCalendarView, viewModel: HomeViewModel) {
    view.setOnDateChangedListener(OnDateSelectedListener { _, date, _ ->
        viewModel.onDateSelected(date)
    })
}

@BindingAdapter("app:notes")
fun setNotes(recyclerView: RecyclerView, notes: List<NoteItem>?) {
    val adapter = recyclerView.adapter as? NoteAdapter
    //adapter?.updateData(notes ?: emptyList())
}

@BindingAdapter("app:selectedDate")
fun setSelectedDate(view: MaterialCalendarView, date: CalendarDay?) {
    val today = CalendarDay.today()
    if (date == null || view.selectedDate != date) {
        view.selectedDate = today
    }
}

@BindingAdapter("app:overScrollOrientation")
fun setUpOverScroll(recyclerView: RecyclerView, orientation: Int) {
    OverScrollDecoratorHelper.setUpOverScroll(recyclerView, orientation)
}

@BindingAdapter("statusTextColor")
fun setStatusTextColor(textView: TextView, status: String?) {
    when (status) {
        "To Do" -> textView.setTextColor(textView.context.getColor(R.color.teal_700))
        "In Progress" -> textView.setTextColor(textView.context.getColor(R.color.lightItemColor))
        "Complete" -> textView.setTextColor(textView.context.getColor(R.color.red))
        "المهام" -> textView.setTextColor(textView.context.getColor(R.color.teal_700))
        "قيد التنفيذ" -> textView.setTextColor(textView.context.getColor(R.color.lightItemColor))
        "اكتملت" -> textView.setTextColor(textView.context.getColor(R.color.red))
    }
}

@BindingAdapter("selectedItemPosition", "onItemSelected", requireAll = false)
fun setSpinnerListeners(
    spinner: Spinner,
    selectedPosition: Int?,
    onItemSelected: AdapterView.OnItemSelectedListener?,
) {
    spinner.setSelection(selectedPosition ?: 0)
    spinner.onItemSelectedListener = onItemSelected
}

@BindingAdapter("selectedItemPosition")
fun Spinner.setSelectedItemPosition(position: Int) {
    if (this.selectedItemPosition != position) {
        this.setSelection(position, false)
    }
}


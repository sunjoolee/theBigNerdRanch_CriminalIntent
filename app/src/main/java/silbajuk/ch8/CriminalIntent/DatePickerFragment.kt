package silbajuk.ch8.CriminalIntent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.util.*

private const val ARG_DATE = "date"
private const val ARG_REQUEST_CODE = "requestCode"
private const val RESULT_DATE_KEY = "resultDate"

class DatePickerFragment : DialogFragment(){

    companion object{
        fun newInstance(date: Date, requestCode : String):DatePickerFragment{
            val args = Bundle().apply{
                putSerializable(ARG_DATE, date)
                putString(ARG_REQUEST_CODE, requestCode)
            }
            return DatePickerFragment().apply{
                arguments = args
            }
        }
        fun getSelectedDate(result: Bundle) = result.getSerializable(RESULT_DATE_KEY) as Date
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date

        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        val dateListener = DatePickerDialog.OnDateSetListener{
            _:DatePicker, year:Int, month:Int, day:Int ->

            val resultDate : Date = GregorianCalendar(year, month, day).time

            val result = Bundle().apply {
                putSerializable(RESULT_DATE_KEY, resultDate)
            }

            val resultRequestCode = requireArguments().getString(ARG_REQUEST_CODE, "")
            setFragmentResult(resultRequestCode, result)
        }

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }
}
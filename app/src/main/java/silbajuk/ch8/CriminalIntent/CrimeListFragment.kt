package silbajuk.ch8.CriminalIntent

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CrimeListFragment : Fragment() {

    //호스팅 액티비티에서 구현할 인터페이스
    interface Callbacks{
        fun onCrimeSelected(crimeId: UUID)
    }
    private var callbacks : Callbacks? = null

    private lateinit var crimeRecyclerView: RecyclerView

    private var adapter : CrimeAdapter? = CrimeAdapter(emptyList())

    private val crimeListViewModel : CrimeListViewModel by lazy{
        ViewModelProvider(this).get(CrimeListViewModel :: class.java)
    }

    companion object{
        fun newInstance(): CrimeListFragment{
            return CrimeListFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view)
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        crimeRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer{ crimes ->
                crimes?.let{
                    updateUI(crimes)
                }
            }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(crimes:List<Crime>){
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }

    private inner class CrimeHolder (view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener{
        private lateinit var crime : Crime

        val titleTextView : TextView = itemView.findViewById(R.id.crime_title)
        val dateTextView : TextView = itemView.findViewById(R.id.crime_date)
        val solvedImageView : ImageView = itemView.findViewById(R.id.crime_solved)

        init{
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime){
            this.crime = crime
            titleTextView.text = crime.title
            dateTextView.text = DateFormat.format("EEE, MMM dd, yyyy", crime.date)

            solvedImageView.visibility = when {
                crime.isSolved -> View.VISIBLE
                else -> View.INVISIBLE
            }
        }

        override fun onClick(p0: View?) {
            //Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_LONG).show()
            callbacks?.onCrimeSelected(crime.id)
        }
    }

    private inner class CrimeAdapter(var crimes:List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)

            return CrimeHolder(view)
        }

        override fun getItemCount(): Int {
            return crimes.size
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }
    }
}
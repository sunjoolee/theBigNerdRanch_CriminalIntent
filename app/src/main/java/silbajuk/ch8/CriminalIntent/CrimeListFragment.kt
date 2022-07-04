package silbajuk.ch8.CriminalIntent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView

    private var adapter : CrimeAdapter? = null

    private val crimeListViewModel : CrimeListViewModel by lazy{
        ViewModelProvider(this).get(CrimeListViewModel :: class.java)
    }

    companion object{
        fun newInstance(): CrimeListFragment{
            return CrimeListFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view)
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    private fun updateUI(){
        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }

    private inner class CrimeHolder (view: View):
        RecyclerView.ViewHolder(view), View.OnClickListener{
        private lateinit var crime : Crime

        val titleTextView : TextView = itemView.findViewById(R.id.crime_title)
        val dateTextView : TextView = itemView.findViewById(R.id.crime_date)

        init{
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime){
            this.crime = crime
            titleTextView.text = crime.title
            dateTextView.text = crime.date.toString()
        }

        override fun onClick(p0: View?) {
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_LONG).show()
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
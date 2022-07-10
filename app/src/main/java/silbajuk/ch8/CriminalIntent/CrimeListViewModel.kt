package silbajuk.ch8.CriminalIntent

import androidx.lifecycle.ViewModel

class CrimeListViewModel : ViewModel(){
    private val crimeRepository = CrimeRepository.get()
    val crimes = crimeRepository.getCrimes()
}
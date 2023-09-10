import com.minutesock.core.data.DailyWordSessionDao
import com.minutesock.core.domain.DailyWordGameState
import com.minutesock.core.domain.GuessWordState
import com.minutesock.profile.domain.GuessDistribution
import com.minutesock.profile.domain.GuessDistributionState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flow

class ProfileRepository(
    private val dailyWordSessionDao: DailyWordSessionDao
) {

    fun getGuessDistribution() = flow {
        var guessDistributionState = GuessDistributionState(
            loading = true
        )
        emit(guessDistributionState)
        val guessDistributions =
            dailyWordSessionDao.getAllSessions().map { dailyWordSessionEntity ->
                val correctAttemptIndex =
                    dailyWordSessionEntity.guesses.indexOfFirst { it.state == GuessWordState.Correct }
                GuessDistribution(
                    correctAttempt = if (correctAttemptIndex == -1) null else correctAttemptIndex,
                    gameState = DailyWordGameState.fromInt(dailyWordSessionEntity.gameState),
                    maxGuessAttempts = dailyWordSessionEntity.maxAttempts
                )
            }

        val maxGuessAttempts = guessDistributions.maxBy { it.maxGuessAttempts }.maxGuessAttempts

        val filteredGuessDistributions = mutableListOf<ImmutableList<GuessDistribution>>()

        for (i in 0..maxGuessAttempts) {
            filteredGuessDistributions.add(
                guessDistributions.filter { it.correctAttempt?.equals(i) == true }.toImmutableList()
            )
        }

        guessDistributionState = guessDistributionState.copy(
            loading = false,
            guessDistributions = filteredGuessDistributions.toImmutableList()
        )
        emit(guessDistributionState)
    }
}
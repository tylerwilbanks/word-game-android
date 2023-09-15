import com.minutesock.core.data.DailyWordSessionDao
import com.minutesock.core.domain.DailyWordGameState
import com.minutesock.core.domain.GuessWordState
import com.minutesock.core.mappers.DATE_FORMAT_PATTERN
import com.minutesock.core.utils.toDate
import com.minutesock.core.utils.toString
import com.minutesock.profile.domain.GuessDistribution
import com.minutesock.profile.domain.GuessDistributionState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.flow
import java.util.Date

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
                    correctAttempt = if (correctAttemptIndex == -1) 0 else correctAttemptIndex + 1,
                    gameState = DailyWordGameState.fromInt(dailyWordSessionEntity.gameState),
                    maxGuessAttempts = dailyWordSessionEntity.maxAttempts,
                    isMostRecentGame = dailyWordSessionEntity.isDaily && dailyWordSessionEntity.date == Date().toString(DATE_FORMAT_PATTERN)
                )
            }

        val maxGuessAttempts = guessDistributions.maxBy { it.maxGuessAttempts }.maxGuessAttempts

        val filteredGuessDistributions = mutableListOf<GuessDistribution>()

        val mostRecentGame = guessDistributions.find { it.isMostRecentGame }

        for (i in 1..maxGuessAttempts) {
            filteredGuessDistributions.add(
                GuessDistribution(
                    correctAttempt = i,
                    correctAttemptCount = guessDistributions.count { it.correctAttempt == i },
                    maxGuessAttempts = maxGuessAttempts,
                    gameState = DailyWordGameState.Success,
                    isMostRecentGame = mostRecentGame?.correctAttempt == i
                )
            )
        }

        val failedGameSessionsCount =
            guessDistributions.count { it.gameState == DailyWordGameState.Failure }
        val maxCorrectAttemptCount = maxOf(
            filteredGuessDistributions.maxOf { it.correctAttemptCount },
            failedGameSessionsCount
        )

        guessDistributionState = guessDistributionState.copy(
            loading = false,
            guessDistributions = filteredGuessDistributions.toImmutableList(),
            failedGameSessionsCount = failedGameSessionsCount,
            maxCorrectAttemptCount = maxCorrectAttemptCount
        )
        emit(guessDistributionState)
    }
}
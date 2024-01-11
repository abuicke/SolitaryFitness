package com.gravitycode.solitaryfitness.tests.ui

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.filters.FlakyTest
import androidx.test.filters.LargeTest
import com.gravitycode.solitaryfitness.app.MainActivity
import com.gravitycode.solitaryfitness.log_workout.util.Workout
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

/**
 * Test sometimes fails with a [NullPointerException]. I have no idea why.
 * */
@LargeTest
@FlakyTest
class RecordWorkoutOfflineUiTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private lateinit var overflow: SemanticsNodeInteraction
    private lateinit var signIn: SemanticsNodeInteraction
    private lateinit var signOut: SemanticsNodeInteraction

    private lateinit var handstandPressUp: SemanticsNodeInteraction
    private lateinit var pressUp: SemanticsNodeInteraction
    private lateinit var sitUp: SemanticsNodeInteraction
    private lateinit var squat: SemanticsNodeInteraction
    private lateinit var squatThrust: SemanticsNodeInteraction
    private lateinit var burpee: SemanticsNodeInteraction
    private lateinit var starJump: SemanticsNodeInteraction
    private lateinit var stepUp: SemanticsNodeInteraction

    @Before
    fun setUpNodes() {
        overflow = rule.onNodeWithTag("overflow")
        signIn = rule.onNodeWithTag("sign in with google")
        signOut = rule.onNodeWithTag("sign out")

        handstandPressUp = rule.onNodeWithTag(Workout.HANDSTAND_PRESS_UP.string)
        pressUp = rule.onNodeWithTag(Workout.PRESS_UP.string)
        sitUp = rule.onNodeWithTag(Workout.SIT_UP.string)
        squat = rule.onNodeWithTag(Workout.SQUAT.string)
        squatThrust = rule.onNodeWithTag(Workout.SQUAT_THRUST.string)
        burpee = rule.onNodeWithTag(Workout.BURPEE.string)
        starJump = rule.onNodeWithTag(Workout.STAR_JUMP.string)
        stepUp = rule.onNodeWithTag(Workout.STEP_UP.string)
    }

    @Test
    fun addReps() {
        val handstandPressUps = handstandPressUp.addRepsRandomly()
        val pressUps = pressUp.addRepsRandomly()
        val sitUps = sitUp.addRepsRandomly()
        val squats = squat.addRepsRandomly()
        val squatThrusts = squatThrust.addRepsRandomly()
        val burpees = burpee.addRepsRandomly()
        val starJumps = starJump.addRepsRandomly()
        val stepUps = stepUp.addRepsRandomly()

        handstandPressUp.assertHasRepCount(handstandPressUps)
        pressUp.assertHasRepCount(pressUps)
        sitUp.assertHasRepCount(sitUps)
        squat.assertHasRepCount(squats)
        squatThrust.assertHasRepCount(squatThrusts)
        burpee.assertHasRepCount(burpees)
        starJump.assertHasRepCount(starJumps)
        stepUp.assertHasRepCount(stepUps)
    }
}

fun SemanticsNodeInteraction.click1Reps() {
    onChildren().filter(hasText("1")).assertCountEquals(1).onFirst().performClick()
}

fun SemanticsNodeInteraction.click5Reps() {
    onChildren().filter(hasText("5")).assertCountEquals(1).onFirst().performClick()
}

fun SemanticsNodeInteraction.click10Reps() {
    onChildren().filter(hasText("10")).assertCountEquals(1).onFirst().performClick()
}

fun SemanticsNodeInteraction.clickXReps() {
    onChildren().filter(hasText("X")).assertCountEquals(1).onFirst().performClick()
}

fun SemanticsNodeInteraction.assertHasRepCount(count: Int) {
    onChildren().filter(hasText(count.toString())).assertCountEquals(1)
}

fun SemanticsNodeInteraction.addRepsRandomly(maxInteractions: Int = 25): Int {
    var totalReps = 0
    for (i in 0 until Random.nextInt(1, maxInteractions + 1)) {
        performClick()
        val rand = Random.nextFloat()
        if (rand < 0.3) {
            click1Reps()
            totalReps += 1
        } else if (rand < 0.6) {
            click5Reps()
            totalReps += 5
        } else if (rand < 0.9) {
            click10Reps()
            totalReps += 10
        } else {
            clickXReps()
        }
    }

    return totalReps
}
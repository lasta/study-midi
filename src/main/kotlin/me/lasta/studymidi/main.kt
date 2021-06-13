package me.lasta.studymidi

import java.io.File
import javax.sound.midi.MidiSystem
import javax.sound.midi.Sequencer

typealias MidiSequence = javax.sound.midi.Sequence

const val MIDI_FILE_NAME =
    "sugar_song_and_bitter_step.midi"

fun main() {
    val sequencer: Sequencer = MidiSystem.getSequencer()
    val midiFile = File(MIDI_FILE_NAME)
    val sequence: MidiSequence = MidiSystem.getSequence(midiFile)
    sequencer.open()

    sequencer.sequence = sequence

    sequencer.start()

    Thread.sleep(20_000)

    sequencer.stop()
    sequencer.close()
}

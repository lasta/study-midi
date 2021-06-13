package me.lasta.studymidi

import me.lasta.studymidi.TemporalyMidiEvent.Companion.toMidiEvent
import java.io.File
import javax.sound.midi.MidiEvent
import javax.sound.midi.MidiMessage
import javax.sound.midi.MidiSystem
import javax.sound.midi.Sequencer
import javax.sound.midi.ShortMessage
import javax.sound.midi.Track

typealias MidiSequence = javax.sound.midi.Sequence

const val MIDI_FILE_NAME = "examples_from_scratch.mid"

data class TemporalyMidiEvent(
    val message: MidiMessage,
    val tick: Long
) {
    companion object {
        fun TemporalyMidiEvent.toMidiEvent(): MidiEvent = MidiEvent(message, tick)
    }
}

fun main() {
    val sequencer: Sequencer = MidiSystem.getSequencer()
    val midiFile = File(MIDI_FILE_NAME)
    val sequence: MidiSequence = MidiSystem.getSequence(midiFile)

    val tracks: Array<Track> = sequence.tracks
    val track1: Track = tracks[1]

    val messages: MutableList<TemporalyMidiEvent> = (0 until track1.size()).map { index ->
        TemporalyMidiEvent(
            message = track1[index].message,
            tick = track1[index].tick
        )
    }.toMutableList()

    val programChangeToRockOrgan: MidiMessage = ShortMessage(ShortMessage.PROGRAM_CHANGE, 0, 19, 0)
    messages[3] = messages[3].copy(message = programChangeToRockOrgan)

    sequence.deleteTrack(track1)
    val organTrack = sequence.createTrack()
    messages.forEach { message -> organTrack.add(message.toMidiEvent()) }

    (0 until organTrack.size()).forEach { index ->
        println("$index: ${organTrack[index].message.message.map { "%02x".format(it) }}")
    }

    sequencer.open()

    sequencer.sequence = sequence

    sequencer.start()
    sequencer.tickLength

//    Thread.sleep(20_000)

    sequencer.stop()
    sequencer.close()

    val outputFile = File("output.midi")
    MidiSystem.write(sequence, 1, outputFile)
}

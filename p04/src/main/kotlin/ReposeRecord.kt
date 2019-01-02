package p04

import common.file.readLines
import java.text.SimpleDateFormat
import java.util.*

enum class GuardEventType(val value: String) {
  BEGIN_SHIFT("begins shift"),
  FALL_ASLEEP("falls asleep"),
  WAKE_UP("wakes up");

  companion object {
    fun from(search: String): GuardEventType = requireNotNull(values().find { it.value == search }) { "No TaskAction with value $search" }
  }
}

class LogMessage {
  val guardId: Int
  val dateStr: String
  val minute: Int
  val type: GuardEventType

  constructor(msg: String, defaultGuardId: Int) {
    val matches = Regex("""\[([\d-]+)\s\d+:(\d+)\]\s(?:Guard #)?(\d+)?\s?(.+)""").matchEntire(msg)

    this.dateStr = matches?.groups?.get(1)?.value!!
    this.minute = matches.groups.get(2)?.value!!.toInt()
    this.guardId = matches.groups.get(3)?.value?.toInt() ?: defaultGuardId
    this.type = GuardEventType.from(matches.groups.get(4)?.value!!)
  }

  override fun toString(): String {
    return "$dateStr:$minute - $guardId - $type"
  }
}

fun rangesAsleepByGuardId(logs: List<LogMessage>): Map<Int, List<Pair<Int, Int>>> {
  var lastSleepMinute = 0
  val map = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()
  //There's gotta be a cleaner way to deal with a nullable accumulator...
  logs.groupingBy { it.guardId }.aggregateTo(map) { _, total, msg, first ->
    if (first) {
      mutableListOf()
    } else {
      when (msg.type) {
        GuardEventType.FALL_ASLEEP ->
          lastSleepMinute = msg.minute
        GuardEventType.WAKE_UP ->
          total!!.add(Pair(lastSleepMinute, msg.minute - 1))
        GuardEventType.BEGIN_SHIFT -> total!!
      }

      total!!
    }
  }

  return map
}

fun mostMinutesAsleep(logs: List<LogMessage>): Pair<Int, Int> {
  val ranges = rangesAsleepByGuardId(logs)
  val asTotal = ranges.mapValues {
    it.value.fold(Pair(it.key, 0)) { acc, pair ->
      val total = acc.second + pair.second - pair.first
      Pair(it.key, total)
    }
  }

  return asTotal.maxBy { foo -> foo.value.second }?.value!!
}

fun mostSleepyMinuteByGuardId(logs: List<LogMessage>): Map<Int, Pair<Int, Int>> {
  val ranges = rangesAsleepByGuardId(logs)

  return ranges.mapValues {range ->
    val minuteCounts = mostSleepyMinuteForRanges(range.value)
    minuteCounts.maxBy { it.value }?.toPair()!!
  }
}

fun mostSleepyMinuteForRanges(sleepiestRanges: List<Pair<Int, Int>>): Map<Int, Int> {
  if(sleepiestRanges.isNotEmpty()) {
    return sleepiestRanges.flatMap {
      it.first.rangeTo(it.second)
    }.groupingBy { it }.eachCount()
  }

  return mutableMapOf(Pair(-1, -1))
}

fun parseLogDate(dateStr: String): Date {
  return SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr)
}

fun parseLogs(rawLogs: List<String>): List<LogMessage> {
  var lastGuardId = -1
  return rawLogs.sortedBy { parseLogDate(it.substringBeforeLast("]").substring(1)) }.map {
    val msg = LogMessage(it, lastGuardId)
    if (msg.guardId != -1) lastGuardId = msg.guardId
    msg
  }
}

fun main() {
  val logs = parseLogs(readLines("input.txt"))
  val sleepyGuard = mostMinutesAsleep(logs)
  val sleepyMinutesByGuard = mostSleepyMinuteByGuardId(logs)
  val sleepyGuardMinute = sleepyMinutesByGuard[sleepyGuard.first]!!
  val sleepiestGuardMinute = sleepyMinutesByGuard.maxBy { it.value.second }

  println("Sleepiest guard: $sleepyGuard")
  println("Sleepiest minute for that guard: $sleepyGuardMinute")
  println("Sleepy guard value: ${sleepyGuard.first * sleepyGuardMinute.first}")
  println("Sleepiest overall minute: $sleepiestGuardMinute")
  println("Sleepiest overall guard value: ${sleepiestGuardMinute?.key!! * sleepiestGuardMinute.value.first}")
}
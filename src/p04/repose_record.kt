package p04

import common.file.readLines
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
}

fun mostMinutesAsleep(logs: List<LogMessage>): Pair<Int, Int> {
  var lastSleepMinute = 0

  val map = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()
  //There's gotta be a cleaner way to deal with a nullable accumulator...
  logs.groupingBy { it.guardId }.aggregateTo(map) { key, total, msg, first ->
    if(first) {
      mutableListOf()
    } else {
      when (msg.type) {
        GuardEventType.FALL_ASLEEP ->
          lastSleepMinute = msg.minute
        GuardEventType.WAKE_UP ->
          total!!.add(Pair(lastSleepMinute, msg.minute))
        GuardEventType.BEGIN_SHIFT -> total!!
      }

      total!!
    }
  }

  map.mapValues {
    it.value.reduce<Int, Pair<Int, Int>> { acc, pair ->
      acc + pair.first - pair.second
  }
}

fun mostSleepyMinute(guardId: Int, logs: List<LogMessage>): Int {
  return 0
}

fun guardActivity(logs: List<LogMessage>): Int {
  val sleepyGuard = mostMinutesAsleep(logs)
  println(sleepyGuard)
  val sleepyMinute = mostSleepyMinute(sleepyGuard.first, logs)

  return sleepyGuard.second * sleepyMinute
}

fun parseLogDate(dateStr: String): Date {
  return SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr)
}

fun parseLogs(rawLogs: List<String>): Int {
  var lastGuardId = -1
  val logs = rawLogs.sortedBy { parseLogDate(it.substringBeforeLast("]").substring(1)) }.map {
    val msg = LogMessage(it, lastGuardId)
    if(msg.guardId != -1) lastGuardId = msg.guardId
    msg
  }
  return guardActivity(logs)
}

fun main() {
  val guardValue = parseLogs(readLines("src/p04/input.txt"))
  println("Sleepy guard value: $guardValue")
}
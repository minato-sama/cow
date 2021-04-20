import java.io.File
import java.io.FileNotFoundException
import java.util.*
import kotlin.collections.HashMap

fun parseCycles(instructions: List<String>): HashMap<Int, Int> {
    val brackets = HashMap<Int, Int>()
    val stack = Stack<Int>()
    for ((i, instruction) in instructions.withIndex()) {
        if (instruction == "MOO") {
            stack.push(i)
        } else if (instruction == "moo") {
            if (stack.empty()) {
                throw Exception("Wrong expression")
            } else {
                val start = stack.pop()
                brackets[i] = start
                brackets[start] = i
            }
        }
    }
    return brackets
}

fun run(source: String) {
    val instructions = source.split("\\s+".toRegex())
    val memory = Array(20000){0}
    var ptr = 0;
    var i = 0;
    var register = 0;
    var save = false;

    val brackets = parseCycles(instructions)
    while (i != instructions.size) {
        when (instructions[i]) {
            "MoO" -> memory[ptr] = memory[ptr] + 1
            "MOo" -> memory[ptr] = memory[ptr] - 1
            "moO" -> ptr++
            "mOo" -> ptr--
            "OOM" -> print(memory[ptr])
            "oom" -> memory[ptr] = readLine()?.toInt()!!
            "OOO" -> memory[ptr] = 0
            "Moo" -> if (memory[ptr] == 0) memory[ptr]=readLine()?.toInt()!! else print(memory[ptr].toChar())
            "MMM" -> {
                if (save) memory[ptr] = register else register = memory[ptr]
                save = !save
            }
            "MOO" -> {
                if (memory[ptr] == 0) {
                    i = brackets[i]!!
                }
            }
            "moo" -> {
                if (memory[ptr] != 0) {
                    i = brackets[i]!!
                }
            }
        }
        i++;
    }
}

fun main() {
    val filename = readLine()
    try {
        val source = File(filename).readText().trim()
        run(source)
    } catch (e: FileNotFoundException) {
        print("File not found")
    }
}
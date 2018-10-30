package commands

import commands.create.{MakeDirectory, MakeFile}
import commands.display.{Concatenate, Echo, ListSegments, PrintWorkingDirectory}
import commands.traverse.ChangeDirectory
import file_system.State
import files.Directory

trait Command extends (State => State)

object Command {
  def emptyCmd: Command = (currentState: State) => currentState
  def incompleteCmd(args: String): Command = (currentState: State) => currentState.setMessage(s"$args: incomplete command!")
  def from(input: String): Command = {
    val tokens: List[String] = input.split(" ").toList
    tokens match {
      case List() => emptyCmd
      case List(_) => tokens.head match {
        case ListSegments.CMD => new ListSegments
        case PrintWorkingDirectory.CMD => new PrintWorkingDirectory
        case _: String => incompleteCmd(tokens.head)
      }
      case List(_, _) => tokens.head match {
        case MakeDirectory.CMD => new MakeDirectory(tokens.tail.head)
        case MakeFile.CMD => new MakeFile(tokens.tail.head)
        case ChangeDirectory.CMD => new ChangeDirectory(tokens.tail.head)
        case RemoveEntry.CMD => new RemoveEntry(tokens.tail.head)
        case Concatenate.CMD => new Concatenate(tokens.tail.head)
        case _: String => incompleteCmd(tokens.head)
      }
      case List(_, _, _*) => new Echo(tokens.tail)
      case _ => new UnknownCommand
    }
  }

  def tokenize(path: String): List[String] =
    path.substring(1).split(Directory.SEPARATOR).toList
}
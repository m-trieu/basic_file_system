package commands.display

import commands.Command
import commands.display.Echo.Arguments
import file_system.State
import files.{Directory, File}

import scala.annotation.tailrec

class Echo(args: Arguments) extends Command {
  override def apply(currentState: State): State =
    args match {
      case List() => currentState
      case List(_) => currentState.setMessage(args.head)
      case List(_, _, _*) => args(args.length - 2) match {
        case ">>" => execute(currentState, createContents(args, args.length - 2), args.last, append = true)
        case ">" => execute(currentState, createContents(args, args.length - 2), args.last)
        case _:String => currentState.setMessage(createContents(args, args.length))
      }
    }

  def createContents(args: Arguments, last: Int): String = {
    @tailrec
    def create(iterator: Int, accumulator: String): String =
      iterator match {
        case `last` => accumulator
        case _: Int => create(iterator + 1, s"$accumulator ${args(iterator)}")
      }
    create(0, "")
  }

  def getNewRootAfterEcho(currentDirectory: Directory,
                          path: List[String],
                          contents: String,
                          append: Boolean): Directory =
    path match {
      case List() => currentDirectory
      case List(_, _*) => path.tail match {
        case List() =>
          val entry = currentDirectory.find(path.head)
          entry match {
            case null => currentDirectory.add(new File(currentDirectory.path, path.head, contents))
            case _:Directory => currentDirectory
            case _ => currentDirectory.swap(path.head, entry.asFile.setContents(contents, append))
          }
        case _ =>
          val next = currentDirectory.find(path.head).asDirectory
          val nextNext = getNewRootAfterEcho(next, path.tail, contents, append)
          if(next == nextNext) currentDirectory else currentDirectory.swap(path.head, nextNext)
      }
    }

  def execute(currentState: State, contents: String, fileName: String, append: Boolean = false): State = {
    if(fileName.contains(Directory.SEPARATOR)) currentState.setMessage("Echo: filename must not contain separators")
    val newRoot: Directory = getNewRootAfterEcho(currentState.root, currentState.wd.foldersInPath :+ fileName, contents, append)
    newRoot match {
      case currentState.root => currentState.setMessage(s"$fileName does not exist")
      case _ => State(newRoot, newRoot.findChild(currentState.wd.foldersInPath))
    }
  }
}

object Echo {
  type Arguments = List[String]
  val CMD = "echo"
}

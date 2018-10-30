package commands.traverse

import commands.Command
import file_system.State
import files.{Directory, File, FileSystemEntry}

import scala.annotation.tailrec

class ChangeDirectory(destination: String) extends Command {
  override def apply(currentState: State): State = {
    val root = currentState.root
    val wd = currentState.wd
    val absolutePath =
      if(destination.startsWith(Directory.SEPARATOR)) destination
      else if(wd.isRoot) wd.path + destination
      else wd.path + Directory.SEPARATOR + destination
    val destinationDir = findEntry(root, absolutePath)
    if(destinationDir == null || !destinationDir.is[Directory])
      currentState.setMessage(s"$destination : no such directory")
    else State(root, destinationDir.asDirectory)
  }

  def findEntry(root: Directory, path: String): FileSystemEntry = {
    @tailrec
    def find(currentDir: Directory, path: List[String]): FileSystemEntry =
      if(path.isEmpty || path.head.isEmpty) currentDir
      else if(path.tail.isEmpty) currentDir.find(path.head)
      else {
        val next = currentDir.find(path.head)
        if(next == null || !next.is[Directory]) null
        else find(next.asDirectory, path.tail)
      }

    @tailrec
    def collapse(path: List[String], newPath: List[String]): List[String] =
      if(path.isEmpty) newPath
      else if("." equals path.head) collapse(path.tail, newPath)
      else if(".." equals path.head)
        if(newPath.isEmpty) null else collapse(path.tail, newPath.init)
      else collapse(path.tail, newPath :+ path.head)

    val tokens = collapse(Command.tokenize(path), List())

    if(tokens == null) null else find(root, tokens)
  }
}

object ChangeDirectory {
  val CMD = "cd"
}

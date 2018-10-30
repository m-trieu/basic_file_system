package files

import commands.Command
import file_system.FileSystemException

import scala.annotation.tailrec

class Directory(override val parentPath: String,
                override val name: String,
                val contents: List[FileSystemEntry])
  extends FileSystemEntry(parentPath, name) {

  def hasEntry(name: String): Boolean = find(name) != null

  def foldersInPath: List[String] = Command.tokenize(path).filter(e => !e.isEmpty)

  def findChild(path: List[String]): Directory =
    if(path.isEmpty) this else find(path.head).asDirectory.findChild(path.tail)


  def findChild(relativePath: String) : Directory =
    if(relativePath.isEmpty) this else findChild(Command.tokenize(relativePath))

  def removeEntry(name: String): Directory =
    if(!hasEntry(name)) this
    else new Directory(parentPath, name, contents.filter(e => e.name != name))

  def add(newEntry: FileSystemEntry): Directory =
    new Directory(parentPath, name, contents :+ newEntry)

  def find(entryName: String): FileSystemEntry = {
    @tailrec
    def findEntry(name: String, entries: List[FileSystemEntry]): FileSystemEntry =
      if(entries.isEmpty) null
      else if(entries.head.name.equals(name)) entries.head
      else findEntry(name, entries.tail)

    findEntry(entryName, contents)
  }

  def swap(entryName: String, newEntry: FileSystemEntry): Directory =
    new Directory(
      parentPath,
      name,
      contents.filter(entry => !entry.name.equals(entryName)) :+ newEntry)

  def isRoot: Boolean = parentPath.isEmpty
  override def asDirectory: Directory = this
  override def asFile: File = throw new FileSystemException("not a file")
  override def entryType: EntryType.Value = EntryType.DIRECTORY
}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"
  def ROOT: Directory = Directory.empty("", "")
  def empty(parentPath: String, name: String): Directory =
    new Directory(parentPath, name, List())
}

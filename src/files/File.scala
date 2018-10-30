package files

import file_system.FileSystemException

class File(override val parentPath: String, override val name: String, val contents: String)
  extends FileSystemEntry(parentPath, name) {

  override def asDirectory: Directory =
    throw new FileSystemException("File cannot be converted to a directory")

  override def asFile: File = this

  override def entryType: EntryType.Value = EntryType.FILE

  def setContents(newContents: String, append: Boolean = false): File =
    new File(parentPath, name, if(append) s"$contents\n$newContents" else newContents)
}

object File {
  def empty(parentPath: String, name: String): File =
    new File(parentPath, name, "")
}

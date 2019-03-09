
SLFilesDir = "D:/Torrents/icons/summer-love-cicadas-icons-by-raindropmemory/png/"
DestFileDir = "../app/src/main/res/"


if WScript.Arguments.Count<1 then
  WScript.echo "No valid arguments"
  WScript.Quit(1)
end if

FileName = WScript.Arguments.Item(0)
WScript.Echo "File name to process:" & FileName

CopyFile "64x64", "drawable-hdpi"
CopyFile "32x32", "drawable-ldpi"
CopyFile "48x48", "drawable-mdpi"
CopyFile "128x128", "drawable-xhdpi"
CopyFile "256x256", "drawable-xxhdpi"
CopyFile "256x256", "drawable-xxxhdpi"

Sub CopyFile(SDirName, DDirName)

SFileName = SLFilesDir & SDirName & "/" & FileName
DFileName = DestFileDir & DDirName & "/" & "img_" & LCase(Replace(FileName, " ", "_"))

WScript.Echo "Copy from"
WScript.Echo "Source file name:" & SFileName
WScript.Echo "to"
WScript.Echo "Dest file name:" & DFileName

Set fso = CreateObject("Scripting.FileSystemObject")

WScript.Echo "Copy successful"
WScript.Echo ""

fso.CopyFile SFileName, DFileName, True

End Sub
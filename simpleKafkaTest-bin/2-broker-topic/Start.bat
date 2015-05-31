unzip openjdk-1.7.0-u80-unofficial-windows-i586-image.zip
set path=%path%;%~dp0openjdk-1.7.0-u80-unofficial-windows-i586-image\bin
start StartBroker.bat
ping -n 10 127.1>nul
start CreateTopic.bat
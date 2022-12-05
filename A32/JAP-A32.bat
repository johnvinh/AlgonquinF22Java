:: ---------------------------------------------------------------------
:: JAP COURSE - SCRIPT
:: ASSIGNMENTS - CST8221 - Fall 2022
:: ---------------------------------------------------------------------
:: Begin of Script (Assignments - F22)
:: ---------------------------------------------------------------------


CLS

:: LOCAL VARIABLES ....................................................
SET SRCDIR=src
SET BINDIR=bin
SET BINOUT=game-javac.out
SET BINERR=game-javac.err
SET JARNAME=Game.jar
SET JAROUT=game-jar.out
SET JARERR=game-jar.err
SET DOCDIR=doc
SET DOCPACK=game
SET DOCOUT=game-javadoc.out
SET DOCERR=game-javadoc.err
SET MAINCLASSSRC=src/Game.java
SET MAINCLASSBIN=Game

@echo off

ECHO " _________________________________ "
ECHO "|     __    _  ___    ___  _      |"
ECHO "|    |  |  / \ \  \  /  / / \     |"
ECHO "|    |  | /   \ \  \/  / /   \    |"
ECHO "|    |  |/  _  \ \    / /  _  \   |"
ECHO "|  __|  |  / \  \ \  / /  / \  \  |"
ECHO "|  \____/_/   \__\ \/ /__/   \__\ |"
ECHO "|                                 |"
ECHO "| .. ALGONQUIN COLLEGE - 2022F .. |"
ECHO "|_________________________________|"
ECHO "                                   "
ECHO "[ASSIGNMENT SCRIPT ---------------]"

xcopy %SRCDIR%\*.png %BINDIR%\

ECHO "1. Compiling ......................"
javac -Xlint %MAINCLASSSRC% src/GameController.java src/GameModel.java src/GameView.java^
 src/NumPuzClientGui.java src/NumPuzServer.java src/NumPuzServerGui.java -d %BINDIR% > %BINOUT% 2> %BINERR%

:: ECHO "Running  ........................."
:: start java -cp ".;%BINDIR%;%JAVAFXDIR%/*" %MAINCLASSBIN%

ECHO "2. Creating Jar ..................."
cd bin
jar cvfe %JARNAME% %MAINCLASSBIN% . > %JAROUT% 2> %JARERR%

ECHO "3. Creating Javadoc ..............."
cd ..
javadoc -d %DOCDIR% -sourcepath %SRCDIR% -subpackages %DOCPACK% > %DOCOUT% 2> %DOCERR%

cd bin
ECHO "4. Running Jar ...................."
start java -jar %JARNAME%
cd ..

ECHO "[END OF SCRIPT -------------------]"
ECHO "                                   "
@echo on

:: ---------------------------------------------------------------------
:: End of Script (Assignments - F22)
:: ---------------------------------------------------------------------

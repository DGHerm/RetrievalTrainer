# RetrievalTrainer
This piece of software is for educational purposes. It may be useful for information retrieval learning and in preparing for assessment tests.

## Usage

First of all, I must confess, the application _RetrievalTrainer_ is still not perfect, I am sorry about that.

To warm up with _RetrievalTrainer_ download the file `history.xml` located in project directory `example`. Then open this file with the menu command `Read Exercise Item List ...`. The dialog `Choose Resource File To Open` shows only file names with the extension `*.xml`.  

Any file with the extension `*.xml` that does not conform to the xml schema definition of _RetrievalTrainer_ will provoke an error message.

If you have chosen the file `history.xml` you will be presented with a small multiple choice test.

Each query item consists of the question on top, followed by several check (square) or option (circle) boxes.  

Check boxes (square) allows for more then one answer to tick (multiple choice), the option boxes (round) are needed for single choice answers.

The navigation bar on the bottom of the dialog window allows you to jump to all other exercise items. The number to the right of the navigation buttons denotes the index number of an exercise item.

You can edit this exercise item, and can even create more exercise items. Before that, save the exercise item list to a new file by selecting the command `Save As...` in the file menu.

Now select in the list menu the command `Edit Exercise Item List`. A new menu, named `Edit` will then be shown up in the menu bar. Open it and select the last menu entry `Show Editor Panel`.

The editor panel is partitioned in five groups, `Exercise Item`, `Question Part`, `Answer Part`, `Solution Part`, and at last a button `Delete Item Part`.

Apart the buttons from the first group, all buttons are disabled.

In the first group, `Exercise Item`, the functions of the three buttons concerns the exercise item as a whole. 

If you click on `Append New`, a new empty default exercise item will be created and appended to the already existing ones.

The default exercise item is a single choice item, but you can convert it into a multiple choice item by selecting `Multiple Choice`in the panel group `Answer Part``

The empty text fields show up the actions you have to do.

If you type an answer text in, your intention of this text may be to distract the testee with a fake answer. Otherwise, in case you typed in a correct answer to be given, do not forget to tick the option button to the left of the text field to mark it as correct.

A question can have more than two answers, click on `Append New`.

To delete a part of the current exercise item, select the candidates by ticking the check boxes at the bottom right corners of the text fields.

If you save your exercise item list, your new entries will be written in a xml-file. 

--------

## Release Notes to v1.0.4

Oracle's distribution of JDK 8 contained JavaFX. But as of JDK 11, JavaFX is no longer included in the JDK.

The releases of _RetrievalTrainer_ up to v1.0.3 needed a JRE 8 to run. Now with the new release v1.0.4 you must have a JRE 11 at least.

Included in this release are installer packages for macOS and Windows, tested on macOS 10.13.6 and Windows 7 (see below, "Installer Packages"). This way you do not need any Java runtime environment. All necessary runtime resources are included in the application bundles.

Also included is the jar file of RetrievalTrainer, digitally signed by me with the command line tool `jarsigner`.

If you prefer the command line, you can use the `java` command to launch the _RetrievalTrainer_ application ([Tools Reference - Oracle Docs](https://docs.oracle.com/en/java/javase/11/tools/java.html)):

#### macOS & Linux:

`export PATH_TO_JDOM=${HOME}/jdom-2.0.6.jar`

`export PATH_TO_FX=${HOME}/javafx-sdk-14.0.1/lib`

`export REQUIRES=javafx.controls,javafx.fxml,jdom`

`export EXPORTS=javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED`

`export OPENS=javafx.graphics/com.sun.javafx.application=ALL-UNNAMED`

`java --module-path ${PATH_TO_FX}:${PATH_TO_JDOM} --add-modules ${REQUIRES} --add-exports ${EXPORTS} --add-opens ${OPENS} -Dfile.encoding=UTF-8 -jar ${HOME}/RetrievalTrainer-1.0.4.jar`


#### Windows:

`set PATH_TO_JDOM=.\jdom-2.0.6.jar`

`set PATH_TO_FX=.\javafx-sdk-14.0.1\lib`

`set REQUIRES=javafx.controls,javafx.fxml,jdom`

`set EXPORTS=javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED`

`set OPENS=javafx.graphics/com.sun.javafx.application=ALL-UNNAMED`

`java --module-path %PATH_TO_FX%;%PATH_TO_JDOM% --add-modules %REQUIRES% --add-exports %EXPORTS% --add-opens %OPENS% -Dfile.encoding=UTF-8 -jar .\RetrievalTrainer-1.0.4.jar`



### Installer Packages (Windows, macOS, and Linux)

I got very helpful information on the packaging process from following sources:

(1) youtube.com, 2019-11-05, 31 min:

[Java Packaging Tool: Create Native Packages to Deploy Java Applications by Kevin Rushforth](https://www.youtube.com/watch?v=ZGW9AalZLN4)

(2) youtube.com, 2019-11-07, 50 min:

[Java Packaging Tool: Create Native Packages to Deploy Java Applications by Kevin Rushforth](https://www.youtube.com/watch?v=JWwlGWlgxe0)

(3) medium.com, 2018-10-10:

[Using jlink to Build Java Runtimes for non-Modular Applications](https://medium.com/azulsystems/using-jlink-to-build-java-runtimes-for-non-modular-applications-9568c5e70ef4)

The packaging process is dependent on the operating system. On windows you need to install the WIX toolset ([The WiX Toolset](https://wixtoolset.org/)). It requires the .NET Framework 3.5.1 Windows feature to be enabled ([Introduction to Windows Installer XML (WiX) toolset v3.x](https://wixtoolset.org/documentation/manual/v3/main/)).

#### For macOS the procedure as follows:

(1) Look up the needed modules by the jar file:

`jdeps --module-path ${PATH_TO_FX}:${PATH_TO_JDOM} --add-modules ${REQUIRES} --list-deps ${HOME}/RetrievalTrainer-1.0.4.jar`

You will get a list of modules needed by the jar file.

(2) Assemble a slim runtime image (Java SDK) containing all needed modules:

`./jlink --no-header-files --no-man-pages --compress=2 --strip-debug --output ${HOME}/jdk11+fx14_slim --module-path ${HOME}/javafx-jmods-14.0.1 --add-modules javafx.controls,javafx.fxml,java.logging,java.base,java.datatransfer,java.desktop,java.prefs,java.scripting,java.xml,jdk.jsobject,jdk.unsupported,jdk.unsupported.desktop,jdk.xml.dom`

On macOS the "jlink" tool is not on the binary path, i.e. you have to find the "bin" directory of your Java SDK (in my case jdk-11), and call "jlink" from there.

The output depends on the selected SDK, i.e. if you select SDK 14 you get a slim version of this SDK.

(3) Make the installer package:

In your home directory create a folder "jpackage", and inside this folder two more folders: "input" and "output".

Put all resources in the input folder, i.e. in my case this is the jar file of RetrievalTrainer and the jar file of jdom2, and the apache licence document (given twice, a copy thereof also in folder "jpackage").

`./jpackage --app-version 1.0.4 --copyright "Copyright 2016 Detlef Gregor Herm" --name RetrievalTrainer --license-file ${HOME}/jpackage/LICENSE --input ${HOME}/jpackage/input --dest ${HOME}/jpackage/output --main-jar RetrievalTrainer-1.0.4.jar --main-class de.herm_detlef.java.application.preloader.CustomPreloader --runtime-image ${HOME}/jdk11+fx14_slim --arguments "--add-exports" --arguments "javafx.graphics/com.sun.javafx.application=ALL-UNNAMED"`

The "jpackage" tool is only contained in JDK 14 onwards, and it is not on the binary path.
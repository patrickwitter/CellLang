# CellLang
A domain specific prgramming language that allows users to express tabular formulas with more clarity and reusablilty than in excel

## Before coding 
* Change directory to "cellang" 
## Scripts are provided to run the program easier 
* .sh and .bat scripts provided. An update to one must be followed by the other  

  ### **Script descriptions**
  + **run.*** (Run lexer , parser, compile and Run the language to accept some user input)

  + **runarg.*** [path/to/prog] (Run lexer , parser, compile  and Run the language to compile some existing program ) 

  + **runTest.*** (Run lexer , parser and compile the language)

  + **runargf.*** [path/to/prog] (Run the language to compile some existing program ) 
  + **runf.*** (Run the language to accept some user input)

## Before Using Scripts  
* Change the location of the cup file in the commands 
* You may have to change the commands used in each script 

## Commands to run scripts 
*  **cmd.exe /c run.bat** ( RUN in bash console on windows ) 

* **run.bat** (Using command prompt on windows)

* **./run.sh** (RUN in bash window on windows )

## WORKFLOW - ENSURE YOU COMMENT 

### Adding a new DataType 
* Add necessary tokens to Lexer 

* Create an Expression type. This means extend
the EXP class and implement the methods 

  + By extending the EXP class you will have to implement the visitor methods and modify Evaluator and ToScheme
   
* Add parsing rules to parser 

* **Ensure No Conflicts**

* Add a new CellLang type. This means extend the base class and create a type. Implement the operations that you want to be done on the type 






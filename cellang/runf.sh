export CLASSPATH=$CLASSPATH:.

# Use an array to hold the jars
jars=()

# Populate the array with the jar files
for jar in $(find poi-bin-5.2.3 -name "*.jar"); do
  jars+=("$jar")
done

# Join the jars with ; and add to CLASSPATH
CLASSPATH="$CLASSPATH;$(IFS=';'; echo "${jars[*]}")"
export CLASSPATH

# Print the classpath
# echo "Classpath is: $CLASSPATH"

java -cp ".;C:\cup\lib\java-cup-11b.jar;lib3652.jar;$CLASSPATH" Main -w CellInterpreter -
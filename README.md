


Keystore generation
-------------------------
In current application the keystore is built into the resource folder. This is not a usual pattern. The keystore should be out of generated jar file in the classpath.

Keystore is generated on the following way:
keytool -genkeypair -alias demokey -keyalg RSA -keypass storeit -keystore keystore.jks -storepass storeit

The generated keystore.jks is copied to the root of resource folder.

Build
-------------------------
mvn clean install

Execute
-------------------------
mvn exec:exec

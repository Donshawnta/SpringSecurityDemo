


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

Get access token as default admin
-------------------------
curl -v -u admin:admin -d "grant_type=client_credentials" http://localhost:8080/authentication

Query admin user details
-------------------------
curl -v -H "Authorization:bearer <<returned access token>>" http://localhost:8080/demo/user/admin

Query all users
-------------------------
curl -v -H "Authorization:bearer <<returned access token>>" http://localhost:8080/demo/user

Add new user
-------------------------
curl -v -X POST -H "Authorization:bearer <<returned access token>>" -d '{"active": true,"address":"1024 Szava utca 2.","email":"tivadar@gmail.com","name":"Hodos Tivadar","nextLoginChangePwd": true,"phone":"01234","settlementId":"settId","password":"demo","userType":"normal","username":"tivadar","settlementsBySettlementId": null,"roles": ["PUBLIC"]}' http://localhost:8080/demo/user




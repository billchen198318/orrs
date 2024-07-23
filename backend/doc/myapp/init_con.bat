docker run -d --name myappcon -p 8088:8088 -p 22:22 -p 25:25 -p 21:21 -ti -v c:\myapp_data:/root myapp-img
docker cp initdb.sh myappcon:/root/initdb.sh
docker cp C:\jenkins_home\workspace\qifu4test\backend\doc\qifu4.sql myappcon:/root/qifu4.sql
docker cp C:\jenkins_home\workspace\qifu4test\backend\app\target\core-app-0.0.4-SNAPSHOT.jar myappcon:/root/core-app-0.0.4-SNAPSHOT.jar
docker exec -ti myappcon /bin/sh -c '/root/initdb.sh'
docker exec myappcon java -Xmx384M -jar /root/core-app-0.0.4-SNAPSHOT.jar
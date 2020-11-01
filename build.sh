./mvnw clean package -pl backend-main -am -Dmaven.test.skip=true
./mvnw clean package -pl batch -am -Dmaven.test.skip=true
cp backend-main/target/segjjs.jar backend-main/docker/
cp batch/target/segjjs.jar batch/docker/
run-front:
	npm run --prefix ./src/frontend serve
run-back:
	mvn spring-boot:run

clean:
	mvn clean
	
git-push:
    mvn clean
	git add .
	git commit -m "$m"
    git push

run:
    mvn clean package
    java -jar .\target\gitclout-0.0.1-SNAPSHOT.jar
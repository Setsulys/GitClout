run-front:
	npm run --prefix ./src/frontend serve
run-back:
	mvn spring-boot:run

clean:
	mvn clean
	
git-push: 
	git add .
	git commit -m "$m"
	git push

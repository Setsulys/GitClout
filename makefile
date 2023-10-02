run:
	mvn spring-boot:run

clean:
	mvn clean
	
git push: 
	git add .
	git commit -m "$m"
	git push

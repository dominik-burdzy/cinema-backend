# Makefile for easier handling common tasks
MVN = ./mvnw



lint:
	$(MVN) antrun:run@ktlint

lint.format:
	$(MVN) antrun:run@ktlint-format



dist: install




install:
	$(MVN) clean install



start:
	$(MVN) spring-boot:run

start.local:
	$(MVN) spring-boot:run -Dspring-boot.run.profiles=local

test:
	$(MVN) test



clean:
	$(MVN) clean



generate-changelog:
	PWD=pwd docker run \
		--rm \
		-it \
		-v "$(PWD)/changelogs/unreleased":/app/changelogs/unreleased \
		-v "$(PWD)/.git":/app/.git registry.gitlab.com/dentalux/gitlab-ci/changelog
